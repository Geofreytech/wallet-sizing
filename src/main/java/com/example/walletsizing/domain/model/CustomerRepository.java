package com.example.walletsizing.domain.model;


import com.example.walletsizing.application.usecase.dto.CustomerSearchResponse;
import com.example.walletsizing.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCif(String cif);

    @Query("SELECT new com.example.walletsizing.application.usecase.dto.CustomerSearchResponse(c.id, c.name, c.cif, c.industry, c.segment) " +
            "FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR c.cif LIKE CONCAT('%', :query, '%')")
    List<CustomerSearchResponse> searchCustomers(@Param("query") String query);
}