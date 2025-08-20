package com.blockchain.learning.model;

import java.time.LocalDateTime;

/**
 * 错误响应模型类
 * 
 * 用于统一封装API错误响应的格式，提供一致的错误信息结构。
 * 当API调用发生错误时，返回此格式的错误信息给客户端。
 * 
 * @author Blockchain Learning Project
 * @version 1.0
 */
public class ErrorResponse {
    
    /** 错误类型或错误代码 */
    private String error;
    
    /** 详细的错误描述信息 */
    private String message;
    
    /** 错误发生的时间戳 */
    private LocalDateTime timestamp;
    
    /** 发生错误的API路径 */
    private String path;
    
    /** HTTP状态码 */
    private int status;

    /**
     * 默认构造函数
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 构造函数
     * 
     * @param error 错误类型
     * @param message 错误消息
     * @param path API路径
     * @param status HTTP状态码
     */
    public ErrorResponse(String error, String message, String path, int status) {
        this.error = error;
        this.message = message;
        this.path = path;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 获取错误类型
     * 
     * @return 错误类型或错误代码
     */
    public String getError() {
        return error;
    }

    /**
     * 设置错误类型
     * 
     * @param error 错误类型
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * 获取错误消息
     * 
     * @return 详细的错误描述信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置错误消息
     * 
     * @param message 错误描述信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取错误发生时间
     * 
     * @return 错误发生的时间戳
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * 设置错误发生时间
     * 
     * @param timestamp 时间戳
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取API路径
     * 
     * @return 发生错误的API端点路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置API路径
     * 
     * @param path API端点路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取HTTP状态码
     * 
     * @return HTTP响应状态码
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置HTTP状态码
     * 
     * @param status HTTP状态码
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", path='" + path + '\'' +
                ", status=" + status +
                '}';
    }
}