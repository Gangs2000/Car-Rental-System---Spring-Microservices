package com.carrentalsystem.emailservice.Repository;

import com.carrentalsystem.emailservice.Model.OTPBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPBucketRepository extends JpaRepository<OTPBucket, String> {

}
