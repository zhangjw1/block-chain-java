# 故障排除指南

## 常见问题和解决方案

### 1. Maven未安装错误

**错误信息**: `mvn : 无法将"mvn"项识别为 cmdlet、函数、脚本文件或可运行程序的名称`

**解决方案**:
- 使用Maven Wrapper: `.\mvnw.cmd clean compile`
- 或安装Maven: 参考README.md中的安装说明

### 2. JAVA_HOME未设置错误

**错误信息**: `Error: JAVA_HOME not found in your environment`

**解决方案**:
```bash
# Windows (临时设置)
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_XXX

# Windows (永久设置)
# 在系统环境变量中添加 JAVA_HOME
```

### 3. Springfox Swagger启动错误

**错误信息**: `Failed to start bean 'documentationPluginsBootstrapper'`

**解决方案**: 
- 已在当前版本中移除Springfox依赖
- 后续版本将使用SpringDoc OpenAPI替代

### 4. Infura连接错误

**错误信息**: `Failed to connect to blockchain network`

**解决方案**:
1. 确保设置了正确的环境变量:
   ```bash
   set INFURA_PROJECT_ID=your-actual-project-id
   ```
2. 检查Infura项目ID是否有效
3. 确保网络连接正常

### 5. 端口占用错误

**错误信息**: `Port 8080 was already in use`

**解决方案**:
1. 修改application.yml中的端口:
   ```yaml
   server:
     port: 8081
   ```
2. 或停止占用8080端口的其他应用

### 6. 编译错误

**错误信息**: 各种编译错误

**解决方案**:
1. 确保Java版本为1.8或更高:
   ```bash
   java -version
   ```
2. 清理并重新编译:
   ```bash
   .\mvnw.cmd clean compile
   ```

### 7. Web3j版本兼容性问题

**错误信息**: `TransactionReceipt.getGasLimit()` 或 `getEffectiveGasPrice()` 方法不存在

**解决方案**:
- 这是Web3j版本差异导致的问题
- Web3j 4.9.x版本中TransactionReceipt类的API与新版本不同：
  - 没有`gasLimit`字段
  - 可能没有`getEffectiveGasPrice()`方法
- 项目已适配此版本，使用以下替代方案：
  - 在`calculateTransactionFee()`中使用默认Gas价格（20 Gwei）
  - 提供重载方法支持自定义Gas价格计算
  - 通过Gas使用量推断是否Gas用尽
  - 在实际应用中，可通过Web3Service获取当前Gas价格
- 如果遇到类似问题，检查Web3j版本兼容性

## 验证应用是否正常运行

1. **检查应用启动日志**:
   - 应该看到 "Java Blockchain Learning Application Started"
   - 如果连接成功，会显示网络信息

2. **访问健康检查端点**:
   ```bash
   curl http://localhost:8080/api/health
   ```
   应该返回:
   ```json
   {
     "status": "UP",
     "message": "Java Blockchain Learning Application is running",
     "timestamp": 1234567890
   }
   ```

3. **检查网络连接**:
   - 启动日志中应显示区块链网络连接状态
   - 如果显示连接失败，检查Infura配置

## 获取帮助

如果问题仍然存在:
1. 检查完整的错误日志
2. 确认所有环境变量设置正确
3. 验证网络连接
4. 查看项目的GitHub Issues页面