package com.blockchain.learning.service;

import com.blockchain.learning.exception.WalletException;
import com.blockchain.learning.model.WalletInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private Web3Service web3Service;

    @InjectMocks
    private WalletService walletService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // 设置临时目录作为钱包存储路径
        ReflectionTestUtils.setField(walletService, "walletStoragePath", tempDir.toString());
        ReflectionTestUtils.setField(walletService, "encryptionKey", "test-encryption-key-16");
    }

    @Test
    void testCreateWallet_Success() throws IOException {
        // Arrange
        BigInteger mockBalance = new BigInteger("1000000000000000000"); // 1 ETH
        when(web3Service.getBalance(anyString())).thenReturn(mockBalance);

        // Act
        WalletInfo walletInfo = walletService.createWallet();

        // Assert
        assertNotNull(walletInfo);
        assertNotNull(walletInfo.getAddress());
        assertTrue(walletInfo.getAddress().startsWith("0x"));
        assertEquals(mockBalance, walletInfo.getBalance());
        assertEquals("1", walletInfo.getBalanceInEth());
        assertFalse(walletInfo.isImported());
        assertNotNull(walletInfo.getCreatedAt());
        assertTrue(walletService.hasCurrentWallet());
    }

    @Test
    void testImportWallet_Success() throws IOException {
        // Arrange
        String privateKey = "0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318";
        BigInteger mockBalance = new BigInteger("2000000000000000000"); // 2 ETH
        when(web3Service.getBalance(anyString())).thenReturn(mockBalance);

        // Act
        WalletInfo walletInfo = walletService.importWallet(privateKey);

        // Assert
        assertNotNull(walletInfo);
        assertNotNull(walletInfo.getAddress());
        assertTrue(walletInfo.getAddress().startsWith("0x"));
        assertEquals(mockBalance, walletInfo.getBalance());
        assertEquals("2", walletInfo.getBalanceInEth());
        assertTrue(walletInfo.isImported());
        assertTrue(walletService.hasCurrentWallet());
    }

    @Test
    void testImportWallet_WithoutHexPrefix() throws IOException {
        // Arrange
        String privateKey = "4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318";
        BigInteger mockBalance = BigInteger.ZERO;
        when(web3Service.getBalance(anyString())).thenReturn(mockBalance);

        // Act
        WalletInfo walletInfo = walletService.importWallet(privateKey);

        // Assert
        assertNotNull(walletInfo);
        assertTrue(walletInfo.isImported());
    }

    @Test
    void testImportWallet_EmptyPrivateKey() {
        // Act & Assert
        assertThrows(WalletException.class, () -> {
            walletService.importWallet("");
        });

        assertThrows(WalletException.class, () -> {
            walletService.importWallet(null);
        });
    }

    @Test
    void testImportWallet_InvalidPrivateKey() {
        // Act & Assert
        assertThrows(WalletException.class, () -> {
            walletService.importWallet("invalid-private-key");
        });
    }

    @Test
    void testGetBalance_Success() throws IOException {
        // Arrange
        String address = "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b";
        BigInteger expectedBalance = new BigInteger("5000000000000000000"); // 5 ETH
        when(web3Service.getBalance(address)).thenReturn(expectedBalance);

        // Act
        BigInteger balance = walletService.getBalance(address);

        // Assert
        assertEquals(expectedBalance, balance);
    }

    @Test
    void testGetBalance_NetworkError() throws IOException {
        // Arrange
        String address = "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b";
        when(web3Service.getBalance(address)).thenThrow(new IOException("Network error"));

        // Act & Assert
        assertThrows(WalletException.class, () -> {
            walletService.getBalance(address);
        });
    }

    @Test
    void testGetCurrentWallet_NoWalletLoaded() {
        // Act & Assert
        assertThrows(WalletException.class, () -> {
            walletService.getCurrentWallet();
        });

        assertFalse(walletService.hasCurrentWallet());
    }

    @Test
    void testGetCurrentWallet_Success() throws IOException {
        // Arrange
        BigInteger mockBalance = new BigInteger("3000000000000000000"); // 3 ETH
        when(web3Service.getBalance(anyString())).thenReturn(mockBalance);

        // 先创建一个钱包
        walletService.createWallet();

        // Act
        WalletInfo currentWallet = walletService.getCurrentWallet();

        // Assert
        assertNotNull(currentWallet);
        assertEquals(mockBalance, currentWallet.getBalance());
        assertEquals("3", currentWallet.getBalanceInEth());
    }

    @Test
    void testGetCurrentCredentials_NoWalletLoaded() {
        // Act & Assert
        assertThrows(WalletException.class, () -> {
            walletService.getCurrentCredentials();
        });
    }

    @Test
    void testGetCurrentCredentials_Success() throws IOException {
        // Arrange
        when(web3Service.getBalance(anyString())).thenReturn(BigInteger.ZERO);

        // 先创建一个钱包
        walletService.createWallet();

        // Act
        assertDoesNotThrow(() -> {
            walletService.getCurrentCredentials();
        });

        // Assert
        assertNotNull(walletService.getCurrentCredentials());
    }

    @Test
    void testWalletPersistence() throws IOException {
        // Arrange
        BigInteger mockBalance = BigInteger.ZERO;
        when(web3Service.getBalance(anyString())).thenReturn(mockBalance);

        // 创建钱包
        WalletInfo originalWallet = walletService.createWallet();
        String address = originalWallet.getAddress();

        // Act - 加载钱包
        WalletInfo loadedWallet = walletService.loadWallet(address);

        // Assert
        assertNotNull(loadedWallet);
        assertEquals(address, loadedWallet.getAddress());
        assertTrue(loadedWallet.isImported()); // 加载的钱包被标记为导入的
    }
}