package com.example.orderservice.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ReportingService {

	private final OrderService orderService;
	private final PaymentService paymentService;

	public ReportingService(OrderService orderService, PaymentService paymentService) {
		this.orderService = orderService;
		this.paymentService = paymentService;
	}

	public Map<String, Object> getDailySalesReport(String businessDate) throws InterruptedException {
		long start = System.currentTimeMillis();

		if (businessDate == null || businessDate.isBlank()) {
			businessDate = LocalDate.now().minusDays(1).toString();
		}

		double orderTotal = generateSalesSnapshot(businessDate);
		double paymentTotal = reconcilePaymentSnapshot(businessDate);

		// simulate external IO delay
		Thread.sleep(1200);

		long elapsed = System.currentTimeMillis() - start;

		Map<String, Object> report = new HashMap<>();
		report.put("reportName", "DAILY_SALES_SUMMARY");
		report.put("businessDate", businessDate);
		report.put("totalOrderAmount", orderTotal);
		report.put("totalPaymentReconciled", paymentTotal);
		report.put("currency", "INR");
		report.put("generatedAt", System.currentTimeMillis());
		report.put("generationTimeMs", elapsed);

		return report;
	}

	private double generateSalesSnapshot(String businessDate) {
		Map<String, Object> listResponse = orderService.listOrders("COMPLETED", 50);
		Object totalAmount = listResponse.get("totalAmount");
		return totalAmount instanceof Number ? ((Number) totalAmount).doubleValue() : 0.0;
	}

	private double reconcilePaymentSnapshot(String businessDate) {
		return paymentService.reconcilePaymentsForDate(businessDate);
	}
}
