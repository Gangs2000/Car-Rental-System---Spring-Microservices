package com.carrentalsystem.emailservice.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Component
@Getter
@Setter
public class ReservationDetails implements Serializable {
    @Id
    private String _id;     //Razorpay ID
    private String paymentId;   //Payment ID will be received from Razor pay app
    private String refundId;    //Refund sets if trip is cancelled
    private String carId;
    private String rentedBy;
    private String tripStatus;
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate tripStartDate;
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate tripEndDate;
    private float amount;
    private String currencyType;
    private String orderStatus;
    private String cancellationReason;
    private String cancelledBy;
    private int noOfAttempts;
    private LocalDateTime timestamp;
    private boolean ratingGiven;
}
