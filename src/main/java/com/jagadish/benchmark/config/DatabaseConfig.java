package com.jagadish.benchmark.config;

public final class DatabaseConfig {

    private DatabaseConfig() {
    }

    public static final String URI = requiredEnv("COGNODB_URI");
    public static final String USERNAME = requiredEnv("COGNODB_USERNAME");
    public static final String PASSWORD = requiredEnv("COGNODB_PASSWORD");

    private static String requiredEnv(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                "Missing required environment variable: " + name
            );
        }

        return value;
    }
}