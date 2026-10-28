package com.weatherprediction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherService {

    public String getWeather(String city) throws Exception {

        String encodedCity = URLEncoder.encode(
                city, StandardCharsets.UTF_8);

        String geoUrl =
                "https://geocoding-api.open-meteo.com/v1/search?name="
                + encodedCity + "&count=1&language=en&format=json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest geoRequest = HttpRequest.newBuilder()
                .uri(URI.create(geoUrl))
                .GET()
                .build();

        HttpResponse<String> geoResponse = client.send(
                geoRequest,
                HttpResponse.BodyHandlers.ofString());

        String geoJson = geoResponse.body();
        System.out.println("City entered: " + city);
System.out.println("Geocoding response: " + geoJson);

        if (!geoJson.contains("\"results\"")) {
            return "City not found. Please enter a valid city.";
        }

        double latitude = extractNumber(geoJson, "latitude");
        double longitude = extractNumber(geoJson, "longitude");

        String weatherUrl =
                "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&current=temperature_2m,relative_humidity_2m,"
                + "wind_speed_10m";

        HttpRequest weatherRequest = HttpRequest.newBuilder()
                .uri(URI.create(weatherUrl))
                .GET()
                .build();

        HttpResponse<String> weatherResponse = client.send(
                weatherRequest,
                HttpResponse.BodyHandlers.ofString());

        String weatherJson = weatherResponse.body();

        double temperature =
                extractNumber(weatherJson, "temperature_2m");

        double humidity =
                extractNumber(weatherJson, "relative_humidity_2m");

        double windSpeed =
                extractNumber(weatherJson, "wind_speed_10m");

        String advice;

if (temperature >= 35) {
    advice = "Very hot weather. Stay hydrated.";
} else if (temperature >= 25) {
    advice = "Warm weather.";
} else if (temperature >= 15) {
    advice = "Pleasant weather.";
} else {
    advice = "Cold weather. Wear warm clothes.";
}

return "City: " + city
        + "\nTemperature: " + temperature + " °C"
        + "\nHumidity: " + humidity + "%"
        + "\nWind Speed: " + windSpeed + " km/h"
        + "\nWeather Advice: " + advice;
    }

   
    private double extractNumber(String json, String key) {
    String pattern = "\"" + key + "\"\\s*:\\s*(-?\\d+(\\.\\d+)?)";

    java.util.regex.Matcher matcher =
            java.util.regex.Pattern.compile(pattern).matcher(json);

    if (matcher.find()) {
        return Double.parseDouble(matcher.group(1));
    }

    throw new IllegalArgumentException(
            "Weather data not found: " + key);
}

public String getForecast(String city) throws Exception {

    String encodedCity = java.net.URLEncoder.encode(
            city, java.nio.charset.StandardCharsets.UTF_8);

    String geoUrl =
            "https://geocoding-api.open-meteo.com/v1/search?name="
            + encodedCity + "&count=1&language=en&format=json";

    java.net.http.HttpClient client =
            java.net.http.HttpClient.newHttpClient();

    java.net.http.HttpRequest geoRequest =
            java.net.http.HttpRequest.newBuilder()
            .uri(java.net.URI.create(geoUrl))
            .GET()
            .build();

    String geoJson = client.send(
            geoRequest,
            java.net.http.HttpResponse.BodyHandlers.ofString()
    ).body();

   if (geoJson.contains("\"results\": null")
        || !geoJson.contains("\"results\"")) {
    return "City not found. Please check the city name and try again.";
}

    double latitude = extractNumber(geoJson, "latitude");
    double longitude = extractNumber(geoJson, "longitude");

    String forecastUrl =
            "https://api.open-meteo.com/v1/forecast"
            + "?latitude=" + latitude
            + "&longitude=" + longitude
            + "&daily=temperature_2m_max,temperature_2m_min,"
            + "precipitation_probability_max"
            + "&forecast_days=5&timezone=auto";

    java.net.http.HttpRequest forecastRequest =
            java.net.http.HttpRequest.newBuilder()
            .uri(java.net.URI.create(forecastUrl))
            .GET()
            .build();

    String json = client.send(
            forecastRequest,
            java.net.http.HttpResponse.BodyHandlers.ofString()
    ).body();

    java.util.regex.Matcher dates =
            java.util.regex.Pattern.compile(
            "\"time\"\\s*:\\s*\\[(.*?)\\]")
            .matcher(json);

    java.util.regex.Matcher maxTemps =
            java.util.regex.Pattern.compile(
            "\"temperature_2m_max\"\\s*:\\s*\\[(.*?)\\]")
            .matcher(json);

    java.util.regex.Matcher minTemps =
            java.util.regex.Pattern.compile(
            "\"temperature_2m_min\"\\s*:\\s*\\[(.*?)\\]")
            .matcher(json);

    java.util.regex.Matcher rain =
            java.util.regex.Pattern.compile(
            "\"precipitation_probability_max\"\\s*:\\s*\\[(.*?)\\]")
            .matcher(json);

    if (!dates.find() || !maxTemps.find()
            || !minTemps.find() || !rain.find()) {
        return "Unable to read forecast data.";
    }

    String[] dateList = dates.group(1).replace("\"", "").split(",");
    String[] maxList = maxTemps.group(1).split(",");
    String[] minList = minTemps.group(1).split(",");
    String[] rainList = rain.group(1).split(",");

    StringBuilder result = new StringBuilder();
    result.append("5-Day Forecast for ").append(city).append("\n\n");

    for (int i = 0; i < dateList.length; i++) {
        result.append(dateList[i].trim())
              .append("\nMin: ").append(minList[i].trim()).append(" °C")
              .append(" | Max: ").append(maxList[i].trim()).append(" °C")
              .append("\nRain chance: ").append(rainList[i].trim()).append("%")
              .append("\n\n");
    }

    return result.toString();
}
}

  