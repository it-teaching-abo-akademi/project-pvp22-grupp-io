package pvp.api.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import pvp.api.product.ProductService;
import pvp.models.interfaces.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductParser extends DefaultHandler {
    private StringBuilder currentValue = new StringBuilder();
    List<Product> result;
    Product currentProduct;

    private final ProductService productService;

    public ProductParser(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getResult() {
        return result;
    }

    @Override
    public void startDocument() {
        result = new ArrayList<>();
    }

    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) {

        // reset the tag value
        currentValue.setLength(0);

        if (qName.equalsIgnoreCase("product")) {
            String id = attributes.getValue("id");
            currentProduct = productService.findProductById(Integer.parseInt(id));
            if (currentProduct == null) {
                currentProduct = new pvp.api.product.Product(
                        Integer.parseInt(id),
                        "",
                        0,
                        0,
                        ""
                );
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("name")) {
            currentProduct.setName(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("barCode")) {
            currentProduct.setSku(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("product")) {
            result.add(currentProduct);
            productService.addNewProduct(currentProduct);
        }
    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
