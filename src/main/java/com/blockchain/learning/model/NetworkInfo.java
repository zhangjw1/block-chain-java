package com.blockchain.learning.model;

import java.math.BigInteger;

public class NetworkInfo {
    private String networkName;
    private BigInteger chainId;
    private BigInteger latestBlockNumber;
    private String nodeVersion;
    private boolean isConnected;

    public NetworkInfo() {
    }

    public NetworkInfo(String networkName, BigInteger chainId, BigInteger latestBlockNumber, 
                      String nodeVersion, boolean isConnected) {
        this.networkName = networkName;
        this.chainId = chainId;
        this.latestBlockNumber = latestBlockNumber;
        this.nodeVersion = nodeVersion;
        this.isConnected = isConnected;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public BigInteger getChainId() {
        return chainId;
    }

    public void setChainId(BigInteger chainId) {
        this.chainId = chainId;
    }

    public BigInteger getLatestBlockNumber() {
        return latestBlockNumber;
    }

    public void setLatestBlockNumber(BigInteger latestBlockNumber) {
        this.latestBlockNumber = latestBlockNumber;
    }

    public String getNodeVersion() {
        return nodeVersion;
    }

    public void setNodeVersion(String nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    public boolean isConnected() {
        return isConnected;
    }

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