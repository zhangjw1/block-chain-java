package com.blockchain.learning.controller;

import com.blockchain.learning.exception.WalletException;
import com.blockchain.learning.model.WalletInfo;
import com.blockchain.learning.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    private WalletInfo mockWalletInfo;

    @BeforeEach
    void setUp() {
        mockWalletInfo = new WalletInfo();
        mockWalletInfo.setAddress("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b");
        mockWalletInfo.setBalance(new BigInteger("1000000000000000000")); // 1 ETH
        mockWalletInfo.setBalanceInEth("1.0");
        mockWalletInfo.setCreatedAt(LocalDateTime.now());
        mockWalletInfo.setImported(false);
    }

    @Test
    void testCreateWallet_Success() throws Exception {
        when(walletService.createWallet()).thenReturn(mockWalletInfo);

        mockMvc.perform(post("/api/wallet/create"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"))
                .andExpect(jsonPath("$.balanceInEth").value("1.0"))
                .andExpect(jsonPath("$.imported").value(false));
    }

    @Test
    void testCreateWallet_Error() throws Exception {
        when(walletService.createWallet()).thenThrow(new WalletException("Creation failed"));

        mockMvc.perform(post("/api/wallet/create"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testImportWallet_Success() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("privateKey", "0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318");

        mockWalletInfo.setImported(true);
        when(walletService.importWallet(anyString())).thenReturn(mockWalletInfo);

        mockMvc.perform(post("/api/wallet/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.wallet.address").value("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"))
                .andExpect(jsonPath("$.wallet.imported").value(true));
    }

    @Test
    void testImportWallet_EmptyPrivateKey() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("privateKey", "");

        mockMvc.perform(post("/api/wallet/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Private key is required"));
    }

    @Test
    void testImportWallet_InvalidPrivateKey() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("privateKey", "invalid-key");

        mockMvc.perform(post("/api/wallet/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Invalid private key format"));
    }

    @Test
    void testImportWallet_ServiceError() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("privateKey", "0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318");

        when(walletService.importWallet(anyString())).thenThrow(new WalletException("Import failed"));

        mockMvc.perform(post("/api/wallet/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Import failed"));
    }

    @Test
    void testGetBalance_Success() throws Exception {
        String address = "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b";
        BigInteger balance = new BigInteger("2000000000000000000"); // 2 ETH

        when(walletService.getBalance(address)).thenReturn(balance);

        mockMvc.perform(get("/api/wallet/balance/" + address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.balanceWei").value("2000000000000000000"))
                .andExpect(jsonPath("$.balanceEth").value("2"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetBalance_InvalidAddress() throws Exception {
        String invalidAddress = "invalid-address";

        mockMvc.perform(get("/api/wallet/balance/" + invalidAddress))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Invalid address format"));
    }

    @Test
    void testGetBalance_ServiceError() throws Exception {
        String address = "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b";

        when(walletService.getBalance(address)).thenThrow(new WalletException("Network error"));

        mockMvc.perform(get("/api/wallet/balance/" + address))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Network error"));
    }

    @Test
    void testGetCurrentWalletInfo_HasWallet() throws Exception {
        when(walletService.hasCurrentWallet()).thenReturn(true);
        when(walletService.getCurrentWallet()).thenReturn(mockWalletInfo);

        mockMvc.perform(get("/api/wallet/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasWallet").value(true))
                .andExpect(jsonPath("$.wallet.address").value("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetCurrentWalletInfo_NoWallet() throws Exception {
        when(walletService.hasCurrentWallet()).thenReturn(false);

        mockMvc.perform(get("/api/wallet/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasWallet").value(false))
                .andExpect(jsonPath("$.message").value("No wallet is currently loaded"));
    }

    @Test
    void testLoadWallet_Success() throws Exception {
        String address = "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b";
        mockWalletInfo.setImported(true);

        when(walletService.loadWallet(address)).thenReturn(mockWalletInfo);

        mockMvc.perform(post("/api/wallet/load/" + address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.wallet.address").value(address))
                .andExpect(jsonPath("$.message").value("Wallet loaded successfully"));
    }

    @Test
    void testLoadWallet_InvalidAddress() throws Exception {
        String invalidAddress = "invalid-address";

        mockMvc.perform(post("/api/wallet/load/" + invalidAddress))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Invalid address format"));
    }

    @Test
    void testValidateAddress_Valid() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("address", "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b");

        mockMvc.perform(post("/api/wallet/validate-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.formattedAddress").exists())
                .andExpect(jsonPath("$.checksumAddress").exists());
    }

    @Test
    void testValidateAddress_Invalid() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("address", "invalid-address");

        mockMvc.perform(post("/api/wallet/validate-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.error").value("Invalid address format"));
    }

    @Test
    void testValidatePrivateKey_Valid() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("privateKey", "0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318");

        mockMvc.perform(post("/api/wallet/validate-private-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.formattedPrivateKey").exists());
    }

    @Test
    void testValidatePrivateKey_Invalid() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("privateKey", "invalid-key");

        mockMvc.perform(post("/api/wallet/validate-private-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.error").value("Invalid private key format"));
    }

    @Test
    void testGetWalletStatus_HasWallet() throws Exception {
        when(walletService.hasCurrentWallet()).thenReturn(true);
        when(walletService.getCurrentWallet()).thenReturn(mockWalletInfo);

        mockMvc.perform(get("/api/wallet/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasCurrentWallet").value(true))
                .andExpect(jsonPath("$.currentAddress").value("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"))
                .andExpect(jsonPath("$.balance").value("1.0 ETH"))
                .andExpect(jsonPath("$.isImported").value(false));
    }

    @Test
    void testGetWalletStatus_NoWallet() throws Exception {
        when(walletService.hasCurrentWallet()).thenReturn(false);

        mockMvc.perform(get("/api/wallet/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasCurrentWallet").value(false))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}