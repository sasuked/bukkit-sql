package io.github.sasuked.bukkitsql.repository;

import io.github.sasuked.bukkitsql.provider.ConnectionProvider;

import java.util.List;

public interface CrudRepository<T> {

	boolean createTable();

	int updateOne(T element);

	int deleteOne(T element);

	List<T> selectAll();

	T selectOne(String condition);

	ConnectionProvider getConnectionProvider();
}
