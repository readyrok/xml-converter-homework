package com.codecool.xml;

public class Product {
    private final String description;
    private final String gtin;
    private final String currency;
    private final String price;
    private final String supplier;

    public Product(String description, String gtin, String currency, String price, String supplier) {
        this.description = description;
        this.gtin = gtin;
        this.currency = currency;
        this.price = price;
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public String getGtin() {
        return gtin;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPrice() {
        return price;
    }

    public String getSupplier() {
        return supplier;
    }

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                ", gtin='" + gtin + '\'' +
                ", currency='" + currency + '\'' +
                ", price='" + price + '\'' +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
