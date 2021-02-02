package io.github.sasuked.bukkitsql.provider.mysql;

import io.github.sasuked.bukkitsql.provider.ConnectionProvider;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public final class MysqlConnectionProvider extends ConnectionProvider {
	
	private static final String MYSQL_CONNECTION_STRING = "jdbc:mysql://<host>:<port>/<database>";
	
	private String host;
	private int port;
	private String databaseName;
	private String user;
	private String password;
	
	private Connection cachedConnection;
	
	public MysqlConnectionProvider(String host, int port, String databaseName, String user, String password) {
		this.host = host;
		this.port = port;
		this.databaseName = databaseName;
		this.user = user;
		this.password = password;
	}
	
	@SneakyThrows
	@Override
	public Connection getCurrentConnection() {
		if (cachedConnection != null && !cachedConnection.isClosed()) {
			return cachedConnection;
		}
		
		cachedConnection = this.connect();
		if (cachedConnection != null) {
			return cachedConnection;
		}
		
		throw new NullPointerException("Conexão foi nula, verifique as credenciais!");
	}
	
	private Connection connect() {
		try {
			// pqp
			Class.forName("com.mysql.jdbc.Driver");
			
			return DriverManager.getConnection(MYSQL_CONNECTION_STRING, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	private String getConnectionString() {
		return MYSQL_CONNECTION_STRING.replace("<host>", host)
			.replace("<port>", String.valueOf(port))
			.replace("<database>", password);
	}
}
