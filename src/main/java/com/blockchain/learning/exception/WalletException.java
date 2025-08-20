package com.blockchain.learning.exception;

/**
 * 钱包操作异常类
 * 
 * 用于处理钱包相关操作中发生的异常情况，如钱包创建失败、私钥格式错误、
 * 钱包导入失败等。继承自RuntimeException，属于非检查异常。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class WalletException extends RuntimeException {
    
    /**
     * 构造函数
     * 
     * @param message 异常描述信息
     */
    public WalletException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 异常描述信息
     * @param cause 引起此异常的原因
     */
    public WalletException(String message, Throwable cause) {
        super(message, cause);
    }
}