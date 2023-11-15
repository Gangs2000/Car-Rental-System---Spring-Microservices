package com.carrentalsystem.restapi.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Component
@Entity
public class OTPBucket {
    @Id
    private String emailId;
    private String otp;
}
