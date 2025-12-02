package com.example.orderservice.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private static final Map<String, Map<String, Object>> MOCK_CUSTOMERS = new HashMap<>();

	static {
		Map<String, Object> customer1 = new HashMap<>();
		customer1.put("id", "CUST-1001");
		customer1.put("name", "Sumanth Kumar");
		customer1.put("segment", "PREMIUM");
		customer1.put("createdDate", LocalDate.now().minusYears(1).toString());
		MOCK_CUSTOMERS.put("CUST-1001", customer1);
	}

	public Map<String, Object> getCustomerProfile(String customerId) {
		Map<String, Object> customer = loadCustomer(customerId);
		enrichCustomerWithMetadata(customer);
		computeAndAttachRiskScore(customer);
		return customer;
	}

	private Map<String, Object> loadCustomer(String customerId) {
		Map<String, Object> customer = MOCK_CUSTOMERS.get(customerId);
		if (customer == null) {
			throw new RuntimeException("Customer not found: " + customerId);
		}
		return new HashMap<>(customer);
	}

	private void enrichCustomerWithMetadata(Map<String, Object> customer) {
		customer.put("lastLoginDate", LocalDate.now().minusDays(2).toString());
		customer.put("preferredLanguage", "en-IN");
		customer.put("loyaltyTier", "GOLD");
	}

	private void computeAndAttachRiskScore(Map<String, Object> customer) {
		double riskScore = performRiskScoreLoop(customer);
		customer.put("riskScore", riskScore);
	}

	private double performRiskScoreLoop(Map<String, Object> customer) {
		double score = 0.0;
		String segment = (String) customer.getOrDefault("segment", "STANDARD");
		int factor = "PREMIUM".equals(segment) ? 2 : 1;

		for (int i = 0; i < 100_000; i++) { // 100k iterations
			score += (i % 7) * factor * 0.01;
		}
		return score;
	}
}
