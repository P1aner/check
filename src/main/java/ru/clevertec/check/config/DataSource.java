package ru.clevertec.check.config;


public class DataSource {

    private DataSource() {
    }

    public static String datasourceUrl = System.getProperty("datasource.url");
    public static String datasourceUsername = System.getProperty("datasource.username");
    public static String datasourcePassword = System.getProperty("datasource.password");

}