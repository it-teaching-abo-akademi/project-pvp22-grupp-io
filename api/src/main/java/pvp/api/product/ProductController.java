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
    public List<Product> getAllUsers() {
        return this.productService.getAllProducts();
    }

    @GetMapping("/{sku}")
    public Product getNoteById(@PathVariable(value = "sku") String sku){
        return productService.findBySku(sku);
    }

    @PostMapping
    public void addNewUser(@RequestBody Product product) {
        this.productService.addNewProduct(product);
    }

}
