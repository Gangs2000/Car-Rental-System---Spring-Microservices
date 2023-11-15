package com.carrentalsystem.emailservice.Repository;

import com.carrentalsystem.emailservice.Model.ReservationDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationDetailsRepository extends MongoRepository<ReservationDetails, String> {

}
