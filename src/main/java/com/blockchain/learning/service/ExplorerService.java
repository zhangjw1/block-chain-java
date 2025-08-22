package com.blockchain.learning.service;

import com.blockchain.learning.model.EtherscanResponse;
import com.blockchain.learning.model.EtherscanTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(java.time.Duration.ofSeconds(5))
                .setReadTimeout(java.time.Duration.ofSeconds(10))
                .build();
    }

    /**
     * Fetches the transaction history for a given address from the Etherscan API.
     *
     * @param address The Ethereum address to query.
     * @return A list of transactions, or an empty list if there are none or an error occurs.
     */
    public List<EtherscanTransaction> getTransactionHistory(String address) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("module", "account")
                .queryParam("action", "txlist")
                .queryParam("address", address)
                .queryParam("startblock", 0)
                .queryParam("endblock", 99999999)
                .queryParam("sort", "desc")
                .queryParam("apikey", apiKey)
                .toUriString();

        try {
            String maskedKey = (apiKey == null) ? "null" : (apiKey.length() <= 6 ? "******" : (apiKey.substring(0, 3) + "***" + apiKey.substring(apiKey.length() - 3)));
            logger.debug("Fetching transaction history. urlBase={}, addr={}, key={}", apiUrl, address, maskedKey);
            EtherscanResponse response = restTemplate.getForObject(url, EtherscanResponse.class);

            if (response != null && "1".equals(response.getStatus())) {
                logger.info("Successfully fetched {} transactions for address {}", response.getResult().size(), address);
                return response.getResult();
            } else {
                String errorMessage = (response != null) ? response.getMessage() : "No response from Etherscan API";
                logger.warn("Could not fetch transaction history for address {}: {}", address, errorMessage);
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String body = e.getResponseBodyAsString();
            logger.error("HTTP error from Etherscan for address {}: status={}, body={}", address, e.getStatusCode(), body);
            return Collections.emptyList();
        } catch (ResourceAccessException e) {
            logger.error("Network/timeout when calling Etherscan for address {}: {}", address, e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error while fetching transaction history for address {}: {}", address, e.getMessage());
            return Collections.emptyList();
        }
    }
}
