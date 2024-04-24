package com.example.myproject.repository;

import com.example.myproject.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {
void deleteById(Long id);
@Query("SELECT p FROM Product p WHERE "
        +"CONCAT(p.id, p.brand, p.name, p.price, p.category, p.status) "
        +"LIKE %?1%")
 Page<Product> findAll(String keyword, Pageable pageable);

}
