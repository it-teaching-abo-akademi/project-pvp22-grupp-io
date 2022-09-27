package pvp.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pvp.api.user.User;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return this.productService.getAllProducts();
    }

    @GetMapping("/{sku}")
    public Product getProductById(@PathVariable(value = "sku") String sku){
        return productService.findBySku(sku);
    }

    @GetMapping("/search/{input}")
    public List<Product> getProductBySearch(@PathVariable(value = "input") String input){
        return productService.getProductsBySearch(input);
    }

    @PostMapping
    public void addNewProduct(@RequestBody Product product) {
        this.productService.addNewProduct(product);
    }

}
