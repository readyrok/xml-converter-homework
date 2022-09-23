package com.codecool.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

public class DocumentWriter {
    private static final String XML_DOCTYPE = "{http://xml.apache.org/xslt}indent-amount";

    public DocumentWriter() {
    }

    public Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        doc.setXmlStandalone(true);
        return doc;
    }

    public Element createRootElement(Document doc) {
        Element root = doc.createElement("products");
        doc.appendChild(root);
        return root;
    }

    public void createChildrenElements(ArrayList<Order> orderList, Document document, String supplier, Element root) {
        for (Order order : orderList) {
            for (Product product : order.getProducts()) {
                if (Objects.equals(product.getSupplier(), supplier)) {
                    root.appendChild(createProduct(document, product.getDescription(), product.getGtin(), product.getCurrency(), product.getPrice(), order.getId()));
                }
            }
        }
    }

    private Node createProduct(Document document, String description, String gtin, String currency, String price, String orderId) {
        Element product = document.createElement("product");
        product.appendChild(createProductElement(document, "description", description));
        product.appendChild(createProductElement(document, "gtin", gtin));
        product.appendChild(createProductElement(document, "price", price, "currency", currency));
        product.appendChild(createProductElement(document, "orderId", orderId));
        return product;
    }

    private Node createProductElement(Document document, String name, String value) {
        Element node = document.createElement(name);
        node.appendChild(document.createTextNode(value));
        return node;
    }

    private Node createProductElement(Document doc, String name, String value, String attribute, String attributeValue) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        node.setAttribute(attribute, attributeValue);
        return node;
    }

    public void writeFile(Document document, String supplier, String orderNumber) throws TransformerException, IOException {
        Transformer transformer = getTransformer();
        DOMSource source = new DOMSource(document);
        String pathName = getOutputPath() + supplier + orderNumber + ".xml";
        StreamResult file = getStreamResult(pathName);
        transformer.transform(source, file);
    }

    private String getOutputPath() throws IOException {
        Properties config = new Properties();
        config.load(new FileInputStream("src/resources/config.properties"));
        return config.getProperty("output");
    }

    private StreamResult getStreamResult(String pathName) {
        File myFile = new File(pathName);
        return new StreamResult(myFile);
    }

    private static Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(XML_DOCTYPE, "2");
        return transformer;
    }
}
