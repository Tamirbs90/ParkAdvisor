package com.example.myapplication;

public enum VehicleType {
    PRIVATE("Private Vehicle"),
    ABOVE_4_TON("Above 4 Tone Vehicle"),
    TWO_AND_HELF_UNTIL_4_TON("2.5-4 Ton Vehicle"),
    SCHOOL_BUS("School Bus"),
    TAXI("Taxi");

    private String text;

    VehicleType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static VehicleType fromString(String text) {
        for (VehicleType type : VehicleType.values()) {
            if (type.text.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}


