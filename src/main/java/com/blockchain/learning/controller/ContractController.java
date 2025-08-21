package com.blockchain.learning.controller;

import com.blockchain.learning.model.ContractDeployResult;
import com.blockchain.learning.model.TransactionResult;
import com.blockchain.learning.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;

@Tag(name = "智能合约交互", description = "用于部署智能合约及交互的API")
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;

    @Operation(summary = "部署新的SimpleStorage合约",
               description = "将SimpleStorage合约部署到区块链。调用前必须已加载钱包。",
               responses = {
                   @ApiResponse(responseCode = "200", description = "合约部署成功",
                                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDeployResult.class))),
                   @ApiResponse(responseCode = "500", description = "服务器或区块链错误")
               })
    @PostMapping("/deploy")
    public ResponseEntity<ContractDeployResult> deployContract() {
        logger.info("API request received to deploy SimpleStorage contract.");
        ContractDeployResult result = contractService.deploySimpleStorageContract();
        logger.info("Contract deployment successful via API. Address: {}", result.getContractAddress());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "从合约中获取存储的值",
               description = "调用SimpleStorage合约的'get'方法以获取当前存储的数字。",
               responses = {
                   @ApiResponse(responseCode = "200", description = "成功获取到值"),
                   @ApiResponse(responseCode = "500", description = "服务器或区块链错误")
               })
    @GetMapping("/{contractAddress}/value")
    public ResponseEntity<?> getValue(
            @Parameter(description = "智能合约的20字节地址 (例如, 0x...)", required = true)
            @PathVariable String contractAddress) {
        logger.info("API request to get value from contract: {}", contractAddress);
        BigInteger value = contractService.getValue(contractAddress);
        return ResponseEntity.ok(Collections.singletonMap("value", value));
    }

    @Operation(summary = "向合约设置一个新的值",
               description = "调用SimpleStorage合约的'set'方法以存储一个新的数字。这会在区块链上创建一笔交易。",
               requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "需要存储的新值。", required = true,
                                content = @Content(mediaType = "application/json",
                                             schema = @Schema(type = "object"),
                                             examples = @ExampleObject(value = "{\"value\": \"123\"}"))),
               responses = {
                   @ApiResponse(responseCode = "200", description = "交易发送成功",
                                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResult.class))),
                   @ApiResponse(responseCode = "400", description = "输入值无效"),
                   @ApiResponse(responseCode = "500", description = "服务器或区块链错误")
               })
    @PostMapping("/{contractAddress}/value")
    public ResponseEntity<TransactionResult> setValue(
            @Parameter(description = "智能合约的20字节地址 (例如, 0x...)", required = true)
            @PathVariable String contractAddress,
            @RequestBody Map<String, String> payload) {
        logger.info("API request to set value for contract: {}", contractAddress);
        String valueStr = payload.get("value");
        if (valueStr == null || valueStr.trim().isEmpty()) {
            throw new IllegalArgumentException("请求体中必须包含value字段。");
        }
        BigInteger newValue;
        try {
            newValue = new BigInteger(valueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的数字格式。", e);
        }
        TransactionResult result = contractService.setValue(contractAddress, newValue);
        return ResponseEntity.ok(result);
    }
}
