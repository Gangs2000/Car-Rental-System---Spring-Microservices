package com.carrentalsystem.restapi.Service;

import com.carrentalsystem.restapi.Interface.CarInventoryInterface;
import com.carrentalsystem.restapi.Model.CarInventory;
import com.carrentalsystem.restapi.Model.ReservationDetails;
import com.carrentalsystem.restapi.Repository.CarInventoryRepository;
import com.carrentalsystem.restapi.Repository.ReservationDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarInventoryService implements CarInventoryInterface {
    @Autowired CarInventoryRepository carInventoryRepository;
    @Autowired ReservationDetailsRepository reservationDetailsRepository;
    @Autowired CarInventory existingCarInventory;
    @Autowired ReservationDetails existingReservationDetails;
    @Autowired CarInventoryInterface carInventoryInterface;

    @Override
    public boolean registerNewCar(CarInventory carInventory) {
        String carNumber=carInventory.get_id().toUpperCase();
        String[] words=carNumber.split(" ");
        String carId=Arrays.stream(words).reduce((a,b)->(a+b)).get();
        if(!carInventoryRepository.existsById(carId)){
            carInventory.set_id(carId);
            //Initialise an empty reservation list since car is newly registered
            carInventory.setReservationIds(new LinkedList<>());
            //Initialise an empty reviews list since car is newly registered
            carInventory.setReviews(new LinkedList<>());
            //Initializing list with float values 0th index - overall rating sum, 1st index - people count, 2nd index - current rating
            List<Float> initializeList=List.of(0.0f, 0.0f, 0.0f);
            carInventory.setCarRating(initializeList);
            carInventoryRepository.save(carInventory);
            return true;
        }
        return false;
    }

    @Override
    public int isCarAvailable(String carId, LocalDate tripStartDate, LocalDate tripEndDate) {
        String[] words=carId.split(" ");
        existingCarInventory=carInventoryRepository.findById(Arrays.stream(words).reduce((a,b)->(a+b)).get()).get();
        List<String> reservationIds=existingCarInventory.getReservationIds();
        PriorityQueue<List<LocalDate>> fetchAllBookedTripDates=new PriorityQueue<>(new Comparator<List<LocalDate>>() {
            @Override
            public int compare(List<LocalDate> o1, List<LocalDate> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });
        for(String reservationId : reservationIds){
            existingReservationDetails=reservationDetailsRepository.findById(reservationId).get();
            if(existingReservationDetails.getTripStatus().equals("NOT STARTED") || existingReservationDetails.getTripStatus().equals("STARTED"))
                fetchAllBookedTripDates.add(List.of(existingReservationDetails.getTripStartDate(), existingReservationDetails.getTripEndDate()));
        }
        return carInventoryInterface.tripSlotValidation(fetchAllBookedTripDates, tripStartDate, tripEndDate);
    }

    @Override
    public CarInventory fetchCarById(String carId) {
        String[] words=carId.split(" ");
        return carInventoryRepository.findById(Arrays.stream(words).reduce((a,b)->(a+b)).get()).orElse(null);
    }

    @Override
    public List<CarInventory> fetchCarsByUserEmail(String mailId) {
        return carInventoryRepository.findByOwnedBy(mailId);
    }

    @Override
    public List<CarInventory> fetchOtherCarsExceptCurrentEmail(String emailId) {
        return carInventoryRepository.findAllExceptOwnedBy(emailId);
    }

    @Override
    public List<List<LocalDate>> fetchSlotsByCarId(String carId) {
        List<List<LocalDate>> bookedSlots=new LinkedList<>();
        List<String> reservationIds=carInventoryRepository.findById(carId).get().getReservationIds();
        for(String reservationId : reservationIds){
            ReservationDetails reservationDetails=reservationDetailsRepository.findById(reservationId).get();
            if(reservationDetails.getTripStatus().equals("NOT STARTED") || reservationDetails.getTripStatus().equals("STARTED")) {
                LocalDate startDate = reservationDetails.getTripStartDate();
                LocalDate endDate = reservationDetails.getTripEndDate();
                bookedSlots.add(List.of(startDate, endDate));
            }
        }
        return bookedSlots.stream().sorted(Comparator.comparing(date -> date.get(0))).collect(Collectors.toList());
    }

    @Override
    public Set<String> fetchCarModels() {
        Set<String> carModels=carInventoryRepository.findAll().stream().map(car->car.getManufacturer().toLowerCase()).collect(Collectors.toSet());
        return carModels;
    }

    @Override
    public List<CarInventory> fetchCarsByGeoLocation(String geoLocationInfo, int option) {
        List<CarInventory> carsList=new LinkedList<>();
        switch (option){
            case 1: carsList=carInventoryRepository.findByLocality(geoLocationInfo); break;     //Filter cars by locality
            case 2: carsList=carInventoryRepository.findByDistrict(geoLocationInfo); break;     //Filter cars by district
            case 3: carsList=carInventoryRepository.findByState(geoLocationInfo); break;     //Filter cars by state
            case 4: carsList=carInventoryRepository.findByPinCode(Integer.valueOf(geoLocationInfo)); break;     //Filter cars by pinCode
        }
        return carsList;
    }

    @Override
    public List<CarInventory> fetchCarsByLocation(String emailId, CarInventory carInventory) {
        String locality=carInventory.getLocality();
        String district=carInventory.getDistrict();
        String state=carInventory.getState();
        return carInventoryRepository.findByOwnedByNotAndLocalityAndDistrictAndState(emailId, locality, district, state);
    }

    @Override
    public List<List<String>> fetchCarReviews(String carId) {
        return carInventoryRepository.findById(carId).get().getReviews();
    }

    @Override
    public List<CarInventory> filterCarsByPrice(String emailId, float price, int option) {
        List<CarInventory> carsList=new LinkedList<>();
        switch (option){
            case 1: carsList=carInventoryRepository.findByOwnedByNotAndPricePerDayGreaterThanEqual(emailId, price); break;      //Filter cars by price above or equal
            case 2: carsList=carInventoryRepository.findByOwnedByNotAndPricePerDayLessThanEqual(emailId, price); break;        //Filter cars by price below
        }
        return carsList;
    }

    @Override
    public List<CarInventory> filterPriceRange(String emailId, String carManufacturer, float minPrice, float maxPrice) {
        //Exclude Car Model Option
        if(carManufacturer.equals("EXCLUDE"))
            return carInventoryRepository.findByOwnedByNotAndPricePerDayBetween(emailId, minPrice, maxPrice);
        //Include Car Model Option
        else
            return carInventoryRepository.findByOwnedByNotAndManufacturerAndPricePerDayBetween(emailId, carManufacturer, minPrice, maxPrice);
    }

    @Override
    public List<CarInventory> filterCarsUsingCustomValue(CarInventory carInventory, float minPrice, float maxPrice) {
        //Custom filter with Car Model
        if(Optional.ofNullable(carInventory.getCarModel()).isPresent()) {
            return carInventoryRepository.findByCarModelAndLocalityAndDistrictAndStateAndPricePerDayBetween(
                    carInventory.getCarModel(), carInventory.getLocality(), carInventory.getDistrict(), carInventory.getState(),
                    minPrice, maxPrice
            );
        }
        //Custom filter without car model
        else
            return carInventoryRepository.findByLocalityAndDistrictAndStateAndPricePerDayBetween(
                    carInventory.getLocality(), carInventory.getDistrict(), carInventory.getState(),
                    minPrice, maxPrice
            );
    }

    @Override
    public CarInventory updateCarDetails(CarInventory carInventory) {
        String[] words=carInventory.get_id().split(" ");
        existingCarInventory=carInventoryRepository.findById(Arrays.stream(words).reduce((a,b)->(a+b)).get()).get();
        existingCarInventory.setCarModel(carInventory.getCarModel());
        existingCarInventory.setManufacturer(carInventory.getManufacturer());
        existingCarInventory.setYear(carInventory.getYear());
        existingCarInventory.setPricePerDay(carInventory.getPricePerDay());
        return carInventoryRepository.save(existingCarInventory);
    }

    @Override
    public CarInventory updateLocationDetails(CarInventory carInventory) {
        String[] words=carInventory.get_id().split(" ");
        existingCarInventory=carInventoryRepository.findById(Arrays.stream(words).reduce((a,b)->(a+b)).get()).get();
        existingCarInventory.setLocality(carInventory.getLocality());
        existingCarInventory.setDistrict(carInventory.getDistrict());
        existingCarInventory.setState(carInventory.getState());
        existingCarInventory.setCountry(carInventory.getCountry());
        existingCarInventory.setPinCode(carInventory.getPinCode());
        return carInventoryRepository.save(existingCarInventory);
    }

    @Override
    public CarInventory updateCarRating(String carId, float rate) {
        existingCarInventory=carInventoryRepository.findById(carId).get();
        List<Float> ratingList=existingCarInventory.getCarRating();
        float totalSum=ratingList.get(0)+rate;
        float ratingGivenCount=ratingList.get(1)+1;
        existingCarInventory.setCarRating(List.of(totalSum, ratingGivenCount, Float.valueOf(String.format("%.1f", (totalSum/ratingGivenCount)))));
        return carInventoryRepository.save(existingCarInventory);
    }

    @Override
    public boolean postReview(String carId, List<String> payload) {
        existingCarInventory=carInventoryRepository.findById(carId).get();
        List<List<String>> reviews=existingCarInventory.getReviews();
        reviews.add(payload);
        existingCarInventory.setReviews(reviews);
        carInventoryRepository.save(existingCarInventory);
        return true;
    }

    @Override
    public boolean deleteCarFromInventory(String carId) {
        existingCarInventory=carInventoryRepository.findById(carId).get();
        if(existingCarInventory.getReservationIds().size()==0){
            carInventoryRepository.deleteById(carId);
            return true;
        }
        //Return false since car is rented by someone
        return false;
    }

    @Override
    public int tripSlotValidation(PriorityQueue<List<LocalDate>> fetchAllBookedTripDates, LocalDate tripStartDate, LocalDate tripEndDate) {
        // returning 1 means slot is available
        // returning 2 means slot is booked
        // returning 3 means given date is not valid date
        Set<LocalDate> allBookedDatesForTheCar=new HashSet<>();
        if (!tripStartDate.equals(LocalDate.now()) && !tripEndDate.equals(LocalDate.now()) && !tripStartDate.isBefore(LocalDate.now()) && !tripEndDate.isBefore(LocalDate.now())) {
            if(tripEndDate.isBefore(tripStartDate))
                return 3;
            while(!fetchAllBookedTripDates.isEmpty()){
                LocalDate startDate=fetchAllBookedTripDates.peek().get(0);
                LocalDate endDate=fetchAllBookedTripDates.peek().get(1);
                allBookedDatesForTheCar.add(startDate);
                while(startDate.isBefore(endDate)){
                    startDate=startDate.plusDays(1);
                    allBookedDatesForTheCar.add(startDate);
                }
                allBookedDatesForTheCar.add(endDate);
                fetchAllBookedTripDates.poll();
            }
            return (!allBookedDatesForTheCar.contains(tripStartDate) && !allBookedDatesForTheCar.contains(tripEndDate))?(1):(2);
        }
        return 3;
    }
}
