# Java区块链学习项目

基于Spring Boot + Maven + Web3j的Java区块链学习平台，连接Sepolia测试网络进行智能合约部署和交易操作。

## 环境要求

- **JDK**: Java 8 或更高版本
- **Maven**: 3.6.x 或更高版本
- **Infura账号**: 需要注册获取项目ID

## 快速开始

### 1. 环境准备

确保已安装Java 8和Maven：

```bash
java -version
mvn -version
```

**如果Maven未安装，请按以下步骤安装：**

#### Windows安装Maven：
1. 下载Maven：访问 [Apache Maven官网](https://maven.apache.org/download.cgi)
2. 下载 `apache-maven-3.9.6-bin.zip`
3. 解压到 `C:\Program Files\Apache\maven`
4. 添加环境变量：
   - `MAVEN_HOME`: `C:\Program Files\Apache\maven`
   - 在`PATH`中添加: `%MAVEN_HOME%\bin`
5. 重启命令行，验证：`mvn -version`

#### 或者使用包管理器：
```bash
# 使用Chocolatey (Windows)
choco install maven

# 使用Scoop (Windows)
scoop install maven
```

### 2. 获取Infura项目ID

1. 访问 [Infura官网](https://infura.io/) 注册账号
2. 创建新项目，选择Ethereum网络
3. 复制项目ID

### 3. 配置环境变量

设置以下环境变量：

```bash
# Windows
set INFURA_PROJECT_ID=your-actual-project-id

# Linux/Mac
export INFURA_PROJECT_ID=your-actual-project-id
```

### 4. 编译和运行

**方法1：使用Maven Wrapper（推荐，无需安装Maven）**
```bash
# Windows
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw clean compile
./mvnw spring-boot:run
```

**方法2：使用已安装的Maven**
```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

**方法3：简单编译测试（仅验证语法）**
```bash
# Windows
compile.bat
```

### 5. 访问应用

- 应用地址: http://localhost:8080
- 健康检查: http://localhost:8080/api/health
- 欢迎页面: http://localhost:8080/api/

### 6. API端点

#### 基础信息
- `GET /api/health` - 应用健康检查
- `GET /api/` - 欢迎页面和API列表

#### 区块链查询
- `GET /api/blockchain/network-info` - 获取网络信息
- `GET /api/blockchain/blocks/latest` - 获取最新区块
- `GET /api/blockchain/blocks/{blockNumber}` - 根据区块号查询
- `GET /api/blockchain/blocks/hash/{blockHash}` - 根据区块哈希查询
- `GET /api/blockchain/transactions/{txHash}` - 查询交易信息
- `GET /api/blockchain/balance/{address}` - 查询地址余额
- `GET /api/blockchain/address/{address}/transaction-count` - 查询地址交易数量
- `GET /api/blockchain/gas-price` - 获取当前Gas价格

#### 钱包管理
- `POST /api/wallet/create` - 创建新的EOA钱包
- `POST /api/wallet/import` - 导入EOA钱包（需要私钥）
- `GET /api/wallet/balance/{address}` - 查询钱包余额
- `GET /api/wallet/info` - 获取当前钱包信息
- `POST /api/wallet/load/{address}` - 加载已保存的钱包
- `GET /api/wallet/status` - 获取钱包状态概览
- `POST /api/wallet/validate-address` - 验证地址格式
- `POST /api/wallet/validate-private-key` - 验证私钥格式

### 7. 智能合约

项目包含一个完整的SimpleStorage智能合约示例：

#### Solidity合约 (`src/main/resources/contracts/SimpleStorage.sol`)
- 基础存储功能（set/get）
- 所有权管理
- 事件日志
- 安全修饰符

#### Java包装类 (`src/main/java/com/blockchain/learning/contracts/SimpleStorage.java`)
- Web3j生成的合约包装类
- 完整的方法映射
- 事件监听支持
- 部署和加载功能

### 8. API使用示例

#### 创建新的EOA钱包
```bash
curl -X POST http://localhost:8080/api/wallet/create
```

#### 导入钱包
```bash
curl -X POST http://localhost:8080/api/wallet/import \
  -H "Content-Type: application/json" \
  -d '{"privateKey": "0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318"}'
```

#### 查询钱包余额
```bash
curl http://localhost:8080/api/wallet/balance/0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b
```

#### 获取网络信息
```bash
curl http://localhost:8080/api/blockchain/network-info
```

#### 验证地址格式
```bash
curl -X POST http://localhost:8080/api/wallet/validate-address \
  -H "Content-Type: application/json" \
  -d '{"address": "0x742d35Cc6634C0532925a3b8D4C9db96c4b4d8b"}'
```

**注意**: API文档功能暂时禁用，因为Springfox与Spring Boot 2.7.x存在兼容性问题。后续会使用SpringDoc OpenAPI替代。

## 项目结构

```
src/
├── main/
│   ├── java/com/blockchain/learning/
│   │   ├── config/          # 配置类
│   │   ├── controller/      # REST控制器
│   │   ├── service/         # 业务服务
│   │   ├── model/           # 数据模型
│   │   ├── exception/       # 异常类
│   │   └── BlockchainLearningApplication.java
│   └── resources/
│       ├── contracts/       # Solidity合约
│       └── application.yml  # 配置文件
└── test/                    # 测试代码
```

## 主要功能

- ✅ 项目基础结构搭建
- ✅ Web3j网络连接配置
- ✅ 区块链数据查询
- ✅ 钱包管理功能
- ✅ 智能合约创建和包装类
- 🔄 智能合约部署和交互
- 🔒 安全配置和私钥管理

## 学习路径

1. 理解项目结构和配置
2. 学习Web3j基础用法
3. 实现钱包创建和管理
4. 部署和交互智能合约
5. 构建完整的区块链应用

## 注意事项

- 本项目仅用于学习目的，请勿在生产环境使用
- 使用Sepolia测试网络，需要测试ETH进行交易
- 私钥信息请妥善保管，不要泄露

## 获取测试ETH

访问以下水龙头获取Sepolia测试网ETH：
- [Sepolia Faucet](https://sepoliafaucet.com/)
- [Alchemy Sepolia Faucet](https://sepoliafaucet.com/)

## 技术栈

- Spring Boot 2.7.x
- Web3j 4.9.x
- Maven 3.6.x
- Java 8
- Infura (区块链节点服务)# block-chain-java
