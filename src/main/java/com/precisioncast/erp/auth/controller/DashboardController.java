package com.precisioncast.erp.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Dashboard Module",
        description = "Role-based dashboard APIs for admin, sales, production, quality, & procurement."
)
@RestController
public class DashboardController {

    @GetMapping("/api/dashboard/admin")
    public String adminDashboard() {
        return "Welcome to Admin Dashboard";
    }

    @GetMapping("/api/dashboard/sales")
    public String salesDashboard() {
        return "Welcome to Sales dashboard";
    }

    @GetMapping("/api/dashboard/production")
    public String productionDashboard() {
        return "Welcome to Production dashboard";
    }

    @GetMapping("/api/dashboard/quality")
    public String qualityDashboard() {
        return "Welcome to Quality dashboard";
    }

    @GetMapping("/api/dashboard/procurement")
    public String procurementDashboard() {
        return "Welcome to Procurement dashboard";
    }
}
