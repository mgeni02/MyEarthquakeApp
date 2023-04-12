package com.example.mgeni_ingrid_s2034327;

public class Earthquake {

    private String title;
    private String link;
    private String date;
    private String description;
    private double magnitude;

    public Earthquake(String title, String link, String date, String description, double magnitude) {
        this.title = title;
        this.link = link;
        this.date = date;
        this.description = description;
        this.magnitude = magnitude;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n"
                + "Date: " + date + "\n"
                + "Link: " + link + "\n"
                + "Description: " + description + "\n"
                + "Magnitude: " + magnitude;
    }
}
