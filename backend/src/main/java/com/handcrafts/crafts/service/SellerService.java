package com.handcrafts.crafts.service;

import com.handcrafts.crafts.entity.Seller;
import com.handcrafts.crafts.enums.SellerStatus;
import com.handcrafts.crafts.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerSeller(Seller seller) {
        if (sellerRepository.findByEmail(seller.getEmail()).isPresent()) {
            throw new RuntimeException("Seller already exists");
        }

        // ✅ Encode password
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        seller.setStatus(SellerStatus.PENDING); // Use enum directly
        sellerRepository.save(seller);
        return seller.getName() + " registered successfully!";
    }

    public Seller getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
    }

    public void updateProfile(String email, Seller updatedSeller) {
        Seller seller = getSellerByEmail(email);
        seller.setName(updatedSeller.getName());

        if (updatedSeller.getPassword() != null && !updatedSeller.getPassword().isBlank()) {
            seller.setPassword(passwordEncoder.encode(updatedSeller.getPassword()));
        }

        sellerRepository.save(seller);
    }

    public String getStatus(String email) {
        return getSellerByEmail(email).getStatus().name(); // Return enum as String
    }

    public List<Seller> getPendingSellers() {
        return sellerRepository.findByStatus(SellerStatus.PENDING);
    }

    public List<Seller> getApprovedSellers() {
        return sellerRepository.findByStatus(SellerStatus.APPROVED);
    }

    public boolean approveSeller(int id) {
        return sellerRepository.findById(id).map(seller -> {
            if (SellerStatus.PENDING.equals(seller.getStatus())) {
                seller.setStatus(SellerStatus.APPROVED);
                sellerRepository.save(seller);
                return true;
            }
            return false;
        }).orElse(false);
    }
}
