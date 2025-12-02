package com.example.orderservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

	public Map<String, Object> getHealth() {
		long start = System.currentTimeMillis();

		Map<String, Object> response = buildBaseHealthPayload();
		enrichWithBuildInfo(response);
		enrichWithRuntimeMetrics(response);
		calculateAndAttachUptime(response, start);

		return response;
	}

	private Map<String, Object> buildBaseHealthPayload() {
		Map<String, Object> response = new HashMap<>();
		response.put("status", "UP");
		response.put("service", "order-service");
		return response;
	}

	private void enrichWithBuildInfo(Map<String, Object> payload) {
		payload.put("version", "1.0.0");
		payload.put("buildNumber", "2025.12.01-001");
	}

	private void enrichWithRuntimeMetrics(Map<String, Object> payload) {
		Runtime runtime = Runtime.getRuntime();
		long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		payload.put("usedMemoryBytes", usedMemory);
		payload.put("availableProcessors", runtime.availableProcessors());
	}

	private void calculateAndAttachUptime(Map<String, Object> payload, long start) {
		long elapsed = System.currentTimeMillis() - start;
		payload.put("healthCheckProcessingTimeMs", elapsed);
		payload.put("timestamp", System.currentTimeMillis());
	}
}
