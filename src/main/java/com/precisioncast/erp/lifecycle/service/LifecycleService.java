package com.precisioncast.erp.lifecycle.service;

import java.util.List;

public interface LifecycleService {

    List<Object> getCustomerList();

    Object getCustomerById(Long customerId);

    List<Object> getCustomerOrders(Long customerId);

    List<Object> getCustomerInvoices(Long customerId);

    List<Object> getCustomerDispatches(Long customerId);

    List<Object> getCustomerReturns(Long customerId);

    List<Object> getItemList();

    Object getItemById(Long itemId);

    List<Object> getItemSalesHistory(Long itemId);

    List<Object> getItemDispatchHistory(Long itemId);

    List<Object> getItemReturnHistory(Long itemId);

}
