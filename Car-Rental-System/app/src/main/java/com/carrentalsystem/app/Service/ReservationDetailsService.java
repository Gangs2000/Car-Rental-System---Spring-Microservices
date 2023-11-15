package com.carrentalsystem.app.Service;

import com.carrentalsystem.app.Interface.ReservationDetailsInterface;
import com.carrentalsystem.app.Model.CarInventory;
import com.carrentalsystem.app.Model.ReservationDetails;
import com.carrentalsystem.app.OpenFeign.CarInventoryOpenFeignInterface;
import com.carrentalsystem.app.OpenFeign.ReservationDetailsOpenFeignInterface;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservationDetailsService implements ReservationDetailsInterface {
    @Autowired ReservationDetails reservationDetails;
    @Autowired ReservationDetailsOpenFeignInterface reservationDetailsOpenFeignInterface;
    @Autowired CarInventoryOpenFeignInterface carInventoryOpenFeignInterface;

    Comparator<ReservationDetails> reservationDetailsComparator=new Comparator<ReservationDetails>() {
        @Override
        public int compare(ReservationDetails o1, ReservationDetails o2) {
            return o1.getTripStartDate().compareTo(o2.getTripStartDate());
        }
    };


    @Override
    public List<ReservationDetails> fetchTripStatus(String emailId, int option) {
        List<ReservationDetails> reservationDetailsList=new LinkedList<>();
        //Need to set cancellation button if and only if current date is greater than trip start date
        if(option==1){
            List<ReservationDetails> list=reservationDetailsOpenFeignInterface.fetchTripStatus(emailId, option).getBody();
            for(ReservationDetails reservationDetails : list){
                LocalDate tripStartDate=reservationDetails.getTripStartDate();
                long getDays= ChronoUnit.DAYS.between(LocalDate.now(), tripStartDate);
                reservationDetails.setCanBeCancelled((getDays>1));
                reservationDetailsList.add(reservationDetails);
            }
        }
        else
            reservationDetailsList=reservationDetailsOpenFeignInterface.fetchTripStatus(emailId, option).getBody();
        Collections.sort(reservationDetailsList, reservationDetailsComparator);
        return reservationDetailsList;
    }

    @Override
    public List<ReservationDetails> fetchRentedCars(String emailId) {
        return reservationDetailsOpenFeignInterface.fetchBookingDetailsByUserEmail(emailId, 1).getBody();
    }

    @Override
    public List<ReservationDetails> fetchBookedCars(String emailId) {
        List<ReservationDetails> reservationDetailsList=new LinkedList<>();
        List<CarInventory> carInventoryList=carInventoryOpenFeignInterface.fetchCarsByUserEmail(emailId);
        for(CarInventory carInventory : carInventoryList) {
            List<String> reservationIds = carInventory.getReservationIds();
            if (reservationIds.size() != 0) {
                for (String reservationId : reservationIds)
                    reservationDetailsList.add(reservationDetailsOpenFeignInterface.fetchBookingDetailsById(reservationId).getBody());
            }
            Collections.sort(reservationDetailsList, reservationDetailsComparator);
        }
        return reservationDetailsList;
    }

    @Override
    public ReservationDetails fetchBookingInfoById(String orderId) {
        return reservationDetailsOpenFeignInterface.fetchBookingDetailsById(orderId).getBody();
    }

    @Override
    public List<ReservationDetails> fetchTransactionDetails(String emailId) {
        List<ReservationDetails> reservationDetailsList=reservationDetailsOpenFeignInterface.fetchBookingDetailsByUserEmail(emailId, 2).getBody();
        Collections.sort(reservationDetailsList, reservationDetailsComparator);
        return reservationDetailsList;
    }

    @Override
    public String createOrderForBooking(Map<String, Object> mapper, HttpSession httpSession) throws RazorpayException {
        reservationDetails.setCarId(mapper.get("carId").toString());
        reservationDetails.setRentedBy(httpSession.getAttribute("sessionEmailId").toString());
        reservationDetails.setTripStartDate(LocalDate.parse(mapper.get("tripStartDate").toString()));
        reservationDetails.setTripEndDate(LocalDate.parse(mapper.get("tripEndDate").toString()));
        return reservationDetailsOpenFeignInterface.createBookCarRequest(reservationDetails).getBody();
    }

    @Override
    public String updateOrderForBooking(Map<String, Object> mapper) throws RazorpayException {
        reservationDetails.set_id(mapper.get("orderId").toString());
        reservationDetails.setPaymentId(mapper.get("paymentId").toString());
        return reservationDetailsOpenFeignInterface.updateBookedCarRequest(reservationDetails).getBody();
    }

    @Override
    public String cancelOrderForBooking(Map<String, Object> mapper, HttpSession httpSession) throws RazorpayException {
        reservationDetails.set_id(mapper.get("orderId").toString());
        reservationDetails.setCancellationReason(mapper.get("reason").toString());
        return reservationDetailsOpenFeignInterface.cancelBookedCarRequest(reservationDetails, httpSession.getAttribute("sessionEmailId").toString()).getBody();
    }
}
