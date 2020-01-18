package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.stockmarket.www.entity.Analysis;
import com.stockmarket.www.entity.StockDetail;

public class JdbcAnalysisDao {
	public List<Analysis> getlist() {
		String sql = "SELECT * FROM ANALYSIS_DATA"
				+ "order by RECORD_DATE desc";
		
		JdbcDaoContext daoContext = new JdbcDaoContext();
		Statement statement = null;
		ResultSet resultSet = null;
		List<Analysis> list = new ArrayList<>();
		try {
			statement = daoContext.getStatement();
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				Analysis entity = new Analysis(
						resultSet.getString("STOCKCODE"), 
						resultSet.getString("RECORD_DATE"), 
						resultSet.getInt("SUPPLY"), 
						resultSet.getInt("INFLUENCE"), 
						resultSet.getInt("TREND"), 
						resultSet.getInt("CONTENTS"), 
						resultSet.getInt("SCALE"),
						resultSet.getInt("RESULT"));
				list.add(entity);
			}
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(resultSet, statement);
		}
		return list;
	}
	
	public Analysis get(String codeNum) {
		String sql = "SELECT * FROM ANALYSIS_DATA"
				+ " WHERE STOCKCODE=" + codeNum + " order by RECORD_DATE desc";
		
		JdbcDaoContext daoContext = new JdbcDaoContext();
		Statement statement = null;
		ResultSet resultSet = null;
		Analysis entity = new Analysis();
		try {
			statement = daoContext.getStatement();
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				entity = new Analysis(
						resultSet.getString("STOCKCODE"), 
						resultSet.getString("RECORD_DATE"), 
						resultSet.getInt("SUPPLY"), 
						resultSet.getInt("INFLUENCE"), 
						resultSet.getInt("TREND"), 
						resultSet.getInt("CONTENTS"), 
						resultSet.getInt("SCALE"),
						resultSet.getInt("RESULT"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(resultSet, statement);
		}
		return entity;
	}


	public int insert(List<Analysis> entityList) {
		int result = 0;
		
		String sql = "INSERT INTO ANALYSIS_DATA "
				+ "(STOCKCODE, SUPPLY, INFLUENCE, TREND, CONTENTS, SCALE, RESULT, RECORD_DATE) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement statement = null;
		try {
			statement = daoContext.getPreparedStatement(sql);
			
			for(Analysis entity: entityList) {
				statement.setString(1, entity.getCodeNum());
				statement.setInt(2, entity.getSupply());
				statement.setInt(3, entity.getInfluence());
				statement.setInt(4, entity.getTrend());
				statement.setInt(5, entity.getContents());
				statement.setInt(6, entity.getScale());
				statement.setInt(7, entity.getResult());
				statement.setString(8, entity.getRecord_date());
				
				result = statement.executeUpdate();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(statement);
		}
		return result;
	}


	public int delete(String codeNum) {
		// TODO Auto-generated method stub
		return 0;
	}
}
