package com.carrentalsystem.app.Controller;


import com.carrentalsystem.app.Model.CarInventory;
import com.carrentalsystem.app.Model.ReservationDetails;
import com.carrentalsystem.app.Model.UserAccount;
import com.carrentalsystem.app.Service.*;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/car-rental-system/app")
@CrossOrigin("*")
public class RouteController {
    @Autowired UserAccountService userAccountService;
    @Autowired ThirdPartiesAPIService thirdPartiesAPIService;
    @Autowired CarInventoryService carInventoryService;
    @Autowired ReservationDetailsService reservationDetailsService;
    @Autowired DataReportsService dataReportsService;
    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);
    @GetMapping("/")
    public String landingPage(){
        return "landing";
    }

    @GetMapping("/titleBar")
    public String titleBar(){
        return "titleBar";
    }

    @GetMapping("/sideBar")
    public String sideBar(){
        return "sideBar";
    }

    @PostMapping(path = "/login-request", consumes = "application/x-www-form-urlencoded")
    public RedirectView loginRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        return userAccountService.validateLogin(httpServletRequest, httpSession);
    }

    @PostMapping(path = "/send-otp", consumes = "application/json")
    @ResponseBody
    public boolean sendOTP(@RequestBody Map<String, String> mapper) {
        return userAccountService.sendOTP(mapper.get("emailId").toString());
    }

    @PostMapping(path = "/verify-otp", consumes = "application/json")
    @ResponseBody
    public boolean verifyOTP(@RequestBody Map<String, String> mapper){
        return userAccountService.verifyOTP(mapper.get("emailId").toString(), mapper.get("otp").toString());
    }

    @PostMapping(path = "/forgot-update-password", consumes = "application/json")
    @ResponseBody
    public boolean updatePassword(@RequestBody Map<String, Object> mapper){
        return userAccountService.updatePassword(mapper);
    }

    @PostMapping(path = "/registration-request", consumes = "application/json")
    @ResponseBody
    public int registrationRequest(@RequestBody Map<String, Object> mapper){
        return userAccountService.registerNewUser(mapper);
    }

    @PostMapping(path = "/fetch-geo-location", consumes = "application/json")
    @ResponseBody
    public Map<String, List<String>> fetchGeoLocationByPostalCode(@RequestBody Map<String, Object> mapper){
        ResponseEntity<Map<String, List<String>>> response=thirdPartiesAPIService.fetchLocationDetailsByPostalCode(mapper.get("postalCode").toString());
        return response.getBody();
    }

    @GetMapping("/dashboard")
    public ModelAndView dashBoard(HttpSession httpSession){
        httpSession.setAttribute("ownedCarsCount", carInventoryService.fetchCarsByUserEmail(httpSession.getAttribute("sessionEmailId").toString()).size());
        httpSession.setAttribute("rentedCarsCount", reservationDetailsService.fetchRentedCars(httpSession.getAttribute("sessionEmailId").toString()).size());
        return new ModelAndView("dashboard");
    }

    @PostMapping(path = "/update-profile", consumes = "application/json")
    @ResponseBody
    public UserAccount updateProfile(@RequestBody Map<String, Object> mapper){
        return userAccountService.updateProfileDetails(mapper);
    }

    @GetMapping("/updateProfile")
    public String renderUpdateProfile(Model model, HttpSession httpSession){
        UserAccount userAccount=userAccountService.fetchUserById(httpSession.getAttribute("sessionEmailId").toString());
        model.addAttribute("userAccount", userAccount);
        return "updateProfile";
    }

    @GetMapping("/fetchOwnedCars")
    public String fetchOwnedCars(Model model, HttpSession httpSession){
        List<CarInventory> carsList= carInventoryService.fetchCarsByUserEmail(httpSession.getAttribute("sessionEmailId").toString());
        model.addAttribute("carsList", carsList);
        model.addAttribute("listSize", carsList.size());
        return "ownedCars";
    }

    @GetMapping("/fetch/carId/{carId}")
    @ResponseBody
    public CarInventory fetchCarDetailsById(@PathVariable("carId") String carId){
        return carInventoryService.fetchCarById(carId);
    }

    @PostMapping(path = "/update/car-details", consumes = "application/x-www-form-urlencoded")
    public RedirectView updateCarDetails(HttpServletRequest httpServletRequest){
        return carInventoryService.updateCarDetails(httpServletRequest);
    }

    @PostMapping(path = "/update/location-details", consumes = "application/x-www-form-urlencoded")
    public RedirectView updateLocationDetails(HttpServletRequest httpServletRequest){
        return carInventoryService.updateLocationDetails(httpServletRequest);
    }

    @DeleteMapping(path = "/delete/car-details", consumes = "application/json")
    @ResponseBody
    public boolean deleteCarFromInventory(@RequestBody Map<String, Object> mapper){
        return carInventoryService.deleteCarFromInventory(mapper);
    }

    @GetMapping("/registerNewCar")
    public String renderRegisterNewCarPage(){
        return "registerCar";
    }

    @PostMapping(path = "/register-new-car", consumes = "application/json")
    @ResponseBody
    public boolean registerNewCar(@RequestBody Map<String, Object> mapper, HttpSession httpSession){
        return carInventoryService.registerCarIntoInventory(mapper, httpSession);
    }

    @GetMapping("/bookedCars")
    public String bookedCars(Model model, HttpSession httpSession){
        List<ReservationDetails> reservationDetailsList=reservationDetailsService.fetchBookedCars(httpSession.getAttribute("sessionEmailId").toString());
        model.addAttribute("reservationList", reservationDetailsList);
        model.addAttribute("listSize", reservationDetailsList.size());
        return "bookedCars";
    }

    @GetMapping("/dataReports")
    public ModelAndView prepareDataReports(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView("reports");
        String sessionEmailId=httpSession.getAttribute("sessionEmailId").toString();
        List<Integer> graph1=List.of(Integer.valueOf(httpSession.getAttribute("ownedCarsCount").toString()), Integer.valueOf(httpSession.getAttribute("rentedCarsCount").toString()));
        modelAndView.addObject("graph1", graph1);
        List<List<Object>> graph2=dataReportsService.fetchCurrentYearRevenueReport(sessionEmailId);
        modelAndView.addObject("graph2", graph2);
        List<List<Object>> graph3=dataReportsService.fetchOverAllRevenueReport(sessionEmailId);
        modelAndView.addObject("graph3", graph3);
        List<List<Object>> graph4=dataReportsService.fetchRevenueReportFromEachCar(sessionEmailId);
        modelAndView.addObject("graph4X", graph4.get(0));
        modelAndView.addObject("graph4Y", graph4.get(1));
        List<List<Object>> graph5=dataReportsService.fetchRentedCountPerCar(sessionEmailId);
        modelAndView.addObject("graph5X", graph5.get(0));
        modelAndView.addObject("graph5Y", graph5.get(1));
        List<List<Object>> graph6=dataReportsService.fetchRatingPerCar(sessionEmailId);
        modelAndView.addObject("graph6", graph6);
        List<List<Object>> graph7=dataReportsService.fetchTransactionReport(sessionEmailId);
        modelAndView.addObject("graph7", graph7);
        return modelAndView;
    }

    @GetMapping("/fetchAllCars")
    public String fetchAllCars(Model model, HttpSession httpSession){
        List<CarInventory> allCars=carInventoryService.fetchAllCars(httpSession.getAttribute("sessionEmailId").toString());
        model.addAttribute("carsList", allCars);
        model.addAttribute("listSize", allCars.size());
        return "allCars";
    }

    @PostMapping(path = "/book-trip", consumes = "application/json")
    @ResponseBody
    public int bookTrip(@RequestBody Map<String, Object> mapper, HttpSession httpSession){
        return carInventoryService.bookTrip(mapper, httpSession);
    }

    @GetMapping("/fetch-slots/carId/{carId}")
    @ResponseBody
    public List<List<LocalDate>> fetchSlotsByCarId(@PathVariable("carId") String carId){
        List<List<LocalDate>> slotLists=carInventoryService.fetchSlotsByCarId(carId);
        return slotLists;
    }

    @PutMapping(path = "/update/rating", consumes = "application/json")
    @ResponseBody
    public ReservationDetails updateRating(@RequestBody Map<String, Object> mapper){
        return carInventoryService.updateRating(mapper);
    }

    @GetMapping(path = "/fetch-reviews/carId/{carId}")
    @ResponseBody
    public List<List<String>> fetchReviews(@PathVariable("carId") String carId){
        return carInventoryService.fetchCarReviews(carId);
    }

    @PostMapping(path = "/post/review", consumes = "application/json")
    @ResponseBody
    public boolean postReview(@RequestBody Map<String, Object> mapper, HttpSession httpSession){
        return carInventoryService.postReview(mapper, httpSession);
    }

    @GetMapping("/fetch/contact-info/email/{emailId}")
    @ResponseBody
    public UserAccount fetchContactInfoByEmailId(@PathVariable("emailId") String emailId){
        return userAccountService.fetchUserById(emailId);
    }

    @PostMapping(path = "/create-order", consumes = "application/json")
    @ResponseBody
    public String createOrderForTripBooking(@RequestBody Map<String, Object> mapper, HttpSession httpSession) throws RazorpayException {
        return reservationDetailsService.createOrderForBooking(mapper, httpSession);
    }

    @PutMapping("/update-order")
    @ResponseBody
    public String updateOrderForTripBooking(@RequestBody Map<String, Object> mapper) throws RazorpayException {
        return reservationDetailsService.updateOrderForBooking(mapper);
    }

    @PostMapping(path = "/cancel-order", consumes = "application/json")
    @ResponseBody
    public String cancelOrderForBookedTrip(@RequestBody Map<String, Object> mapper, HttpSession httpSession) throws RazorpayException {
        return reservationDetailsService.cancelOrderForBooking(mapper, httpSession);
    }

    @GetMapping("/fetchTrips")
    public String fetchRentedCars(Model model, HttpSession httpSession){
        List<ReservationDetails> upcomingTrips=reservationDetailsService.fetchTripStatus(httpSession.getAttribute("sessionEmailId").toString(), 1);
        List<ReservationDetails> ongoingTrips=reservationDetailsService.fetchTripStatus(httpSession.getAttribute("sessionEmailId").toString(), 2);
        List<ReservationDetails> pastTrips=reservationDetailsService.fetchTripStatus(httpSession.getAttribute("sessionEmailId").toString(), 3);
        //Adding list attributes
        model.addAttribute("upcoming", upcomingTrips);
        model.addAttribute("ongoing", ongoingTrips);
        model.addAttribute("past", pastTrips);
        //Adding list size attributes
        model.addAttribute("upcomingSize", upcomingTrips.size());
        model.addAttribute("ongoingSize", ongoingTrips.size());
        model.addAttribute("pastSize", pastTrips.size());
        return "trips";
    }

    @PostMapping(path = "/filter-price-range", consumes = "application/json")
    public String filterPriceRange(@RequestBody Map<String, Object> mapper, HttpSession httpSession, Model model){
        List<CarInventory> filteredCars=carInventoryService.filterPriceRange(mapper, httpSession);
        model.addAttribute("carsList", filteredCars);
        model.addAttribute("listSize", filteredCars.size());
        return "allCars";
    }

    @PostMapping(path = "/filter-price", consumes = "application/json")
    public String filterPrice(@RequestBody Map<String, Object> mapper, HttpSession httpSession, Model model){
        List<CarInventory> filteredCars=carInventoryService.filterPrice(mapper, httpSession);
        model.addAttribute("carsList", filteredCars);
        model.addAttribute("listSize", filteredCars.size());
        return "allCars";
    }

    @PostMapping(path = "/filter-location", consumes = "application/json")
    public String filterLocation(@RequestBody Map<String, Object> mapper, HttpSession httpSession, Model model){
        List<CarInventory> filteredCars=carInventoryService.filterCarsByLocation(mapper, httpSession);
        model.addAttribute("carsList", filteredCars);
        model.addAttribute("listSize", filteredCars.size());
        return "allCars";
    }

    @GetMapping("/fetchTransactions")
    public String fetchTransactions(HttpSession httpSession, Model model){
        List<ReservationDetails> transactionsList=reservationDetailsService.fetchTransactionDetails(httpSession.getAttribute("sessionEmailId").toString());
        model.addAttribute("transactionsList", transactionsList);
        model.addAttribute("listSize", transactionsList.size());
        return "fetchTransactions";
    }

    @GetMapping(value = "/fetch-order/orderId/{orderId}")
    @ResponseBody
    public ReservationDetails fetchOrderDetailsById(@PathVariable("orderId") String orderId){
        return reservationDetailsService.fetchBookingInfoById(orderId);
    }

    @GetMapping("/login-failure")
    public String loginFailure(){
        return "loginError";
    }

    @GetMapping("/logout-success")
    public String logout(){
        return "logout";
    }
}
