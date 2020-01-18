package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.stockmarket.www.dao.StockDao;
import com.stockmarket.www.dao.StockDetailDao;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.entity.StockDetail;

public class JdbcStockDetailDao implements StockDetailDao {

	@Override
	public List<StockDetail> get(String codeNum) {
		String sql = "SELECT * FROM STOCK_DETAIL WHERE CODENUM=" + codeNum + " order by RECORD_DATE desc";
		
		List<StockDetail> list = new ArrayList<>();
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			Statement statement = daoContext.getStatement();
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				StockDetail stock = new StockDetail(
						resultSet.getInt("TRADE_VOLUME"), 
						resultSet.getString("RECORD_DATE"), 
						resultSet.getString("CODENUM"), 
						resultSet.getInt("FOREIGN"), 
						resultSet.getInt("CLOSE_PRICE"), 
						resultSet.getInt("INSTITUTION"), 
						resultSet.getInt("INDIVISUAL"));
				list.add(stock);
			}
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public int insert(StockDetail[] stocks) {
		int result = 0;
		
		String sql = "INSERT INTO STOCK_DETAIL (CODENUM, RECORD_DATE, CLOSE_PRICE, TRADE_VOLUME, FOREIGN, INSTITUTION, INDIVISUAL)  "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);
			for(int i = 0; i < stocks.length; i++) {
	
				statement.setString(1, stocks[i].getItemcode());
				statement.setString(2, stocks[i].getBizdate());
				statement.setInt(3, stocks[i].getClose_val());
				statement.setInt(4, stocks[i].getAcc_quant());
				statement.setInt(5, stocks[i].getFrgn_pure_buy_quant());
				statement.setInt(6, stocks[i].getOrgan_pure_buy_quant());
				statement.setInt(7, stocks[i].getIndi_pure_buy_quant());
	
				result = statement.executeUpdate();
			}
			statement.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int insertDaily(StockDetail stock) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	//2016년 이전 데이터는 지워버린다
	@Override
	public int deletePreDate() {
		int result = 0;
		String sql = "DELETE STOCK_DETAIL WHERE RECORD_DATE <= 20151231 ";
		JdbcDaoContext context = new JdbcDaoContext();
		try {
			PreparedStatement st = context.getPreparedStatement(sql);
			result = st.executeUpdate();
			context.close(st);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int delete(String codeNum) {
		// TODO Auto-generated method stub
		return 0;
	}


}
