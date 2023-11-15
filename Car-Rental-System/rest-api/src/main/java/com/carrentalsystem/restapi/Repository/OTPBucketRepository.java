package com.carrentalsystem.restapi.Repository;

import com.carrentalsystem.restapi.Model.OTPBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPBucketRepository extends JpaRepository<OTPBucket, String> {

}
