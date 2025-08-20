package com.blockchain.learning.controller;

import com.blockchain.learning.model.NetworkInfo;
import com.blockchain.learning.service.Web3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private static final Logger logger = LoggerFactory.getLogger(BlockchainController.class);

    @Autowired
    private Web3Service web3Service;

    /**
     * 获取网络信息
     */
    @GetMapping("/network-info")
    public ResponseEntity<NetworkInfo> getNetworkInfo() {
        try {
            logger.info("Fetching network information");
            NetworkInfo networkInfo = web3Service.getNetworkInfo();
            return ResponseEntity.ok(networkInfo);
        } catch (IOException e) {
            logger.error("Error fetching network info: {}", e.getMessage());
            NetworkInfo errorInfo = new NetworkInfo();
            errorInfo.setConnected(false);
            return ResponseEntity.ok(errorInfo);
        }
    }

    /**
     * 获取最新区块
     */
    @GetMapping("/blocks/latest")
    public ResponseEntity<Map<String, Object>> getLatestBlock() {
        try {
            logger.info("Fetching latest block");
            EthBlock ethBlock = web3Service.getLatestBlock();
            EthBlock.Block block = ethBlock.getBlock();
            
            Map<String, Object> response = new HashMap<>();
            response.put("number", block.getNumber());
            response.put("hash", block.getHash());
            response.put("parentHash", block.getParentHash());
            response.put("timestamp", block.getTimestamp());
            response.put("gasLimit", block.getGasLimit());
            response.put("gasUsed", block.getGasUsed());
            response.put("transactionCount", block.getTransactions().size());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error fetching latest block: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根据区块号获取区块
     */
    @GetMapping("/blocks/{blockNumber}")
    public ResponseEntity<Map<String, Object>> getBlockByNumber(@PathVariable String blockNumber) {
        try {
            logger.info("Fetching block by number: {}", blockNumber);
            BigInteger blockNum = new BigInteger(blockNumber);
            EthBlock ethBlock = web3Service.getBlockByNumber(blockNum);
            
            if (ethBlock.getBlock() == null) {
                return ResponseEntity.notFound().build();
            }
            
            EthBlock.Block block = ethBlock.getBlock();
            Map<String, Object> response = new HashMap<>();
            response.put("number", block.getNumber());
            response.put("hash", block.getHash());
            response.put("parentHash", block.getParentHash());
            response.put("timestamp", block.getTimestamp());
            response.put("gasLimit", block.getGasLimit());
            response.put("gasUsed", block.getGasUsed());
            response.put("transactionCount", block.getTransactions().size());
            response.put("miner", block.getMiner());
            
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            logger.error("Invalid block number format: {}", blockNumber);
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            logger.error("Error fetching block by number: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根据区块哈希获取区块
     */
    @GetMapping("/blocks/hash/{blockHash}")
    public ResponseEntity<Map<String, Object>> getBlockByHash(@PathVariable String blockHash) {
        try {
            logger.info("Fetching block by hash: {}", blockHash);
            EthBlock ethBlock = web3Service.getBlockByHash(blockHash);
            
            if (ethBlock.getBlock() == null) {
                return ResponseEntity.notFound().build();
            }
            
            EthBlock.Block block = ethBlock.getBlock();
            Map<String, Object> response = new HashMap<>();
            response.put("number", block.getNumber());
            response.put("hash", block.getHash());
            response.put("parentHash", block.getParentHash());
            response.put("timestamp", block.getTimestamp());
            response.put("gasLimit", block.getGasLimit());
            response.put("gasUsed", block.getGasUsed());
            response.put("transactionCount", block.getTransactions().size());
            response.put("miner", block.getMiner());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error fetching block by hash: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根据交易哈希获取交易信息
     */
    @GetMapping("/transactions/{txHash}")
    public ResponseEntity<Map<String, Object>> getTransaction(@PathVariable String txHash) {
        try {
            logger.info("Fetching transaction: {}", txHash);
            Transaction transaction = web3Service.getTransaction(txHash);
            
            if (transaction == null) {
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("hash", transaction.getHash());
            response.put("blockNumber", transaction.getBlockNumber());
            response.put("blockHash", transaction.getBlockHash());
            response.put("transactionIndex", transaction.getTransactionIndex());
            response.put("from", transaction.getFrom());
            response.put("to", transaction.getTo());
            response.put("value", transaction.getValue());
            response.put("valueInEth", Convert.fromWei(transaction.getValue().toString(), Convert.Unit.ETHER));
            response.put("gas", transaction.getGas());
            response.put("gasPrice", transaction.getGasPrice());
            response.put("nonce", transaction.getNonce());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error fetching transaction: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取地址余额
     */
    @GetMapping("/balance/{address}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String address) {
        try {
            logger.info("Fetching balance for address: {}", address);
            BigInteger balance = web3Service.getBalance(address);
            
            Map<String, Object> response = new HashMap<>();
            response.put("address", address);
            response.put("balanceWei", balance);
            response.put("balanceEth", Convert.fromWei(balance.toString(), Convert.Unit.ETHER));
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error fetching balance: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取地址交易数量
     */
    @GetMapping("/address/{address}/transaction-count")
    public ResponseEntity<Map<String, Object>> getTransactionCount(@PathVariable String address) {
        try {
            logger.info("Fetching transaction count for address: {}", address);
            BigInteger count = web3Service.getTransactionCount(address);
            
            Map<String, Object> response = new HashMap<>();
            response.put("address", address);
            response.put("transactionCount", count);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error fetching transaction count: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取当前Gas价格
     */
    @GetMapping("/gas-price")
    public ResponseEntity<Map<String, Object>> getGasPrice() {
        try {
            logger.info("Fetching current gas price");
            BigInteger gasPrice = web3Service.getGasPrice();
            
            Map<String, Object> response = new HashMap<>();
            response.put("gasPriceWei", gasPrice);
            response.put("gasPriceGwei", Convert.fromWei(gasPrice.toString(), Convert.Unit.GWEI));
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error fetching gas price: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}