package com.handcrafts.crafts.repository;

import com.handcrafts.crafts.entity.Seller;
import com.handcrafts.crafts.enums.SellerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    Optional<Seller> findByEmail(String email);
    List<Seller> findByStatus(SellerStatus status); // Correct: Use enum type
}
