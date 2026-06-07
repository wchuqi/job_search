package com.javastudy.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcPersistenceDemo {

    public void createSchema(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    create table account (
                        id int primary key,
                        owner varchar(64) not null,
                        balance int not null
                    )
                    """);
        }
    }

    public int insertAccount(DataSource dataSource, int id, String owner, int balance) throws SQLException {
        String sql = "insert into account(id, owner, balance) values (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, owner);
            statement.setInt(3, balance);
            return statement.executeUpdate();
        }
    }

    public String findOwner(DataSource dataSource, int id) throws SQLException {
        String sql = "select owner from account where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString("owner") : "";
            }
        }
    }

    public void transfer(DataSource dataSource, int fromId, int toId, int amount) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                updateBalance(connection, fromId, -amount);
                updateBalance(connection, toId, amount);
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public int balance(DataSource dataSource, int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select balance from account where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public int[] batchInsert(DataSource dataSource, List<AccountRow> rows) throws SQLException {
        String sql = "insert into account(id, owner, balance) values (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (AccountRow row : rows) {
                statement.setInt(1, row.id());
                statement.setString(2, row.owner());
                statement.setInt(3, row.balance());
                statement.addBatch();
            }
            return statement.executeBatch();
        }
    }

    public List<String> listOwners(DataSource dataSource) throws SQLException {
        List<String> owners = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select owner from account order by id");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                owners.add(resultSet.getString(1));
            }
            return owners;
        }
    }

    public List<String> isolationLevels() {
        return List.of("READ_UNCOMMITTED", "READ_COMMITTED", "REPEATABLE_READ", "SERIALIZABLE");
    }

    public List<String> persistenceConcepts() {
        return List.of("JPA", "Hibernate", "MyBatis", "N+1 query", "transaction boundary");
    }

    public String sqlInjectionDefense() {
        return "use PreparedStatement parameters, never concatenate untrusted input into SQL";
    }

    private void updateBalance(Connection connection, int id, int delta) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "update account set balance = balance + ? where id = ?")) {
            statement.setInt(1, delta);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    public record AccountRow(int id, String owner, int balance) {}
}
