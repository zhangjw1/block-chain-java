package com.blockchain.learning.contracts;

import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * SimpleStorage合约包装类测试
 */
class SimpleStorageTest {

    @Test
    void testContractConstants() {
        // 测试合约常量
        assertEquals(BigInteger.valueOf(4_700_000L), SimpleStorage.GAS_LIMIT);
        assertEquals(BigInteger.valueOf(20_000_000_000L), SimpleStorage.GAS_PRICE);
        assertNotNull(SimpleStorage.BINARY);
        assertTrue(SimpleStorage.BINARY.startsWith("0x"));
    }

    @Test
    void testContractCreation() {
        // 测试合约实例创建（不实际连接网络）
        Web3j web3j = mock(Web3j.class);
        Credentials credentials = mock(Credentials.class);
        
        // 测试load方法不会抛出异常
        assertDoesNotThrow(() -> {
            SimpleStorage contract = SimpleStorage.load(
                "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b",
                web3j,
                credentials,
                SimpleStorage.GAS_PRICE,
                SimpleStorage.GAS_LIMIT
            );
            assertNotNull(contract);
        });
    }

    @Test
    void testEventDefinitions() {
        // 测试事件定义
        assertNotNull(SimpleStorage.DATASTORED_EVENT);
        assertNotNull(SimpleStorage.OWNERSHIPTRANSFERRED_EVENT);
        
        assertEquals("DataStored", SimpleStorage.DATASTORED_EVENT.getName());
        assertEquals("OwnershipTransferred", SimpleStorage.OWNERSHIPTRANSFERRED_EVENT.getName());
    }

    @Test
    void testBinaryCodeExists() {
        // 测试字节码存在且格式正确
        String binary = SimpleStorage.BINARY;
        assertNotNull(binary);
        assertFalse(binary.isEmpty());
        assertTrue(binary.startsWith("0x"));
        assertTrue(binary.length() > 100); // 合约字节码应该相当长
    }
}