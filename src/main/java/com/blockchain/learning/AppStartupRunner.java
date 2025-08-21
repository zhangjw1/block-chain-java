package com.blockchain.learning;

import com.blockchain.learning.model.NetworkInfo;
import com.blockchain.learning.service.Web3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Autowired
    private Web3Service web3Service;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== Java Blockchain Learning Application Started ===");

        try {
            NetworkInfo networkInfo = web3Service.getNetworkInfo();
            if (networkInfo.isConnected()) {
                logger.info("✅ Successfully connected to blockchain network!");
                logger.info("📡 Network: {}", networkInfo.getNetworkName());
                logger.info("🔗 Chain ID: {}", networkInfo.getChainId());
                logger.info("📦 Latest Block: {}", networkInfo.getLatestBlockNumber());
                logger.info("🖥️  Node Version: {}", networkInfo.getNodeVersion());
            } else {
                logger.warn("❌ Failed to connect to blockchain network");
                logger.warn("Please check your Infura configuration and network settings");
            }
        } catch (Exception e) {
            logger.error("❌ Error during network connection: {}", e.getMessage());
            logger.error("Please ensure INFURA_PROJECT_ID environment variable is set correctly");
        }

        logger.info("🚀 Application is ready! Visit http://localhost:8080");
        logger.info("📚 API Documentation is available at http://localhost:8080/swagger-ui.html");
    }
}
