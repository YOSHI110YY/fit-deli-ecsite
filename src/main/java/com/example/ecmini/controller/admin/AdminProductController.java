package com.example.ecmini.controller.admin;

import com.example.ecmini.service.CategoryService;
import com.example.ecmini.entity.Product;
import com.example.ecmini.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    private final CategoryService categoryService;

    public AdminProductController(ProductService productService,
                                  CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    // 商品一覧（ページング対応）
    @GetMapping
    public String list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {

        Page<Product> productPage = productService.searchWithPaging(name, category, page);

        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("category", category);

        return "admin/products/list";
    }

    // 商品登録フォーム
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());

        model.addAttribute("categories", categoryService.findAll());

        return "admin/products/new";
    }

    // 商品登録処理
    @PostMapping("/new")
    public String createProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setDescription(description);

        productService.create(product, imageFile);

        return "redirect:/admin/products";
    }

    // 商品編集フォーム
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);

        model.addAttribute("categories", categoryService.findAll());

        return "admin/products/edit";
    }

    // 商品更新処理
    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setDescription(description);

        productService.update(id, product, imageFile);

        return "redirect:/admin/products";
    }


    // 商品削除
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return "redirect:/admin/products?success=deleted";
        } catch (Exception e) {
            return "redirect:/admin/products?error=foreignkey";
        }
    }

    @GetMapping("/delete-image/{id}")
    public String deleteImage(@PathVariable Long id) {
        productService.deleteImage(id);
        return "redirect:/admin/products/edit/" + id;
    }



    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "admin/products/detail";
    }

}
