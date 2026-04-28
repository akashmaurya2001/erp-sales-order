package com.precisioncast.erp.lifecycle.service.impl;

import com.precisioncast.erp.dispatchnote.entity.DispatchNote;
import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import com.precisioncast.erp.lifecycle.service.LifecycleService;
import com.precisioncast.erp.master.repository.CustomerMasterRepository;
import com.precisioncast.erp.master.repository.ItemMasterRepository;
import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import com.precisioncast.erp.salesorder.entity.SalesOrder;
import com.precisioncast.erp.salesorder.entity.SalesOrderItem;
import com.precisioncast.erp.salesorder.repository.SalesOrderItemRepository;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import com.precisioncast.erp.salesreturn.entity.SalesReturn;
import com.precisioncast.erp.salesreturn.entity.SalesReturnItem;
import com.precisioncast.erp.salesreturn.repository.SalesReturnItemRepository;
import com.precisioncast.erp.salesreturn.repository.SalesReturnRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LifecycleServiceImpl implements LifecycleService {

    private final CustomerMasterRepository customerMasterRepository;
    private final ItemMasterRepository itemMasterRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final DispatchNoteRepository  dispatchNoteRepository;
    private final SalesReturnRepository salesReturnRepository;
    private final SalesReturnItemRepository salesReturnItemRepository;

    @Override
    public List<Object> getCustomerList() {
        return new ArrayList<>(customerMasterRepository.findAll());
    }

    @Override
    public Object getCustomerById(Long customerId) {
        return customerMasterRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + customerId));
    }

    @Override
    public List<Object> getCustomerOrders(Long customerId) {
        List<Object> result = new ArrayList<>();

        for (SalesOrder order : salesOrderRepository.findAll()) {
            if (customerId.equals(order.getCustomerId())) {
                result.add(order);
            }
        }

        return result;
    }

    @Override
    public List<Object> getCustomerInvoices(Long customerId) {
        List<Object> result = new ArrayList<>();

        for (SalesInvoice invoice : salesInvoiceRepository.findAll()) {
            if (invoice.getSalesOrderId() ==  null) {
                continue;
            }

            SalesOrder order = salesOrderRepository.findById(invoice.getSalesOrderId()).orElse(null);

            if (order != null && customerId.equals(order.getCustomerId())) {
                result.add(invoice);
            }
        }

        return result;
    }

    @Override
    public List<Object> getCustomerDispatches(Long customerId) {
        List<Object> result = new ArrayList<>();

        for (DispatchNote dispatch :  dispatchNoteRepository.findAll()) {
            SalesOrder order = salesOrderRepository.findById(dispatch.getSalesOrderId()).orElse(null);

            if (order != null && customerId.equals(order.getCustomerId())) {
                result.add(dispatch);
            }
        }

        return result;
    }

    @Override
    public List<Object> getCustomerReturns(Long customerId) {
        List<Object> result = new ArrayList<>();

        for (SalesReturn salesReturn : salesReturnRepository.findAll()) {
            SalesInvoice invoice = salesInvoiceRepository.findById(salesReturn.getInvoiceId()).orElse(null);

            if (invoice == null || invoice.getSalesOrderId() ==  null) {
                continue;
            }

            SalesOrder order = salesOrderRepository.findById(invoice.getSalesOrderId()).orElse(null);

            if (order != null && customerId.equals(order.getCustomerId())) {
                result.add(salesReturn);
            }
        }

        return result;
    }

    @Override
    public List<Object> getItemList() {
        return new ArrayList<>(itemMasterRepository.findAll());
    }

    @Override
    public Object getItemById(Long itemId) {
        return itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id " + itemId));
    }

    @Override
    public List<Object> getItemSalesHistory(Long itemId) {
        validateItemExists(itemId);
        List<Object> result = new ArrayList<>();

        for (SalesOrderItem item : salesOrderItemRepository.findAll()) {
            if (itemId.equals(item.getProductId())) {
                result.add(item);
            }
        }

        return result;
    }

    @Override
    public List<Object> getItemDispatchHistory(Long itemId) {

        validateItemExists(itemId);

        List<Object> result = new ArrayList<>();
        List<Long> matchedSalesOrderIds = new ArrayList<>();

        for (SalesOrderItem item : salesOrderItemRepository.findAll()) {
            if (itemId.equals(item.getProductId())
                    && item.getSalesOrder() != null
                    && item.getSalesOrder().getSalesOrderId() != null) {

                matchedSalesOrderIds.add(item.getSalesOrder().getSalesOrderId());
            }
        }

        for (DispatchNote dispatch : dispatchNoteRepository.findAll()) {
            if (matchedSalesOrderIds.contains(dispatch.getSalesOrderId())) {
                result.add(dispatch);
            }
        }

        return result;

    }

    @Override
    public List<Object> getItemReturnHistory(Long itemId) {
        validateItemExists(itemId);
        List<Object> result = new ArrayList<>();

        for (SalesReturnItem item : salesReturnItemRepository.findAll()) {
            if (itemId.equals(item.getProductId())) {
                result.add(item);
            }
        }

        return result;
    }

    private void validateItemExists(Long itemId) {
        itemMasterRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id " + itemId));
    }
}
