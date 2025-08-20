package com.blockchain.learning.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractVerifierTest {

    @Test
    void testVerifySimpleStorageContract() {
        String result = ContractVerifier.verifySimpleStorageContract();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("SimpleStorage合约验证"));
        assertTrue(result.contains("字节码存在"));
        assertTrue(result.contains("Gas限制"));
        assertTrue(result.contains("Gas价格"));
    }

    @Test
    void testIsValidContractBinary() {
        // Valid binary
        assertTrue(ContractVerifier.isValidContractBinary("0x608060405234801561001057600080fd5b50"));
        
        // Invalid binaries
        assertFalse(ContractVerifier.isValidContractBinary(null));
        assertFalse(ContractVerifier.isValidContractBinary(""));
        assertFalse(ContractVerifier.isValidContractBinary("608060405234801561001057600080fd5b50")); // No 0x prefix
        assertFalse(ContractVerifier.isValidContractBinary("0x")); // Too short
        assertFalse(ContractVerifier.isValidContractBinary("0xgg")); // Invalid hex
    }

    @Test
    void testEstimateDeploymentCost() {
        String result = ContractVerifier.estimateDeploymentCost();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("合约部署成本估算"));
        assertTrue(result.contains("合约大小"));
        assertTrue(result.contains("估算Gas"));
        assertTrue(result.contains("估算费用"));
    }
}