package com.carrentalsystem.restapi.Interface;

import java.util.List;

public interface DataReportsInterface {
    List<List<Object>> fetchCurrentYearRevenueReport(String emailId);
    List<List<Object>> fetchOverAllRevenueReport(String emailId);
    List<List<Object>> fetchRevenueFromEachCar(String emailId);
    List<List<Object>> fetchRentedCountFromEachCar(String emailId);
    List<List<Object>> fetchRatingReportForEachCar(String emailId);
    List<List<Object>> fetchTransactionReport(String emailId);
}
