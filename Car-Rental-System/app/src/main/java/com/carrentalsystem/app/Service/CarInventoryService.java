package com.carrentalsystem.app.Service;

import com.carrentalsystem.app.Interface.CarInventoryInterface;
import com.carrentalsystem.app.Model.CarInventory;
import com.carrentalsystem.app.Model.ReservationDetails;
import com.carrentalsystem.app.OpenFeign.CarInventoryOpenFeignInterface;
import com.carrentalsystem.app.OpenFeign.ReservationDetailsOpenFeignInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarInventoryService implements CarInventoryInterface {

    @Autowired CarInventory carInventory;
    @Autowired CarInventoryOpenFeignInterface carInventoryOpenFeignInterface;
    @Autowired ReservationDetailsOpenFeignInterface reservationDetailsOpenFeignInterface;
    RedirectView redirectView=new RedirectView();
    Comparator<CarInventory> carInventoryComparator=new Comparator<CarInventory>() {
        @Override
        public int compare(CarInventory o1, CarInventory o2) {
            return o1.getCarModel().compareToIgnoreCase(o2.getCarModel());
        }
    };

    @Override
    public CarInventory fetchCarById(String carId) {
        return carInventoryOpenFeignInterface.fetchCarById(carId);
    }

    @Override
    public int bookTrip(Map<String, Object> mapper, HttpSession httpSession) {
        String carId=mapper.get("carId").toString();
        LocalDate tripStartDate= LocalDate.parse(mapper.get("tripStartDate").toString());
        LocalDate tripEndDate= LocalDate.parse(mapper.get("tripEndDate").toString());
        int statusCode=carInventoryOpenFeignInterface.bookTrip(carId, tripStartDate, tripEndDate);
        if(statusCode==1){
            //Check if user is already booked for another car on the given trip dates
            List<String> orderIds=reservationDetailsOpenFeignInterface.fetchBookingDetailsByUserEmail(
                    httpSession.getAttribute("sessionEmailId").toString(),1
            ).getBody().stream().map(reservationDetails -> reservationDetails.get_id()).collect(Collectors.toList());
            PriorityQueue<List<LocalDate>> tripDates=new PriorityQueue<>(new Comparator<List<LocalDate>>() {
                @Override
                public int compare(List<LocalDate> o1, List<LocalDate> o2) {
                    return o1.get(0).compareTo(o2.get(0));
                }
            });
            for(String orderId : orderIds){
                ReservationDetails reservationDetails=reservationDetailsOpenFeignInterface.fetchBookingDetailsById(orderId).getBody();
                if(reservationDetails.getTripStatus().equals("NOT STARTED") || reservationDetails.getTripStatus().equals("STARTED"))
                    tripDates.add(List.of(reservationDetails.getTripStartDate(), reservationDetails.getTripEndDate()));
            }
            Set<LocalDate> allBookedDatesByUser=new HashSet<>();
            while(!tripDates.isEmpty()){
                LocalDate startDate=tripDates.peek().get(0);
                LocalDate endDate=tripDates.peek().get(1);
                allBookedDatesByUser.add(startDate);
                while(startDate.isBefore(endDate)){
                    startDate=startDate.plusDays(1);
                    allBookedDatesByUser.add(startDate);
                }
                allBookedDatesByUser.add(endDate);
                tripDates.poll();
            }
            return (!allBookedDatesByUser.contains(tripStartDate) && !allBookedDatesByUser.contains(tripEndDate))?(1):(4);
        }
        return statusCode;
    }

    @Override
    public List<CarInventory> fetchCarsByUserEmail(String emailId) {
        List<CarInventory> carInventoryList=carInventoryOpenFeignInterface.fetchCarsByUserEmail(emailId);
        Collections.sort(carInventoryList, carInventoryComparator);
        return carInventoryList;
    }

    @Override
    public List<List<LocalDate>> fetchSlotsByCarId(String carId) {
        return carInventoryOpenFeignInterface.fetchSlotsByCarId(carId);
    }

    @Override
    public List<CarInventory> fetchAllCars(String emailId) {
        List<CarInventory> carInventoryList=carInventoryOpenFeignInterface.fetchOtherCarsExceptCurrentEmail(emailId);
        Collections.sort(carInventoryList, carInventoryComparator);
        return carInventoryList;
    }

    @Override
    public Set<String> fetchCarManufacturer() {
        return carInventoryOpenFeignInterface.fetchCarModels();
    }

    @Override
    public List<List<String>> fetchCarReviews(String carId) {
        return carInventoryOpenFeignInterface.fetchCarReviews(carId);
    }

    @Override
    public List<CarInventory> filterPriceRange(Map<String, Object> mapper, HttpSession httpSession) {
        String emailId=httpSession.getAttribute("sessionEmailId").toString();
        String carManufacturer=mapper.get("carManufacturer").toString().toUpperCase();
        Float minPrice=Float.valueOf(mapper.get("minPrice").toString())-1; //Inclusive
        Float maxPrice=Float.valueOf(mapper.get("maxPrice").toString())+1;  //Inclusive
        List<CarInventory> filteredCarsList=carInventoryOpenFeignInterface.filterPriceRange(emailId, carManufacturer, minPrice, maxPrice);
        Collections.sort(filteredCarsList, carInventoryComparator);
        return filteredCarsList;
    }

    @Override
    public List<CarInventory> filterPrice(Map<String, Object> mapper, HttpSession httpSession) {
        String emailId=httpSession.getAttribute("sessionEmailId").toString();
        float price=Float.valueOf(mapper.get("price").toString());
        int option=Integer.valueOf(mapper.get("option").toString());
        List<CarInventory> filteredCarsList=carInventoryOpenFeignInterface.filterCarsByPrice(emailId, price, option);
        Collections.sort(filteredCarsList, carInventoryComparator);
        return filteredCarsList;
    }

    @Override
    public List<CarInventory> filterCarsByLocation(Map<String, Object> mapper, HttpSession httpSession) {
        String emailId=httpSession.getAttribute("sessionEmailId").toString();
        carInventory.setLocality(mapper.get("locality").toString());
        carInventory.setDistrict(mapper.get("district").toString());
        carInventory.setState(mapper.get("state").toString());
        List<CarInventory> filteredCarsList=carInventoryOpenFeignInterface.fetchCarsByLocation(carInventory, emailId);
        Collections.sort(filteredCarsList, carInventoryComparator);
        return filteredCarsList;
    }

    @Override
    public RedirectView updateCarDetails(HttpServletRequest httpServletRequest) {
        redirectView.setUrl("/car-rental-system/app/fetchOwnedCars");
        carInventory.set_id(httpServletRequest.getParameter("carId").toString());
        carInventory.setCarModel(httpServletRequest.getParameter("carModel").toString());
        carInventory.setManufacturer(httpServletRequest.getParameter("manufacturer").toString());
        carInventory.setYear(Integer.valueOf(httpServletRequest.getParameter("year").toString()));
        carInventory.setPricePerDay(Float.valueOf(httpServletRequest.getParameter("pricePerDay").toString()));
        //Updating Car Details
        carInventoryOpenFeignInterface.updateCarDetails(carInventory);
        return redirectView;
    }

    @Override
    public RedirectView updateLocationDetails(HttpServletRequest httpServletRequest) {
        redirectView.setUrl("/car-rental-system/app/fetchOwnedCars");
        carInventory.set_id(httpServletRequest.getParameter("carId").toString());
        carInventory.setPinCode(Integer.valueOf(httpServletRequest.getParameter("postalCode").toString()));
        carInventory.setLocality(httpServletRequest.getParameter("locality").toString());
        carInventory.setDistrict(httpServletRequest.getParameter("district").toString());
        carInventory.setState(httpServletRequest.getParameter("state").toString());
        carInventory.setCountry("IN");
        carInventoryOpenFeignInterface.updateLocationDetails(carInventory);
        return redirectView;
    }

    @Override
    public ReservationDetails updateRating(Map<String, Object> mapper) {
        String carId=mapper.get("carId").toString();
        float rating=Float.valueOf(mapper.get("rating").toString());
        carInventory = carInventoryOpenFeignInterface.updateCarRating(carId, rating);
        return reservationDetailsOpenFeignInterface.updateRating(mapper.get("orderId").toString()).getBody();
    }

    @Override
    public boolean postReview(Map<String, Object> mapper, HttpSession httpSession) {
        List<String> payloadData=new LinkedList<>();
        payloadData.add(httpSession.getAttribute("sessionEmailId").toString());
        payloadData.add(mapper.get("feedBack").toString().replace(",","."));
        payloadData.add(String.valueOf(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth())));
        payloadData.add(String.valueOf(LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute())));
        return carInventoryOpenFeignInterface.postReview(mapper.get("carId").toString(), payloadData);
    }

    @Override
    public boolean deleteCarFromInventory(Map<String, Object> mapper) {
        return carInventoryOpenFeignInterface.deleteCarFromInventory(mapper.get("carId").toString());
    }

    @Override
    public boolean registerCarIntoInventory(Map<String, Object> mapper, HttpSession httpSession) {
        carInventory.set_id(mapper.get("carId").toString());
        carInventory.setOwnedBy(httpSession.getAttribute("sessionEmailId").toString());
        carInventory.setCarModel(mapper.get("carModel").toString().toUpperCase());
        carInventory.setManufacturer(mapper.get("manufacturer").toString().toUpperCase());
        carInventory.setYear(Integer.valueOf(mapper.get("year").toString()));
        carInventory.setPricePerDay(Float.valueOf(mapper.get("pricePerDay").toString()));
        carInventory.setLocality(mapper.get("locality").toString());
        carInventory.setDistrict(mapper.get("district").toString());
        carInventory.setState(mapper.get("state").toString());
        carInventory.setCountry("IN");
        carInventory.setPinCode(Integer.valueOf(mapper.get("postalCode").toString()));
        return carInventoryOpenFeignInterface.registerCarIntoInventory(carInventory);
    }
}
