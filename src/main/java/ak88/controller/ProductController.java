package ak88.controller;

import ak88.model.Category;
import ak88.model.Product;
import ak88.service.CategoryService;
import ak88.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@RequestMapping("/products")
@Controller
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @ModelAttribute("categorys")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping("")
    public String showList(Model model, @PageableDefault(value = 5) Pageable pageable, String key) {
        Page<Product> products;
        if (key != null) {
            model.addAttribute("products", productService.findByNameContaining(key, pageable));
            model.addAttribute("key",key);
        } else {
            products = productService.findAll(pageable);
            model.addAttribute("products", products);
            model.addAttribute("key",key);
        }
        return "/product/list";
    }
    @GetMapping("sort")
    public String sortDesc(Model model, @PageableDefault(value = 4) Pageable pageable,String key){

        Page<Product> products=productService.findAllByOrderByPriceDesc(pageable);
        model.addAttribute("products", products);
        model.addAttribute("key",key);
        model.addAttribute("pageable",pageable);
        return "/product/sort";
    }
    @GetMapping("create")
    public String showCreate(Model model) {
        model.addAttribute("product",new Product());
        return "/product/create";
    }
    @PostMapping("create")
    public String createProduct(Product product, @RequestParam MultipartFile image) {
        String fileName = image.getOriginalFilename();
        try {
            FileCopyUtils.copy(image.getBytes(),
                    new File("D:\\ak\\" + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        product.setImg(fileName);
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.remove(id);
        return "redirect:/products";
    }
    @GetMapping("findCategory")
    public String findCategory(Long idCategory,Pageable pageable,Model model){
        Optional<Category> category=categoryService.findById(idCategory);
        Category category1=category.get();
        Page<Product> products=productService.findAllByCategory(category1,pageable);
        model.addAttribute("products",products);
        return "/product/findCategory";
    }

    @GetMapping("edit/{id}")
    public String showEdit(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id));
        return "/product/edit";
    }

    @PostMapping("edit/{id}")
    public String editProduct(Long idCategory, Product product) {
        Optional<Category> category = categoryService.findById(idCategory);
        product.setCategory(category.get());
        productService.save(product);
        return "redirect:/products";
    }
}
