package com.blockchain.learning.contracts;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

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