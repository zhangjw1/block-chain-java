package com.blockchain.learning.util;

import org.web3j.crypto.Keys;

public class WalletUtils {

    /**
     * 验证以太坊地址格式是否正确
     */
    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        
        try {
            // 移除0x前缀进行验证
            String cleanAddress = address.startsWith("0x") ? address.substring(2) : address;
            
            // 检查长度（40个十六进制字符）
            if (cleanAddress.length() != 40) {
                return false;
            }
            
            // 检查是否为有效的十六进制字符
            Long.parseLong(cleanAddress, 16);
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证私钥格式是否正确
     */
    public static boolean isValidPrivateKey(String privateKey) {
        if (privateKey == null || privateKey.trim().isEmpty()) {
            return false;
        }
        
        try {
            // 移除0x前缀
            String cleanPrivateKey = privateKey.startsWith("0x") ? privateKey.substring(2) : privateKey;
            
            // 检查长度（64个十六进制字符）
            if (cleanPrivateKey.length() != 64) {
                return false;
            }
            
            // 检查是否为有效的十六进制字符
            Long.parseLong(cleanPrivateKey.substring(0, 32), 16);
            Long.parseLong(cleanPrivateKey.substring(32), 16);
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 格式化地址（确保有0x前缀）
     */
    public static String formatAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return address;
        }
        
        String cleanAddress = address.trim();
        if (!cleanAddress.startsWith("0x")) {
            return "0x" + cleanAddress;
        }
        
        return cleanAddress;
    }

    /**
     * 格式化私钥（确保有0x前缀）
     */
    public static String formatPrivateKey(String privateKey) {
        if (privateKey == null || privateKey.trim().isEmpty()) {
            return privateKey;
        }
        
        String cleanPrivateKey = privateKey.trim();
        if (!cleanPrivateKey.startsWith("0x")) {
            return "0x" + cleanPrivateKey;
        }
        
        return cleanPrivateKey;
    }

    /**
     * 检查地址的校验和是否正确
     */
    public static boolean isValidChecksumAddress(String address) {
        if (!isValidAddress(address)) {
            return false;
        }
        
        try {
            // 使用Web3j的Keys类验证校验和
            return Keys.toChecksumAddress(address).equals(address);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 转换为校验和地址
     */
    public static String toChecksumAddress(String address) {
        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address format");
        }
        
        return Keys.toChecksumAddress(address);
    }
}