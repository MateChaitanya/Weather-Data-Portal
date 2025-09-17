package com.project.sqlConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate; // Import for LocalDate
import java.time.format.DateTimeFormatter; // Import for DateTimeFormatter

@Component
public class WeatherExtractor {

    // API URL template with placeholders for query (city) and dates
    private static final String WEATHER_API_URL_TEMPLATE = "https://api.weatherapi.com/v1/history.json?q=%s&dt=%s&end_dt=%s&key=2a0bf8fccd444896b9974650252107";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Sena@120";

    // Add the array of capital cities
    private static final String[] CAPITAL_CITIES = {
            "Amaravati", "Itanagar", "Dispur", "Patna", "Raipur", "Panaji", "Gandhinagar",
            "Chandigarh", "Shimla", "Ranchi", "Bengaluru", "Thiruvananthapuram", "Bhopal",
            "Mumbai", "Imphal", "Shillong", "Aizawl", "Kohima", "Bhubaneswar",
            "Jaipur", "Gangtok", "Chennai", "Hyderabad", "Agartala",
            "Lucknow", "Dehradun", "Kolkata","Delhi,India","Pune"
    };

    // Define start and end dates for data fetching

    private static final LocalDate END_DATE = LocalDate.now();// Example: July 31, 2024
    private static final LocalDate START_DATE = END_DATE.minusDays(29);

    // Formatter for API date format (YYYY-MM-DD)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public void executeWeatherExtraction() {
        System.out.println("WeatherExtractor: Starting data extraction and database insertion for multiple cities and date range.");
        System.out.println("Fetching data from " + START_DATE.format(DATE_FORMATTER) + " to " + END_DATE.format(DATE_FORMATTER));

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established successfully! Autocommit: " + connection.getAutoCommit());

            String insertSQL = "INSERT INTO Historical_Data (city_name, date, temperature, `conditions`, humidity, rainfall, sunrise, sunset, moonrise, moonset) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertSQL);
            System.out.println("Prepared SQL statement for insertion.");

            // Format dates for API URL
            String formattedStartDate = START_DATE.format(DATE_FORMATTER);
            String formattedEndDate = END_DATE.format(DATE_FORMATTER);


            // Loop through each city
            for (String cityName : CAPITAL_CITIES) {
                System.out.println("\n--- Processing data for city: " + cityName + " ---");

                // Construct the API URL for the current city and dates
                String apiUrl = String.format(WEATHER_API_URL_TEMPLATE, cityName, formattedStartDate, formattedEndDate);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .GET()
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int statusCode = response.statusCode();
                String responseBody = response.body();
                System.out.println("Status Code for " + cityName + ": " + statusCode);

                if (statusCode == 200) {
                    JsonElement jsonElement = JsonParser.parseString(responseBody);
                    JsonObject rootObject = jsonElement.getAsJsonObject();
                    JsonObject forecastObject = rootObject.getAsJsonObject("forecast");

                    if (forecastObject != null) {
                        JsonArray forecastdayArray = forecastObject.getAsJsonArray("forecastday");

                        if (forecastdayArray != null && !forecastdayArray.isEmpty()) {
                            System.out.println("--- Found " + forecastdayArray.size() + " days of data for " + cityName + " ---");

                            for (int i = 0; i < forecastdayArray.size(); i++) {
                                JsonObject currentDayObject = forecastdayArray.get(i).getAsJsonObject();
                                String date = currentDayObject.has("date") ? currentDayObject.get("date").getAsString() : "N/A";
                                System.out.println("Processing Day " + (i + 1) + " (Date: " + date + ") for " + cityName);

                                Double avgTempC = null;
                                Integer avgHumidity = null;
                                Double totalPrecipMm = null;
                                String conditionText = null;
                                String sunrise = null;
                                String sunset = null;
                                String moonrise = null;
                                String moonset = null;

                                JsonObject dayData = currentDayObject.getAsJsonObject("day");
                                if (dayData != null) {
                                    if (dayData.has("avgtemp_c")) avgTempC = dayData.get("avgtemp_c").getAsDouble();
                                    if (dayData.has("avghumidity")) avgHumidity = dayData.get("avghumidity").getAsInt();
                                    if (dayData.has("totalprecip_mm")) totalPrecipMm = dayData.get("totalprecip_mm").getAsDouble();
                                    JsonObject conditionObject = dayData.getAsJsonObject("condition");
                                    if (conditionObject != null && conditionObject.has("text")) {
                                        conditionText = conditionObject.get("text").getAsString();
                                    }
                                }

                                JsonObject astroData = currentDayObject.getAsJsonObject("astro");
                                if (astroData != null) {
                                    if (astroData.has("sunrise")) sunrise = astroData.get("sunrise").getAsString();
                                    if (astroData.has("sunset")) sunset = astroData.get("sunset").getAsString();
                                    if (astroData.has("moonrise")) moonrise = astroData.get("moonrise").getAsString();
                                    if (astroData.has("moonset")) moonset = astroData.get("moonset").getAsString();
                                }

                                preparedStatement.setString(1, cityName);
                                preparedStatement.setString(2, date);
                                if (avgTempC != null) preparedStatement.setDouble(3, avgTempC); else preparedStatement.setNull(3, java.sql.Types.DECIMAL);
                                preparedStatement.setString(4, conditionText);
                                if (avgHumidity != null) preparedStatement.setInt(5, avgHumidity); else preparedStatement.setNull(5, java.sql.Types.INTEGER);
                                if (totalPrecipMm != null) preparedStatement.setDouble(6, totalPrecipMm); else preparedStatement.setNull(6, java.sql.Types.DECIMAL);
                                preparedStatement.setString(7, sunrise);
                                preparedStatement.setString(8, sunset);
                                preparedStatement.setString(9, moonrise);
                                preparedStatement.setString(10, moonset);

                                int rowsAffected = preparedStatement.executeUpdate();
                                System.out.println("Inserted " + rowsAffected + " row(s) for date: " + date + " in " + cityName);
                            }
                        } else {
                            System.out.println("The 'forecastday' array is empty for " + cityName + ". No historical data found.");
                        }
                    } else {
                        System.out.println("The 'forecast' object is missing or malformed for " + cityName + " in the API response.");
                    }
                } else {
                    System.err.println("API call failed for " + cityName + " with status code: " + statusCode + ". Error Response: " + responseBody);
                }
            }
            System.out.println("\nAll weather data extraction and insertion completed.");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred during API call or parsing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}