package com.target.myretail.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = "productId",ignoreUnknown = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice implements Serializable {

    @JsonProperty("value")
    @NotNull(message = "Invalid Price.")
    @DecimalMin("0.00")
    private BigDecimal price;

    @JsonProperty("currency_code")
    @Size(min = 2, max = 3, message = "Invalid Currency Code.Valid Values:USD")
    @NotNull
    private String currencyCode;
}
