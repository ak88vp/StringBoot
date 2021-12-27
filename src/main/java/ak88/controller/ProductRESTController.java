package ak88.controller;

import ak88.model.Product;
import ak88.service.CategoryService;
import ak88.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/products")
public class ProductRESTController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<Page<Product>> showList( @PageableDefault(value = 2) Pageable pageable){
        Page<Product> products=productService.findAll(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){

        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,@RequestBody Product product){
        Optional<Product> productOptional=productService.findById(id);
        product.setId(productOptional.get().getId());
        return new ResponseEntity<>(productService.save(product),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        Optional<Product> productOptional=productService.findById(id);
        productService.remove(id);
        return new ResponseEntity<>(productOptional.get(),HttpStatus.NO_CONTENT);
    }


}
