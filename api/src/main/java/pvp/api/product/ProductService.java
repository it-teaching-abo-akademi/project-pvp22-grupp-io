package pvp.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import pvp.api.user.User;
import pvp.api.xmlparser.ProductParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductDataAccessService productDataAccessService;

    @Autowired
    public ProductService(ProductDataAccessService productDataAccessService) {
        this.productDataAccessService = productDataAccessService;
        String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<products>\n" +
                "    <product id=\"1\" optLockVersion=\"1\">\n" +
                "        <barCode>123456789</barCode>\n" +
                "        <name>Test prod</name>\n" +
                "        <vat>2.00</vat>\n" +
                "        <keyword>aa</keyword>\n" +
                "        <keyword>test</keyword>\n" +
                "    </product>\n" +
                "    <product id=\"51\" optLockVersion=\"1\">\n" +
                "        <barCode>09876543</barCode>\n" +
                "        <name>hu</name>\n" +
                "        <vat>2.00</vat>\n" +
                "        <keyword></keyword>\n" +
                "    </product>\n" +
                "</products>";
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = factory.newSAXParser();
            ProductParser handler = new ProductParser(this);
            saxParser.parse(new ByteArrayInputStream(test.getBytes()), handler);
            List<pvp.models.interfaces.Product> result = handler.getResult();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<Product> getProductsBySearch(String search) {
        return productDataAccessService.selectAllProductsBySearch(search);
    }

    public Product findProductById(int id) { return productDataAccessService.getProductById(id); }

    List<Product> getAllProducts() {
        return productDataAccessService.selectAllProducts();
    }

    Product findBySku(String sku) { return productDataAccessService.getProductBySku(sku); }

    public void addNewProduct(pvp.models.interfaces.Product product) {
        addNewProduct(product.getSku(), product);
    }

    void addNewProduct(String sku, pvp.models.interfaces.Product product) {
        if (sku == null) {
            sku = UUID.randomUUID().toString();
        }
        productDataAccessService.insertProduct(sku, product);
    }
}
