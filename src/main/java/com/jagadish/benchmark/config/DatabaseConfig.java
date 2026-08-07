package com.jagadish.benchmark.config;

public class DatabaseConfig {

    public static final String URI = System.getenv("COGNODB_URI");
    public static final String USERNAME = System.getenv("COGNODB_USERNAME");
    public static final String PASSWORD = System.getenv("COGNODB_PASSWORD");
}