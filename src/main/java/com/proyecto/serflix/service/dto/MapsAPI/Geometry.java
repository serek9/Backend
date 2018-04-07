
package com.proyecto.serflix.service.dto.MapsAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.proyecto.serflix.service.dto.WeatherDatabase.LocationDTO;

public class Geometry {

    @SerializedName("location")
    @Expose
    private LocationDTO location;
    @SerializedName("location_type")
    @Expose
    private String locationType;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;
    @SerializedName("bounds")
    @Expose
    private Bounds bounds;

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

}
