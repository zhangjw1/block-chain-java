package com.blockchain.learning.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.Web3j;

import static org.junit.jupiter.api.Assertions.*;

class Web3ConfigTest {

    @Test
    void testWeb3jBeanCreation() {
        // Arrange
        Web3Config config = new Web3Config();
        ReflectionTestUtils.setField(config, "infuraProjectId", "test-project-id");
        ReflectionTestUtils.setField(config, "networkName", "sepolia");

        // Act
        Web3j web3j = config.web3j();

        // Assert
        assertNotNull(web3j);
    }

    @Test
    void testWeb3jBeanCreation_WithDefaultNetwork() {
        // Arrange
        Web3Config config = new Web3Config();
        ReflectionTestUtils.setField(config, "infuraProjectId", "test-project-id");
        ReflectionTestUtils.setField(config, "networkName", "sepolia"); // 默认值

        // Act
        Web3j web3j = config.web3j();

        // Assert
        assertNotNull(web3j);
    }
}