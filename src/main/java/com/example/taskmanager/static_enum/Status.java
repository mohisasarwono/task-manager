package com.example.taskmanager.static_enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    CREATED("CREATED"),
    ON_PROGRESS("ON_PROGRESS"),
    FINISHED("FINISHED"),
    DELAYED("DELAYED"),
    CANCELED("CANCELLED");

    private final String status;

    Status(String status){
        this.status = status;
    }

    @JsonValue
    public String getStatus(){
        return status;
    }

    @JsonCreator
    public static Status fromValue(String value){
        for(Status currStatus : values()){
            if(currStatus.getStatus().equals(value))
                return currStatus;
        }
        throw  new IllegalArgumentException("Invalid value for Status type Enum: "+ value);
    }



}
