package com.carrentalsystem.emailservice.Repository;

import com.carrentalsystem.emailservice.Model.CarInventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarInventoryRepository extends MongoRepository<CarInventory, String> {

}
