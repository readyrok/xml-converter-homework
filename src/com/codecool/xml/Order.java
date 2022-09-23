package com.codecool.xml;

import java.util.ArrayList;

public class Order {
    private final String id;
    private ArrayList<Product> products;

    public Order(String id) {
        this.id = id;
        this.products = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product){
        products.add(product);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", products=" + products +
                '}';
    }
}
