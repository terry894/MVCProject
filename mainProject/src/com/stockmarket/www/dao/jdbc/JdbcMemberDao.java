package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.stockmarket.www.dao.MemberDao;
import com.stockmarket.www.entity.Member;

public class JdbcMemberDao implements MemberDao {
	@Override
	public List<Member> getMemberList() {
		String sql = "SELECT * FROM MEMBER";
		
		List<Member> members = new ArrayList<>();

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			Statement statement = daoContext.getStatement();
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				String email = resultSet.getString("EMAIL");
				String nickName = resultSet.getString("NICKNAME");
				String password = resultSet.getString("PASSWORD");
				long vmoney = resultSet.getLong("VMONEY");
				Date regdate = resultSet.getDate("REGDATE");
				String cardPos = resultSet.getString("CARD_POS");
				int profileImg = resultSet.getInt("PROFILE_IMG");

				Member member = new Member(id, email, nickName, password, vmoney, cardPos, profileImg);
				members.add(member);
			}
			
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return members;
	}

	@Override
	public List<Member> getRankerList() {
		Statement statement = null;
		ResultSet resultSet = null;
		
		// 가상머니 상위 50위까지 뽑아오는 쿼리
		String sql = "SELECT * FROM MEMBER ORDER BY VMONEY " + "DESC OFFSET 0 ROWS FETCH NEXT 50 ROWS ONLY";
		
		List<Member> members = new ArrayList<>();

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			statement = daoContext.getStatement();
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				String email = resultSet.getString("EMAIL");
				String nickName = resultSet.getString("NICKNAME");
				String password = resultSet.getString("PASSWORD");
				long vmoney = resultSet.getLong("VMONEY");
				Date regdate = resultSet.getDate("REGDATE");
				String cardPos = resultSet.getString("CARD_POS");
				int profileImg = resultSet.getInt("PROFILE_IMG");	

				Member member = new Member(id, email, nickName, password, vmoney, cardPos, profileImg);
				members.add(member);
			}
			
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return members;
	}

	@Override
	public Member getMember(int id) {
		String sql = "SELECT * FROM MEMBER WHERE ID=" + id;
		
		Member member = null;

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			Statement statement = daoContext.getStatement();
			ResultSet resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				String email = resultSet.getString("EMAIL");
				String nickName = resultSet.getString("NICKNAME");
				String password = resultSet.getString("PASSWORD");
				long vmoney = resultSet.getLong("VMONEY");
				String cardPos = resultSet.getString("CARD_POS");
				int profileImg = resultSet.getInt("PROFILE_IMG");	

				member = new Member(id, email, nickName, password, vmoney, cardPos, profileImg);
			}
			
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}

	@Override
	public Member getMemberByEmail(String query) {
		String sql = "SELECT * FROM MEMBER WHERE EMAIL=?";
		
		Member member = null;

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setString(1, query);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int id = resultSet.getInt("ID");
				String email = resultSet.getString("EMAIL");
				String nickName = resultSet.getString("NICKNAME");
				String password = resultSet.getString("PASSWORD");
				long vmoney = resultSet.getLong("VMONEY");
				String cardPos = resultSet.getString("CARD_POS");	
				int profileImg = resultSet.getInt("PROFILE_IMG");

				member = new Member(id, email, nickName, password, vmoney, cardPos, profileImg);
			}
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}

	@Override
	public int getMemberRank(int id) {
		String sql = "SELECT * FROM (\r\n"
				+ "    SELECT DENSE_RANK() OVER (ORDER BY VMONEY DESC) AS \"RANK\", MEMBER.* FROM MEMBER\r\n"
				+ "    ) \r\n" + "WHERE id = " + id;
		
		int rank = 0;

		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			Statement statement = daoContext.getStatement();
			ResultSet resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				rank = resultSet.getInt("RANK");
			}
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rank;
	}

	@Override
	public int updateMember(int id, long vmoney) {
		String sql = "UPDATE MEMBER SET VMONEY = ? WHERE ID = ?";

		int result = 0;
		
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setLong(1, vmoney);
			statement.setInt(2, id);

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
	public int updateMember(int id, String cardPos) {
		String sql = "UPDATE MEMBER SET CARD_POS = ? WHERE ID = ?";

		int result = 0;
		
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setString(1, cardPos);
			statement.setInt(2, id);

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
	public int insertMember(Member member) {
		int result = 0;
		
		String sql = "INSERT INTO MEMBER (EMAIL, NICKNAME, PASSWORD, VMONEY) "
				+ "VALUES (?, ?, ?, ?)";
		
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setString(1, member.getEmail());
			statement.setString(2, member.getNickName());
			statement.setString(3, member.getPassword());
			statement.setLong(4, member.getvMoney());

			result = statement.executeUpdate();
			statement.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int updateMember(int id, int profileImg, String imgChange) {
		String sql = "UPDATE MEMBER SET PROFILE_IMG = ? WHERE ID = ?";

		int result = 0;
		
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setInt(1, profileImg);
			statement.setInt(2, id);

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
	public int updateMember(int id, String newPwd, String pwdChange) {
		String sql = "UPDATE MEMBER SET PASSWORD = ? WHERE ID = ?";

		int result = 0;
		
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setString(1, newPwd);
			statement.setInt(2, id);

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
	public Boolean isDuplicatedId(String nickname) {
		String sql = "SELECT * FROM MEMBER WHERE NICKNAME=?";
		
		String member = null;
		try {
			JdbcDaoContext daoContext = new JdbcDaoContext();
			PreparedStatement statement = daoContext.getPreparedStatement(sql);

			statement.setString(1, nickname);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				member = resultSet.getString("NICKNAME");
			}
			daoContext.close(resultSet, statement);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(member != null)
			return true;
		
		return false;
	}
/*
 * =======================================================================
 * ============================= for Test ================================
 * =======================================================================
 */
//	public static void main(String[] args) {
//		
//		JdbcMemberDao memberDao = new JdbcMemberDao();
//		Member member = new Member("test-5@test.com", "a", "123", 500);
//		
//		System.out.println("TEST---");
//		int result= memberDao.insertMember(member);
//		System.out.println(result);
//	}


}
