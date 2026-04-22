package com.precisioncast.erp.customerpricelist.repository;

import com.precisioncast.erp.customerpricelist.entity.CustomerPriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerPriceListRepository extends JpaRepository<CustomerPriceList, Long> {

    List<CustomerPriceList> findByCustomerId(Long customerId);

    Optional<CustomerPriceList> findByCustomerIdAndItemId(Long customerId, Long itemId);
}