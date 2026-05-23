package com.example.ecmini.service;

import com.example.ecmini.entity.Product;
import com.example.ecmini.form.ProductForm;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    List<Product> searchByName(String keyword);

    List<Product> findByCategory(String category);

    Product findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    Page<Product> getPage(int page);

    void create(ProductForm form, MultipartFile imageFile);

    void update(Long id, ProductForm form, MultipartFile imageFile);

    String getImagePath(Product product);

    Page<Product> searchByName(String keyword, int page);

    List<Product> findRelatedProducts(String category, Long id);

    List<Product> findByIds(List<Long> ids);

    List<Product> searchProducts(String name, String category);

    void deleteImage(Long id);

    Page<Product> searchWithPaging(String name, String category, int page);

}
