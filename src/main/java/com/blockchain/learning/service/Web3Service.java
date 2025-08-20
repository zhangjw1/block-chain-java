package com.blockchain.learning.service;

import com.blockchain.learning.model.NetworkInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;

@Service
public class Web3Service {

    private static final Logger logger = LoggerFactory.getLogger(Web3Service.class);

    @Autowired
    private Web3j web3j;

    @Value("${blockchain.network.display-name:Unknown Network}")
    private String networkDisplayName;

    @PostConstruct
    public void init() {
        try {
            NetworkInfo networkInfo = getNetworkInfo();
            logger.info("Successfully connected to blockchain network: {}", networkInfo);
        } catch (Exception e) {
            logger.error("Failed to connect to blockchain network: {}", e.getMessage());
        }
    }

    /**
     * 获取网络信息
     */
    public NetworkInfo getNetworkInfo() throws IOException {
        logger.debug("Fetching network information...");

        try {
            // 获取客户端版本
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            String nodeVersion = clientVersion.getWeb3ClientVersion();

            // 获取链ID
            EthChainId chainIdResponse = web3j.ethChainId().send();
            BigInteger chainId = chainIdResponse.getChainId();

            // 获取最新区块号
            BigInteger latestBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();

            NetworkInfo networkInfo = new NetworkInfo(
                    networkDisplayName,
                    chainId,
                    latestBlockNumber,
                    nodeVersion,
                    true
            );

            logger.debug("Network info retrieved: {}", networkInfo);
            return networkInfo;

        } catch (IOException e) {
            logger.error("Error fetching network info: {}", e.getMessage());
            return new NetworkInfo(networkDisplayName, null, null, null, false);
        }
    }

    /**
     * 获取最新区块
     */
    public EthBlock getLatestBlock() throws IOException {
        logger.debug("Fetching latest block...");
        EthBlock block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
        logger.debug("Latest block number: {}", block.getBlock().getNumber());
        return block;
    }

    /**
     * 根据区块号获取区块
     */
    public EthBlock getBlockByNumber(BigInteger blockNumber) throws IOException {
        logger.debug("Fetching block by number: {}", blockNumber);
        EthBlock block = web3j.ethGetBlockByNumber(
                org.web3j.protocol.core.DefaultBlockParameter.valueOf(blockNumber), 
                false
        ).send();
        return block;
    }

    /**
     * 根据交易哈希获取交易信息
     */
    public Transaction getTransaction(String transactionHash) throws IOException {
        logger.debug("Fetching transaction: {}", transactionHash);
        return web3j.ethGetTransactionByHash(transactionHash).send().getTransaction().orElse(null);
    }

    /**
     * 获取地址余额
     */
    public BigInteger getBalance(String address) throws IOException {
        logger.debug("Fetching balance for address: {}", address);
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return balance.getBalance();
    }

    /**
     * 获取地址的交易数量（nonce）
     */
    public BigInteger getTransactionCount(String address) throws IOException {
        logger.debug("Fetching transaction count for address: {}", address);
        return web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
    }

    /**
     * 获取当前Gas价格
     */
    public BigInteger getGasPrice() throws IOException {
        logger.debug("Fetching current gas price...");
        return web3j.ethGasPrice().send().getGasPrice();
    }

    /**
     * 根据区块哈希获取区块
     */
    public EthBlock getBlockByHash(String blockHash) throws IOException {
        logger.debug("Fetching block by hash: {}", blockHash);
        return web3j.ethGetBlockByHash(blockHash, false).send();
    }

    /**
     * 检查连接状态
     */
    public boolean isConnected() {
        try {
            web3j.web3ClientVersion().send();
            return true;
        } catch (IOException e) {
            logger.warn("Connection check failed: {}", e.getMessage());
            return false;
        }
    }
}