package com.carrentalsystem.app.Interface;

import com.carrentalsystem.app.Model.CarInventory;
import com.carrentalsystem.app.Model.ReservationDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CarInventoryInterface {
    CarInventory fetchCarById(String carId);
    int bookTrip(Map<String, Object> mapper, HttpSession httpSession);
    List<CarInventory> fetchCarsByUserEmail(String emailId);
    List<List<LocalDate>> fetchSlotsByCarId(String carId);
    List<CarInventory> fetchAllCars(String emailId);
    Set<String> fetchCarManufacturer();
    List<List<String>> fetchCarReviews(String carId);
    List<CarInventory> filterPriceRange(Map<String, Object> mapper, HttpSession httpSession);
    List<CarInventory> filterPrice(Map<String, Object> mapper, HttpSession httpSession);
    List<CarInventory> filterCarsByLocation(Map<String, Object> mapper, HttpSession httpSession);
    RedirectView updateCarDetails(HttpServletRequest httpServletRequest);
    RedirectView updateLocationDetails(HttpServletRequest httpServletRequest);
    ReservationDetails updateRating(Map<String, Object> mapper);
    boolean postReview(Map<String, Object> mapper, HttpSession httpSession);
    boolean deleteCarFromInventory(Map<String, Object> mapper);
    boolean registerCarIntoInventory(Map<String, Object> mapper, HttpSession httpSession);
}
