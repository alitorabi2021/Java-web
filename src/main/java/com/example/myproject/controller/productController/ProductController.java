package com.example.myproject.controller.productController;

import com.example.myproject.dto.ProductDto;
import com.example.myproject.model.Product;
import com.example.myproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @RequestMapping("/")
    public String login(Model model ) {
            return getSortBy(model, "id", "asc",1, null);
        }
    @RequestMapping("/page/{pageNumber}")
    public String getSortBy(Model model,
     @Param("sortField") String sortFiled,
     @Param("sortDir") String sortDir,
     @PathVariable("pageNumber") int currentPage,
     @Param("keyword")String keyword){
        Page<Product> page = productService.getAllSort(sortFiled,sortDir,currentPage,keyword);
        long items=page.getTotalElements();
        int pages= page.getTotalPages();
        List<Product>  pageProduct=page.getContent();
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("items",items);
        model.addAttribute("products", pageProduct);
        model.addAttribute("totalPage",pages);
        model.addAttribute("sortFiled",sortFiled);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("keyword",keyword);
        String reverseSortDir=sortDir.equals("asc") ?  "des" : "asc";
        model.addAttribute("reverseSortDir",reverseSortDir);

            return "/allProduct";
    }

    @GetMapping("/addProduct")
    public String getProduct(Model model){
        ProductDto product=new ProductDto();
        model.addAttribute("product",product);
        return "newProduct";
    }

    @PostMapping("/save")
    public String addProduct(@ModelAttribute Product product){
        productService.saveProduct(product);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable("id") Long id, Model model){
        Product product = productService.findById(id);
        model.addAttribute("edit", product);
        return "editProduct";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@ModelAttribute("edit") Product product) {
        productService.editProduct(product);
        return "redirect:/";
    }
    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Long id ){
        productService.deleteProduct(id);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String findByProductDelete(@PathVariable(name = "id") Long id,Model model ){
        Product delProduct=productService.findById(id);
        model.addAttribute("delete",delProduct);
        return "deleteProduct";
    }


}
