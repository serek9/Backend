package com.proyecto.serflix.service;

import com.proyecto.serflix.domain.Forecast;
import com.proyecto.serflix.domain.Location;
import com.proyecto.serflix.domain.Request;
import com.proyecto.serflix.domain.User;
import com.proyecto.serflix.domain.enumeration.Company;
import com.proyecto.serflix.domain.enumeration.Type;
import com.proyecto.serflix.repository.ForecastRepository;
import com.proyecto.serflix.repository.LocationRepository;
import com.proyecto.serflix.repository.RequestRepository;
import com.proyecto.serflix.repository.UserRepository;
import com.proyecto.serflix.security.SecurityUtils;
import com.proyecto.serflix.service.MapsAPI.MapsDTOService;
import com.proyecto.serflix.service.WeatherDatabase.WeatherDTOService;
import com.proyecto.serflix.web.rest.dto.RequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class RequestService {
    @Inject
    private RequestRepository requestRepository;

    @Inject
    private UserRepository userRepository;

    @Autowired
    private MapsDTOService mapsDTOService;

    @Autowired
    private WeatherDTOService weatherDTOService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ForecastRepository forecastRepository;

    public Request buildRequest(RequestDTO requestFromAndroid){

        Request request = new Request();
        requestRepository.save(request);
        Set<Forecast> forecasts = new HashSet<>();
        String locationFromAndroid = requestFromAndroid.getLocation();
        if(locationFromAndroid.equals("") || locationFromAndroid.isEmpty() || locationFromAndroid == null){
            locationFromAndroid = "";
        }
        Location location = mapsDTOService.getLocation(locationFromAndroid);
        locationRepository.save(location);
        Type type = requestFromAndroid.getType();
        String name = type+" recommendation from "+requestFromAndroid.getCreationDate();

        Company company = requestFromAndroid.getCompany();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH);
        String viewDateStr = requestFromAndroid.getViewDate();
        String creationDateStr = requestFromAndroid.getCreationDate();
        Date viewDateD = new Date();
        Date creationDateD = new Date();
        try {
            viewDateD = formatter.parse(viewDateStr);
            creationDateD = formatter.parse(creationDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ZonedDateTime viewDate =
            ZonedDateTime.ofInstant(viewDateD.toInstant(), ZoneId.systemDefault());
        ZonedDateTime creationDate =
            ZonedDateTime.ofInstant(creationDateD.toInstant(), ZoneId.systemDefault());
        User userRequester = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        if (viewDate.isBefore(ZonedDateTime.now().plusHours(3))){
            Forecast currentForecast = weatherDTOService.getCurrentForecast(requestFromAndroid.getLocation());
            currentForecast.setRequest(request);
            //Save forecast
            forecastRepository.save(currentForecast);
            forecasts.add(currentForecast);
        }
        request.setType(type);
        request.setName(name);
        request.setViewDate(viewDate);
        request.setCreationDate(creationDate);
        request.setCompany(company);
        request.setUserRequester(userRequester);
        location.setLatLon(requestFromAndroid.getLocation());
        request.setLocation(location);
        request.setForecasts(forecasts);
        return request;
    }
}
