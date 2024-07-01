package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.CategoryService;
import com.vanlang.webbanhang.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
//    Hien thi danh sach tat ca san pham
    @GetMapping
    public String showProducts(Model model) {
        model.addAttribute("products",productService.getAllProducts());
        return "/products/products-list";
    }
//    Them san pham
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories",categoryService.getAllCategories()); // Tai cac danh muc
        return "/products/add-product";
    }
//    Gui form them san pham
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "products/add-product";
        }
        productService.addProduct(product);
        return "redirect:/products";
    }
//    Sua san pham
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product",product);
        model.addAttribute("categories",categoryService.getAllCategories());
        return "/products/edit-product";
    }
//    Gui form sua san pham
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            product.setId(id); //Set id de luu trong form trong truong hop bi loi
            return "/products/edit-product";
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }
//    Xoa san pham theo id
    @GetMapping("delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}
