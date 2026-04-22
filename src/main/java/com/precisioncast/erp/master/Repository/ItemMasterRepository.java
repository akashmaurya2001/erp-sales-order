package com.precisioncast.erp.master.Repository;

import com.precisioncast.erp.master.entity.ItemMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemMasterRepository extends JpaRepository<ItemMaster, Long> {

    Optional<ItemMaster>  findByItemCode(String itemCode);

    List<ItemMaster> findByIsActiveTrue();

    List<ItemMaster> findByItemNameContainingIgnoreCase(String itemName);

    List<ItemMaster> findByCategoryId(Long categoryId);
}
