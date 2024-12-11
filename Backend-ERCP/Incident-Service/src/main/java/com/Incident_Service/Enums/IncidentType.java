package com.Incident_Service.Enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IncidentType {
    CRIME("CRIME"),
    MEDICAL_EMERGENCY("MEDICAL_EMERGENCY"),
    FIRE("FIRE"),
    NATURAL_DISASTER("NATURAL_DISASTER"),
    HAZMAT("HAZMAT"),
    SOS_REQUEST("SOS_REQUEST");

    private final String value;

    IncidentType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static IncidentType fromValue(String value) {
        for (IncidentType type : IncidentType.values()) {
            if (type.value.equalsIgnoreCase(value.replace(" ", "_"))) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid incident type: " + value);
    }
}

