package com.blockchain.learning.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 钱包API集成测试
 * 
 * 测试钱包相关API的完整流程和集成功能
 */
@Disabled("Integration tests are disabled by default. Provide a valid Infura Project ID to run.")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WalletApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testWalletApiEndpointsAreAccessible() throws Exception {
        // 测试钱包状态端点
        mockMvc.perform(get("/api/wallet/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasCurrentWallet").exists());

        // 测试地址验证端点
        Map<String, String> addressRequest = new HashMap<>();
        addressRequest.put("address", "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b");

        mockMvc.perform(post("/api/wallet/validate-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").exists());

        // 测试私钥验证端点
        Map<String, String> privateKeyRequest = new HashMap<>();
        privateKeyRequest.put("privateKey", "0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318");

        mockMvc.perform(post("/api/wallet/validate-private-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(privateKeyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").exists());
    }

    @Test
    void testWalletCreationFlow() throws Exception {
        // 注意：这个测试可能会因为网络连接问题而失败
        // 在实际环境中，需要有效的Infura配置

        // 测试创建钱包端点是否可访问
        mockMvc.perform(post("/api/wallet/create"))
                .andExpect(status().isOk());
    }

    @Test
    void testValidationEndpoints() throws Exception {
        // 测试有效地址验证
        Map<String, String> validAddressRequest = new HashMap<>();
        validAddressRequest.put("address", "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b");

        mockMvc.perform(post("/api/wallet/validate-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validAddressRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));

        // 测试无效地址验证
        Map<String, String> invalidAddressRequest = new HashMap<>();
        invalidAddressRequest.put("address", "invalid-address");

        mockMvc.perform(post("/api/wallet/validate-address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAddressRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));

        // 测试有效私钥验证
        Map<String, String> validPrivateKeyRequest = new HashMap<>();
        validPrivateKeyRequest.put("privateKey", "0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318");

        mockMvc.perform(post("/api/wallet/validate-private-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPrivateKeyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));

        // 测试无效私钥验证
        Map<String, String> invalidPrivateKeyRequest = new HashMap<>();
        invalidPrivateKeyRequest.put("privateKey", "invalid-key");

        mockMvc.perform(post("/api/wallet/validate-private-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPrivateKeyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    void testErrorHandling() throws Exception {
        // 测试空私钥导入
        Map<String, String> emptyPrivateKeyRequest = new HashMap<>();
        emptyPrivateKeyRequest.put("privateKey", "");

        mockMvc.perform(post("/api/wallet/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyPrivateKeyRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // 测试无效地址余额查询
        mockMvc.perform(get("/api/wallet/balance/invalid-address"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // 测试无效地址加载
        mockMvc.perform(post("/api/wallet/load/invalid-address"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}