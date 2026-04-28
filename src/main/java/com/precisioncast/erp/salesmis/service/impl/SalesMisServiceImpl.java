package com.precisioncast.erp.salesmis.service.impl;

import com.precisioncast.erp.dispatchnote.repository.DispatchNoteRepository;
import com.precisioncast.erp.salesinvoice.entity.SalesInvoice;
import com.precisioncast.erp.salesinvoice.repository.SalesInvoiceRepository;
import com.precisioncast.erp.salesmis.service.SalesMisService;
import com.precisioncast.erp.salesorder.repository.SalesOrderRepository;
import com.precisioncast.erp.salesreturn.repository.SalesReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesMisServiceImpl implements SalesMisService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final DispatchNoteRepository dispatchNoteRepository;
    private final SalesReturnRepository salesReturnRepository;

    @Override
    public Long getSalesCount() {
        return salesOrderRepository.count();
    }

    @Override
    public Long getInvoiceCount() {
        return salesInvoiceRepository.count();
    }

    @Override
    public Long getDispatchCount() {
        return dispatchNoteRepository.count();
    }

    @Override
    public Long getSalesReturnCount() {
        return salesReturnRepository.count();
    }

    @Override
    public BigDecimal getRevenueToday() {
        LocalDate today = LocalDate.now();
        BigDecimal total = BigDecimal.ZERO;

        for (SalesInvoice invoice : salesInvoiceRepository.findAll()) {
            if (invoice.getInvoiceDate() != null && invoice.getInvoiceDate().isEqual(today)
                    && invoice.getTotalAmount() != null && !"CANCELLED".equalsIgnoreCase(invoice.getStatus()))
            {
                total = total.add(invoice.getTotalAmount());
            }
        }

        return total;
    }

    @Override
    public BigDecimal getRevenueRange(LocalDate from, LocalDate to) {
        BigDecimal total = BigDecimal.ZERO;

        for (SalesInvoice invoice : salesInvoiceRepository.findAll()) {
            if (invoice.getInvoiceDate() == null || invoice.getTotalAmount() == null) {
                continue;
            }

            boolean afterFrom = from == null || !invoice.getInvoiceDate().isBefore(from);
            boolean beforeTo = to == null || !invoice.getInvoiceDate().isAfter(to);
            boolean notCancelled = !"CANCELLED".equalsIgnoreCase(invoice.getStatus());

            if (afterFrom && beforeTo && notCancelled) {
                total = total.add(invoice.getTotalAmount());
            }
        }

        return total;
    }

    @Override
    public byte[] exportSalesSnapshot(Integer month, Integer year) {
        BigDecimal revenue = BigDecimal.ZERO;

        for (SalesInvoice invoice : salesInvoiceRepository.findAll()) {
            if (invoice.getInvoiceDate() != null && invoice.getInvoiceDate().getMonthValue() == month
             && invoice.getInvoiceDate().getYear() == year && invoice.getTotalAmount() != null
                && !"CANCELLED".equalsIgnoreCase(invoice.getStatus()))
            {
                revenue = revenue.add(invoice.getTotalAmount());
            }
        }

        String csv = "Month, Year, Sales Count, Invoice Count, Dispatch Count, Sales Return Count, Revenue\n"
                + month + ","
                + year + ","
                + getSalesCount() + ","
                + getInvoiceCount() + ","
                + getDispatchCount() + ","
                + getSalesReturnCount() + ","
                + revenue;

        return csv.getBytes(StandardCharsets.UTF_8);
    }
}
