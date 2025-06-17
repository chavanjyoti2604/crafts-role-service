package com.handcrafts.crafts.security;

import com.handcrafts.crafts.entity.Admin;
import com.handcrafts.crafts.entity.Seller;
import com.handcrafts.crafts.entity.User;
import com.handcrafts.crafts.repository.AdminRepository;
import com.handcrafts.crafts.repository.SellerRepository;
import com.handcrafts.crafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class RoleBasedUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check all roles
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(), user.getAuthorities()
            );
        }

        Seller seller = sellerRepository.findByEmail(email).orElse(null);
        if (seller != null) {
            return new org.springframework.security.core.userdetails.User(
                    seller.getEmail(), seller.getPassword(), seller.getAuthorities()
            );
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(), admin.getPassword(), admin.getAuthorities()
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
