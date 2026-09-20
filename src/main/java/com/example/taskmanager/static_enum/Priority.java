package com.example.taskmanager.static_enum;

public enum Priority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    URGENT("URGENT");

    private final String priority;

    Priority(String priority){
        this.priority = priority;
    }

    private String getPriority(){
        return priority;
    }

    public static Priority valueFrom(String value){
        for(Priority currPriority : values()){
            if(currPriority.getPriority().equals(value))
                return  currPriority;
        }
        throw new IllegalArgumentException("Invalid value for Status type Enum: "+ value);
    }

}
