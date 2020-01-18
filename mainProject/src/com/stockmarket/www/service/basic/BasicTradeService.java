package com.stockmarket.www.service.basic;

import java.util.List;

import com.stockmarket.www.dao.HaveStockDao;
import com.stockmarket.www.dao.MemberDao;
import com.stockmarket.www.dao.StockDetailDao;
import com.stockmarket.www.dao.KoreaStocksDao;
import com.stockmarket.www.dao.jdbc.JdbcHaveStockDao;
import com.stockmarket.www.dao.jdbc.JdbcMemberDao;
import com.stockmarket.www.dao.jdbc.JdbcStockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbckoreaStocksDao;
import com.stockmarket.www.entity.HaveStock;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.entity.StockDetail;
import com.stockmarket.www.service.TradeService;

public class BasicTradeService implements TradeService{
	MemberDao memberDao;
	HaveStockDao stockDao;
	StockDetailDao stockDetailDao;
	
	public BasicTradeService() {
		memberDao = new JdbcMemberDao();
		stockDao = new JdbcHaveStockDao();
		stockDetailDao = new JdbcStockDetailDao();
	}

	@Override
	public long getAssets(int id) {
		Member member = memberDao.getMember(id);
		if(member == null)
			return 0;
		
		return member.getvMoney();
	}

	@Override
	public int getStockAssets(int id, String stockId) {
		HaveStock haveStock = stockDao.get(id, stockId);
		if(haveStock == null)
			return 0;
		
		return haveStock.getSum();
	}
	
	@Override
	public int getQty(int id, String stockId) {
		HaveStock haveStock = stockDao.get(id, stockId);
		if(haveStock == null)
			return 0;
		
		return haveStock.getQuantity();
	}
	
	@Override
	public void setQty(int id, String stockId, int qty, int curPrice) {
		JdbcHaveStockDao stockDao = new JdbcHaveStockDao();
		
		HaveStock haveStock = stockDao.get(id, stockId);
			
		Member member = memberDao.getMember(id);
		memberDao.updateMember(id, member.getvMoney() + (-qty * curPrice));
		haveStock.setQuantity(haveStock.getQuantity() + qty);
		haveStock.setSum(haveStock.getSum() + qty * curPrice);
		stockDao.update(haveStock);
	}
	

	@Override
	public int checkVmoney(int id, int qty, int curStockPrice) {
		Member member = memberDao.getMember(id);
		int buyMoney = qty * curStockPrice;
		
		if(member.getvMoney() < buyMoney)
			return 1; //vmoney 부족
		
		return 0;
	}
	
	@Override
	public boolean checkHaveStock(int id, String codeNum) {
		HaveStock haveStock = stockDao.get(id, codeNum);
		if(haveStock == null)
			return false;
		
		return true;
	}
	
	@Override
	public boolean checkMinusHaveStock(int id, String codeNum, int qty) {
		HaveStock haveStock = stockDao.get(id, codeNum);
		//마이나스 수량 체크
		if(haveStock.getQuantity() - qty < 0) 
			return true;
		
		return false;
	}
	
	@Override
	public boolean checkZeroHaveStock(int id, String codeNum) {
		HaveStock haveStock = stockDao.get(id, codeNum);
		//Zero 수량 체크
		if(haveStock.getQuantity() == 0) 
			return true;
		
		return false;
	}
	
	
	@Override
	public void addHaveStock(int id, String codeNum) {
		HaveStock haveStock = new HaveStock(id, codeNum, 0, 0);
		stockDao.insert(haveStock);
	};
	
	@Override
	public void delHaveStock(int memberId, String codeNum) {
		stockDao.delete(memberId, codeNum);
	}
	
	@Override
	public void trade(int id, String codeNum, int qty, int curStockPrice) {
		Member member = memberDao.getMember(id);
		HaveStock haveStock = stockDao.get(id, codeNum);
		
		//회원의 가상머니 수정
		memberDao.updateMember(id, member.getvMoney() + (-qty * curStockPrice));
		//보유종목에 대한 수량과 sum 수정
		haveStock.setQuantity(haveStock.getQuantity() + qty);
		haveStock.setSum(haveStock.getSum() + qty * curStockPrice);
		stockDao.update(haveStock);
	}
	
	@Override
	public String getCompanyName(String codeNum) {
		KoreaStocksDao koreaStockDao = new JdbckoreaStocksDao();
		
		return koreaStockDao.get(codeNum).getCompanyName();
	}
	
	@Override
	public List<StockDetail> getDailyPrice(String codeNum) {
		return stockDetailDao.get(codeNum);
	}
	
/////////////////////////////////////////////////////////
////////////////////////// TEST /////////////////////////
/////////////////////////////////////////////////////////
	public static void main(String[] args) {
/*		
		BasicTradeService service = new BasicTradeService();
		
		
		//check trade buy
		service.tradeBuy(2, "215600", 4, 20000);

		//가상머니 체크 Test
		for(int i = 0; i < 10; i++)
			System.out.println(service.checkVmoney(14, i, 300000));

		//HaveStock check
		System.out.println(service.checkHaveStock(2, "095660"));
		System.out.println(service.checkHaveStock(2, "005380"));
		System.out.println(service.checkHaveStock(2, "035420"));
		
		//check add HaveStock
		service.addHaveStock(2, "215600", 3, 10000);
*/		
		
	}

}
