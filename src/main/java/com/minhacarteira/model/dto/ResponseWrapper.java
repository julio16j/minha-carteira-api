package com.minhacarteira.model.dto;

import java.util.List;

public record ResponseWrapper(
        List<MarketPriceDTO> results
) {}