package com.carrentalsystem.restapi.Interface;

import com.carrentalsystem.restapi.Model.ReservationDetails;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReservationDetailsInterface {
    String createOrderForBookingCar(ReservationDetails reservationDetails) throws RazorpayException;
    String updateOrderForBookingCar(ReservationDetails reservationDetails) throws RazorpayException;
    List<ReservationDetails> fetchTripStatus(String emailId, int option);
    ReservationDetails fetchBookingDetailsById(String orderId);
    List<ReservationDetails> fetchBookingDetailsByUserEmail(String emailId, int option);
    ReservationDetails updateRating(String orderId);
    ResponseEntity<String> cancelOrderForBookedCar(ReservationDetails reservationDetails, String emailId) throws RazorpayException;
    void updateTripStatus();
}
