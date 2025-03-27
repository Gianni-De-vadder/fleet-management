package fr.extia.mentoring.fleetmanagement.entities;

import java.util.Arrays;

public enum DangerType {
	EXPLOSIVE,
	FLAMMABLE,
	RADIOACTIVE,
	CORROSIVE,
	TOXIC;
	

    public static DangerType fromValue(String str) {
        return Arrays.asList(values())
                .stream()
                .filter(value -> value.toString().equalsIgnoreCase(str))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No danger type exists for value: %s".formatted(str)));
    }
}
