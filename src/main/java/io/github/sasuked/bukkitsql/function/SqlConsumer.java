package io.github.sasuked.bukkitsql.function;

import java.sql.SQLException;
import java.util.function.Consumer;

public interface SqlConsumer<T> extends Consumer<T> {
	
	void applyThrowing(T obj) throws SQLException;
	
	@Override
	default void accept(T t) {
		try {
			applyThrowing(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
