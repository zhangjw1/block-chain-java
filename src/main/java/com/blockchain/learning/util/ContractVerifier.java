package com.blockchain.learning.util;

import com.blockchain.learning.contracts.SimpleStorage;

/**
 * 合约验证工具类
 * 
 * 用于验证智能合约相关的配置和设置
 */
public class ContractVerifier {

    /**
     * 验证SimpleStorage合约配置
     * 
     * @return 验证结果信息
     */
    public static String verifySimpleStorageContract() {
        StringBuilder result = new StringBuilder();
        result.append("=== SimpleStorage合约验证 ===\n");
        
        try {
            // 检查字节码
            String binary = SimpleStorage.BINARY;
            result.append("✅ 字节码存在: ").append(binary != null && !binary.isEmpty()).append("\n");
            result.append("   字节码长度: ").append(binary != null ? binary.length() : 0).append(" 字符\n");
            
            // 检查Gas设置
            result.append("✅ Gas限制: ").append(SimpleStorage.GAS_LIMIT).append("\n");
            result.append("✅ Gas价格: ").append(SimpleStorage.GAS_PRICE).append(" Wei (")
                  .append(SimpleStorage.GAS_PRICE.divide(java.math.BigInteger.valueOf(1_000_000_000L)))
                  .append(" Gwei)\n");
            
            // 检查事件定义
            result.append("✅ DataStored事件: ").append(SimpleStorage.DATASTORED_EVENT.getName()).append("\n");
            result.append("✅ OwnershipTransferred事件: ").append(SimpleStorage.OWNERSHIPTRANSFERRED_EVENT.getName()).append("\n");
            
            result.append("✅ 合约验证通过！\n");
            
        } catch (Exception e) {
            result.append("❌ 合约验证失败: ").append(e.getMessage()).append("\n");
        }
        
        return result.toString();
    }

    /**
     * 验证合约字节码格式
     * 
     * @param binary 合约字节码
     * @return true如果格式正确
     */
    public static boolean isValidContractBinary(String binary) {
        if (binary == null || binary.trim().isEmpty()) {
            return false;
        }
        
        // 检查是否以0x开头
        if (!binary.startsWith("0x")) {
            return false;
        }
        
        // 检查长度（至少应该有一些字节码）
        if (binary.length() < 10) {
            return false;
        }
        
        // 检查是否为有效的十六进制
        try {
            String hexPart = binary.substring(2);
            new java.math.BigInteger(hexPart, 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 估算合约部署成本
     * 
     * @return 部署成本信息
     */
    public static String estimateDeploymentCost() {
        StringBuilder result = new StringBuilder();
        result.append("=== 合约部署成本估算 ===\n");
        
        try {
            String binary = SimpleStorage.BINARY;
            int binarySize = binary != null ? (binary.length() - 2) / 2 : 0; // 减去0x前缀，除以2得到字节数
            
            java.math.BigInteger estimatedGas = ContractUtils.estimateDeploymentGas(binarySize);
            java.math.BigInteger deploymentCost = ContractUtils.calculateTransactionFee(estimatedGas, SimpleStorage.GAS_PRICE);
            
            result.append("📦 合约大小: ").append(binarySize).append(" 字节\n");
            result.append("⛽ 估算Gas: ").append(ContractUtils.formatGas(estimatedGas)).append("\n");
            result.append("💰 估算费用: ").append(deploymentCost).append(" Wei\n");
            result.append("💰 估算费用: ").append(
                org.web3j.utils.Convert.fromWei(deploymentCost.toString(), org.web3j.utils.Convert.Unit.ETHER)
            ).append(" ETH\n");
            
        } catch (Exception e) {
            result.append("❌ 成本估算失败: ").append(e.getMessage()).append("\n");
        }
        
        return result.toString();
    }
}