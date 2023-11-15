package com.carrentalsystem.restapi.Controller;

import com.carrentalsystem.restapi.Model.ReservationDetails;
import com.carrentalsystem.restapi.Service.ReservationDetailsService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-rental-system/reservation")
public class ReservationDetailsController {
    @Autowired ReservationDetailsService reservationDetailsService;

    @PostMapping("/create-booking-order")
    public ResponseEntity<String> createBookCarRequest(@RequestBody ReservationDetails reservationDetails) throws RazorpayException {
        return new ResponseEntity<>(reservationDetailsService.createOrderForBookingCar(reservationDetails), HttpStatus.CREATED);
    }

    @PutMapping("/update-booking-order")
    public ResponseEntity<String> updateBookedCarRequest(@RequestBody ReservationDetails reservationDetails) throws RazorpayException {
        return new ResponseEntity<>(reservationDetailsService.updateOrderForBookingCar(reservationDetails), HttpStatus.ACCEPTED);
    }

    @GetMapping("/fetch-trip-status/email/{emailId}/option/{option}")
    public ResponseEntity<List<ReservationDetails>> fetchTripStatus(@PathVariable("emailId") String emailId, @PathVariable("option") int option){
        return new ResponseEntity<>(reservationDetailsService.fetchTripStatus(emailId, option), HttpStatus.OK);
    }

    @GetMapping("/fetch-order/orderId/{orderId}")
    public ResponseEntity<ReservationDetails> fetchBookingDetailsById(@PathVariable("orderId") String orderId){
        return new ResponseEntity<>(reservationDetailsService.fetchBookingDetailsById(orderId), HttpStatus.OK);
    }

    @GetMapping("/fetch-orders/email/{emailId}/option/{option}")
    public ResponseEntity<List<ReservationDetails>> fetchBookingDetailsByUserEmail(@PathVariable("emailId") String emailId, @PathVariable("option") int option){
        return new ResponseEntity<>(reservationDetailsService.fetchBookingDetailsByUserEmail(emailId, option), HttpStatus.OK);
    }

    @PutMapping("/update/rating/orderId/{orderId}")
    public ResponseEntity<ReservationDetails> updateRating(@PathVariable("orderId") String orderId){
        return new ResponseEntity<>(reservationDetailsService.updateRating(orderId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/cancel-booking-order/email/{emailId}")
    public ResponseEntity<String> cancelBookedCarRequest(@RequestBody ReservationDetails reservationDetails, @PathVariable("emailId") String emailId) throws RazorpayException {
        return reservationDetailsService.cancelOrderForBookedCar(reservationDetails, emailId);
    }

    //Cron Job will be running for every 10 minutes to update trip status
    @PutMapping("/trigger/update/trip-status")
    public void updateTripStatus(){
        reservationDetailsService.updateTripStatus();
    }
}
