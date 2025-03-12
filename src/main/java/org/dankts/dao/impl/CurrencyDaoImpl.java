package org.dankts.dao.impl;

import org.dankts.dao.CurrencyDao;
import org.dankts.entity.Currency;
import org.dankts.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {
    private static final String FIND_ALL_SQL = """
            SELECT id, code, full_name, sign FROM currencies
            """;
    private static final String GET_CURRENCY_BY_CODE_SQL = """
            SELECT id, code, full_name, sign FROM currencies
            WHERE code = ?
            """;
    private static final String SAVE_CURRENCY_SQL = """
            INSERT INTO currencies (code, full_name, sign)
            VALUES (?, ? ,?);
            """;

    @Override
    public List<Currency> findAll() {
        try (Connection connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildCurrency(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currency save(Currency entity) {
        try (Connection connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(SAVE_CURRENCY_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getFullName());
            preparedStatement.setString(3, entity.getSign());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getObject("id", Integer.class));
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Currency> getByCode(String code) {
        try (Connection connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(GET_CURRENCY_BY_CODE_SQL)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getObject("id", Integer.class),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
