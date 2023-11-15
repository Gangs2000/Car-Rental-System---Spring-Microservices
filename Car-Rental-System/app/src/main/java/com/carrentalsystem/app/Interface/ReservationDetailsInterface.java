package com.carrentalsystem.app.Interface;

import com.carrentalsystem.app.Model.ReservationDetails;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

public interface ReservationDetailsInterface {
    List<ReservationDetails> fetchTripStatus(String emailId, int option);
    List<ReservationDetails> fetchRentedCars(String emailId);
    List<ReservationDetails> fetchBookedCars(String emailId);
    ReservationDetails fetchBookingInfoById(String orderId);
    List<ReservationDetails> fetchTransactionDetails(String emailId);
    String createOrderForBooking(Map<String, Object> mapper, HttpSession httpSession) throws RazorpayException;
    String updateOrderForBooking(Map<String, Object> mapper) throws RazorpayException;
    String cancelOrderForBooking(Map<String, Object> mapper, HttpSession httpSession) throws RazorpayException;
}
