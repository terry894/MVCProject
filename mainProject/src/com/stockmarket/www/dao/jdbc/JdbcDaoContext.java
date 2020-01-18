package com.stockmarket.www.dao.jdbc;

import java.sql.*;

public class JdbcDaoContext {
	private Connection connection;
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:oracle:thin:@112.223.37.243:1521/xepdb1";
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection(url, "ACORNGROUP1", "month100man");

        return connection;
    }

    public Statement getStatement() throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        return statement;
    }

    public PreparedStatement getPreparedStatement(String sql) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        return statement;
    }
    
    public int close(ResultSet resultSet, Statement statement) {
		int result = 0;
		
    	try {
    		if(!resultSet.isClosed())
    			resultSet.close();
    		if(!statement.isClosed())
    			statement.close();
			if(!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			result = -1;
		}
    	return result;
    }
    
    public int close(Statement statement) {
		int result = 0;
		
    	try {
    		if(!statement.isClosed())
    			statement.close();
			if(!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			result = -1;
		}
    	return result;
    }
    
    public int close(ResultSet resultSet, PreparedStatement statement) {
		int result = 0;
		
    	try {
    		if(!resultSet.isClosed())
    			resultSet.close();
    		if(!statement.isClosed())
    			statement.close();
			if(!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			result = -1;
		}
    	return result;
    }
    
    public int close(PreparedStatement statement) {
		int result = 0;
		
    	try {
    		if(!statement.isClosed())
    			statement.close();
			if(!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			result = -1;
		}
    	return result;
    }
}
