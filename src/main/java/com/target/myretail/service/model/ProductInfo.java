package com.target.myretail.service.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ProductInfo {
    @JsonProperty("id")
    @Valid
    @Positive
    private Long productId;

    @JsonProperty("name")
    private String name;

    @NotNull
    @Valid
    @JsonProperty("current_price")
    private ProductPrice productPrice;
}
