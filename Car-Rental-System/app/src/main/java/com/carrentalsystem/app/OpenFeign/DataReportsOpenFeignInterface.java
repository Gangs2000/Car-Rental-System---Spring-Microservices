package com.carrentalsystem.app.OpenFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "reports-and-statistics", url = "localhost:7070/api/car-rental-system/reports-and-statistics")
public interface DataReportsOpenFeignInterface {
    @GetMapping("/fetch/revenue-report/currentYear/mailId/{emailId}")
    List<List<Object>> fetchCurrentYearRevenueReport(@PathVariable("emailId") String emailId);

    @GetMapping("/fetch/revenue-report/overall/mailId/{emailId}")
    List<List<Object>> fetchOverAllRevenueReport(@PathVariable("emailId") String emailId);
    @GetMapping("/fetch/revenue-report/each-car/mailId/{emailId}")
    List<List<Object>> fetchRevenueReportFromEachCar(@PathVariable("emailId") String emailId);
    @GetMapping("/fetch/rented-count/each-car/mailId/{emailId}")
    List<List<Object>> fetchRentedCountReportForEachCar(@PathVariable("emailId") String emailId);
    @GetMapping("/fetch/rating/each-car/mailId/{emailId}")
    List<List<Object>> fetchRatingReportForEachCar(@PathVariable("emailId") String emailId);
    @GetMapping("/fetch/transaction-report/mailId/{emailId}")
    List<List<Object>> fetchTransactionReport(@PathVariable("emailId") String emailId);
}
