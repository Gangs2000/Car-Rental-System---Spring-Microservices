package com.carrentalsystem.restapi.Controller;

import com.carrentalsystem.restapi.Service.DataReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car-rental-system/reports-and-statistics")
public class DataReportsController {

    @Autowired DataReportsService dataReportsService;

    @GetMapping("/fetch/revenue-report/currentYear/mailId/{emailId}")
    public List<List<Object>> fetchCurrentYearRevenueReport(@PathVariable("emailId") String emailId){
        return dataReportsService.fetchCurrentYearRevenueReport(emailId);
    }

    @GetMapping("/fetch/revenue-report/overall/mailId/{emailId}")
    public List<List<Object>> fetchOverAllRevenueReport(@PathVariable("emailId") String emailId){
        return dataReportsService.fetchOverAllRevenueReport(emailId);
    }

    @GetMapping("/fetch/revenue-report/each-car/mailId/{emailId}")
    public List<List<Object>> fetchRevenueReportFromEachCar(@PathVariable("emailId") String emailId){
        return dataReportsService.fetchRevenueFromEachCar(emailId);
    }

    @GetMapping("/fetch/rented-count/each-car/mailId/{emailId}")
    public List<List<Object>> fetchRentedCountReportForEachCar(@PathVariable("emailId") String emailId){
        return dataReportsService.fetchRentedCountFromEachCar(emailId);
    }

    @GetMapping("/fetch/rating/each-car/mailId/{emailId}")
    public List<List<Object>> fetchRatingReportForEachCar(@PathVariable("emailId") String emailId){
        return dataReportsService.fetchRatingReportForEachCar(emailId);
    }

    @GetMapping("/fetch/transaction-report/mailId/{emailId}")
    public List<List<Object>> fetchTransactionReport(@PathVariable("emailId") String emailId){
        return dataReportsService.fetchTransactionReport(emailId);
    }
}
