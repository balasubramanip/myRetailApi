package com.target.myretail.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProductResponse implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    @JsonProperty("product")
    private Product product;

}
