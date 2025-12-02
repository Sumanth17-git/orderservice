package com.example.orderservice.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private static final Map<String, Map<String, Object>> MOCK_ORDERS = new HashMap<>();

	static {
		Map<String, Object> order1 = new HashMap<>();
		order1.put("id", "ORD-5001");
		order1.put("customerId", "CUST-1001");
		order1.put("status", "COMPLETED");
		order1.put("amount", 1299.99);
		order1.put("createdDate", LocalDate.now().minusDays(1).toString());
		order1.put("channel", "WEB");
		MOCK_ORDERS.put("ORD-5001", order1);
	}

	// ========= /api/orders/{orderId} =========

	public Map<String, Object> getOrderById(String orderId) {
		long start = System.currentTimeMillis();

		Map<String, Object> order = loadOrder(orderId);
		validateOrder(order);
		enrichOrder(order);
		applyFraudChecks(order);

		long elapsed = System.currentTimeMillis() - start;

		Map<String, Object> enrichedOrder = new HashMap<>(order);
		enrichedOrder.put("processingTimeMs", elapsed);
		return enrichedOrder;
	}

	private Map<String, Object> loadOrder(String orderId) {
		Map<String, Object> order = MOCK_ORDERS.get(orderId);
		if (order == null) {
			throw new RuntimeException("Order not found: " + orderId);
		}
		return new HashMap<>(order);
	}

	private void validateOrder(Map<String, Object> order) {
		runBasicValidation(order);
		runAmountValidation(order);
	}

	private void runBasicValidation(Map<String, Object> order) {
		if (!order.containsKey("id") || !order.containsKey("customerId")) {
			throw new IllegalStateException("Invalid order structure: " + order);
		}
	}

	private void runAmountValidation(Map<String, Object> order) {
		Object amountObj = order.get("amount");
		if (!(amountObj instanceof Number) || ((Number) amountObj).doubleValue() < 0) {
			throw new IllegalStateException("Invalid order amount: " + amountObj);
		}
	}

	private void enrichOrder(Map<String, Object> order) {
		order.putIfAbsent("country", "IN");
		order.putIfAbsent("channel", "WEB");
		order.putIfAbsent("fulfilmentType", "STANDARD");
	}

	private void applyFraudChecks(Map<String, Object> order) {
		double fraudScore = computeFraudScore(order);
		order.put("fraudScore", fraudScore);
		order.put("isFlagged", fraudScore > 70.0);
	}

	private double computeFraudScore(Map<String, Object> order) {
		return performFraudScoreLoop(order);
	}

	/**
	 * CPU-heavy loop using Math.sqrt so /api/orders endpoints show up nicely in
	 * profiler.
	 */
	private double performFraudScoreLoop(Map<String, Object> order) {
		String channel = (String) order.getOrDefault("channel", "WEB");
		int factor = "WEB".equals(channel) ? 3 : 1;

		double score = 0.0;

		// 🔥 CPU hotspot for profiling
		for (int i = 1; i <= 5_000_000; i++) { // adjust 5M/10M depending on how heavy you want
			score += Math.sqrt(i) * factor * 0.0001;
		}

		return score;
	}

	// ========= /api/orders =========

	public Map<String, Object> listOrders(String status, int limit) {
		long start = System.currentTimeMillis();

		List<Map<String, Object>> orders = generateBaseOrders(status, limit);
		applyBusinessFilters(orders, status);
		sortOrdersForResponse(orders);
		double totalAmount = aggregateOrders(orders);

		long elapsed = System.currentTimeMillis() - start;

		Map<String, Object> response = new HashMap<>();
		response.put("statusFilter", status);
		response.put("totalAmount", totalAmount);
		response.put("count", orders.size());
		response.put("processingTimeMs", elapsed);
		response.put("data", orders);

		return response;
	}

	private List<Map<String, Object>> generateBaseOrders(String status, int limit) {
		List<Map<String, Object>> orders = new ArrayList<>();
		for (int i = 1; i <= limit; i++) {
			Map<String, Object> o = new HashMap<>();
			o.put("id", "ORD-" + (5000 + i));
			o.put("status", status);
			o.put("amount", 500.0 + i * 10); // Double on purpose
			o.put("createdDate", LocalDate.now().minusDays(i % 5).toString());
			o.put("channel", (i % 2 == 0) ? "MOBILE" : "WEB");
			orders.add(o);
		}
		return orders;
	}

	private void applyBusinessFilters(List<Map<String, Object>> orders, String status) {
		removeCancelledOrders(orders);
		markHighValueOrders(orders);
	}

	private void removeCancelledOrders(List<Map<String, Object>> orders) {
		orders.removeIf(o -> "CANCELLED".equals(o.get("status")));
	}

	private void markHighValueOrders(List<Map<String, Object>> orders) {
		for (Map<String, Object> order : orders) {
			Number n = (Number) order.get("amount");
			double amount = n.doubleValue();
			order.put("highValue", amount > 1000.0);
		}
	}

	private void sortOrdersForResponse(List<Map<String, Object>> orders) {
		orders.sort(Comparator.comparingDouble(o -> {
			Number n = (Number) o.get("amount");
			return n.doubleValue();
		}));
	}

	private double aggregateOrders(List<Map<String, Object>> orders) {
		double total = 0.0;
		for (Map<String, Object> order : orders) {
			Number n = (Number) order.get("amount");
			total += n.doubleValue();
		}
		return total;
	}
}
