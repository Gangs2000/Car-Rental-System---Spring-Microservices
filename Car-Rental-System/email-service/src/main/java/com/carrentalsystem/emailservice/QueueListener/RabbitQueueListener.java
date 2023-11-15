package com.carrentalsystem.emailservice.QueueListener;

import com.carrentalsystem.emailservice.Config.RabbitMqConfig;
import com.carrentalsystem.emailservice.Model.CarInventory;
import com.carrentalsystem.emailservice.Model.OTPBucket;
import com.carrentalsystem.emailservice.Model.ReservationDetails;
import com.carrentalsystem.emailservice.Model.UserAccount;
import com.carrentalsystem.emailservice.Repository.CarInventoryRepository;
import com.carrentalsystem.emailservice.Repository.OTPBucketRepository;
import com.carrentalsystem.emailservice.Repository.ReservationDetailsRepository;
import com.carrentalsystem.emailservice.Repository.UserAccountRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class RabbitQueueListener {
    @Autowired JavaMailSender javaMailSender;
    SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
    @Autowired UserAccount userAccount;
    @Autowired CarInventory carInventory;
    @Autowired ReservationDetails reservationDetails;
    @Autowired OTPBucket otpBucket;
    @Autowired UserAccountRepository userAccountRepository;
    @Autowired CarInventoryRepository carInventoryRepository;
    @Autowired ReservationDetailsRepository reservationDetailsRepository;
    @Autowired OTPBucketRepository otpBucketRepository;

    @RabbitListener(queues = RabbitMqConfig.QUEUE1)
    public void bookingConfirmationEmail(String bookingId){
        reservationDetails=reservationDetailsRepository.findById(bookingId).get();
        carInventory=carInventoryRepository.findById(reservationDetails.getCarId()).get();
        userAccount=userAccountRepository.findById(carInventory.getOwnedBy()).get();
        simpleMailMessage.setSubject("Car Rental System : Booking confirmation email - Car Number - "+carInventory.get_id());
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        simpleMailMessage.setTo(reservationDetails.getRentedBy());
        String emailText="\n This is to confirm you that you have successfully booked your trip, Please find the below details"+
                    "\n \n Booked Car Details : "+
                    "\n \n Car Number : "+carInventory.get_id()+
                    "\n Model : "+carInventory.getCarModel()+
                    "\n Manufacturer : "+carInventory.getManufacturer()+
                    "\n Model Year : "+carInventory.getYear()+
                    "\n Rating : "+carInventory.getCarRating().get(2)+" / 5"+
                    "\n \n Venue Details to pick car : "+
                    "\n \n Address : "+carInventory.getLocality()+", "+carInventory.getDistrict()+", "+carInventory.getState()+
                    "\n PinCode - "+carInventory.getPinCode()+
                    "\n \n Trip Details : "+
                    "\n \n Trip Start Date : "+reservationDetails.getTripStartDate()+
                    "\n Trip End Date : "+reservationDetails.getTripEndDate()+
                    "\n No.of Days Rented : "+ ChronoUnit.DAYS.between(reservationDetails.getTripStartDate(), reservationDetails.getTripEndDate().plusDays(1))+" Days"+
                    "\n \n Payment Details : "+
                    "\n \n Payment ID : "+reservationDetails.getPaymentId()+
                    "\n Currency Type : "+reservationDetails.getCurrencyType()+
                    "\n Amount : Rs."+reservationDetails.getAmount()+
                    "\n Transaction Status : "+reservationDetails.getOrderStatus()+
                    "\n Trip booked time : "+reservationDetails.getTimestamp()+
                    "\n \n Car Owner Details : "+
                    "\n \n Owner Name : "+userAccount.getUserName()+
                    "\n Email ID : "+userAccount.getEmailId()+
                    "\n Contact Number : "+userAccount.getMobileNumber()+
                    "\n Address : "+userAccount.getCity()+", "+userAccount.getDistrict()+", "+userAccount.getState()+
                    "\n PinCode : "+userAccount.getPinCode()+
                    "\n ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"+
                    "\n For any concern or queries related to our application please do drop an email to evento.bookconfirmation@gmail.com"+
                    "\n For any concern or queries related to your trip or car details please do contact "+userAccount.getEmailId()+" or "+userAccount.getMobileNumber()+
                    "\n \n Thanks & Regards,"+
                    "\n Car Rental System Team";
        simpleMailMessage.setText(emailText);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email has been sent to "+reservationDetails.getRentedBy());
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE2)
    public void cancellationConfirmationEmail(String orderId){
        reservationDetails=reservationDetailsRepository.findById(orderId).get();
        carInventory=carInventoryRepository.findById(reservationDetails.getCarId()).get();
        userAccount=userAccountRepository.findById(carInventory.getOwnedBy()).get();
        simpleMailMessage.setSubject("Car Rental System : Cancellation confirmation email - Car Number - "+carInventory.get_id());
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        simpleMailMessage.setTo(reservationDetails.getRentedBy());
        String emailText="\n This is to confirm you that you have cancelled your trip, Please find the below details"+
                "\n \n Cancellation and Payment Details : "+
                "\n \n Payment ID : "+reservationDetails.getPaymentId()+
                "\n Refund ID : "+reservationDetails.getRefundId()+
                "\n Current Type : "+reservationDetails.getCurrencyType()+
                "\n Amount Refunded : Rs."+reservationDetails.getAmount()+
                "\n Transaction Status : "+reservationDetails.getOrderStatus()+
                "\n Trip cancelled time : "+reservationDetails.getTimestamp()+
                "\n Cancellation Reason : "+reservationDetails.getCancellationReason()+
                "\n \n Cancelled Car Details : "+
                "\n \n Car Number : "+carInventory.get_id()+
                "\n Model : "+carInventory.getCarModel()+
                "\n Manufacturer : "+carInventory.getManufacturer()+
                "\n Model Year : "+carInventory.getYear()+
                "\n \n Car Owner Details : "+
                "\n \n Owner Name : "+userAccount.getUserName()+
                "\n Email ID : "+userAccount.getEmailId()+
                "\n Contact Number : "+userAccount.getMobileNumber()+
                "\n Address : "+userAccount.getCity()+", "+userAccount.getDistrict()+", "+userAccount.getState()+
                "\n PinCode : "+userAccount.getPinCode()+
                "\n \n Note : There may be possibilities that details not updated, This details you have received by the time you booked your trip, Most recent details can be seen in the rental application"+
                "\n ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"+
                "\n For any concern or queries related to our application please do drop an email to evento.bookconfirmation@gmail.com"+
                "\n For any concern or queries related to your trip cancellation or car details please do contact "+userAccount.getEmailId()+" or "+userAccount.getMobileNumber()+
                "\n \n Thanks & Regards,"+
                "\n Car Rental System Team";
        simpleMailMessage.setText(emailText);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email has been sent to "+reservationDetails.getRentedBy());
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE3)
    public void otpEmail(String emailId){
        otpBucket=otpBucketRepository.findById(emailId).get();
        simpleMailMessage.setSubject("Car Rental System : OTP Verification for Password change request");
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        simpleMailMessage.setTo(emailId);
        String emailText="\n Please enter the below OTP number in the OTP field and click verify button to change password."+
                "\n OTP PIN : "+otpBucket.getOtp()+
                "\n Please keep this is email as confidential and do not share OTP "+
                "\n ---------------------------------------------------------------------------------------------------------------------------------------------------"+
                "\n For any concern or queries related to our application please do drop an email to evento.bookconfirmation@gmail.com"+
                "\n \n Thanks & Regards,"+
                "\n Car Rental System Team";
        simpleMailMessage.setText(emailText);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email has been sent to "+emailId);
    }
}
