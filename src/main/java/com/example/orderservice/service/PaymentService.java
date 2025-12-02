package com.example.orderservice.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

	// Entry point used by ReportingService
	public double reconcilePaymentsForDate(String businessDate) {
		return getPaymentSummary(businessDate);
	}

	private double getPaymentSummary(String businessDate) {
		return getTransactionIds(businessDate);
	}

	private double getTransactionIds(String businessDate) {
		return getUserDetails(businessDate);
	}

	private double getUserDetails(String businessDate) {
		return validatePayment(businessDate);
	}

	private double validatePayment(String businessDate) {
		return getPaymentId(businessDate);
	}

	private double getPaymentId(String businessDate) {
		return updatePaymentStatus(businessDate);
	}

	// Deepest level: heavy CPU hotspot
	private double updatePaymentStatus(String businessDate) {
		double total = 0.0;
		for (int i = 1; i <= 50_000_000; i++) { // 50M iterations
			total += Math.sqrt(i) * 0.01;
		}
		return total;
	}
}
