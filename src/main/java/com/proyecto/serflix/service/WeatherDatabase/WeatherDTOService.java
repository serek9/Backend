package com.proyecto.serflix.service.WeatherDatabase;


import com.proyecto.serflix.domain.Forecast;
import com.proyecto.serflix.domain.enumeration.Weather;
import com.proyecto.serflix.service.dto.WeatherDatabase.Currently;
import com.proyecto.serflix.service.dto.WeatherDatabase.WeatherData;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.util.Date;

@Service
public class WeatherDTOService {
    public static final String apiKey = "b663aac760fab18d52b433a1d2c84a5e";
    static WeatherDTORepository apiService = WeatherDTORepository.retrofit.create(WeatherDTORepository.class);

    //https://api.darksky.net/forecast/b663aac760fab18d52b433a1d2c84a5e/37.8267,-122.4233?exclude=minutely,flags

    public Forecast getCurrentForecast(String location) {
        Forecast forecast = new Forecast();
        WeatherData weatherData = null;
        //Double temperature, Weather weather
        Call<WeatherData> weatherDataCall = apiService.getWeatherData(apiKey, location);
        try {
            weatherData = weatherDataCall.execute().body();
            Currently currently = weatherData.getCurrently();
            forecast.setTemperature(currently.getTemperature());
            forecast.setWeather(getWeatherFromIcon(currently.getIcon()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forecast;
    }

    public Forecast getHourDayWeather(String location, Date date){
        long unixdate = getDateLongFormat(date);
        Forecast forecast = new Forecast();
        WeatherData weatherData = null;
        Call<WeatherData> weatherDataCall = apiService.getWeatherDataTime(apiKey, location, unixdate);
        try{
            weatherData = weatherDataCall.execute().body();
            forecast.setWeather(getWeatherFromIcon(weatherData.getCurrently().getIcon().toString()));
            forecast.setTemperature(weatherData.getCurrently().getTemperature());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return forecast;
    }

    //Do not delete
    public WeatherData getWeatherData(String location) {
        WeatherData weatherData = null;
        Call<WeatherData> weatherDataCall = apiService.getWeatherData(apiKey, location);
        try {
            weatherData = weatherDataCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherData;
    }

    public long getDateLongFormat(Date date){
        long unixTimestamp = date.getTime()/1000;
        return unixTimestamp;
    }

    public Weather getWeatherFromIcon(String icon) {
        switch (icon) {
            case "clear-day":
                return Weather.CLEAR;
            case "clear-night":
                return Weather.CLEAR;
            case "rain":
                return Weather.RAIN;
            case "snow":
                return Weather.SNOW;
            case "sleet":
                return Weather.SNOW;
            case "wind":
                return Weather.CLEAR;
            case "fog":
                return Weather.CLOUDY;
            case "partly-cloudy-day":
                return Weather.PARTLY_CLOUDY;
            case "partly-cloudy-night":
                return Weather.PARTLY_CLOUDY;
            case "hail":
                return Weather.RAIN;
            case "thunderstorm":
                return Weather.RAIN;
            case "tornado":
                return Weather.CLOUDY;
            default:
                return Weather.CLEAR;
        }
    }
}
