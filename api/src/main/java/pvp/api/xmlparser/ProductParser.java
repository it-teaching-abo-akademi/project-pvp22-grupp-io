package pvp.api.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import pvp.api.bonuscrads.BonusCardAccessService;
import pvp.api.product.ProductService;
import pvp.models.interfaces.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductParser extends DefaultHandler {
    private StringBuilder currentValue = new StringBuilder();
    List<Product> result;
    Set<String> keywords;
    Product currentProduct;
    Integer id;

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
            keywords = new HashSet<String>();
            id = Integer.parseInt(attributes.getValue("id"));
            currentProduct = productService.findProductByOldId(id);
            if (currentProduct == null) {
                currentProduct = new pvp.api.product.Product(
                        id,
                        "",
                        0,
                        0,
                        "",
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
        if (qName.equalsIgnoreCase("vat")) {
            currentProduct.setVat((int) Float.parseFloat(currentValue.toString()));
        }
        if (qName.equalsIgnoreCase("keyword")) {
            keywords.add(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("product")) {
            currentProduct.setKeywords(keywords);
            result.add(currentProduct);
            productService.addNewProductWithOldId(currentProduct, id);
        }
    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
