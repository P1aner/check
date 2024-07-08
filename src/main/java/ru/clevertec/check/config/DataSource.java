package ru.clevertec.check.config;


public class DataSource {

    private DataSource() {
    }

    public static final String DATASOURCE_URL = System.getProperty("datasource.url");
    public static final String DATASOURCE_USERNAME = System.getProperty("datasource.username");
    public static final String DATASOURCE_PASSWORD = System.getProperty("datasource.password");

}