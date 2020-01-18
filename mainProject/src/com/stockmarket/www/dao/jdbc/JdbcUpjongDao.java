package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.stockmarket.www.dao.UpjongDao;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.entity.Upjong;

public class JdbcUpjongDao implements UpjongDao {

	@Override
	public String getUpjong(String stockName) {
		String sql = "SELECT UPJONG FROM UPJONG WHERE STOCKNAME=?";
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement pst =null;
		ResultSet rs = null;
		
		try {
			pst = daoContext.getPreparedStatement(sql);
			pst.setString(1, stockName);
			rs = pst.executeQuery();

			if (rs.next()) 
				return rs.getString("UPJONG");

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
	public List<String> getStockNames(String upjong) {
		String sql = "SELECT STOCKNAME FROM UPJONG WHERE UPJONG=?";
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement pst =null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		
		try {
			pst = daoContext.getPreparedStatement(sql);
			pst.setString(1, upjong);
			rs = pst.executeQuery();

			while(rs.next()) {
				list.add(rs.getString("STOCKNAME"));
			}
			

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(rs, pst);
		}
		
		return list;
	}
	
	@Override
	public int insert(List<Upjong> list) {
		int result = 0;
		
		String sql = "INSERT INTO Upjong (UPJONG, STOCKNAME) "
				+ "VALUES (?, ?)";
		
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement statement = null;
		try {
			statement = daoContext.getPreparedStatement(sql);
			
			for(int i = 0 ; i <list.size(); i++) {
				statement.setString(1, list.get(i).getUpjong());
				statement.setString(2, list.get(i).getStockName());
			}
			
			result = statement.executeUpdate();
			

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(statement);
		}
		return result;
	}

	@Override
	public int delete() {
		int result = 0;
		String sql = "DELETE FROM UPJONG";
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement st = null;
		
		try {
			st = daoContext.getPreparedStatement(sql);
			result = st.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(st);
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		JdbcUpjongDao dao = new JdbcUpjongDao();
		System.out.println(dao.getUpjong("지어소프트"));
		System.out.println(dao.getStockNames(dao.getUpjong("지어소프트")));
	}
}
