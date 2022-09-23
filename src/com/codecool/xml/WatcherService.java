package com.codecool.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WatcherService {
    private static final Logger logger = Logger.getLogger(WatcherService.class.getName());
    public static final int FILENAME_LENGTH = 12;

    public WatcherService() {
    }

    public void watch() throws InterruptedException, ParserConfigurationException, IOException, SAXException, TransformerException {
        String inputPath = getInputPath();
        WatchService watchService = getWatchService(inputPath);
        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                String filename = event.context() + "";
                if (isFilenameCorrect(filename)) {
                    String documentPath = inputPath + filename;
                    String orderNumber = filename.substring(6, 8);
                    DocumentReader documentReader = new DocumentReader(documentPath);
                    ArrayList<Order> orderList = getOrderList(documentReader);
                    HashSet<String> suppliers = documentReader.getSuppliers(orderList);
                    writeFile(orderList, suppliers, orderNumber);
                    Files.deleteIfExists(Paths.get(documentPath));
                } else {
                    logger.log(Level.SEVERE, "logging:", new IllegalArgumentException("Incorrect filename!"));
                }
            }
            key.reset();
        }
    }

    private ArrayList<Order> getOrderList(DocumentReader documentReader) throws ParserConfigurationException, SAXException, IOException {
        NodeList orders = documentReader.getOrders();
        return documentReader.getOrderList(orders);
    }

    private String getInputPath() throws IOException {
        Properties config = new Properties();
        config.load(new FileInputStream("src/resources/config.properties"));
        return config.getProperty("input");
    }

    private boolean isFilenameCorrect(String filename) {
        return isFilenameLength(filename)
                && verifyLetters(filename)
                && verifyNumber(filename)
                && verifyExtension(filename);
    }

    private boolean verifyExtension(String filename) {
        return filename.substring(8).equals(".xml");
    }

    private boolean verifyNumber(String filename) {
        for (int i = 6; i < 8; i++) {
            if (!Character.isDigit(filename.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyLetters(String filename) {
        for (int i = 0; i < 6; i++) {
            if (!Character.isLetter(filename.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isFilenameLength(String filename) {
        return filename.length() == FILENAME_LENGTH;
    }

    private void writeFile(ArrayList<Order> orderList, HashSet<String> suppliers, String orderNumber) throws ParserConfigurationException, TransformerException, IOException {
        for (String supplier : suppliers) {
            DocumentWriter documentWriter = new DocumentWriter();
            Document document = documentWriter.createDocument();
            Element root = documentWriter.createRootElement(document);
            documentWriter.createChildrenElements(orderList, document, supplier, root);
            documentWriter.writeFile(document, supplier, orderNumber);
        }
    }

    private WatchService getWatchService(String inputPath) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(inputPath);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        return watchService;
    }
}
