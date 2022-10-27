package pvp.api.product;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(@RequestHeader Map<String, String> headers) {
        if (headers.containsKey("filters")) {
            return this.productService.getAllProducts(new JSONObject(headers.get("filters")));
        }
        return this.productService.getAllProducts(new JSONObject());
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
