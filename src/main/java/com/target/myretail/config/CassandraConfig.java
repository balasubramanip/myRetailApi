package com.target.myretail.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;

/**
 *
 */
@Configuration
@EnableReactiveCassandraRepositories(basePackages = "com.target.myretail.data.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {
    @Autowired
    protected Environment environment;

    @Override
    protected String getKeyspaceName() {
        return this.environment.getProperty("spring.data.cassandra.keyspace-name");
    }

    @Override
    protected int getPort() {
        return Integer.parseInt(this.environment.getProperty("spring.data.cassandra.port"));
    }

    @Override
    protected String getContactPoints() {
        return this.environment.getProperty("spring.data.cassandra.contactpoints");
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.target.myretail.data.entity"};
    }

    @Override
    protected boolean getMetricsEnabled() {
        return false;
    }

    @Override
    protected QueryOptions getQueryOptions() {
        final QueryOptions queryOptions = new QueryOptions();
        queryOptions.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM);
        return queryOptions;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName())
                                                                    .ifNotExists()
                                                                    .with(KeyspaceOption.DURABLE_WRITES,
                                                                          true)
                                                                    .withSimpleReplication());
    }

    @Override
    protected List<String> getStartupScripts() {
        return Collections.singletonList("CREATE TABLE IF NOT EXISTS " + getKeyspaceName() + ".product_price(id bigint PRIMARY KEY, price DECIMAL,"
                                         + " currency_code text) WITH COMPACTION = { 'class' : 'LeveledCompactionStrategy' }");
    }
}
