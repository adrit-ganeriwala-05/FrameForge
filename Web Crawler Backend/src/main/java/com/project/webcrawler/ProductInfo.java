package com.project.webcrawler;

public class ProductInfo {
    private final String name;
    private final double price;
    private final String link;

    public ProductInfo(String name, double price, String link) {
        this.name = name;
        this.price = price;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " - " + link;
    }
}
