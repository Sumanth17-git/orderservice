package com.example.orderservice.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.service.CustomerService;
import com.example.orderservice.service.HealthService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.ReportingService;

@RestController
@RequestMapping("/api")
public class OrderController {

	private final HealthService healthService;
	private final CustomerService customerService;
	private final OrderService orderService;
	private final ReportingService reportingService;

	public OrderController(HealthService healthService, CustomerService customerService, OrderService orderService,
			ReportingService reportingService) {
		this.healthService = healthService;
		this.customerService = customerService;
		this.orderService = orderService;
		this.reportingService = reportingService;
	}

	// 1) Health check
	@GetMapping("/health")
	public Map<String, Object> getHealth() {
		return healthService.getHealth();
	}

	// 2) Customer details
	@GetMapping("/customers/{customerId}")
	public Map<String, Object> getCustomerProfile(@PathVariable String customerId) {
		return customerService.getCustomerProfile(customerId);
	}

	// 3) Single order
	@GetMapping("/orders/{orderId}")
	public Map<String, Object> getOrderById(@PathVariable String orderId) {
		return orderService.getOrderById(orderId);
	}

	// 4) List orders
	@GetMapping("/orders")
	public Map<String, Object> listOrders(@RequestParam(required = false, defaultValue = "COMPLETED") String status,
			@RequestParam(required = false, defaultValue = "10") int limit) {
		return orderService.listOrders(status, limit);
	}

	// 5) Heavy daily sales report (profiling target)
	@GetMapping("/reports/daily-sales")
	public Map<String, Object> getDailySalesReport(@RequestParam(required = false) String businessDate)
			throws InterruptedException {
		return reportingService.getDailySalesReport(businessDate);
	}
}
