package com.example.ecmini.service;

import com.example.ecmini.entity.Product;
import com.example.ecmini.exception.ProductNotFoundException;
import com.example.ecmini.repository.ProductRepository;
import com.example.ecmini.form.ProductForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // 保存先（Spring Boot の static 配下）
    private final String uploadDir = "src/main/resources/static/uploads/product/";

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 一覧
    @Override
    public Page<Product> getPage(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }


    @Override
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    @Override
    public List<Product> searchByName(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }


    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }


    // 商品登録
    @Override
    public void create(ProductForm form, MultipartFile imageFile) {

        Product product = new Product();

        applyImage(product, imageFile);

        productRepository.save(product);
    }

    // 商品更新
    @Override
    public void update(Long id, ProductForm form, MultipartFile imageFile) {

        Product existing = findById(id);

        applyForm(existing, form);

        applyImage(existing, imageFile);

        productRepository.save(existing);
    }

    private void applyForm(Product product, ProductForm form) {

        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setStock(form.getStock());
        product.setCategory(form.getCategory());
        product.setDescription(form.getDescription());

    }

    private void applyImage(Product product, MultipartFile imageFile) {

        if (imageFile != null && !imageFile.isEmpty()) {

            String fileName =
                    System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

            saveImage(imageFile, fileName);

            product.setImage(fileName);
        }
    }

    // 画像保存処理（共通化）
    private void saveImage(MultipartFile imageFile, String fileName) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("画像保存に失敗しました", e);
        }
    }

    // NoImage fallback
    @Override
    public String getImagePath(Product product) {
        if (product.getImage() == null || product.getImage().isEmpty()) {
            return "/images/noimage.png";
        }
        return "/uploads/product/" + product.getImage();
    }
    @Override
    public Page<Product> searchByName(String keyword, int page) {
        return productRepository.searchByName(keyword, PageRequest.of(page, 10));
    }
    @Override
    public List<Product> findRelatedProducts(String category, Long id) {
        return productRepository.findRelatedProducts(category, id);
    }
    @Override
    public List<Product> findByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }
    @Override
    public List<Product> searchProducts(String name, String category) {
        return productRepository.searchProducts(name, category);
    }
    @Override
    public void deleteImage(Long id) {
        Product product = findById(id);

        if (product.getImage() != null) {
            Path path = Paths.get("uploads/product/" + product.getImage());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("画像削除に失敗しました", e);
            }
        }

        product.setImage(null);
        productRepository.save(product);
    }
    @Override
    public Page<Product> searchWithPaging(String name, String category, int page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        if ((name == null || name.isEmpty()) &&
                (category == null || category.isEmpty())) {
            return productRepository.findAll(pageable);
        }

        return productRepository.search(name, category, pageable);
    }

}



