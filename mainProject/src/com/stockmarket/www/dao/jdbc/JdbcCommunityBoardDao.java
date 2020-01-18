package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.stockmarket.www.dao.CommunityBoardDao;
import com.stockmarket.www.entity.CommunityBoard;

public class JdbcCommunityBoardDao implements CommunityBoardDao {

	@Override
	public List<CommunityBoard> getCommunityBoardList(int page, String field, String query, String stockCode, int loginId) {
		
		List<CommunityBoard> list = new ArrayList<>();

		String sql = "SELECT * FROM ("
						+ "SELECT ROWNUM NUM, B.* FROM("
							+ "SELECT * FROM ("
								+ "SELECT BV.*, ("
									+ "SELECT BOARD_ID FROM INTEREST_BOARD WHERE MEMBER_ID=? AND BOARD_ID = BV.ID"
								+ ") INTEREST FROM BOARD_VIEW BV"
							+ ") WHERE "+field+" LIKE ? AND STOCKCODE LIKE ? ORDER BY ID DESC"
						+ ") B"
					+ ") WHERE NUM BETWEEN ? AND ?";


		PreparedStatement pst = null;
		ResultSet rs = null;

		JdbcDaoContext daoContext = new JdbcDaoContext();
		try {

			pst = daoContext.getPreparedStatement(sql);

			pst.setInt(1, loginId);
			pst.setString(2, "%" + query + "%");
			pst.setString(3, "%" + stockCode + "%");
			pst.setInt(4, 1 + 10 * (page - 1));
			pst.setInt(5, 10 * page);

			rs = pst.executeQuery();
			while (rs.next()) {
				CommunityBoard communityBoard = new CommunityBoard(rs.getInt("ID"), rs.getString("TITLE"),
						rs.getString("WRITER_ID"), rs.getDate("REGDATE"), rs.getInt("HIT"), rs.getString("STOCKNAME"),
						rs.getInt("REPLY_CNT"), rs.getInt("INTEREST"));
				list.add(communityBoard);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(rs, pst);
		}
		return list;
	}

	@Override
	public int getReplyCnt(String field, String query, String stockName) {

		int count = 0;

		String sql = "SELECT REPLY_CNT FROM BOARD_VIEW WHERE " + field + " LIKE ?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();
		try {

			pst = daoContext.getPreparedStatement(sql);

			pst.setString(1, "%" + query + "%");

			rs = pst.executeQuery();

			if (rs.next()) {
				count = rs.getInt("COUNT");
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(rs, pst);
		}
		return count;
	}

	@Override
	public CommunityBoard getCommunityBoardDetail(int id) {

		String sql = "SELECT b.ID ID, B.TITLE TITLE, B.WRITER_ID WRITER_ID, B.REGDATE REGDATE, B.CONTENT CONTENT, B.HIT HIT , S.COMPANYNAME STOCKNAME FROM BOARD B LEFT OUTER JOIN KOREASTOCKS S ON b.STOCKCODE = s.STOCKCODE WHERE ID=?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		CommunityBoard communityBoard = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();
		try {
			pst = daoContext.getPreparedStatement(sql);
			pst.setInt(1, id);

			rs = pst.executeQuery();
			rs.next();
			communityBoard = new CommunityBoard(rs.getInt("ID"), rs.getString("TITLE"), rs.getString("WRITER_ID"),
					rs.getDate("REGDATE"), rs.getInt("HIT"), rs.getString("CONTENT"), rs.getString("STOCKNAME"));

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(rs, pst);
		}
		return communityBoard;
	}

	@Override
	public List<CommunityBoard> getReplyList(int boardId) {

		List<CommunityBoard> list = new ArrayList<>();

		String sql = "SELECT * FROM REPLY WHERE BOARD_ID=? ORDER BY REGDATE DESC";
		PreparedStatement pst = null;
		ResultSet rs = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();
		try {

			pst = daoContext.getPreparedStatement(sql);

			pst.setInt(1, boardId);

			rs = pst.executeQuery();
			while (rs.next()) {
				CommunityBoard communityBoard = new CommunityBoard(rs.getInt("ID"), rs.getString("RE_CONTENT"),
						rs.getString("WRITER_ID"), rs.getDate("REGDATE"), rs.getInt("BOARD_ID"));
				list.add(communityBoard);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(rs, pst);
		}
		return list;
	}

	@Override
	public int lastReplyNum(int boardId) {
		int returnReplyNum = 0;
		String sql = "select id from (select rownum num, r.* from (select * from reply order by id desc) r) where num like '1'";

		PreparedStatement pst = null;
		ResultSet rs = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				returnReplyNum = rs.getInt("ID");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return returnReplyNum;
	}

	@Override
	public int insertCommunityBoard(CommunityBoard communityBoard) {
		int result = 0;
		String sql = "INSERT INTO BOARD (ID, TITLE, WRITER_ID, CONTENT, STOCKCODE) "
				+ "VALUES ((SELECT NVL(MAX(ID),0)+1 FROM BOARD), ?, ?, ?, ?)";

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);

			pst.setString(1, communityBoard.getTitle());
			pst.setString(2, communityBoard.getWriterId());
			pst.setString(3, communityBoard.getContent());
			pst.setString(4, communityBoard.getStockCode());

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int updateCommunityBoard(CommunityBoard updateCommunityBoard) {
		int result = 0;
		String sql = "UPDATE BOARD SET TITLE=?, CONTENT=?, HIT=? WHERE ID=?";

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);

			pst.setString(1, updateCommunityBoard.getTitle());
			pst.setString(2, updateCommunityBoard.getContent());
			pst.setInt(3, updateCommunityBoard.getHit());
			pst.setInt(4, updateCommunityBoard.getId());

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int deleteCommunityBoard(int boardId) {
		int result = 0;
		String sql = "DELETE FROM BOARD WHERE ID = " + boardId;

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);
			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int insertReply(CommunityBoard insertReply) {
		int result = 0;
		String sql = "INSERT INTO REPLY (ID, RE_CONTENT, WRITER_ID, REGDATE, BOARD_ID) "
				+ "VALUES ((SELECT NVL(MAX(ID),0)+1 FROM REPLY), ?, ?, SYSTIMESTAMP, ?)";
		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {

			pst = daoContext.getPreparedStatement(sql);

			pst.setString(1, insertReply.getReContent());
			pst.setString(2, insertReply.getWriterId());
			pst.setInt(3, insertReply.getId());

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int updateReply(CommunityBoard updateReply) {
		int result = 0;
		String sql = "UPDATE REPLY SET RE_CONTENT=?, REGDATE=SYSTIMESTAMP WHERE ID=?";

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);

			pst.setString(1, updateReply.getReContent());
			pst.setInt(2, updateReply.getReplyId());

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int deleteReply(int replyId) {
		int result = 0;
		String sql = "DELETE FROM REPLY WHERE ID = " + replyId;

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);
			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int deleteReplys(int boardId) {
		int result = 0;
		String sql = "DELETE FROM REPLY WHERE BOARD_ID = " + boardId;

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);
			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int selectInterestBoard(CommunityBoard selectInterestBoard) {
		int result = 0;
		String sql = "SELECT * FROM INTEREST_BOARD WHERE MEMBER_ID=? AND BOARD_ID=?";

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);

			pst.setInt(1, selectInterestBoard.getId());
			pst.setInt(2, selectInterestBoard.getLoginId());

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int insertInterestBoard(CommunityBoard insertInterestBoard) {
		int result = 0;
		String sql = "INSERT INTO INTEREST_BOARD (MEMBER_ID, BOARD_ID)" + 
				"VALUES (?, ?)";

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);

			pst.setInt(1, insertInterestBoard.getLoginId());
			pst.setInt(2, insertInterestBoard.getId());

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	@Override
	public int deleteInterestBoard(CommunityBoard deleteInterestBoard) {
		int result = 0;
		String sql = "DELETE FROM INTEREST_BOARD WHERE MEMBER_ID=? AND BOARD_ID=?";

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);

			pst.setInt(1, deleteInterestBoard.getLoginId());
			pst.setInt(2, deleteInterestBoard.getId());

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

	public static void main(String[] args) {
		JdbcCommunityBoardDao com = new JdbcCommunityBoardDao();
		CommunityBoard lastReplyNum = new CommunityBoard();

		com.lastReplyNum(1);
	}

	@Override
	public int deleteInterestBoards(int boardId) {
		int result = 0;
		String sql = "DELETE FROM INTEREST_BOARD WHERE BOARD_ID=?";

		PreparedStatement pst = null;
		JdbcDaoContext daoContext = new JdbcDaoContext();

		try {
			pst = daoContext.getPreparedStatement(sql);
			pst.setInt(1, boardId);

			result = pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			daoContext.close(pst);
		}
		return result;
	}

}
