package com.blockchain.learning.service;

import com.blockchain.learning.model.NetworkInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Web3ServiceTest {

    @Mock
    private Web3j web3j;

    @Mock
    private Request<?, Web3ClientVersion> clientVersionRequest;

    @Mock
    private Request<?, EthChainId> chainIdRequest;

    @Mock
    private Request<?, EthBlockNumber> blockNumberRequest;

    @Mock
    private Request<?, EthBlock> blockRequest;

    @InjectMocks
    private Web3Service web3Service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(web3Service, "networkDisplayName", "Test Network");
    }

    @Test
    void testGetNetworkInfo_Success() throws IOException {
        // Arrange
        Web3ClientVersion clientVersion = new Web3ClientVersion();
        clientVersion.setResult("Test-Client/v1.0.0");

        EthChainId chainIdResponse = new EthChainId();
        chainIdResponse.setResult("0x" + BigInteger.valueOf(11155111).toString(16)); // Sepolia chain ID

        EthBlockNumber blockNumberResponse = new EthBlockNumber();
        blockNumberResponse.setResult("0x" + BigInteger.valueOf(1000000).toString(16));

        when(web3j.web3ClientVersion()).thenReturn(clientVersionRequest);
        when(clientVersionRequest.send()).thenReturn(clientVersion);

        when(web3j.ethChainId()).thenReturn(chainIdRequest);
        when(chainIdRequest.send()).thenReturn(chainIdResponse);

        when(web3j.ethBlockNumber()).thenReturn(blockNumberRequest);
        when(blockNumberRequest.send()).thenReturn(blockNumberResponse);

        // Act
        NetworkInfo networkInfo = web3Service.getNetworkInfo();

        // Assert
        assertNotNull(networkInfo);
        assertEquals("Test Network", networkInfo.getNetworkName());
        assertEquals(BigInteger.valueOf(11155111), networkInfo.getChainId());
        assertEquals(BigInteger.valueOf(1000000), networkInfo.getLatestBlockNumber());
        assertEquals("Test-Client/v1.0.0", networkInfo.getNodeVersion());
        assertTrue(networkInfo.isConnected());
    }

    @Test
    void testGetNetworkInfo_IOException() throws IOException {
        // Arrange
        when(web3j.web3ClientVersion()).thenReturn(clientVersionRequest);
        when(clientVersionRequest.send()).thenThrow(new IOException("Connection failed"));

        // Act
        NetworkInfo networkInfo = web3Service.getNetworkInfo();

        // Assert
        assertNotNull(networkInfo);
        assertEquals("Test Network", networkInfo.getNetworkName());
        assertNull(networkInfo.getChainId());
        assertNull(networkInfo.getLatestBlockNumber());
        assertNull(networkInfo.getNodeVersion());
        assertFalse(networkInfo.isConnected());
    }

    @Test
    void testIsConnected_Success() throws IOException {
        // Arrange
        when(web3j.web3ClientVersion()).thenReturn(clientVersionRequest);
        when(clientVersionRequest.send()).thenReturn(new Web3ClientVersion());

        // Act
        boolean connected = web3Service.isConnected();

        // Assert
        assertTrue(connected);
    }

    @Test
    void testIsConnected_Failure() throws IOException {
        // Arrange
        when(web3j.web3ClientVersion()).thenReturn(clientVersionRequest);
        when(clientVersionRequest.send()).thenThrow(new IOException("Connection failed"));

        // Act
        boolean connected = web3Service.isConnected();

        // Assert
        assertFalse(connected);
    }

    @Test
    void testGetLatestBlock() throws IOException {
        // Arrange
        EthBlock.Block mockBlock = mock(EthBlock.Block.class);
        when(mockBlock.getNumber()).thenReturn(BigInteger.valueOf(1000000));

        EthBlock ethBlock = new EthBlock();
        ethBlock.setResult(mockBlock);

        when(web3j.ethGetBlockByNumber(any(), eq(false))).thenReturn(blockRequest);
        when(blockRequest.send()).thenReturn(ethBlock);

        // Act
        EthBlock result = web3Service.getLatestBlock();

        // Assert
        assertNotNull(result);
        assertEquals(BigInteger.valueOf(1000000), result.getBlock().getNumber());
    }
}