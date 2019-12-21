package com.target.myretail.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"title"})
@Getter
@Setter
@NoArgsConstructor
public class ProductDescription implements Serializable {
    private static final long serialVersionUID = 2405172041950251807L;
    @JsonProperty("title")
    private String title;
}
