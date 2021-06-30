package io.github.sasuked.bukkitsql.repository;

import io.github.sasuked.bukkitsql.provider.ConnectionProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AsyncCrudRepository<T> implements CrudRepository<T> {

	private final ConnectionProvider provider;
	private final Executor executor;

	public AsyncCrudRepository(ConnectionProvider provider, Executor executor) {
		this.provider = provider;
		this.executor = executor;
	}

	public CompletableFuture<Boolean> createTableAsync() {
		return CompletableFuture.supplyAsync(this::createTable, executor);
	}

	public CompletableFuture<Integer> updateOneAsync(T element) {
		return CompletableFuture.supplyAsync(() -> updateOne(element), executor);
	}

	public CompletableFuture<Integer> deleteOneAsync(T element) {
		return CompletableFuture.supplyAsync(() -> deleteOne(element), executor);
	}

	public CompletableFuture<List<T>> selectAllAsync() {
		return CompletableFuture.supplyAsync(this::selectAll, executor);
	}

	public CompletableFuture<T> selectOneAsync(String condition) {
		return CompletableFuture.supplyAsync(() -> selectOne(condition), executor);
	}

	@Override
	public ConnectionProvider getConnectionProvider() {
		return provider;
	}

	public Executor getExecutor() {
		return executor;
	}
}
