CREATE KEYSPACE IF NOT EXISTS myretail WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1};
USE myretail;
CREATE TABLE IF NOT EXISTS myretail.product_price(id bigint PRIMARY KEY, price DECIMAL, currency_code text) WITH COMPACTION = { 'class' : 'LeveledCompactionStrategy'}
