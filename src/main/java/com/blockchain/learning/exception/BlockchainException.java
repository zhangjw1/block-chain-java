package com.blockchain.learning.exception;

/**
 * 区块链操作异常类
 * 
 * 用于处理区块链相关操作中发生的异常情况，如网络连接失败、交易发送失败、
 * 智能合约调用错误等。继承自RuntimeException，属于非检查异常。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class BlockchainException extends RuntimeException {
    
    /**
     * 构造函数
     * 
     * @param message 异常描述信息
     */
    public BlockchainException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 异常描述信息
     * @param cause 引起此异常的原因
     */
    public BlockchainException(String message, Throwable cause) {
        super(message, cause);
    }
}