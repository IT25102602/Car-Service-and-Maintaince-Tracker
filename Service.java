package com.carservicetracker.model;

public class Service {
    private int id;                    // Changed to int
    private String name;
    private String description;
    private String duration;
    private double price;
    private String category;

    public Service() {}

    public Service(int id, String name, String description, String duration, double price, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.category = category;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return id + "|" + name + "|" + description + "|" + duration + "|" + price + "|" + category;
    }
}