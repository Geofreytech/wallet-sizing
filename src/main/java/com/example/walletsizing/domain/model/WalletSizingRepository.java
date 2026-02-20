package com.example.walletsizing.domain.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WalletSizingRepository extends JpaRepository<WalletSizing, Long> {

    // Find all sizing records for a specific customer
    List<WalletSizing> findByCustomerId(Long customerId);

    // Find records created by a specific RM (Section 6.2)
    List<WalletSizing> findByCreatedBy(String rmUsername);

    List<WalletSizing> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}