package org.launchcode.techjobs.persistent.models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Employer extends AbstractEntity {

    @NotBlank(message = "Location is required.")
    @Size(min = 2, max = 50, message = "message must be between 2 and 50 characters long.")
    private String location;

    public Employer(){}

    public String getLocation(){
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
