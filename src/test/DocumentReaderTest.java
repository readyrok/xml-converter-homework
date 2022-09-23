package test;

import com.codecool.xml.DocumentReader;
import com.codecool.xml.Order;
import com.codecool.xml.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentReaderTest {
    static DocumentReader documentReader;
    static ArrayList<Order> orderList;
    static NodeList orders;

    @BeforeAll
    static void setUp() throws ParserConfigurationException, IOException, SAXException {
        documentReader = new DocumentReader("src/input/orders23.xml");
        orderList = new ArrayList<>();
        orders = documentReader.getOrders();

        Order order1 = new Order("2343");
        Order order2 = new Order("2344");

        order1.addProduct(new Product("Sony 54.6\" (Diag) Xbr Hx929 Internet Tv",
                "00027242816657",
                "USD",
                "2999.99",
                "Sony"));
        order1.addProduct(new Product("Apple iPad 2 with Wi-Fi 16GB - iOS 5 - Black",
                "00885909464517",
                "USD",
                "399.0",
                "Apple"));
        order1.addProduct(new Product("Sony NWZ-E464 8GB E Series Walkman Video MP3 Player Blue",
                "00027242831438",
                "USD",
                "91.99",
                "Sony"));
        order2.addProduct(new Product("Apple MacBook Air A 11.6\" Mac OS X v10.7 Lion MacBook",
                "00885909464043",
                "USD",
                "1149.0",
                "Apple"));
        order2.addProduct(new Product("Panasonic TC-L47E50 47\" Smart TV Viera E50 Series LED HDTV",
                "00885170076471",
                "USD",
                "999.99",
                "Panasonic"));

        orderList.add(order1);
        orderList.add(order2);
    }

    @Test
    void testGetSuppliers(){
        HashSet<String> suppliers = new HashSet<>(Arrays.asList("Sony", "Panasonic", "Apple"));
        assertEquals(suppliers, documentReader.getSuppliers(orderList));
    }

    @Test
    void testGetOrderList(){
        assertEquals(orderList, documentReader.getOrderList(orders));
    }
}

