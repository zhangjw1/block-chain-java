package com.blockchain.learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Java Blockchain Learning Application is running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Java Blockchain Learning API");
        response.put("version", "1.0.0");
        response.put("health", "/api/health");
        
        // API端点列表
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("networkInfo", "/api/blockchain/network-info");
        endpoints.put("latestBlock", "/api/blockchain/blocks/latest");
        endpoints.put("blockByNumber", "/api/blockchain/blocks/{blockNumber}");
        endpoints.put("blockByHash", "/api/blockchain/blocks/hash/{blockHash}");
        endpoints.put("transaction", "/api/blockchain/transactions/{txHash}");
        endpoints.put("balance", "/api/blockchain/balance/{address}");
        endpoints.put("transactionCount", "/api/blockchain/address/{address}/transaction-count");
        endpoints.put("gasPrice", "/api/blockchain/gas-price");
        
        response.put("endpoints", endpoints);
        return response;
    }
}