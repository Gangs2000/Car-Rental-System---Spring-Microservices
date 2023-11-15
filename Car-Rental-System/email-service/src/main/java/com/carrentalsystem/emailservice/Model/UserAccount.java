package com.carrentalsystem.emailservice.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Component
@Getter
@Setter
public class UserAccount {
    @Id
    private String emailId;
    private String password;
    private String userName;
    private long mobileNumber;
    private String city;
    private String district;
    private String state;
    private String country;
    private int pinCode;
    private LocalDateTime lastUpdated;
}
