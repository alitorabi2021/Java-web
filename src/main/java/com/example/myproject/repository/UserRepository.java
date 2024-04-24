package com.example.myproject.repository;

import com.example.myproject.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    @Query("SELECT u FROM  User u WHERE u.email = :email")
  User getUserByEmail(@Param("email") String email);

   User findByRestPasswordToken(String token);
    @Query("SELECT u FROM  User u WHERE u.verificationCode = :code")
   User findByVerificationCode(@Param("code") String code);

}
