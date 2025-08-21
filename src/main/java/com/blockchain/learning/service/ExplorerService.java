package com.blockchain.learning.service;

import com.blockchain.learning.model.EtherscanResponse;
import com.blockchain.learning.model.EtherscanTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ExplorerService {

    private static final Logger logger = LoggerFactory.getLogger(ExplorerService.class);

    private final RestTemplate restTemplate;

    @Value("${explorer.api.url}")
    private String apiUrl;

    @Value("${explorer.api.key}")
    private String apiKey;

    public ExplorerService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Fetches the transaction history for a given address from the Etherscan API.
     *
     * @param address The Ethereum address to query.
     * @return A list of transactions, or an empty list if there are none or an error occurs.
     */
    public List<EtherscanTransaction> getTransactionHistory(String address) {
        String url = String.format(
                "%s?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=desc&apikey=%s",
                apiUrl, address, apiKey);

        try {
            logger.debug("Fetching transaction history from URL: {}", url);
            EtherscanResponse response = restTemplate.getForObject(url, EtherscanResponse.class);

            if (response != null && "1".equals(response.getStatus())) {
                logger.info("Successfully fetched {} transactions for address {}", response.getResult().size(), address);
                return response.getResult();
            } else {
                String errorMessage = (response != null) ? response.getMessage() : "No response from Etherscan API";
                logger.warn("Could not fetch transaction history for address {}: {}", address, errorMessage);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching transaction history for address {}: {}", address, e.getMessage());
            return Collections.emptyList();
        }
    }
}
