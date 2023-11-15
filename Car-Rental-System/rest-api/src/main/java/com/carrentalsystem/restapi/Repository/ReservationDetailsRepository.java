package com.carrentalsystem.restapi.Repository;

import com.carrentalsystem.restapi.Model.ReservationDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDetailsRepository extends MongoRepository<ReservationDetails, String> {
    List<ReservationDetails> findByRentedBy(String emailId);
    List<ReservationDetails> findByRentedByAndOrderStatus(String emailId, String orderStatus);
    List<ReservationDetails> findByRentedByAndTripStatusAndOrderStatus(String emailId, String tripStatus, String orderStatus);
}
