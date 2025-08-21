package com.blockchain.learning.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the top-level response from the Etherscan API for a list of transactions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtherscanResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("result")
    private List<EtherscanTransaction> result;

    // Getters and Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EtherscanTransaction> getResult() {
        return result;
    }

    public void setResult(List<EtherscanTransaction> result) {
        this.result = result;
    }
}
