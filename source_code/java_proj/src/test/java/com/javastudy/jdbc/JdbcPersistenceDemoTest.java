package com.javastudy.jdbc;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcPersistenceDemoTest {

    private final JdbcPersistenceDemo demo = new JdbcPersistenceDemo();
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
        h2.setUser("sa");
        dataSource = h2;
        demo.createSchema(dataSource);
    }

    @Test
    void preparedStatementQueryAndResourceReleaseWork() throws Exception {
        assertEquals(1, demo.insertAccount(dataSource, 1, "Alice", 100));
        assertEquals("Alice", demo.findOwner(dataSource, 1));
        assertEquals("", demo.findOwner(dataSource, 404));
    }

    @Test
    void transactionAndBatchOperationsWork() throws Exception {
        demo.batchInsert(dataSource, List.of(
                new JdbcPersistenceDemo.AccountRow(1, "A", 100),
                new JdbcPersistenceDemo.AccountRow(2, "B", 30)));
        demo.transfer(dataSource, 1, 2, 25);
        assertEquals(75, demo.balance(dataSource, 1));
        assertEquals(55, demo.balance(dataSource, 2));
        assertArrayEquals(new int[]{1}, demo.batchInsert(dataSource,
                List.of(new JdbcPersistenceDemo.AccountRow(3, "C", 10))));
        assertEquals(List.of("A", "B", "C"), demo.listOwners(dataSource));
    }

    @Test
    void jdbcPersistenceConceptsAreNamed() {
        assertTrue(demo.isolationLevels().contains("SERIALIZABLE"));
        assertTrue(demo.persistenceConcepts().contains("N+1 query"));
        assertTrue(demo.sqlInjectionDefense().contains("PreparedStatement"));
    }
}
