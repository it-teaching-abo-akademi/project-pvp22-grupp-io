package pvp.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pvp.api.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductDataAccessService productDataAccessService;

    @Autowired
    public ProductService(ProductDataAccessService productDataAccessService) {
        this.productDataAccessService = productDataAccessService;
    }

    List<Product> getAllProducts() {
        return productDataAccessService.selectAllProducts();
    }

    Product findBySku(String sku) { return productDataAccessService.getProductBySku(sku); }

    void addNewProduct(Product product) {
        addNewProduct(product.getSku(), product);
    }

    void addNewProduct(String sku, Product product) {
        if (sku == null) {
            sku = UUID.randomUUID().toString();
        }
        productDataAccessService.insertProduct(sku, product);
    }
}
