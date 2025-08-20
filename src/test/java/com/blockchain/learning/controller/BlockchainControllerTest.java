package com.blockchain.learning.controller;

import com.blockchain.learning.model.NetworkInfo;
import com.blockchain.learning.service.Web3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlockchainController.class)
class BlockchainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Web3Service web3Service;

    private NetworkInfo mockNetworkInfo;
    private EthBlock mockEthBlock;
    private EthBlock.Block mockBlock;
    private Transaction mockTransaction;

    @BeforeEach
    void setUp() {
        // Setup mock NetworkInfo
        mockNetworkInfo = new NetworkInfo();
        mockNetworkInfo.setNetworkName("Test Network");
        mockNetworkInfo.setChainId(BigInteger.valueOf(11155111));
        mockNetworkInfo.setLatestBlockNumber(BigInteger.valueOf(1000000));
        mockNetworkInfo.setNodeVersion("Test-Client/v1.0.0");
        mockNetworkInfo.setConnected(true);

        // Setup mock Block
        mockBlock = mock(EthBlock.Block.class);
        when(mockBlock.getNumber()).thenReturn(BigInteger.valueOf(1000000));
        when(mockBlock.getHash()).thenReturn("0x123456789abcdef");
        when(mockBlock.getParentHash()).thenReturn("0x987654321fedcba");
        when(mockBlock.getTimestamp()).thenReturn(BigInteger.valueOf(1640995200));
        when(mockBlock.getGasLimit()).thenReturn(BigInteger.valueOf(30000000));
        when(mockBlock.getGasUsed()).thenReturn(BigInteger.valueOf(15000000));
        when(mockBlock.getTransactions()).thenReturn(java.util.Collections.emptyList());
        when(mockBlock.getMiner()).thenReturn("0xminer123");

        mockEthBlock = mock(EthBlock.class);
        when(mockEthBlock.getBlock()).thenReturn(mockBlock);

        // Setup mock Transaction
        mockTransaction = mock(Transaction.class);
        when(mockTransaction.getHash()).thenReturn("0xtxhash123");
        when(mockTransaction.getBlockNumber()).thenReturn(BigInteger.valueOf(1000000));
        when(mockTransaction.getBlockHash()).thenReturn("0x123456789abcdef");
        when(mockTransaction.getTransactionIndex()).thenReturn(BigInteger.valueOf(0));
        when(mockTransaction.getFrom()).thenReturn("0xfrom123");
        when(mockTransaction.getTo()).thenReturn("0xto456");
        when(mockTransaction.getValue()).thenReturn(BigInteger.valueOf(1000000000000000000L)); // 1 ETH in Wei
        when(mockTransaction.getGas()).thenReturn(BigInteger.valueOf(21000));
        when(mockTransaction.getGasPrice()).thenReturn(BigInteger.valueOf(20000000000L));
        when(mockTransaction.getNonce()).thenReturn(BigInteger.valueOf(1));
    }

    @Test
    void testGetNetworkInfo_Success() throws Exception {
        when(web3Service.getNetworkInfo()).thenReturn(mockNetworkInfo);

        mockMvc.perform(get("/api/blockchain/network-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.networkName").value("Test Network"))
                .andExpect(jsonPath("$.chainId").value(11155111))
                .andExpect(jsonPath("$.latestBlockNumber").value(1000000))
                .andExpect(jsonPath("$.connected").value(true));
    }

    @Test
    void testGetNetworkInfo_Error() throws Exception {
        when(web3Service.getNetworkInfo()).thenThrow(new IOException("Connection failed"));

        mockMvc.perform(get("/api/blockchain/network-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.connected").value(false));
    }

    @Test
    void testGetLatestBlock_Success() throws Exception {
        when(web3Service.getLatestBlock()).thenReturn(mockEthBlock);

        mockMvc.perform(get("/api/blockchain/blocks/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1000000))
                .andExpect(jsonPath("$.hash").value("0x123456789abcdef"))
                .andExpect(jsonPath("$.transactionCount").value(0));
    }

    @Test
    void testGetBlockByNumber_Success() throws Exception {
        when(web3Service.getBlockByNumber(any(BigInteger.class))).thenReturn(mockEthBlock);

        mockMvc.perform(get("/api/blockchain/blocks/1000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1000000))
                .andExpect(jsonPath("$.hash").value("0x123456789abcdef"))
                .andExpect(jsonPath("$.miner").value("0xminer123"));
    }

    @Test
    void testGetBlockByNumber_InvalidNumber() throws Exception {
        mockMvc.perform(get("/api/blockchain/blocks/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBlockByNumber_NotFound() throws Exception {
        EthBlock emptyBlock = mock(EthBlock.class);
        when(emptyBlock.getBlock()).thenReturn(null);
        when(web3Service.getBlockByNumber(any(BigInteger.class))).thenReturn(emptyBlock);

        mockMvc.perform(get("/api/blockchain/blocks/999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTransaction_Success() throws Exception {
        when(web3Service.getTransaction(anyString())).thenReturn(mockTransaction);

        mockMvc.perform(get("/api/blockchain/transactions/0xtxhash123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hash").value("0xtxhash123"))
                .andExpect(jsonPath("$.from").value("0xfrom123"))
                .andExpect(jsonPath("$.to").value("0xto456"))
                .andExpect(jsonPath("$.valueInEth").value("1"));
    }

    @Test
    void testGetTransaction_NotFound() throws Exception {
        when(web3Service.getTransaction(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/blockchain/transactions/0xnonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBalance_Success() throws Exception {
        BigInteger balance = new BigInteger("1000000000000000000"); // 1 ETH in Wei
        when(web3Service.getBalance(anyString())).thenReturn(balance);

        mockMvc.perform(get("/api/blockchain/balance/0xaddress123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("0xaddress123"))
                .andExpect(jsonPath("$.balanceWei").value("1000000000000000000"))
                .andExpect(jsonPath("$.balanceEth").value("1"));
    }

    @Test
    void testGetTransactionCount_Success() throws Exception {
        when(web3Service.getTransactionCount(anyString())).thenReturn(BigInteger.valueOf(42));

        mockMvc.perform(get("/api/blockchain/address/0xaddress123/transaction-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("0xaddress123"))
                .andExpect(jsonPath("$.transactionCount").value(42));
    }

    @Test
    void testGetGasPrice_Success() throws Exception {
        BigInteger gasPrice = new BigInteger("20000000000"); // 20 Gwei
        when(web3Service.getGasPrice()).thenReturn(gasPrice);

        mockMvc.perform(get("/api/blockchain/gas-price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gasPriceWei").value("20000000000"))
                .andExpect(jsonPath("$.gasPriceGwei").value("20"));
    }
}