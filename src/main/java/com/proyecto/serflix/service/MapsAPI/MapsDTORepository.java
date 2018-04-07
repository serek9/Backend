package com.proyecto.serflix.service.MapsAPI;

import com.proyecto.serflix.domain.Location;
import com.proyecto.serflix.service.dto.MapsAPI.AddressDTO;
import com.proyecto.serflix.service.dto.MovieDatabase.GenreList;
import com.proyecto.serflix.service.dto.MovieDatabase.KeywordList;
import com.proyecto.serflix.service.dto.MovieDatabase.MovieDTO;
import com.proyecto.serflix.service.dto.MovieDatabase.MovieDTOList;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

@Repository
public interface MapsDTORepository {


    //https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyBIsWoDLus9G4yQespRGvvy8_dZeOnw71c

    @GET("geocode/json")
    Call<AddressDTO> geocode(@Query("latlng") String latlng, @Query("api_key") String apiKey);
    public static String url = "https://maps.googleapis.com/maps/api/";
    public static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
}
