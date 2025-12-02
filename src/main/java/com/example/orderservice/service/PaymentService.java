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

	// now this is the last validation step
	private double validatePayment(String businessDate) {
		return completePayment(businessDate);
	}

	/**
	 * ✅ New final method in the chain. Lighter CPU work compared to the old
	 * updatePaymentStatus.
	 */
	private double completePayment(String businessDate) {
		double total = 0.0;

		// reduced CPU loop so report endpoint is faster,
		// but still visible in profiler for comparison
		for (int i = 1; i <= 500_000; i++) { // 500k instead of 50M
			total += Math.sqrt(i) * 0.01;
		}

		return total;
	}

	// (Optional) keep old methods but UNUSED so they disappear from stack

	@SuppressWarnings("unused")
	private double getPaymentId(String businessDate) {
		// no longer called in the new flow
		return 0.0;
	}

	@SuppressWarnings("unused")
	private double updatePaymentStatus(String businessDate) {
		// no longer called in the new flow
		return 0.0;
	}
}
