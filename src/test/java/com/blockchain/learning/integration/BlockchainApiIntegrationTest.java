package com.blockchain.learning.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class BlockchainApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testApplicationStartsSuccessfully() throws Exception {
        // 测试健康检查端点
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void testWelcomeEndpointWithAllLinks() throws Exception {
        // 测试欢迎页面包含所有API链接
        mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.endpoints").exists())
                .andExpect(jsonPath("$.endpoints.networkInfo").exists())
                .andExpect(jsonPath("$.endpoints.latestBlock").exists())
                .andExpect(jsonPath("$.endpoints.balance").exists());
    }

    @Test
    void testBlockchainEndpointsAreAccessible() throws Exception {
        // 注意：这些测试可能会因为网络连接问题而失败
        // 在实际环境中，这些端点需要有效的Infura配置
        
        // 测试网络信息端点是否可访问（不验证具体内容）
        mockMvc.perform(get("/api/blockchain/network-info"))
                .andExpect(status().isOk());
        
        // 测试Gas价格端点是否可访问
        mockMvc.perform(get("/api/blockchain/gas-price"))
                .andExpect(status().isOk());
    }
}