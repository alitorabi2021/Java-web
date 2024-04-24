//package com.example.myproject.model;
//
//import com.example.myproject.repository.ProductRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.Date;
//import java.util.Optional;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//public class ProductTests {
//
//    @Autowired
//    private ProductRepository repository;
//    @Test
//    @Rollback(value = false)
//    public void testCreateProduct(){
//        Product product=new Product("Samsung","note 9", "phone",2000L,"stock",new Date());
//        repository.save(product);
//        repository.delete(product);
//        repository.findAll("sa", Pageable.unpaged());
//        Optional<Product> product1=repository.findById(15L);
//
//
//        Assertions.assertEquals(product,product1);
//    }
//}
