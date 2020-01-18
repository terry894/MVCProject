package com.stockmarket.www.service.basic;

import java.util.List;

import com.stockmarket.www.dao.InterestStocksDao;
import com.stockmarket.www.dao.StockDao;
import com.stockmarket.www.dao.jdbc.JdbcInterestStocksDao;
import com.stockmarket.www.dao.jdbc.JdbcStockDao;
import com.stockmarket.www.entity.InterestStocks;
import com.stockmarket.www.service.InterestStocksService;

public class BasicInterestStocksService implements InterestStocksService {

	
	InterestStocksDao interestStocksDao ;
	StockDao stockDao;
	
	public BasicInterestStocksService() {
		interestStocksDao = new JdbcInterestStocksDao();
		stockDao = new JdbcStockDao();
	}
	
	
	@Override
	public List<InterestStocks> getInterestStockList() {
		
		return interestStocksDao.getInterestStockList();
	}

//	@Override
//	public List<InterestStocks> getInterestStockList(String stockName, int currentPrice, int quentity) {
//		
//		return null;
//	}

	@Override
	public int deleteStock(int userId, String delStockName) {
		
		return interestStocksDao.delete(userId,stockDao.getStockCodeNum(delStockName));
		
	}

}
