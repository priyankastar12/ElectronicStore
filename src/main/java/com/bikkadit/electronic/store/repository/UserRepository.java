package com.bikkadit.electronic.store.repository;

import com.bikkadit.electronic.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByemailandpassword(String email,String password);

    List<User> findByNameContaining(String keyword) ;
}
