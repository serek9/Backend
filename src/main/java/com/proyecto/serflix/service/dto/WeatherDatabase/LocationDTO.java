package com.proyecto.serflix.service.dto.WeatherDatabase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationDTO {
    @SerializedName("lat")
    @Expose
    private Double latitude;
    @SerializedName("lng")
    @Expose
    private Double longitude;


    public LocationDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public String getCoordinates(){
        return this.latitude+","+this.longitude;
    }
}
