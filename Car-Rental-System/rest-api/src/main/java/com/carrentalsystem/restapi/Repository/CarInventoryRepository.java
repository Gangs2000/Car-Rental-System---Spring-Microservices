package com.carrentalsystem.restapi.Repository;

import com.carrentalsystem.restapi.Model.CarInventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarInventoryRepository extends MongoRepository<CarInventory, String> {
    List<CarInventory> findByOwnedBy(String emailId);
    @Query("{'ownedBy': {$ne: ?0}}")
    List<CarInventory> findAllExceptOwnedBy(String emailId);
    List<CarInventory> findByLocality(String locality);
    List<CarInventory> findByDistrict(String district);
    List<CarInventory> findByState(String state);
    List<CarInventory> findByPinCode(int pinCode);
    List<CarInventory> findByOwnedByNotAndPricePerDayGreaterThanEqual(String emailId, float price);
    List<CarInventory> findByOwnedByNotAndPricePerDayLessThanEqual(String emailId, float price);
    List<CarInventory> findByOwnedByNotAndManufacturerAndPricePerDayBetween(String emailId, String carManufacturer, float minPrice, float maxPrice);
    List<CarInventory> findByOwnedByNotAndPricePerDayBetween(String emailId, float minPrice, float maxPrice);
    List<CarInventory> findByOwnedByNotAndLocalityAndDistrictAndState(String emailId, String locality, String district, String state);
    List<CarInventory> findByCarModelAndLocalityAndDistrictAndStateAndPricePerDayBetween(String carModel, String locality, String district, String state, float minPrice, float maxPrice);
    List<CarInventory> findByLocalityAndDistrictAndStateAndPricePerDayBetween(String locality, String district, String state, float minPrice, float maxPrice);
}
