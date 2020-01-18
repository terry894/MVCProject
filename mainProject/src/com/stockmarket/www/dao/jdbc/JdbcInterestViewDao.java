package com.stockmarket.www.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.stockmarket.www.controller.system.AppContext;
import com.stockmarket.www.dao.InterestViewDao;
import com.stockmarket.www.dao.StockDao;
import com.stockmarket.www.entity.CurStock;
import com.stockmarket.www.entity.InterestView;

public class JdbcInterestViewDao implements InterestViewDao {

	@Override
	public List<InterestView> getInterestStockList(int id) {
		JdbcDaoContext daoContext = new JdbcDaoContext();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM INTEREST_STOCK_VIEW WHERE ID = ?";

		List<InterestView> interestlist = new ArrayList<>();

		Map<String, CurStock> map = new HashMap<String, CurStock>();

//		List<CurStock> list = new ArrayList<>();
//		list.add(new CurStock("035420", "3,000", "상승", "3,000", "+", "2.5"));
//		list.add(new CurStock("000660", "5,000", "하강", "3,000", "-", "3.4"));
//		list.add(new CurStock("020560", "6,000", "보합", "3,000", "0.0", "1.5"));
//		list.add(new CurStock("005930", "2,000", "상승", "3,000", "+", "1.6"));
//		list.add(new CurStock("005380", "1,000", "상승", "3,000", "+", "8.9"));
//		list.add(new CurStock("095660", "10,500", "상승", "3,000", "+", "10.2"));
//		list.add(new CurStock("217500", "3,500", "하강", "3,000", "-", "14.2"));
//		list.add(new CurStock("215600", "7,000", "하강", "3,000", "-", "10"));

//		
		if (AppContext.getStockMarket() != null)
			map.putAll(AppContext.getStockMarket());

		try {
			st = daoContext.getPreparedStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			while (rs.next()) {
				String stockName = rs.getString("STOCKNAME");
				String stockId = rs.getString("STOCKCODE");

				for (Entry<String, CurStock> data : map.entrySet()) {

					if (stockId.equals(data.getValue().getCodeNum())) {
						String price = data.getValue().getPrice();
						String gain = data.getValue().getGain();
						String percent = data.getValue().getPercent();
						InterestView interestview = new InterestView(stockName, price, percent, gain);
						interestlist.add(interestview);
						break;
					}
				}
			}
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		} // oracle.jdbc.driver.OracleDriver 객체를 만듬
		catch (SQLException e) {
//			e.printStackTrace();
		} finally {
			daoContext.close(rs, st);
		}
		return interestlist;
	}
}
