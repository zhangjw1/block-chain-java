package com.blockchain.learning.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3Config {

    private static final Logger logger = LoggerFactory.getLogger(Web3Config.class);

    @Value("${blockchain.infura.project-id}")
    private String infuraProjectId;

    @Value("${blockchain.network.name:sepolia}")
    private String networkName;

    @Bean
    public Web3j web3j() {
        String infuraUrl = String.format("https://%s.infura.io/v3/%s", networkName, infuraProjectId);
        logger.info("Connecting to Ethereum network: {} via URL: {}", networkName, infuraUrl);
        
        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
        logger.info("Web3j client initialized successfully");
        
        return web3j;
    }
}
