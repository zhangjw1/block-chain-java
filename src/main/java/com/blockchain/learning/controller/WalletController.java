package com.blockchain.learning.controller;

import com.blockchain.learning.exception.WalletException;
import com.blockchain.learning.model.WalletInfo;
import com.blockchain.learning.service.WalletService;
import com.blockchain.learning.util.WalletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 钱包管理控制器
 * 
 * 提供钱包相关的REST API接口，包括钱包创建、导入、余额查询等功能。
 * 所有接口都返回JSON格式的响应数据。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    /**
     * 创建新的EOA（外部拥有账户）钱包
     * 
     * 生成一个新的椭圆曲线密钥对，创建EOA账户。
     * EOA账户可以持有ETH、发送交易、调用智能合约。
     * 
     * @return 新创建的EOA钱包信息
     */
    @PostMapping("/create")
    public ResponseEntity<WalletInfo> createWallet() {
        try {
            logger.info("Creating new EOA wallet via API");
            WalletInfo walletInfo = walletService.createWallet();
            logger.info("Wallet created successfully: {}", walletInfo.getAddress());
            return ResponseEntity.ok(walletInfo);
        } catch (WalletException e) {
            logger.error("Error creating wallet: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 导入EOA钱包
     * 
     * 通过提供的私钥导入现有的EOA账户。
     * 私钥必须是64位十六进制字符串（可选0x前缀）。
     * 
     * @param request 包含私钥的请求体
     * @return 导入的EOA钱包信息
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importWallet(@RequestBody Map<String, String> request) {
        try {
            String privateKey = request.get("privateKey");

            if (privateKey == null || privateKey.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Private key is required");
                errorResponse.put("success", false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 验证私钥格式
            if (!WalletUtils.isValidPrivateKey(privateKey)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid private key format");
                errorResponse.put("success", false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            logger.info("Importing wallet via API");
            WalletInfo walletInfo = walletService.importWallet(privateKey);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("wallet", walletInfo);
            response.put("message", "Wallet imported successfully");

            logger.info("Wallet imported successfully: {}", walletInfo.getAddress());
            return ResponseEntity.ok(response);

        } catch (WalletException e) {
            logger.error("Error importing wallet: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 查询指定地址的余额
     * 
     * @param address 要查询的钱包地址
     * @return 余额信息
     */
    @GetMapping("/balance/{address}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String address) {
        try {
            // 验证地址格式
            if (!WalletUtils.isValidAddress(address)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid address format");
                errorResponse.put("success", false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            logger.info("Querying balance for address: {}", address);
            BigInteger balance = walletService.getBalance(address);

            Map<String, Object> response = new HashMap<>();
            response.put("address", WalletUtils.formatAddress(address));
            response.put("balanceWei", balance.toString());
            response.put("balanceEth",
                    org.web3j.utils.Convert.fromWei(balance.toString(), org.web3j.utils.Convert.Unit.ETHER).toString());
            response.put("success", true);

            return ResponseEntity.ok(response);

        } catch (WalletException e) {
            logger.error("Error querying balance: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 获取当前钱包信息
     * 
     * @return 当前活跃钱包的信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getCurrentWalletInfo() {
        try {
            if (!walletService.hasCurrentWallet()) {
                Map<String, Object> response = new HashMap<>();
                response.put("hasWallet", false);
                response.put("message", "No wallet is currently loaded");
                return ResponseEntity.ok(response);
            }

            logger.info("Getting current wallet info");
            WalletInfo walletInfo = walletService.getCurrentWallet();

            Map<String, Object> response = new HashMap<>();
            response.put("hasWallet", true);
            response.put("wallet", walletInfo);
            response.put("success", true);

            return ResponseEntity.ok(response);

        } catch (WalletException e) {
            logger.error("Error getting current wallet info: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("hasWallet", false);
            errorResponse.put("success", false);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 加载已保存的钱包
     * 
     * @param address 要加载的钱包地址
     * @return 加载的钱包信息
     */
    @PostMapping("/load/{address}")
    public ResponseEntity<Map<String, Object>> loadWallet(@PathVariable String address) {
        try {
            // 验证地址格式
            if (!WalletUtils.isValidAddress(address)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid address format");
                errorResponse.put("success", false);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            logger.info("Loading wallet: {}", address);
            WalletInfo walletInfo = walletService.loadWallet(address);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("wallet", walletInfo);
            response.put("message", "Wallet loaded successfully");

            logger.info("Wallet loaded successfully: {}", walletInfo.getAddress());
            return ResponseEntity.ok(response);

        } catch (WalletException e) {
            logger.error("Error loading wallet: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 验证地址格式
     * 
     * @param request 包含地址的请求体
     * @return 验证结果
     */
    @PostMapping("/validate-address")
    public ResponseEntity<Map<String, Object>> validateAddress(@RequestBody Map<String, String> request) {
        String address = request.get("address");

        Map<String, Object> response = new HashMap<>();

        if (address == null || address.trim().isEmpty()) {
            response.put("valid", false);
            response.put("error", "Address is required");
            return ResponseEntity.ok(response);
        }

        boolean isValid = WalletUtils.isValidAddress(address);
        response.put("valid", isValid);
        response.put("address", address);

        if (isValid) {
            response.put("formattedAddress", WalletUtils.formatAddress(address));
            response.put("checksumAddress", WalletUtils.toChecksumAddress(address));
        } else {
            response.put("error", "Invalid address format");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 验证私钥格式
     * 
     * @param request 包含私钥的请求体
     * @return 验证结果
     */
    @PostMapping("/validate-private-key")
    public ResponseEntity<Map<String, Object>> validatePrivateKey(@RequestBody Map<String, String> request) {
        String privateKey = request.get("privateKey");

        Map<String, Object> response = new HashMap<>();

        if (privateKey == null || privateKey.trim().isEmpty()) {
            response.put("valid", false);
            response.put("error", "Private key is required");
            return ResponseEntity.ok(response);
        }

        boolean isValid = WalletUtils.isValidPrivateKey(privateKey);
        response.put("valid", isValid);

        if (isValid) {
            response.put("formattedPrivateKey", WalletUtils.formatPrivateKey(privateKey));
        } else {
            response.put("error", "Invalid private key format");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 获取钱包状态概览
     * 
     * @return 钱包状态信息
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getWalletStatus() {
        Map<String, Object> response = new HashMap<>();

        boolean hasWallet = walletService.hasCurrentWallet();
        response.put("hasCurrentWallet", hasWallet);

        if (hasWallet) {
            try {
                WalletInfo walletInfo = walletService.getCurrentWallet();
                response.put("currentAddress", walletInfo.getAddress());
                response.put("balance", walletInfo.getBalanceInEth() + " ETH");
                response.put("isImported", walletInfo.isImported());
            } catch (Exception e) {
                logger.warn("Error getting wallet details: {}", e.getMessage());
                response.put("error", "Error retrieving wallet details");
            }
        }

        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}