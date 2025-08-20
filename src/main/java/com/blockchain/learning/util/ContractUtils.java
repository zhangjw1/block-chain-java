package com.blockchain.learning.util;

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

/**
 * 智能合约工具类
 * 
 * 提供智能合约相关的实用方法，包括Gas计算、交易状态检查等。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class ContractUtils {

    // 默认Gas限制
    public static final BigInteger DEFAULT_GAS_LIMIT = BigInteger.valueOf(4_700_000L);

    // 默认Gas价格（20 Gwei）
    public static final BigInteger DEFAULT_GAS_PRICE = BigInteger.valueOf(20_000_000_000L);

    /**
     * 验证合约地址格式
     * 
     * @param contractAddress 合约地址
     * @return true如果地址格式有效
     */
    public static boolean isValidContractAddress(String contractAddress) {
        return WalletUtils.isValidAddress(contractAddress);
    }

    /**
     * 检查交易是否成功
     * 
     * @param receipt 交易回执
     * @return true如果交易成功
     */
    public static boolean isTransactionSuccessful(TransactionReceipt receipt) {
        if (receipt == null) {
            return false;
        }

        // 检查交易状态
        String status = receipt.getStatus();
        if (status != null) {
            // 状态为"0x1"表示成功，"0x0"表示失败
            return "0x1".equals(status);
        }

        // 如果没有状态字段，检查是否有Gas使用（旧版本以太坊）
        return receipt.getGasUsed() != null && receipt.getGasUsed().compareTo(BigInteger.ZERO) > 0;
    }

    /**
     * 获取交易失败原因
     * 
     * @param receipt 交易回执
     * @return 失败原因描述
     */
    public static String getTransactionFailureReason(TransactionReceipt receipt) {
        if (receipt == null) {
            return "Transaction receipt is null";
        }

        if (isTransactionSuccessful(receipt)) {
            return "Transaction was successful";
        }

        // 检查状态码
        String status = receipt.getStatus();
        if ("0x0".equals(status)) {
            return "Transaction failed: Execution reverted";
        }

        // 检查Gas使用情况
        // 注意：Web3j 4.9.x版本的TransactionReceipt没有gasLimit字段
        // 我们通过检查Gas使用量是否接近常见限制来推断是否Gas用尽
        if (receipt.getGasUsed() != null) {
            BigInteger gasUsed = receipt.getGasUsed();
            // 如果Gas使用量接近或等于常见的Gas限制，可能是Gas用尽
            if (gasUsed.compareTo(BigInteger.valueOf(4_700_000)) >= 0) {
                return "Transaction failed: Possibly out of gas";
            }
        }

        return "Transaction failed: Unknown reason";
    }

    /**
     * 计算交易费用
     * 
     * 注意：由于Web3j 4.9.x版本的TransactionReceipt类API限制，
     * 我们使用默认Gas价格来估算交易费用。
     * 
     * @param receipt 交易回执
     * @return 交易费用（以Wei为单位）
     */
    public static BigInteger calculateTransactionFee(TransactionReceipt receipt) {
        if (receipt == null || receipt.getGasUsed() == null) {
            return BigInteger.ZERO;
        }

        BigInteger gasUsed = receipt.getGasUsed();

        // 由于Web3j 4.9.x版本的TransactionReceipt没有gasPrice相关方法
        // 我们使用默认Gas价格来估算费用
        // 在实际应用中，可以通过其他方式获取实际Gas价格
        BigInteger gasPrice = receipt.getEffectiveGasPrice() == null ? DEFAULT_GAS_PRICE : new BigInteger(receipt.getEffectiveGasPrice());

        return gasUsed.multiply(gasPrice);
    }

    /**
     * 计算交易费用（使用自定义Gas价格）
     * 
     * @param gasUsed  Gas使用量
     * @param gasPrice Gas价格（以Wei为单位）
     * @return 交易费用（以Wei为单位）
     */
    public static BigInteger calculateTransactionFee(BigInteger gasUsed, BigInteger gasPrice) {
        if (gasUsed == null || gasPrice == null) {
            return BigInteger.ZERO;
        }
        return gasUsed.multiply(gasPrice);
    }

    /**
     * 创建默认的Gas提供者
     * 
     * @return ContractGasProvider实例
     */
    public static ContractGasProvider createDefaultGasProvider() {
        return new DefaultGasProvider();
    }

    /**
     * 创建自定义Gas提供者
     * 
     * @param gasPrice Gas价格
     * @param gasLimit Gas限制
     * @return ContractGasProvider实例
     */
    public static ContractGasProvider createCustomGasProvider(BigInteger gasPrice, BigInteger gasLimit) {
        return new ContractGasProvider() {
            @Override
            public BigInteger getGasPrice(String contractFunc) {
                return gasPrice;
            }

            @Override
            public BigInteger getGasPrice() {
                return gasPrice;
            }

            @Override
            public BigInteger getGasLimit(String contractFunc) {
                return gasLimit;
            }

            @Override
            public BigInteger getGasLimit() {
                return gasLimit;
            }
        };
    }

    /**
     * 估算合约部署所需的Gas
     * 
     * @param contractSize 合约字节码大小
     * @return 估算的Gas数量
     */
    public static BigInteger estimateDeploymentGas(int contractSize) {
        // 基础部署成本：21000 Gas
        BigInteger baseCost = BigInteger.valueOf(21000);

        // 每字节数据成本：68 Gas（非零字节）或 4 Gas（零字节）
        // 简化计算，假设平均每字节 50 Gas
        BigInteger dataCost = BigInteger.valueOf(contractSize * 50);

        // 合约创建额外成本：32000 Gas
        BigInteger creationCost = BigInteger.valueOf(32000);

        return baseCost.add(dataCost).add(creationCost);
    }

    /**
     * 格式化Gas数量为可读字符串
     * 
     * @param gas Gas数量
     * @return 格式化的字符串
     */
    public static String formatGas(BigInteger gas) {
        if (gas == null) {
            return "0";
        }

        // 如果Gas数量很大，使用K、M等单位
        if (gas.compareTo(BigInteger.valueOf(1_000_000)) >= 0) {
            return gas.divide(BigInteger.valueOf(1_000_000)) + "M";
        } else if (gas.compareTo(BigInteger.valueOf(1_000)) >= 0) {
            return gas.divide(BigInteger.valueOf(1_000)) + "K";
        } else {
            return gas.toString();
        }
    }

    /**
     * 检查合约地址是否为零地址
     * 
     * @param address 合约地址
     * @return true如果是零地址
     */
    public static boolean isZeroAddress(String address) {
        return address == null ||
                "0x0000000000000000000000000000000000000000".equalsIgnoreCase(address) ||
                "0x0".equalsIgnoreCase(address);
    }

    /**
     * 验证交易哈希格式
     * 
     * @param txHash 交易哈希
     * @return true如果格式有效
     */
    public static boolean isValidTransactionHash(String txHash) {
        if (txHash == null || txHash.trim().isEmpty()) {
            return false;
        }

        // 移除0x前缀
        String cleanHash = txHash.startsWith("0x") ? txHash.substring(2) : txHash;

        // 检查长度（64个十六进制字符）
        if (cleanHash.length() != 64) {
            return false;
        }

        // 检查是否为有效的十六进制字符
        try {
            new BigInteger(cleanHash, 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}