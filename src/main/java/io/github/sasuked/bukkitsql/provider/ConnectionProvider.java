package io.github.sasuked.bukkitsql.provider;

import com.google.common.collect.Maps;
import io.github.sasuked.bukkitsql.function.SqlFunction;
import io.github.sasuked.bukkitsql.mapper.SqlMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class ConnectionProvider {
  
  public abstract Connection getCurrentConnection();
  
  public boolean execute(String sql, Object... statementValues) {
    Connection connection = this.getCurrentConnection();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      applyValuesToStatement(statement, statementValues);
      
      return statement.execute();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  public int executeUpdate(String sql, Object... statementValues) {
    Connection connection = this.getCurrentConnection();
    
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      this.applyValuesToStatement(preparedStatement, statementValues);
      
      return preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }
  
  public ResultSet executeQuery(String sql, Object... statementValues) {
    try (PreparedStatement statement = this.getCurrentConnection().prepareStatement(sql)) {
      applyValuesToStatement(statement, statementValues);
      
      return statement.executeQuery();
    } catch (Exception e) {
      return null;
    }
  }
  
  public <T> List<T> selectAsList(String sql, SqlFunction<ResultSet, T> function, Object... statementValues) {
    Connection currentConnection = getCurrentConnection();
    
    List<T> collected = Collections.synchronizedList(new ArrayList<>());
    try (PreparedStatement statement = currentConnection.prepareStatement(sql)) {
      applyValuesToStatement(statement, statementValues);
      
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          T obj = function.apply(resultSet);
          if (obj != null) {
            collected.add(obj);
          }
        }
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return collected;
  }
  
  public <T> T getFirstFromQuery(String sql, SqlFunction<ResultSet, T> function, Object... statementValues) {
    Connection currentConnection = this.getCurrentConnection();
    
    try (PreparedStatement statement = currentConnection.prepareStatement(sql)) {
      applyValuesToStatement(statement, statementValues);
      
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.first()) {
          return function.apply(resultSet);
        } else {
          return null;
        }
      }
    } catch (Exception e) {
      return null;
    }
  }
  
  public <K, V> Map<K, V> mapFromQuery(String sql, SqlMapper<K, V> mapper, Object... statementValues) {
    try (ResultSet resultSet = executeQuery(sql, statementValues)) {
      Map<K, V> map = Maps.newConcurrentMap();
      
      while (resultSet.next()) {
        
        K key = mapper.transformKey(resultSet);
        V value = mapper.transformValue(resultSet);
        
        map.put(key, value);
      }
      
      return map;
    } catch (Exception e) {
      return Collections.emptyMap();
    }
  }
  
  private void applyValuesToStatement(PreparedStatement statement, Object... values) throws SQLException {
    if (values.length > 0) {
      for (int i = 0; i < values.length; i++) {
        statement.setObject(i + 1, values[i]);
      }
    }
  }
  
}
