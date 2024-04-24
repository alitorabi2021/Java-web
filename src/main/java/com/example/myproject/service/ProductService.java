package com.example.myproject.service;

import com.example.myproject.dto.ProductDto;
import com.example.myproject.model.Product;
import com.example.myproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService  {

    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        product.setCreateAt(new Date());
        return productRepository.save(product);
    }

    public Product editProduct(Product product) {
        product.setCreateAt(new Date());
       return productRepository.save(product);

    }


    public void deleteProduct(Long id) {
         productRepository.deleteById(id);
    }


    public Page<Product> getAllSort(String sortField,String sortDir,int currentPage,String keyword) {
        Sort sort=Sort.by(sortField);
        sort=sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(currentPage - 1, 5,sort);
        if (keyword!=null){
            return productRepository.findAll(keyword,pageable);
        }
        return productRepository.findAll(pageable);

        }

    public Product findById(Long id) {
        Product product= productRepository.findById(id).orElse(null);
        return product;
    }
}
