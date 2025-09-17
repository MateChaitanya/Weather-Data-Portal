package com.example.userldap.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<HistoricalData, Integer> {

    //Derived query method to find data by city_name
    List<HistoricalData> findByCityName(String cityName);


    List<HistoricalData> findByCityNameIgnoreCaseAndDateBetween(
            String cityName, LocalDate startDate, LocalDate endDate);
}




