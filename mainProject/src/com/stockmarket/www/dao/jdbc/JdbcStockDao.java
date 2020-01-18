package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.stockmarket.www.dao.StockDao;

public class JdbcStockDao implements StockDao {

	@Override
	public String getStockName(String codeNum) {
		String sql = "SELECT * FROM KOREASTOCKS WHERE STOCKCODE=?";
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = daoContext.getPreparedStatement(sql);
			pst.setString(1, codeNum);
			rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getString("COMPANYNAME");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(rs, pst);
		}
		return null;
	}

	@Override
	public String getStockCodeNum(String name) {
		String sql = "SELECT * FROM KOREASTOCKS WHERE COMPANYNAME=?";
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = daoContext.getPreparedStatement(sql);
			pst.setString(1, name);
			rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getString("STOCKCODE");
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(rs, pst);
		}
		return null;
	}

	// 단위 검사
	public static void main(String[] args) {
		JdbcStockDao stockDao = new JdbcStockDao();

		System.out.println(stockDao.getStockName("005380"));
		System.out.println(stockDao.getStockCodeNum("현대차"));
		
		while(true) {
			System.out.println("Input ");
			Scanner sc = new Scanner(System.in);
			int input = sc.nextInt(); 
			
			for(int i = 0; i < input; i++) {
				stockDao.getStockName("005380");
				stockDao.getStockCodeNum("현대차");
			}
				
		}
	}
}
