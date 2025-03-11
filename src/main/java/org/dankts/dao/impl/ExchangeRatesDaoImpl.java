package org.dankts.dao.impl;

import org.dankts.dao.ExchangeRatesDao;
import org.dankts.entity.Currency;
import org.dankts.entity.ExchangeRate;
import org.dankts.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDaoImpl implements ExchangeRatesDao {

    @Override
    public ExchangeRate save(ExchangeRate entity) {
        String saveSql = """
                INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
                VALUES  (?, ?, ?)
                """;
        try (Connection connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, entity.getTargetCurrencyId().getId());
            preparedStatement.setObject(2, entity.getBaseCurrencyId().getId());
            preparedStatement.setBigDecimal(3, entity.getRate());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getInt("id"));
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ExchangeRate> findAll() {
        String findAllSql = getFindAllSql();
        try (Connection connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(findAllSql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> result = new ArrayList<>();
            while (resultSet.next()) {
                ExchangeRate exchangeRate = buildExchangeRate(resultSet);
                result.add(exchangeRate);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate findByCode(String baseCurrencyCode, String targetCurrencyCode) {
        String findByCodeSql = getFindAllSql() + " WHERE bc.code = ? AND tc.code = ?";
        try (Connection connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(findByCodeSql)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }
            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate) {
        String updateSql = """
                UPDATE exchange_rates
                SET rate = ?
                WHERE base_currency_id = ? AND target_currency_id = ?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setObject(2, exchangeRate.getBaseCurrencyId().getId());
            preparedStatement.setObject(3, exchangeRate.getTargetCurrencyId().getId());
            preparedStatement.executeUpdate();

            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getInt("bc_id"),
                resultSet.getString("bc_code"),
                resultSet.getString("bc_full_name"),
                resultSet.getString("bc_sign")
        );

        Currency targetCurrency = new Currency(
                resultSet.getInt("tc_id"),
                resultSet.getString("tc_code"),
                resultSet.getString("tc_full_name"),
                resultSet.getString("tc_sign")
        );

        return new ExchangeRate(
                resultSet.getInt("er_id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("er_rate")
        );
    }

    private String getFindAllSql() {
        return """
                SELECT er.id er_id,
                bc.id bc_id, bc.full_name bc_full_name, bc.code bc_code, bc.sign bc_sign,
                tc.id tc_id, tc.full_name tc_full_name, tc.code tc_code, tc.sign tc_sign,
                er.rate er_rate
                FROM exchange_rates er
                JOIN currencies bc on bc.id = er.base_currency_id
                JOIN currencies tc on tc.id = er.target_currency_id
                """;
    }
}
