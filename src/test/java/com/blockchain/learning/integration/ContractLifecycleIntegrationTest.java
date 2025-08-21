package com.blockchain.learning.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 使用单个测试实例，以便在方法间共享非静态字段
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContractLifecycleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 改为实例变量，因为测试类生命周期是PER_CLASS
    private String contractAddress;

    @Test
    @Order(1)
    void step1_createWalletAndDeployContract() throws Exception {
        // 步骤1: 创建一个钱包以确保我们有可用的签名者
        mockMvc.perform(post("/api/wallet/create"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").exists());

        // 步骤2: 部署合约
        MvcResult deployResult = mockMvc.perform(post("/api/contracts/deploy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractAddress").exists())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        // 从响应中提取并存储合约地址
        String jsonResponse = deployResult.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
        this.contractAddress = (String) responseMap.get("contractAddress");
        System.out.println("Deployed Contract Address: " + this.contractAddress);
    }

    @Test
    @Order(2)
    void step2_setValueInContract() throws Exception {
        Assertions.assertNotNull(contractAddress, "Contract address should not be null. Ensure step 1 ran successfully.");

        // 步骤3: 向合约写入一个值 (例如 123)
        BigInteger testValue = new BigInteger("123");
        Map<String, String> requestBody = Collections.singletonMap("value", testValue.toString());

        mockMvc.perform(post("/api/contracts/{contractAddress}/value", contractAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionHash").exists())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @Order(3)
    void step3_getValueFromContractAndVerify() throws Exception {
        Assertions.assertNotNull(contractAddress, "Contract address should not be null. Ensure step 1 ran successfully.");

        // 步骤4: 从合约读取值
        MvcResult getResult = mockMvc.perform(get("/api/contracts/{contractAddress}/value", contractAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").exists())
                .andReturn();

        // 步骤5: 【修正】使用更健壮的方式解析返回的JSON中的数字
        String jsonResponse = getResult.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
        BigInteger retrievedValue = new BigInteger(responseMap.get("value").toString());

        assertEquals(new BigInteger("123"), retrievedValue, "The value read from the contract should match the value that was set.");
        System.out.println("Successfully verified: setValue(123) and getValue() returned 123.");
    }
}
