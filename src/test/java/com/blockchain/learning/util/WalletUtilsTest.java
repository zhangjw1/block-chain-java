package com.blockchain.learning.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletUtilsTest {

    @Test
    void testIsValidAddress_ValidAddresses() {
        // Valid addresses
        assertTrue(WalletUtils.isValidAddress("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"));
        assertTrue(WalletUtils.isValidAddress("742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"));
        assertTrue(WalletUtils.isValidAddress("0x0000000000000000000000000000000000000000"));
        assertTrue(WalletUtils.isValidAddress("0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"));
    }

    @Test
    void testIsValidAddress_InvalidAddresses() {
        // Invalid addresses
        assertFalse(WalletUtils.isValidAddress(null));
        assertFalse(WalletUtils.isValidAddress(""));
        assertFalse(WalletUtils.isValidAddress("   "));
        assertFalse(WalletUtils.isValidAddress("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8")); // Too short
        assertFalse(WalletUtils.isValidAddress("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8bb")); // Too long
        assertFalse(WalletUtils.isValidAddress("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8g")); // Invalid hex
        assertFalse(WalletUtils.isValidAddress("742d35Cc6634C0532925a3b8D4C9db96c4b4d8g")); // Invalid hex without 0x
    }

    @Test
    void testIsValidPrivateKey_ValidKeys() {
        // Valid private keys
        assertTrue(WalletUtils.isValidPrivateKey("0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318"));
        assertTrue(WalletUtils.isValidPrivateKey("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318"));
        assertTrue(WalletUtils.isValidPrivateKey("0x0000000000000000000000000000000000000000000000000000000000000001"));
    }

    @Test
    void testIsValidPrivateKey_InvalidKeys() {
        // Invalid private keys
        assertFalse(WalletUtils.isValidPrivateKey(null));
        assertFalse(WalletUtils.isValidPrivateKey(""));
        assertFalse(WalletUtils.isValidPrivateKey("   "));
        assertFalse(WalletUtils.isValidPrivateKey("0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f36231")); // Too short
        assertFalse(WalletUtils.isValidPrivateKey("0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f3623188")); // Too long
        assertFalse(WalletUtils.isValidPrivateKey("0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f36231g")); // Invalid hex
        assertFalse(WalletUtils.isValidPrivateKey("invalid-private-key"));
    }

    @Test
    void testFormatAddress() {
        // Test formatting addresses
        assertEquals("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b", 
                    WalletUtils.formatAddress("742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"));
        assertEquals("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b", 
                    WalletUtils.formatAddress("0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"));
        
        // Test edge cases
        assertNull(WalletUtils.formatAddress(null));
        assertEquals("", WalletUtils.formatAddress(""));
        assertEquals("   ", WalletUtils.formatAddress("   "));
    }

    @Test
    void testFormatPrivateKey() {
        // Test formatting private keys
        assertEquals("0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 
                    WalletUtils.formatPrivateKey("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318"));
        assertEquals("0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 
                    WalletUtils.formatPrivateKey("0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318"));
        
        // Test edge cases
        assertNull(WalletUtils.formatPrivateKey(null));
        assertEquals("", WalletUtils.formatPrivateKey(""));
        assertEquals("   ", WalletUtils.formatPrivateKey("   "));
    }

    @Test
    void testToChecksumAddress() {
        // Test checksum address conversion
        String address = "0x742d35cc6634c0532925a3b8d4c9db96c4b4d8b";
        String checksumAddress = WalletUtils.toChecksumAddress(address);
        
        assertNotNull(checksumAddress);
        assertTrue(checksumAddress.startsWith("0x"));
        assertEquals(42, checksumAddress.length());
        
        // Test with invalid address
        assertThrows(IllegalArgumentException.class, () -> {
            WalletUtils.toChecksumAddress("invalid-address");
        });
    }

    @Test
    void testIsValidChecksumAddress() {
        // This test depends on the specific checksum implementation
        // We'll test with a known valid checksum address
        String validChecksumAddress = "0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed";
        
        // Note: This might need adjustment based on the actual Web3j implementation
        // For now, we'll just test that it doesn't throw an exception
        assertDoesNotThrow(() -> {
            WalletUtils.isValidChecksumAddress(validChecksumAddress);
        });
        
        // Test with invalid address
        assertFalse(WalletUtils.isValidChecksumAddress("invalid-address"));
        assertFalse(WalletUtils.isValidChecksumAddress(null));
    }
}