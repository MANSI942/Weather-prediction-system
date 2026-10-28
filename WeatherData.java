package com.weatherprediction;

public class WeatherData {

    private final String city;
    private final double temperature;
    private final double windSpeed;
    private final String condition;

    public WeatherData(String city, double temperature,
                       double windSpeed, String condition) {
        this.city = city;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.condition = condition;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getCondition() {
        return condition;
    }
}
