package com.target.myretail.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("product_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceEntity implements Serializable {

    @PrimaryKey
    @Column("id")
    private Long id;

    @Column("price")
    private BigDecimal price;

    @Column("currency_code")
    private String currencyCode;
}
