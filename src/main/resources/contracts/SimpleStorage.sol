// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * SimpleStorage 智能合约
 * 
 * 这是一个用于学习的简单智能合约，演示了基本的存储和读取功能。
 * 合约功能：
 * - 存储一个uint256类型的数值
 * - 提供设置和获取数值的方法
 * - 发出事件通知数值变更
 * 
 * @title SimpleStorage
 * @author Blockchain Learning Project
 * @notice 仅用于学习目的，不要在生产环境使用
 */
contract SimpleStorage {
    
    // 存储的数值（状态变量）
    uint256 private storedData;
    
    // 合约拥有者地址
    address public owner;
    
    // 数据存储事件
    event DataStored(uint256 indexed newValue, uint256 indexed oldValue, address indexed setter);
    
    // 合约拥有者变更事件
    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);
    
    /**
     * 构造函数
     * 部署合约时执行，设置初始值和合约拥有者
     */
    constructor() {
        storedData = 0;
        owner = msg.sender;
        emit DataStored(0, 0, msg.sender);
    }
    
    /**
     * 修饰符：仅合约拥有者可调用
     */
    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can call this function");
        _;
    }
    
    /**
     * 设置存储的数值
     * 任何人都可以调用此方法
     * 
     * @param x 要存储的新数值
     */
    function set(uint256 x) public {
        uint256 oldValue = storedData;
        storedData = x;
        emit DataStored(x, oldValue, msg.sender);
    }
    
    /**
     * 获取存储的数值
     * 这是一个视图函数，不会修改状态，不消耗Gas（当直接调用时）
     * 
     * @return 当前存储的数值
     */
    function get() public view returns (uint256) {
        return storedData;
    }
    
    /**
     * 增加存储的数值
     * 将当前值增加指定的数量
     * 
     * @param increment 要增加的数量
     * @return 增加后的新值
     */
    function increment(uint256 increment) public returns (uint256) {
        uint256 oldValue = storedData;
        storedData += increment;
        emit DataStored(storedData, oldValue, msg.sender);
        return storedData;
    }
    
    /**
     * 重置存储的数值为0
     * 只有合约拥有者可以调用
     */
    function reset() public onlyOwner {
        uint256 oldValue = storedData;
        storedData = 0;
        emit DataStored(0, oldValue, msg.sender);
    }
    
    /**
     * 转移合约所有权
     * 只有当前拥有者可以调用
     * 
     * @param newOwner 新拥有者的地址
     */
    function transferOwnership(address newOwner) public onlyOwner {
        require(newOwner != address(0), "New owner cannot be zero address");
        address oldOwner = owner;
        owner = newOwner;
        emit OwnershipTransferred(oldOwner, newOwner);
    }
    
    /**
     * 获取合约的基本信息
     * 
     * @return currentValue 当前存储的值
     * @return contractOwner 合约拥有者地址
     * @return contractAddress 合约地址
     */
    function getInfo() public view returns (uint256 currentValue, address contractOwner, address contractAddress) {
        return (storedData, owner, address(this));
    }
}