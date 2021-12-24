package ak88.service;
import ak88.model.Category;
import ak88.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends IGeneralService<Product>{
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findAllByCategory(Category category, Pageable pageable);
}
