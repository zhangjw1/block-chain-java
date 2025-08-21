package com.blockchain.learning.controller;

import com.blockchain.learning.model.EtherscanTransaction;
import com.blockchain.learning.service.ExplorerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "区块链浏览器 API", description = "提供与外部区块链浏览器集成的接口")
@RestController
@RequestMapping("/api/explorer")
public class ExplorerController {

    @Autowired
    private ExplorerService explorerService;

    @Operation(summary = "获取地址交易历史", description = "通过Etherscan API查询指定地址的交易列表")
    @GetMapping("/txlist/{address}")
    public ResponseEntity<List<EtherscanTransaction>> getTransactionHistory(
            @Parameter(description = "要查询的以太坊地址", required = true, example = "0xAb5801a7D398351b8bE11C439e05C5B3259aeC9B") @PathVariable String address) {
        List<EtherscanTransaction> transactions = explorerService.getTransactionHistory(address);
        return ResponseEntity.ok(transactions);
    }
}
