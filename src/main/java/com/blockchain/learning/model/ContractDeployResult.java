package com.blockchain.learning.model;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 智能合约部署结果模型类
 * 
 * 用于封装智能合约部署操作的结果信息，包括合约地址、交易哈希、Gas消耗等。
 * 主要用于向用户反馈合约部署的状态和相关信息。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class ContractDeployResult {
    
    /** 部署成功后的智能合约地址 */
    private String contractAddress;
    
    /** 部署交易的哈希值 */
    private String transactionHash;
    
    /** 部署交易实际消耗的Gas数量 */
    private BigInteger gasUsed;
    
    /** 部署状态（如：SUCCESS, FAILED, PENDING） */
    private String status;
    
    /** 合约部署完成的时间戳 */
    private LocalDateTime deployedAt;

    /**
     * 默认构造函数
     */
    public ContractDeployResult() {
    }

    /**
     * 构造函数
     * 
     * @param contractAddress 合约地址
     * @param transactionHash 交易哈希
     * @param gasUsed Gas消耗量
     * @param status 部署状态
     * @param deployedAt 部署时间
     */
    public ContractDeployResult(String contractAddress, String transactionHash, 
                               BigInteger gasUsed, String status, LocalDateTime deployedAt) {
        this.contractAddress = contractAddress;
        this.transactionHash = transactionHash;
        this.gasUsed = gasUsed;
        this.status = status;
        this.deployedAt = deployedAt;
    }

    /**
     * 获取智能合约地址
     * 
     * @return 部署成功后的合约地址（以太坊地址格式）
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * 设置智能合约地址
     * 
     * @param contractAddress 合约地址
     */
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    /**
     * 获取部署交易哈希
     * 
     * @return 部署交易的唯一标识符
     */
    public String getTransactionHash() {
        return transactionHash;
    }

    /**
     * 设置部署交易哈希
     * 
     * @param transactionHash 交易哈希值
     */
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    /**
     * 获取Gas消耗量
     * 
     * @return 部署交易实际消耗的Gas数量
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
     * 获取部署状态
     * 
     * @return 部署状态描述（SUCCESS/FAILED/PENDING等）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置部署状态
     * 
     * @param status 部署状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取部署完成时间
     * 
     * @return 合约部署完成的时间戳
     */
    public LocalDateTime getDeployedAt() {
        return deployedAt;
    }

    /**
     * 设置部署完成时间
     * 
     * @param deployedAt 部署时间戳
     */
    public void setDeployedAt(LocalDateTime deployedAt) {
        this.deployedAt = deployedAt;
    }

    @Override
    public String toString() {
        return "ContractDeployResult{" +
                "contractAddress='" + contractAddress + '\'' +
                ", transactionHash='" + transactionHash + '\'' +
                ", gasUsed=" + gasUsed +
                ", status='" + status + '\'' +
                ", deployedAt=" + deployedAt +
                '}';
    }
}