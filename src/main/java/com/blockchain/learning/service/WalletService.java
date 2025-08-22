package com.blockchain.learning.service;

import com.blockchain.learning.exception.WalletException;
import com.blockchain.learning.model.WalletInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;
import org.web3j.utils.Convert;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    private Web3Service web3Service;

    @Value("${blockchain.wallet.storage-path:./wallets}")
    private String walletStoragePath;

    @Value("${blockchain.wallet.encryption-key:a-16-byte-secret}")
    private String encryptionKey;

    private Credentials currentCredentials;

    /**
     * 创建新的EOA（Externally Owned Account）钱包
     * 
     * EOA钱包特点：
     * - 由私钥控制的外部拥有账户
     * - 可以主动发起交易和调用智能合约
     * - 没有关联的智能合约代码
     * - 通过椭圆曲线密码学生成密钥对
     */
    public WalletInfo createWallet() {
        try {
            logger.info("Creating new EOA wallet...");
            
            // 生成新的椭圆曲线密钥对（secp256k1曲线）
            // 这创建的是EOA账户，不是合约账户
            ECKeyPair keyPair = Keys.createEcKeyPair();
            Credentials credentials = Credentials.create(keyPair);
            
            // 获取地址
            String address = credentials.getAddress();
            logger.info("Generated new wallet address: {}", address);
            
            // 保存私钥（加密存储）
            savePrivateKey(address, credentials.getEcKeyPair().getPrivateKey());
            
            // 设置为当前钱包
            this.currentCredentials = credentials;
            
            // 获取余额
            BigInteger balance = getWalletBalance(address);
            String balanceInEth = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
            
            WalletInfo walletInfo = new WalletInfo(address, balance, balanceInEth, LocalDateTime.now());
            walletInfo.setImported(false);
            
            logger.info("Wallet created successfully: {}", address);
            return walletInfo;
            
        } catch (Exception e) {
            logger.error("Error creating wallet: {}", e.getMessage());
            throw new WalletException("Failed to create wallet", e);
        }
    }

    /**
     * 导入EOA钱包（通过私钥）
     * 
     * 通过提供的私钥导入现有的EOA账户
     */
    public WalletInfo importWallet(String privateKeyHex) {
        try {
            logger.info("Importing EOA wallet from private key...");
            
            // 验证私钥格式
            if (!WalletUtils.isValidPrivateKey(privateKeyHex)) {
                throw new WalletException("Invalid private key format");
            }
            
            // 移除0x前缀（如果存在）
            if (privateKeyHex.startsWith("0x")) {
                privateKeyHex = privateKeyHex.substring(2);
            }
            
            // 创建Credentials
            BigInteger privateKey = new BigInteger(privateKeyHex, 16);
            Credentials credentials = Credentials.create(privateKey.toString(16));
            
            String address = credentials.getAddress();
            logger.info("Importing wallet address: {}", address);
            
            // 保存私钥（加密存储）
            savePrivateKey(address, privateKey);
            
            // 设置为当前钱包
            this.currentCredentials = credentials;
            
            // 获取余额
            BigInteger balance = getWalletBalance(address);
            String balanceInEth = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
            
            WalletInfo walletInfo = new WalletInfo(address, balance, balanceInEth, LocalDateTime.now());
            walletInfo.setImported(true);
            
            logger.info("Wallet imported successfully: {}", address);
            return walletInfo;
            
        } catch (Exception e) {
            logger.error("Error importing wallet: {}", e.getMessage());
            throw new WalletException("Failed to import wallet: " + e.getMessage(), e);
        }
    }

    /**
     * 获取指定地址的余额
     */
    public BigInteger getBalance(String address) {
        try {
            return web3Service.getBalance(address);
        } catch (IOException e) {
            logger.error("Error fetching balance for address {}: {}", address, e.getMessage());
            throw new WalletException("Failed to fetch balance", e);
        }
    }

    /**
     * 获取当前钱包信息
     */
    public WalletInfo getCurrentWallet() {
        if (currentCredentials == null) {
            throw new WalletException("No wallet is currently loaded");
        }
        
        try {
            String address = currentCredentials.getAddress();
            BigInteger balance = getWalletBalance(address);
            String balanceInEth = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();
            
            WalletInfo walletInfo = new WalletInfo(address, balance, balanceInEth, LocalDateTime.now());
            return walletInfo;
            
        } catch (Exception e) {
            logger.error("Error getting current wallet info: {}", e.getMessage());
            throw new WalletException("Failed to get current wallet info", e);
        }
    }

    /**
     * 检查是否有当前钱包
     */
    public boolean hasCurrentWallet() {
        return currentCredentials != null;
    }

    /**
     * 获取当前钱包的Credentials（用于交易）
     */
    public Credentials getCurrentCredentials() {
        if (currentCredentials == null) {
            throw new WalletException("No wallet is currently loaded");
        }
        return currentCredentials;
    }

    /**
     * 私有方法：获取钱包余额
     */
    private BigInteger getWalletBalance(String address) {
        try {
            return web3Service.getBalance(address);
        } catch (IOException e) {
            logger.warn("Could not fetch balance for address {}: {}", address, e.getMessage());
            return BigInteger.ZERO;
        }
    }

    /**
     * 私有方法：保存加密的私钥
     */
    private void savePrivateKey(String address, BigInteger privateKey) {
        try {
            // 创建钱包存储目录
            Path walletDir = Paths.get(walletStoragePath);
            if (!Files.exists(walletDir)) {
                Files.createDirectories(walletDir);
            }
            
            // 加密私钥
            String encryptedPrivateKey = encryptPrivateKey(privateKey.toString(16));
            
            // 保存到文件
            Path walletFile = walletDir.resolve(address + ".wallet");
            Files.write(walletFile, encryptedPrivateKey.getBytes());
            
            logger.debug("Private key saved for address: {}", address);
            
        } catch (Exception e) {
            logger.error("Error saving private key: {}", e.getMessage());
            throw new WalletException("Failed to save private key", e);
        }
    }

    /**
     * 私有方法：加密私钥
     */
    private String encryptPrivateKey(String privateKey) {
        try {
            // 使用AES加密
            SecretKeySpec secretKey = new SecretKeySpec(
                encryptionKey.getBytes(), 0, 16, "AES"
            );
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedBytes = cipher.doFinal(privateKey.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
            
        } catch (Exception e) {
            logger.error("Error encrypting private key: {}", e.getMessage());
            throw new WalletException("Failed to encrypt private key", e);
        }
    }

    /**
     * 私有方法：解密私钥
     */
    private String decryptPrivateKey(String encryptedPrivateKey) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(
                encryptionKey.getBytes(), 0, 16, "AES"
            );
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPrivateKey));
            return new String(decryptedBytes);
            
        } catch (Exception e) {
            logger.error("Error decrypting private key: {}", e.getMessage());
            throw new WalletException("Failed to decrypt private key", e);
        }
    }

    /**
     * 加载已保存的钱包
     */
    public WalletInfo loadWallet(String address) {
        try {
            Path walletFile = Paths.get(walletStoragePath, address + ".wallet");
            
            if (!Files.exists(walletFile)) {
                throw new WalletException("Wallet file not found for address: " + address);
            }
            
            // 读取并解密私钥
            String encryptedPrivateKey = new String(Files.readAllBytes(walletFile));
            String privateKeyHex = decryptPrivateKey(encryptedPrivateKey);
            
            // 导入钱包
            return importWallet(privateKeyHex);
            
        } catch (Exception e) {
            logger.error("Error loading wallet: {}", e.getMessage());
            throw new WalletException("Failed to load wallet", e);
        }
    }

    /**
     * 导出指定地址对应的明文私钥（十六进制，不带0x前缀）。
     * 读取 wallets 目录下的加密文件，解密后返回。
     */
    public String exportPrivateKeyHex(String address) {
        try {
            Path walletFile = Paths.get(walletStoragePath, address + ".wallet");
            if (!Files.exists(walletFile)) {
                throw new WalletException("Wallet file not found for address: " + address);
            }

            String encryptedPrivateKey = new String(Files.readAllBytes(walletFile));
            String privateKeyHex = decryptPrivateKey(encryptedPrivateKey);
            return privateKeyHex;
        } catch (Exception e) {
            logger.error("Error exporting private key: {}", e.getMessage());
            throw new WalletException("Failed to export private key", e);
        }
    }

    /**
     * 通过生成新的助记词来创建钱包
     */
    public WalletInfo createWalletWithMnemonic() {
        try {
            logger.info("Creating new wallet with mnemonic...");
            SecureRandom secureRandom = new SecureRandom();
            byte[] entropy = new byte[16]; // 128 bits = 12 words
            secureRandom.nextBytes(entropy);

            String mnemonic = MnemonicUtils.generateMnemonic(entropy);
            Credentials credentials = WalletUtils.loadBip39Credentials("", mnemonic);

            String address = credentials.getAddress();
            logger.info("Generated new wallet with mnemonic for address: {}", address);

            savePrivateKey(address, credentials.getEcKeyPair().getPrivateKey());
            this.currentCredentials = credentials;

            BigInteger balance = getWalletBalance(address);
            String balanceInEth = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();

            WalletInfo walletInfo = new WalletInfo(address, balance, balanceInEth, LocalDateTime.now());
            walletInfo.setImported(false);
            walletInfo.setMnemonic(mnemonic); // Only set mnemonic on creation

            logger.info("Wallet created successfully with mnemonic for address: {}", address);
            return walletInfo;

        } catch (Exception e) {
            logger.error("Error creating wallet with mnemonic: {}", e.getMessage(), e);
            throw new WalletException("Failed to create wallet with mnemonic", e);
        }
    }

    /**
     * 从助记词导入或加载钱包。
     * 如果钱包已存在本地，则加载它（登录）。
     * 如果不存在，则导入并保存（首次导入）。
     */
    public WalletInfo importWalletFromMnemonic(String mnemonic) {
        try {
            if (!MnemonicUtils.validateMnemonic(mnemonic)) {
                throw new WalletException("Invalid mnemonic phrase");
            }

            Credentials credentials = WalletUtils.loadBip39Credentials("", mnemonic);
            String address = credentials.getAddress();

            Path walletFile = Paths.get(walletStoragePath, address + ".wallet");
            if (Files.exists(walletFile)) {
                logger.info("Wallet for address {} already exists. Loading it...", address);
                return loadWallet(address);
            }

            logger.info("Importing new wallet from mnemonic for address: {}", address);
            savePrivateKey(address, credentials.getEcKeyPair().getPrivateKey());
            this.currentCredentials = credentials;

            BigInteger balance = getWalletBalance(address);
            String balanceInEth = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString();

            WalletInfo walletInfo = new WalletInfo(address, balance, balanceInEth, LocalDateTime.now());
            walletInfo.setImported(true);

            logger.info("Wallet imported successfully from mnemonic for address: {}", address);
            return walletInfo;

        } catch (Exception e) {
            logger.error("Error importing wallet from mnemonic: {}", e.getMessage());
            throw new WalletException("Failed to import wallet from mnemonic: " + e.getMessage(), e);
        }
    }
}