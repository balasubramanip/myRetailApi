package com.target.myretail.data.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.target.myretail.data.entity.ProductPriceEntity;

@Repository
public interface ProductPriceRepository extends ReactiveCassandraRepository<ProductPriceEntity, Long> {
}
