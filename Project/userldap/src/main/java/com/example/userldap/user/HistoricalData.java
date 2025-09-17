package com.example.userldap.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "Historical_Data") // Map to your table name
public class HistoricalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @Column(name = "city_name")
    private String cityName;

    private LocalDate date; // java.sql.Date for date type
    private Double temperature; // decimal(5,2) can be Double
    private String conditions;
    private Double humidity; // decimal(5,2) can be Double
    private Double rainfall; // decimal(5,2) can be Double
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;
    // Constructors
    public HistoricalData() {
    }
    // You might want a constructor for creating new entries without ID
    public HistoricalData(String cityName, LocalDate date, Double temperature, String conditions, Double humidity, Double rainfall, String sunrise, String sunset, String moonrise, String moonset) {
        //used to distinguish method para has same name as class field
        this.cityName = cityName;
        this.date = date;
        this.temperature = temperature;
        this.conditions = conditions;
        this.humidity = humidity;
        this.rainfall = rainfall;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.moonrise = moonrise;
        this.moonset = moonset;
    }
    // Getters and Setters
    public Integer getHistoryId() {
        return historyId;
    }
    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Double getTemperature() {
        return temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public String getConditions() {
        return conditions;
    }
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    public Double getHumidity() {
        return humidity;
    }
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
    public Double getRainfall() {
        return rainfall;
    }
    public void setRainfall(Double rainfall) {
        this.rainfall = rainfall;
    }
    public String getSunrise() {
        return sunrise;
    }
    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }
    public String getSunset() {
        return sunset;
    }
    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
    public String getMoonrise() {
        return moonrise;
    }
    public void setMoonrise(String moonrise) {
        this.moonrise = moonrise;
    }
    public String getMoonset() {
        return moonset;
    }
    public void setMoonset(String moonset) {
        this.moonset = moonset;
    }
}










