package com.target.myretail.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"product_description"})
@Getter
@Setter
public class Item implements Serializable {
    private static final long serialVersionUID = 1483991355281974507L;

    @JsonProperty("product_description")
    protected ProductDescription productDescription;
}
