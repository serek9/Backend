package com.proyecto.serflix.web.rest.dto;

import com.proyecto.serflix.domain.enumeration.Company;
import com.proyecto.serflix.domain.enumeration.Type;

public class RequestDTO {
    private Type type;
    private String viewDate;
    private String creationDate;
    private Company company;

    private String location;

    public RequestDTO(Type type, String viewDate, String creationDate, Company company, String location) {
        this.type = type;
        this.viewDate = viewDate;
        this.creationDate = creationDate;
        this.company = company;
        this.location = location;
    }

    public RequestDTO() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "RequestDTO{" +
            "type=" + type +
            ", viewDate=" + viewDate +
            ", creationDate=" + creationDate +
            ", company=" + company +
            ", location=" + location +
            '}';
    }
}
