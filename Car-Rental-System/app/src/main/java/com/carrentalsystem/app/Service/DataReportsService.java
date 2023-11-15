package com.carrentalsystem.app.Service;

import com.carrentalsystem.app.Interface.DataReportsInterface;
import com.carrentalsystem.app.OpenFeign.DataReportsOpenFeignInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataReportsService implements DataReportsInterface {

    @Autowired DataReportsOpenFeignInterface dataReportsOpenFeignInterface;

    @Override
    public List<List<Object>> fetchCurrentYearRevenueReport(String emailId) {
        return dataReportsOpenFeignInterface.fetchCurrentYearRevenueReport(emailId);
    }

    @Override
    public List<List<Object>> fetchOverAllRevenueReport(String emailId) {
        return dataReportsOpenFeignInterface.fetchOverAllRevenueReport(emailId);
    }

    @Override
    public List<List<Object>> fetchRevenueReportFromEachCar(String emailId) {
        return dataReportsOpenFeignInterface.fetchRevenueReportFromEachCar(emailId);
    }

    @Override
    public List<List<Object>> fetchRentedCountPerCar(String emailId) {
        return dataReportsOpenFeignInterface.fetchRentedCountReportForEachCar(emailId);
    }

    @Override
    public List<List<Object>> fetchRatingPerCar(String emailId) {
        return dataReportsOpenFeignInterface.fetchRatingReportForEachCar(emailId);
    }

    @Override
    public List<List<Object>> fetchTransactionReport(String emailId) {
        return dataReportsOpenFeignInterface.fetchTransactionReport(emailId);
    }
}
