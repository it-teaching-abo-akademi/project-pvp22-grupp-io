package pvp.api.product;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import pvp.api.xmlparser.ProductParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductDataAccessService productDataAccessService;

    @Autowired
    public ProductService(ProductDataAccessService productDataAccessService) {
        this.productDataAccessService = productDataAccessService;

        try {
            URL url = new URL("http://localhost:9003/rest/findByName/*");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser = factory.newSAXParser();
            ProductParser handler = new ProductParser(this);
            saxParser.parse(new ByteArrayInputStream(response.toString().getBytes()), handler);
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

    List<Product> getAllProducts(JSONObject json) {
        return productDataAccessService.selectAllProducts(json);
    }

    Product findBySku(String sku) { return productDataAccessService.getProductBySku(sku); }

    public Product findProductByOldId(Integer id) { return productDataAccessService.getProductByOldId(id); }

    public void addNewProduct(pvp.models.interfaces.Product product) {
        productDataAccessService.insertProduct(product, null);
    }

    public void addNewProductWithOldId(pvp.models.interfaces.Product product, Integer oldId) {
        productDataAccessService.insertProduct(product, oldId);
    }
}
