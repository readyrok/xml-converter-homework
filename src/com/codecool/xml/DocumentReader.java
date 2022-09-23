package com.codecool.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DocumentReader {
    private final String path;

    public DocumentReader(String path) {
        this.path = path;
    }

    public NodeList getOrders() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(path);
        doc.getDocumentElement().normalize();
        return doc.getElementsByTagName("order");
    }

    public ArrayList<Order> getOrderList(NodeList orders) {
        ArrayList<Order> orderList = new ArrayList<>();
        for (int i = 0; i < orders.getLength(); i++) {
            Node orderNode = orders.item(i);
            if (orderNode.getNodeType() == Node.ELEMENT_NODE) {
                Element order = (Element) orderNode;
                String id = order.getAttribute("ID");
                Order orderToAdd = new Order(id);
                NodeList products = order.getChildNodes();
                addProductsToOrder(orderToAdd, products);
                orderList.add(orderToAdd);
            }
        }
        return orderList;
    }

    private void addProductsToOrder(Order orderToAdd, NodeList products) {
        for (int j = 0; j < products.getLength(); j++) {
            Node productNode = products.item(j);
            if (productNode.getNodeType() == Node.ELEMENT_NODE) {
                Element product = (Element) productNode;
                NodeList nDescription = product.getElementsByTagName("description");
                NodeList nGtin = product.getElementsByTagName("gtin");
                NodeList nPrice = product.getElementsByTagName("price");
                NodeList nSupplier = product.getElementsByTagName("supplier");
                addProductToOrder(orderToAdd, nDescription, nGtin, nPrice, nSupplier);
            }
        }
    }

    private void addProductToOrder(Order orderToAdd, NodeList nDescription, NodeList nGtin, NodeList nPrice, NodeList nSupplier) {
        for (int k = 0; k < nDescription.getLength(); k++) {
            String description = nDescription.item(k).getTextContent();
            String gtin = nGtin.item(k).getTextContent();
            String price = nPrice.item(k).getTextContent();
            String currency = nPrice.item(k).getAttributes().getNamedItem("currency").getTextContent();
            String supplier = nSupplier.item(k).getTextContent();
            Product product = new Product(description, gtin, currency, price, supplier);
            orderToAdd.addProduct(product);
        }
    }

    public HashSet<String> getSuppliers(ArrayList<Order> orderList) {
        ArrayList<String> supplierList = new ArrayList<>();
        for (Order order : orderList) {
            for (Product product : order.getProducts()) {
                supplierList.add(product.getSupplier());
            }
        }
        return new HashSet<>(supplierList);
    }
}
