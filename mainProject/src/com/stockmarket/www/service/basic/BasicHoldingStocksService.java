package com.stockmarket.www.service.basic;

import java.util.ArrayList;
import java.util.List;

import com.stockmarket.www.dao.HaveStockDao;
import com.stockmarket.www.dao.jdbc.JdbcHaveStockDao;
import com.stockmarket.www.entity.CurStock;
import com.stockmarket.www.entity.HaveStockView;
import com.stockmarket.www.service.HoldingStocksService;

public class BasicHoldingStocksService implements HoldingStocksService {

	HaveStockDao jdbcHaveStockDao;
	HaveStockView haveStockViewCopy;

	public BasicHoldingStocksService() {
		// TODO Auto-generated constructor stub
		jdbcHaveStockDao = new JdbcHaveStockDao();
		haveStockViewCopy = new HaveStockView();
	}


	private HaveStockView getProfitByStockId(HaveStockView data) {
		int income = 10;
//		int quantity = jdbcHaveStockDao.getView(memberId, stockId).getQuantity();
//		int sum = jdbcHaveStockDao.getView(memberId, stockId).getSum();
//		int presentValue = Integer.parseInt(jdbcHaveStockDao.getView(memberId, stockId).getPrice().replaceAll(",", ""));
		int sum = data.getSum();
		int quantity = data.getQuantity();
		int intPrice = Integer.parseInt(data.getPrice().replaceAll(",", ""));

		income = (quantity * intPrice) - sum;
		data.setIncome(income);
		return data;
	}

	@Override
	public List<HaveStockView> getInterestHoldingList(int userId) {
		
		List<HaveStockView> list = jdbcHaveStockDao.getList(userId);
		List<HaveStockView> data = new ArrayList<>();
//		for (HaveStockView data : list) {
//			haveStockViewCopy = getProfitByStockId(data);
//			
//		}
		for(int i = 0; i < list.size(); i++) {
			data.add(getProfitByStockId(list.get(i)));
		}
		
		return data;
	}

//	public static void main(String[] args) {
//		
//		HaveStockDao jdbcHaveStockDao = new JdbcHaveStockDao();
//		List<HaveStockView> list = jdbcHaveStockDao.getList(3);
//		
//		for(HaveStockView data : list) {
//			String StockName = data.getStockName();
//			int quantity = data.getQuantity();
//			
//		    int price = Integer.parseInt(data.getPrice().replaceAll(",", ""));
// 
//		    System.out.println(StockName);
//	    }
//	}
}
