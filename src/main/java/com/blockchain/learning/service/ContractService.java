package com.blockchain.learning.service;

import com.blockchain.learning.contracts.SimpleStorage;
import com.blockchain.learning.exception.BlockchainException;
import com.blockchain.learning.model.ContractDeployResult;
import com.blockchain.learning.model.TransactionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
public class ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000L);

    @Autowired
    private Web3j web3j;

    @Autowired
    private Web3Service web3Service;

    @Autowired
    private WalletService walletService;

    public ContractDeployResult deploySimpleStorageContract() {
        try {
            Credentials credentials = walletService.getCurrentCredentials();
            logger.info("Deploying contract using wallet address: {}", credentials.getAddress());

            BigInteger gasPrice = web3Service.getGasPrice();
            StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, GAS_LIMIT);

            logger.info("Deploying SimpleStorage.sol with gas price {} and gas limit {}", gasPrice, GAS_LIMIT);
            SimpleStorage contract = SimpleStorage.deploy(web3j, credentials, gasProvider).send();

            String contractAddress = contract.getContractAddress();
            TransactionReceipt receipt = contract.getTransactionReceipt().orElseThrow(
                () -> new BlockchainException("Transaction receipt not available after deployment.")
            );
            String txHash = receipt.getTransactionHash();
            logger.info("Contract deployed successfully! Address: {}, TxHash: {}", contractAddress, txHash);

            ContractDeployResult result = new ContractDeployResult();
            result.setContractAddress(contractAddress);
            result.setTransactionHash(txHash);
            result.setGasUsed(receipt.getGasUsed());
            result.setStatus(receipt.isStatusOK() ? "SUCCESS" : "FAILED");
            result.setDeployedAt(LocalDateTime.now());

            return result;

        } catch (Exception e) {
            logger.error("Failed to deploy SimpleStorage contract: {}", e.getMessage(), e);
            throw new BlockchainException("Could not deploy contract: " + e.getMessage(), e);
        }
    }

    public BigInteger getValue(String contractAddress) {
        try {
            logger.info("Reading value from contract at address: {}", contractAddress);
            Credentials credentials = walletService.getCurrentCredentials();
            BigInteger gasPrice = web3Service.getGasPrice();

            // Corrected call to SimpleStorage.load
            SimpleStorage contract = SimpleStorage.load(contractAddress, web3j, credentials, gasPrice, GAS_LIMIT);

            BigInteger value = contract.get().send();
            logger.info("Value read successfully from contract {}: {}", contractAddress, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to read value from contract {}: {}", contractAddress, e.getMessage(), e);
            throw new BlockchainException("Could not read value from contract: " + e.getMessage(), e);
        }
    }

    public TransactionResult setValue(String contractAddress, BigInteger newValue) {
        try {
            logger.info("Setting value '{}' in contract at address: {}", newValue, contractAddress);

            Credentials credentials = walletService.getCurrentCredentials();
            long chainId = web3j.ethChainId().send().getChainId().longValue();
            TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, chainId);

            BigInteger gasPrice = web3Service.getGasPrice();

            // Corrected call to SimpleStorage.load
            SimpleStorage contract = SimpleStorage.load(contractAddress, web3j, transactionManager, gasPrice, GAS_LIMIT);

            TransactionReceipt receipt = contract.set(newValue).send();
            logger.info("Transaction to set value successful. TxHash: {}", receipt.getTransactionHash());

            TransactionResult result = new TransactionResult();
            result.setTransactionHash(receipt.getTransactionHash());
            result.setStatus(receipt.isStatusOK() ? "SUCCESS" : "FAILED");
            result.setGasUsed(receipt.getGasUsed());
            result.setGasPrice(gasPrice);
            result.setBlockNumber(receipt.getBlockNumber());
            result.setBlockHash(receipt.getBlockHash());

            return result;

        } catch (Exception e) {
            logger.error("Failed to set value in contract {}: {}", contractAddress, e.getMessage(), e);
            throw new BlockchainException("Could not set value in contract: " + e.getMessage(), e);
        }
    }
}
