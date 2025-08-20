package com.blockchain.learning.model;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 钱包信息模型类
 * 
 * 用于封装以太坊钱包的基本信息，包括地址、余额等数据。
 * 注意：此类不包含私钥等敏感信息，仅用于展示和传输钱包的公开信息。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class WalletInfo {
    
    /** 钱包地址（以太坊地址格式，如：0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b） */
    private String address;
    
    /** 钱包余额（以Wei为单位的BigInteger值） */
    private BigInteger balance;
    
    /** 钱包余额（以ETH为单位的字符串表示，便于显示） */
    private String balanceInEth;
    
    /** 钱包创建或导入的时间戳 */
    private LocalDateTime createdAt;
    
    /** 标识钱包是否为导入的（true：导入，false：新创建） */
    private boolean isImported;

    /**
     * 默认构造函数
     */
    public WalletInfo() {
    }

    /**
     * 构造函数
     * 
     * @param address 钱包地址
     * @param balance 钱包余额（Wei）
     * @param balanceInEth 钱包余额（ETH）
     * @param createdAt 创建时间
     */
    public WalletInfo(String address, BigInteger balance, String balanceInEth, LocalDateTime createdAt) {
        this.address = address;
        this.balance = balance;
        this.balanceInEth = balanceInEth;
        this.createdAt = createdAt;
        this.isImported = false;
    }

    /**
     * 获取钱包地址
     * 
     * @return 以太坊钱包地址（格式：0x开头的42位十六进制字符串）
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置钱包地址
     * 
     * @param address 以太坊钱包地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取钱包余额（Wei单位）
     * 
     * @return 余额的BigInteger表示（1 ETH = 10^18 Wei）
     */
    public BigInteger getBalance() {
        return balance;
    }

    /**
     * 设置钱包余额（Wei单位）
     * 
     * @param balance 余额的BigInteger表示
     */
    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    /**
     * 获取钱包余额（ETH单位）
     * 
     * @return 余额的ETH字符串表示，便于用户界面显示
     */
    public String getBalanceInEth() {
        return balanceInEth;
    }

    /**
     * 设置钱包余额（ETH单位）
     * 
     * @param balanceInEth 余额的ETH字符串表示
     */
    public void setBalanceInEth(String balanceInEth) {
        this.balanceInEth = balanceInEth;
    }

    /**
     * 获取钱包创建或导入时间
     * 
     * @return 时间戳
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置钱包创建或导入时间
     * 
     * @param createdAt 时间戳
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 检查钱包是否为导入的
     * 
     * @return true表示钱包是通过私钥导入的，false表示是新创建的
     */
    public boolean isImported() {
        return isImported;
    }

    /**
     * 设置钱包导入标识
     * 
     * @param imported true表示导入的钱包，false表示新创建的钱包
     */
    public void setImported(boolean imported) {
        isImported = imported;
    }

    @Override
    public String toString() {
        return "WalletInfo{" +
                "address='" + address + '\'' +
                ", balance=" + balance +
                ", balanceInEth='" + balanceInEth + '\'' +
                ", createdAt=" + createdAt +
                ", isImported=" + isImported +
                '}';
    }
}