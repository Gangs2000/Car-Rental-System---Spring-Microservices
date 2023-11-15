package com.carrentalsystem.restapi.Service;

import com.carrentalsystem.restapi.Interface.DataReportsInterface;
import com.carrentalsystem.restapi.Model.CarInventory;
import com.carrentalsystem.restapi.Model.ReservationDetails;
import com.carrentalsystem.restapi.Repository.CarInventoryRepository;
import com.carrentalsystem.restapi.Repository.ReservationDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataReportsService implements DataReportsInterface {

    @Autowired ReservationDetails reservationDetails;
    @Autowired CarInventoryRepository carInventoryRepository;
    @Autowired ReservationDetailsRepository reservationDetailsRepository;

    @Override
    public List<List<Object>> fetchCurrentYearRevenueReport(String emailId) {
        List<List<Object>> currentYearRevenueReport=new LinkedList<>();
        Map<String, Float> monthExpenseMapper=new LinkedHashMap<>();
        monthExpenseMapper.put("JANUARY", 0f); monthExpenseMapper.put("FEBRUARY", 0f); monthExpenseMapper.put("MARCH", 0f); monthExpenseMapper.put("APRIL", 0f);
        monthExpenseMapper.put("MAY", 0f); monthExpenseMapper.put("JUNE", 0f); monthExpenseMapper.put("JULY", 0f); monthExpenseMapper.put("AUGUST", 0f);
        monthExpenseMapper.put("SEPTEMBER", 0f); monthExpenseMapper.put("OCTOBER", 0f); monthExpenseMapper.put("NOVEMBER", 0f); monthExpenseMapper.put("DECEMBER", 0f);
        List<CarInventory> currentUserList=carInventoryRepository.findByOwnedBy(emailId).stream().collect(Collectors.toList());
        List<String> reservationIds=currentUserList.stream().map(carInventory->carInventory.getReservationIds()).flatMap(list->list.stream()).collect(Collectors.toList());
        for(String reservationId : reservationIds){
            reservationDetails=reservationDetailsRepository.findById(reservationId).get();
            String getMonth=reservationDetails.getTripStartDate().getMonth().toString();
            monthExpenseMapper.put(getMonth, monthExpenseMapper.get(getMonth)+reservationDetails.getAmount());
        }
        for(Map.Entry<String, Float> entry : monthExpenseMapper.entrySet())
            currentYearRevenueReport.add(List.of(entry.getKey(), entry.getValue()));
        return currentYearRevenueReport;
    }

    @Override
    public List<List<Object>> fetchOverAllRevenueReport(String emailId) {
        int getCurrentYear= LocalDate.now().getYear(), limit=0;
        List<List<Object>> overAllReport=new LinkedList<>();
        Map<String, Float> yearExpenseMapper=new LinkedHashMap<>();
        while(limit<8){
            yearExpenseMapper.put(String.valueOf(getCurrentYear-limit), 0f);
            limit++;
        }
        List<CarInventory> currentUserList=carInventoryRepository.findByOwnedBy(emailId).stream().collect(Collectors.toList());
        List<String> reservationIds=currentUserList.stream().map(carInventory->carInventory.getReservationIds()).flatMap(list->list.stream()).collect(Collectors.toList());
        for(String reservationId : reservationIds){
            reservationDetails=reservationDetailsRepository.findById(reservationId).get();
            String getYear=String.valueOf(reservationDetails.getTripStartDate().getYear());
            if(yearExpenseMapper.containsKey(getYear))
                yearExpenseMapper.put(getYear, yearExpenseMapper.get(getYear)+reservationDetails.getAmount());
        }
        for(Map.Entry<String, Float> entry : yearExpenseMapper.entrySet())
            overAllReport.add(List.of(entry.getKey(), entry.getValue()));
        return overAllReport;
    }

    @Override
    public List<List<Object>> fetchRevenueFromEachCar(String emailId) {
        List<String> ownedCars=carInventoryRepository.findByOwnedBy(emailId).stream().map(carInventory->carInventory.get_id()).collect(Collectors.toList());
        Map<String, Float> revenueCarMapper=new LinkedHashMap<>();
        List<List<Object>> revenueReportPerCar=new LinkedList<>();
        for(String carId : ownedCars){
            List<String> reservationIds=carInventoryRepository.findById(carId).get().getReservationIds();
            Float revenueAmount=0f;
            for(String reservationId : reservationIds)
                revenueAmount+=reservationDetailsRepository.findById(reservationId).get().getAmount();
            revenueCarMapper.put(carId, revenueAmount);
        }
        revenueReportPerCar.add(revenueCarMapper.keySet().stream().collect(Collectors.toList()));
        revenueReportPerCar.add(revenueCarMapper.values().stream().collect(Collectors.toList()));
        return revenueReportPerCar;
    }

    @Override
    public List<List<Object>> fetchRentedCountFromEachCar(String emailId) {
        List<String> ownedCars=carInventoryRepository.findByOwnedBy(emailId).stream().map(carInventory->carInventory.get_id()).collect(Collectors.toList());
        Map<String, Integer> rentedCarCountMapper=new LinkedHashMap<>();
        List<List<Object>> rentedCountReportPerCar=new LinkedList<>();
        for(String carId : ownedCars){
            List<String> reservationIds=carInventoryRepository.findById(carId).get().getReservationIds();
            rentedCarCountMapper.put(carId, reservationIds.size());
        }
        rentedCountReportPerCar.add(rentedCarCountMapper.keySet().stream().collect(Collectors.toList()));
        rentedCountReportPerCar.add(rentedCarCountMapper.values().stream().collect(Collectors.toList()));
        return rentedCountReportPerCar;
    }

    @Override
    public List<List<Object>> fetchRatingReportForEachCar(String emailId) {
        List<String> ownedCars=carInventoryRepository.findByOwnedBy(emailId).stream().map(carInventory->carInventory.get_id()).collect(Collectors.toList());
        Map<String, Float> ratingPerCarMapper=new LinkedHashMap<>();
        List<List<Object>> ratingReportPerCar=new LinkedList<>();
        for(String carId : ownedCars){
            List<Float> ratingDetails=carInventoryRepository.findById(carId).get().getCarRating();
            ratingPerCarMapper.put(carId, ratingDetails.get(0));
        }
        for(Map.Entry<String, Float> entry : ratingPerCarMapper.entrySet())
            ratingReportPerCar.add(List.of(entry.getKey(), entry.getValue()));
        return ratingReportPerCar;
    }

    @Override
    public List<List<Object>> fetchTransactionReport(String emailId) {
        List<List<Object>> transactionReport=new LinkedList<>();
        Map<String, Integer> transactionMapper=new LinkedHashMap<>();
        transactionMapper.put("Paid", 0); transactionMapper.put("Unpaid/Declined", 0); transactionMapper.put("Cancelled/Refunded", 0);
        List<ReservationDetails> transactions=reservationDetailsRepository.findByRentedBy(emailId).stream().toList();
        for(ReservationDetails transaction : transactions){
            String status=transaction.getOrderStatus();
            switch (status){
                case "created" : transactionMapper.put("Unpaid/Declined", transactionMapper.get("Unpaid/Declined")+1); break;
                case "paid" : transactionMapper.put("Paid", transactionMapper.get("Paid")+1); break;
                case "refunded" : transactionMapper.put("Cancelled/Refunded", transactionMapper.get("Cancelled/Refunded")+1); break;
            }
        }
        for(Map.Entry<String, Integer> entry : transactionMapper.entrySet())
            transactionReport.add(List.of(entry.getKey(), entry.getValue()));
        return transactionReport;
    }
}
