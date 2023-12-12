package com.minhacarteira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MarketPriceDTO(
        @JsonProperty("regularMarketPrice") Double regularMarketPrice
) {}