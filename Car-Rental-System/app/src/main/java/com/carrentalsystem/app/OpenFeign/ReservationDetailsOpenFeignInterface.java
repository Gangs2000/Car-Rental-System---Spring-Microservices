package com.carrentalsystem.app.OpenFeign;

import com.carrentalsystem.app.Model.ReservationDetails;
import com.razorpay.RazorpayException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "reservationDetails", url = "localhost:7070/api/car-rental-system/reservation")
public interface ReservationDetailsOpenFeignInterface {
    @GetMapping("/fetch-orders/email/{emailId}/option/{option}")
    ResponseEntity<List<ReservationDetails>> fetchBookingDetailsByUserEmail(@PathVariable("emailId") String emailId, @PathVariable("option") int option);
    @GetMapping("/fetch-trip-status/email/{emailId}/option/{option}")
    ResponseEntity<List<ReservationDetails>> fetchTripStatus(@PathVariable("emailId") String emailId, @PathVariable("option") int option);
    @GetMapping("/fetch-order/orderId/{orderId}")
    ResponseEntity<ReservationDetails> fetchBookingDetailsById(@PathVariable("orderId") String orderId);
    @PutMapping("/update/rating/orderId/{orderId}")
    ResponseEntity<ReservationDetails> updateRating(@PathVariable("orderId") String orderId);
    @PostMapping("/create-booking-order")
    ResponseEntity<String> createBookCarRequest(@RequestBody ReservationDetails reservationDetails) throws RazorpayException;
    @PutMapping("/update-booking-order")
    ResponseEntity<String> updateBookedCarRequest(@RequestBody ReservationDetails reservationDetails) throws RazorpayException;
    @PutMapping("/trigger/update/trip-status")
    void updateTripStatus();
    @DeleteMapping("/cancel-booking-order/email/{emailId}")
    ResponseEntity<String> cancelBookedCarRequest(@RequestBody ReservationDetails reservationDetails, @PathVariable("emailId") String emailId) throws RazorpayException;
}
