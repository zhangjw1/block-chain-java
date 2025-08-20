package com.blockchain.learning.model;

import java.math.BigInteger;

/**
 * 交易结果模型类
 * 
 * 用于封装区块链交易的执行结果信息，包括交易哈希、状态、Gas消耗等。
 * 主要用于向用户反馈交易的执行状态和相关详细信息。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class TransactionResult {
    
    /** 交易哈希值（交易的唯一标识符） */
    private String transactionHash;
    
    /** 交易状态（如：SUCCESS, FAILED, PENDING） */
    private String status;
    
    /** 交易实际消耗的Gas数量 */
    private BigInteger gasUsed;
    
    /** 交易的Gas价格（每单位Gas的价格，以Wei为单位） */
    private BigInteger gasPrice;
    
    /** 包含此交易的区块哈希 */
    private String blockHash;
    
    /** 包含此交易的区块号 */
    private BigInteger blockNumber;

    /**
     * 默认构造函数
     */
    public TransactionResult() {
    }

    /**
     * 构造函数
     * 
     * @param transactionHash 交易哈希
     * @param status 交易状态
     * @param gasUsed Gas消耗量
     * @param gasPrice Gas价格
     * @param blockHash 区块哈希
     * @param blockNumber 区块号
     */
    public TransactionResult(String transactionHash, String status, BigInteger gasUsed, 
                           BigInteger gasPrice, String blockHash, BigInteger blockNumber) {
        this.transactionHash = transactionHash;
        this.status = status;
        this.gasUsed = gasUsed;
        this.gasPrice = gasPrice;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
    }

    /**
     * 获取交易哈希
     * 
     * @return 交易的唯一标识符（64位十六进制字符串）
     */
    public String getTransactionHash() {
        return transactionHash;
    }

    /**
     * 设置交易哈希
     * 
     * @param transactionHash 交易哈希值
     */
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    /**
     * 获取交易状态
     * 
     * @return 交易执行状态（SUCCESS表示成功，FAILED表示失败）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置交易状态
     * 
     * @param status 交易状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取Gas消耗量
     * 
     * @return 交易实际消耗的Gas数量
     */
    public BigInteger getGasUsed() {
        return gasUsed;
    }

    /**
     * 设置Gas消耗量
     * 
     * @param gasUsed Gas消耗数量
     */
    public void setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
    }

    /**
     * 获取Gas价格
     * 
     * @return 每单位Gas的价格（以Wei为单位）
     */
    public BigInteger getGasPrice() {
        return gasPrice;
    }

    /**
     * 设置Gas价格
     * 
     * @param gasPrice Gas价格（Wei）
     */
    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    /**
     * 获取区块哈希
     * 
     * @return 包含此交易的区块哈希值
     */
    public String getBlockHash() {
        return blockHash;
    }

    /**
     * 设置区块哈希
     * 
     * @param blockHash 区块哈希值
     */
    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    /**
     * 获取区块号
     * 
     * @return 包含此交易的区块编号
     */
    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    /**
     * 设置区块号
     * 
     * @param blockNumber 区块编号
     */
    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    /**
     * 计算交易费用
     * 
     * @return 交易费用（gasUsed * gasPrice），以Wei为单位
     */
    public BigInteger getTransactionFee() {
        if (gasUsed != null && gasPrice != null) {
            return gasUsed.multiply(gasPrice);
        }
        return BigInteger.ZERO;
    }

    @Override
    public String toString() {
        return "TransactionResult{" +
                "transactionHash='" + transactionHash + '\'' +
                ", status='" + status + '\'' +
                ", gasUsed=" + gasUsed +
                ", gasPrice=" + gasPrice +
                ", blockHash='" + blockHash + '\'' +
                ", blockNumber=" + blockNumber +
                '}';
    }
}