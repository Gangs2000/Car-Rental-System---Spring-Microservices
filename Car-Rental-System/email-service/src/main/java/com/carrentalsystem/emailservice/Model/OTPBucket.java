package com.carrentalsystem.emailservice.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Component
public class OTPBucket {
    @Id
    private String emailId;
    private String otp;
}
