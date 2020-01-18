package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stockmarket.www.dao.CaptureMemoDao;
import com.stockmarket.www.entity.CaptureMemo;
import com.stockmarket.www.entity.CaptureMemoView;

public class JdbcCaptureMemoDao implements CaptureMemoDao {
	
	@Override
	public CaptureMemo get(int id) {
		String sql = "SELECT * FROM CAPTURE_MEMO WHERE ID=?";
		CaptureMemo captureMemo = null;
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String title = resultSet.getString("TITLE");
				String content = resultSet.getString("CONTENT");
				double PER = resultSet.getDouble("PER");
				double PBR = resultSet.getDouble("PBR");
				double ROE = resultSet.getDouble("ROE");
				double debtRatio = resultSet.getDouble("DEBT_RATIO");
				int marketCap = resultSet.getInt("MARKET_CAP");
				double foreignInvestors = resultSet.getDouble("FOREIGN_INVESTORS");
				Date regdate = resultSet.getDate("REGDATE");
				String codeNum = resultSet.getString("CODENUM");
				int memberId = resultSet.getInt("MEMBER_ID");
				
				captureMemo = new CaptureMemo(
						id, title, regdate, content, 
						PER, PBR, ROE, debtRatio, marketCap, 
						foreignInvestors, codeNum, memberId
				);
			}
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return captureMemo;
	}
	
	@Override
	public List<CaptureMemoView> getList(int uid) {
		List<CaptureMemoView> captureMemos = new ArrayList<>();
		
		String sql = "SELECT S.NAME, C.TITLE, C.REGDATE, C.MEMBER_ID, C.ID "
				+ "FROM CAPTURE_MEMO C JOIN STOCK S ON C.CODENUM = S.CODENUM WHERE MEMBER_ID=? ORDER BY REGDATE DESC";

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);
			statement.setInt(1, uid);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {		
				int id = resultSet.getInt("ID");
				String name = resultSet.getString("NAME");
				String title = resultSet.getString("TITLE");
				Date regdate = resultSet.getDate("regdate");
				
				CaptureMemoView captureMemo = new CaptureMemoView(id, name, title, regdate);
				
				captureMemos.add(captureMemo);
			}
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return captureMemos;
	}

	@Override
	public int insert(CaptureMemo captureMemo) {
		int result = 0;

		String sql = "INSERT INTO CAPTURE_MEMO(PER, PBR, "
				+ "ROE, DEBT_RATIO, MARKET_CAP, FOREIGN_INVESTORS, CODENUM, MEMBER_ID)" 
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);
			statement.setDouble(1, captureMemo.getPER());
			statement.setDouble(2, captureMemo.getPBR());
			statement.setDouble(3, captureMemo.getROE());
			statement.setDouble(4, captureMemo.getDebtRatio());
			statement.setInt(5, captureMemo.getMarketCap());
			statement.setDouble(6, captureMemo.getForeignInvestors());			
			statement.setString(7, captureMemo.getCodeNum());
			statement.setInt(8, captureMemo.getMemberId());

			result = statement.executeUpdate();

			daoContext.close(statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int delete(int id) {
		int result = 0;
		
		String sql = "DELETE CAPTURE_MEMO WHERE ID=?";

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setInt(1, id);

			result = statement.executeUpdate();

			daoContext.close(statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int update(CaptureMemo captureMemo) {
		int result = 0;

		String sql = "UPDATE CAPTURE_MEMO SET TITLE=?, CONTENT=? WHERE ID=?";

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);
			statement.setString(1, captureMemo.getTitle());
			statement.setString(2, captureMemo.getContent());
			statement.setInt(3, captureMemo.getId());

			result = statement.executeUpdate();

			daoContext.close(statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
//	public static void main(String[] args) {
//		JdbcCaptureMemoDao dao = new JdbcCaptureMemoDao();
//		List<CaptureMemoView> list = dao.getList(1);
		
//		for(CaptureMemoView c:list) {
//			System.out.println(c.toString());
//		}
		
		// insert test
//		CaptureMemo captureMemo = new CaptureMemo("이거슨 제목...", "이거슨 메모..", 11, 12, 13, 11, 13, "005380", 2);
//		int result = dao.insert(captureMemo);
//		System.out.println(result);
		
		// update test
		
//		CaptureMemo captureMemo = new CaptureMemo("이거슨 제목!!!", "이거슨 메모..", 11, 12, 13, 11, 13, "005380", 2);
//		captureMemo.setId(1);
//		System.out.println(captureMemo.toString());
//		int result = dao.update(captureMemo);
//		System.out.println(result);		
//	}
}
