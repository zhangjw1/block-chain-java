package com.blockchain.learning.model;

import java.math.BigInteger;

/**
 * 区块链网络信息模型类
 * 
 * 用于封装区块链网络的基本信息，包括网络名称、链ID、最新区块号等。
 * 主要用于显示当前连接的区块链网络状态和基本信息。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class NetworkInfo {
    
    /** 网络名称（如：Sepolia Testnet, Ethereum Mainnet） */
    private String networkName;
    
    /** 链ID（用于标识不同的区块链网络，如：1=主网，11155111=Sepolia测试网） */
    private BigInteger chainId;
    
    /** 最新区块号（当前区块链的最新区块编号） */
    private BigInteger latestBlockNumber;
    
    /** 节点版本信息（连接的区块链节点软件版本） */
    private String nodeVersion;
    
    /** 连接状态（true：已连接，false：连接失败或断开） */
    private boolean isConnected;

    /**
     * 默认构造函数
     */
    public NetworkInfo() {
    }

    /**
     * 构造函数
     * 
     * @param networkName 网络名称
     * @param chainId 链ID
     * @param latestBlockNumber 最新区块号
     * @param nodeVersion 节点版本
     * @param isConnected 连接状态
     */
    public NetworkInfo(String networkName, BigInteger chainId, BigInteger latestBlockNumber, 
                      String nodeVersion, boolean isConnected) {
        this.networkName = networkName;
        this.chainId = chainId;
        this.latestBlockNumber = latestBlockNumber;
        this.nodeVersion = nodeVersion;
        this.isConnected = isConnected;
    }

    /**
     * 获取网络名称
     * 
     * @return 区块链网络的显示名称
     */
    public String getNetworkName() {
        return networkName;
    }

    /**
     * 设置网络名称
     * 
     * @param networkName 区块链网络的显示名称
     */
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    /**
     * 获取链ID
     * 
     * @return 区块链网络的唯一标识符（1=以太坊主网，11155111=Sepolia测试网等）
     */
    public BigInteger getChainId() {
        return chainId;
    }

    /**
     * 设置链ID
     * 
     * @param chainId 区块链网络的唯一标识符
     */
    public void setChainId(BigInteger chainId) {
        this.chainId = chainId;
    }

    /**
     * 获取最新区块号
     * 
     * @return 当前区块链网络的最新区块编号
     */
    public BigInteger getLatestBlockNumber() {
        return latestBlockNumber;
    }

    /**
     * 设置最新区块号
     * 
     * @param latestBlockNumber 最新区块编号
     */
    public void setLatestBlockNumber(BigInteger latestBlockNumber) {
        this.latestBlockNumber = latestBlockNumber;
    }

    /**
     * 获取节点版本信息
     * 
     * @return 连接的区块链节点软件版本信息
     */
    public String getNodeVersion() {
        return nodeVersion;
    }

    /**
     * 设置节点版本信息
     * 
     * @param nodeVersion 节点软件版本信息
     */
    public void setNodeVersion(String nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    /**
     * 检查网络连接状态
     * 
     * @return true表示已成功连接到区块链网络，false表示连接失败或断开
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * 设置网络连接状态
     * 
     * @param connected 连接状态标识
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public String toString() {
        return "NetworkInfo{" +
                "networkName='" + networkName + '\'' +
                ", chainId=" + chainId +
                ", latestBlockNumber=" + latestBlockNumber +
                ", nodeVersion='" + nodeVersion + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }
}