package com.carrentalsystem.restapi.Service;

import com.carrentalsystem.restapi.Config.RabbitMqConfig;
import com.carrentalsystem.restapi.Interface.ReservationDetailsInterface;
import com.carrentalsystem.restapi.Model.CarInventory;
import com.carrentalsystem.restapi.Model.ReservationDetails;
import com.carrentalsystem.restapi.Model.UserAccount;
import com.carrentalsystem.restapi.Repository.CarInventoryRepository;
import com.carrentalsystem.restapi.Repository.ReservationDetailsRepository;
import com.carrentalsystem.restapi.Repository.UserAccountRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservationDetailsService implements ReservationDetailsInterface {
    @Autowired ReservationDetailsRepository reservationDetailsRepository;
    @Autowired CarInventoryRepository carInventoryRepository;
    @Autowired UserAccountRepository userAccountRepository;
    @Autowired ReservationDetails existingReservationDetails;
    @Autowired CarInventory existingCarInventory;
    @Autowired RabbitTemplate rabbitTemplate;
    RazorpayClient razorpay;
    //Using comparator to sort trip start date
    Comparator<ReservationDetails> reservationDetailsComparator=new Comparator<ReservationDetails>() {
        @Override
        public int compare(ReservationDetails o1, ReservationDetails o2) {
            return o1.getTripStartDate().compareTo(o2.getTripStartDate());
        }
    };

    @Override
    public String createOrderForBookingCar(ReservationDetails reservationDetails) throws RazorpayException {
        existingCarInventory=carInventoryRepository.findById(reservationDetails.getCarId()).get();
        razorpay = new RazorpayClient("rzp_test_AZSLzVBmowbvZk", "ecFWgmpgtwxtyXxvkydIuT5N");
        JSONObject orderRequest = new JSONObject();
        LocalDate tripStartDate=reservationDetails.getTripStartDate();
        LocalDate tripEndDate=reservationDetails.getTripEndDate();
        long numberOfDays=ChronoUnit.DAYS.between(tripStartDate, tripEndDate.plusDays(1));
        orderRequest.put("amount", (numberOfDays * existingCarInventory.getPricePerDay() * 100.00));        //Converting Rupees into Paise
        orderRequest.put("currency", "INR");
        Order order = razorpay.orders.create(orderRequest);
        reservationDetails.set_id(order.get("id"));
        reservationDetails.setTripStatus("NOT STARTED");
        reservationDetails.setCurrencyType("INR");
        reservationDetails.setAmount(numberOfDays*existingCarInventory.getPricePerDay());
        reservationDetails.setOrderStatus(order.get("status"));
        reservationDetails.setNoOfAttempts(order.get("attempts"));
        reservationDetails.setCancellationReason("");
        reservationDetails.setTimestamp(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), LocalTime.now().getHour(), LocalTime.now().getMinute()));
        reservationDetails.setRatingGiven(false);
        reservationDetailsRepository.save(reservationDetails);
        return order.toString();
    }

    @Override
    public String updateOrderForBookingCar(ReservationDetails reservationDetails) throws RazorpayException {
        razorpay = new RazorpayClient("rzp_test_AZSLzVBmowbvZk", "ecFWgmpgtwxtyXxvkydIuT5N");
        Order order=razorpay.orders.fetch(reservationDetails.get_id());
        existingReservationDetails=reservationDetailsRepository.findById(reservationDetails.get_id()).get();
        existingReservationDetails.setNoOfAttempts(order.get("attempts"));
        existingReservationDetails.setOrderStatus(order.get("status"));
        existingReservationDetails.setTripStatus("NOT STARTED");
        existingReservationDetails.setPaymentId(reservationDetails.getPaymentId());
        //Update reservation confirmation details
        reservationDetails=reservationDetailsRepository.save(existingReservationDetails);
        if(reservationDetails.getOrderStatus().equals("paid")) {
            existingCarInventory = carInventoryRepository.findById(existingReservationDetails.getCarId()).get();
            List<String> reservationIds = existingCarInventory.getReservationIds();
            reservationIds.add(reservationDetails.get_id());
            //Updating order ID details in car inventory database
            carInventoryRepository.save(existingCarInventory);
            //Post updating process trigger booking confirmation email to end user
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY1, reservationDetails.get_id());
            return "Payment details has been updated and Trip has been booked";
        }
        else
            return "Payment declined or failed";
    }

    @Override
    public List<ReservationDetails> fetchTripStatus(String emailId, int option) {
        List<ReservationDetails> tripStatusList=new LinkedList<>();
        switch (option){
            case 1 : tripStatusList=reservationDetailsRepository.findByRentedByAndTripStatusAndOrderStatus(emailId, "NOT STARTED", "paid"); break;    //Trip not started
            case 2 : tripStatusList=reservationDetailsRepository.findByRentedByAndTripStatusAndOrderStatus(emailId, "STARTED", "paid"); break;        //Trip started
            case 3 : tripStatusList=reservationDetailsRepository.findByRentedByAndTripStatusAndOrderStatus(emailId, "FINISHED", "paid"); break;       //Tip finished
        }
        Collections.sort(tripStatusList, reservationDetailsComparator);
        return tripStatusList;
    }

    @Override
    public ReservationDetails fetchBookingDetailsById(String orderId) {
        return reservationDetailsRepository.findById(orderId).get();
    }

    @Override
    public List<ReservationDetails> fetchBookingDetailsByUserEmail(String emailId, int option) {
        if(option==1)
            return reservationDetailsRepository.findByRentedByAndOrderStatus(emailId, "paid");
        else
            return reservationDetailsRepository.findByRentedBy(emailId);
    }

    @Override
    public ReservationDetails updateRating(String orderId) {
        existingReservationDetails=reservationDetailsRepository.findById(orderId).get();
        existingReservationDetails.setRatingGiven(true);
        return reservationDetailsRepository.save(existingReservationDetails);
    }

    @Override
    public ResponseEntity<String> cancelOrderForBookedCar(ReservationDetails reservationDetails, String emailId) throws RazorpayException {
        existingReservationDetails=reservationDetailsRepository.findById(reservationDetails.get_id()).get();
        LocalDate currentDate= LocalDate.now();
        LocalDate tripStartDate=existingReservationDetails.getTripStartDate();
        long getDays=ChronoUnit.DAYS.between(currentDate, tripStartDate);
        //Check condition - Duration between current date and trip start date must be greater than 1 day
        if((!emailId.equals(existingReservationDetails.getRentedBy()) && getDays>1) || getDays>1){
            razorpay = new RazorpayClient("rzp_test_AZSLzVBmowbvZk", "ecFWgmpgtwxtyXxvkydIuT5N");
            JSONObject cancelOrderRequest =new JSONObject();
            cancelOrderRequest.put("amount", razorpay.payments.fetch(existingReservationDetails.getPaymentId()).get("amount").toString());      //Setting up refund amount
            cancelOrderRequest.put("speed", "normal");
            Refund refund = razorpay.payments.refund(existingReservationDetails.getPaymentId(), cancelOrderRequest);
            if(refund.get("status").toString().equals("processed")) {
                //Removing reservation order ID from car inventory
                existingCarInventory = carInventoryRepository.findById(existingReservationDetails.getCarId()).get();
                List<String> reservationIds = existingCarInventory.getReservationIds();
                reservationIds.remove(existingReservationDetails.get_id());
                carInventoryRepository.save(existingCarInventory);
                //Saving refund details for transaction purpose
                existingReservationDetails.setRefundId(refund.get("id").toString());
                existingReservationDetails.setOrderStatus("refunded");
                existingReservationDetails.setTripStatus("CANCELLED");
                existingReservationDetails.setCancellationReason(reservationDetails.getCancellationReason());
                existingReservationDetails.setCancelledBy((emailId.equals(existingReservationDetails.getRentedBy())?("Trip was cancelled by you"):("Trip was cancelled by "+emailId)));
                existingReservationDetails.setTimestamp(LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), LocalTime.now().getHour(), LocalTime.now().getMinute()));
                //Saving reservation details after setting refund details
                reservationDetails=reservationDetailsRepository.save(existingReservationDetails);
                if(Optional.of(reservationDetails.getRefundId()).isPresent()) {
                    //Post updating process Sending cancellation email to end user
                    rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY2, reservationDetails.get_id());
                    return new ResponseEntity<>("done", HttpStatus.ACCEPTED);
                }
                else
                    return new ResponseEntity<>("error", HttpStatus.ACCEPTED);
            }
            else
                return new ResponseEntity<>("error", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("error", HttpStatus.ACCEPTED);
    }

    @Override
    @Scheduled(cron = "0 */10 * * * *")
    //It will update the status every 10 minutes
    public void updateTripStatus() {
        System.out.println("Trip status has been updated to all users at "+LocalDateTime.now());
        List<UserAccount> listOfUsers=userAccountRepository.findAll();
        //Update Trip status if current day is not equal to last updated day
        for(UserAccount userAccount : listOfUsers) {
            if (userAccount.getLastUpdated().getDayOfMonth() != LocalDateTime.now().getDayOfMonth()) {
                userAccount.setLastUpdated(LocalDateTime.now());
                List<ReservationDetails> reservationDetailsList = reservationDetailsRepository.findAll();
                for (ReservationDetails reservationDetails : reservationDetailsList) {
                    existingReservationDetails = reservationDetailsRepository.findById(reservationDetails.get_id()).get();
                    String tripStatus = existingReservationDetails.getTripStatus();
                    String orderStatus = existingReservationDetails.getOrderStatus();
                    if (orderStatus.equals("paid") && !tripStatus.equals("CANCELLED")) {
                        //Trip is not yet started
                        if (LocalDate.now().isBefore(existingReservationDetails.getTripStartDate()) && (Optional.ofNullable(tripStatus).isEmpty() || tripStatus.equals("NOT STARTED")))
                            existingReservationDetails.setTripStatus("NOT STARTED");
                        //Trip is in progress
                        if ((LocalDate.now().isEqual(existingReservationDetails.getTripStartDate()) && !tripStatus.equals("STARTED")) ||
                                LocalDate.now().isAfter(existingReservationDetails.getTripStartDate()) && tripStatus.equals("NOT STARTED")
                        )
                            existingReservationDetails.setTripStatus("STARTED");
                        //Trip is completely finished
                        if (LocalDate.now().isAfter(existingReservationDetails.getTripStartDate()) && LocalDate.now().isAfter(existingReservationDetails.getTripEndDate()))
                            existingReservationDetails.setTripStatus("FINISHED");
                        reservationDetailsRepository.save(existingReservationDetails);
                    }
                }
                userAccountRepository.save(userAccount);
            }
        }
    }
}
