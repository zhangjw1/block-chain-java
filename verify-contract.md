# 智能合约验证指南

## 合约文件验证

### 1. Solidity合约文件
- 位置: `src/main/resources/contracts/SimpleStorage.sol`
- 版本: Solidity ^0.8.0
- 功能: 基础存储合约，包含设置、获取、增加、重置等功能

### 2. Java包装类
- 位置: `src/main/java/com/blockchain/learning/contracts/SimpleStorage.java`
- 功能: Web3j生成的合约包装类，提供Java方法调用合约

### 3. 合约功能验证

#### 基础功能
- ✅ `set(uint256)` - 设置存储值
- ✅ `get()` - 获取存储值
- ✅ `increment(uint256)` - 增加存储值
- ✅ `reset()` - 重置为0（仅拥有者）
- ✅ `owner()` - 获取拥有者地址
- ✅ `transferOwnership(address)` - 转移所有权
- ✅ `getInfo()` - 获取合约信息

#### 事件
- ✅ `DataStored` - 数据变更事件
- ✅ `OwnershipTransferred` - 所有权转移事件

#### 修饰符
- ✅ `onlyOwner` - 仅拥有者可调用

### 4. 部署参数
- Gas限制: 4,700,000
- Gas价格: 20 Gwei (20,000,000,000 Wei)
- 网络: Sepolia测试网

### 5. 安全特性
- 拥有者权限控制
- 零地址检查
- 事件日志记录
- 输入验证

## 使用Web3j工具生成合约包装类

如果需要重新生成Java包装类，可以使用以下命令：

```bash
# 1. 编译Solidity合约（需要安装solc编译器）
solc --bin --abi --optimize --overwrite -o build src/main/resources/contracts/SimpleStorage.sol

# 2. 使用Web3j生成Java包装类
web3j generate solidity -b build/SimpleStorage.bin -a build/SimpleStorage.abi -o src/main/java -p com.blockchain.learning.contracts
```

或者使用Maven插件：

```bash
mvn web3j:generate-sources
```

## 验证步骤

1. **语法检查**: 确保Solidity代码语法正确
2. **编译测试**: 验证合约可以成功编译
3. **Java类检查**: 确保Java包装类包含所有必要方法
4. **Gas估算**: 验证Gas设置合理
5. **安全审查**: 检查合约安全特性

## 注意事项

- 合约仅用于学习目的，不要在生产环境使用
- 部署前确保有足够的测试ETH
- 合约地址一旦部署不可更改
- 私钥安全至关重要