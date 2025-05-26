package fr.extia.mentoring.fleetmanagement.entities;

import java.util.Arrays;

public enum LoadLevel {
    LIGHT,
    MEDIUM,
    HEAVY;

    public static LoadLevel fromValue(String str) {
        return Arrays.asList(values())
                .stream()
                .filter(value -> value.toString().equalsIgnoreCase(str))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No load level exists for value: %s".formatted(str)));
    }
}
