package com.proyecto.serflix.service.WeatherDatabase;

import com.proyecto.serflix.service.dto.WeatherDatabase.WeatherData;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Repository
public interface WeatherDTORepository {
    @GET("forecast/{apiKey}/{coordinates}?units=uk2")
    Call<WeatherData> getWeatherData(@Path("apiKey") String apikey, @Path("coordinates") String coordinates);
    @GET("forecast/{apiKey}/{coordinates},{time}?units=uk2")
    Call<WeatherData> getWeatherDataTime(@Path("apiKey") String apikey, @Path("coordinates") String coordinates, @Path("time") long time);

    //URL ENTERA : https://api.darksky.net/forecast/b663aac760fab18d52b433a1d2c84a5e/37.8267,-122.4233?exclude=minutely,flags
    public static String url = "https://api.darksky.net/";
    public static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
}
