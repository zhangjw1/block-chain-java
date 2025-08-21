package com.blockchain.learning.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "健康检查与概览", description = "提供应用健康状态和API概览")
@RestController
@RequestMapping("/api")
public class HealthController {

    @Operation(summary = "应用健康检查", description = "返回应用当前的运行状态。如果返回'UP'，则表示应用正常运行。")
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Java Blockchain Learning Application is running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Operation(summary = "API概览", description = "欢迎信息及所有可用API端点的列表。")
    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Java Blockchain Learning API");
        response.put("version", "1.0.0");
        response.put("health", "/api/health");
        
        // API端点列表
        Map<String, String> endpoints = new HashMap<>();
        
        // 区块链查询端点
        endpoints.put("networkInfo", "/api/blockchain/network-info");
        endpoints.put("latestBlock", "/api/blockchain/blocks/latest");
        endpoints.put("blockByNumber", "/api/blockchain/blocks/{blockNumber}");
        endpoints.put("blockByHash", "/api/blockchain/blocks/hash/{blockHash}");
        endpoints.put("transaction", "/api/blockchain/transactions/{txHash}");
        endpoints.put("balance", "/api/blockchain/balance/{address}");
        endpoints.put("transactionCount", "/api/blockchain/address/{address}/transaction-count");
        endpoints.put("gasPrice", "/api/blockchain/gas-price");
        
        // 钱包管理端点
        endpoints.put("createWallet", "/api/wallet/create");
        endpoints.put("importWallet", "/api/wallet/import");
        endpoints.put("walletBalance", "/api/wallet/balance/{address}");
        endpoints.put("walletInfo", "/api/wallet/info");
        endpoints.put("loadWallet", "/api/wallet/load/{address}");
        endpoints.put("walletStatus", "/api/wallet/status");
        endpoints.put("validateAddress", "/api/wallet/validate-address");
        endpoints.put("validatePrivateKey", "/api/wallet/validate-private-key");
        
        response.put("endpoints", endpoints);
        return response;
    }
}