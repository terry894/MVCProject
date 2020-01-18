package com.stockmarket.www.controller.trade;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.stockmarket.www.controller.system.AppContext;
import com.stockmarket.www.dao.KoreaStocksDao;
import com.stockmarket.www.dao.jdbc.JdbckoreaStocksDao;
import com.stockmarket.www.entity.CurStock;
import com.stockmarket.www.entity.StockDetail;
import com.stockmarket.www.entity.TradeCardData;
import com.stockmarket.www.service.TradeService;
import com.stockmarket.www.service.basic.BasicTradeService;

@WebServlet("/card/trade/trade")
public class TradeController extends HttpServlet{
	TradeService service;

	public TradeController() {
		service = new BasicTradeService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int memberId = (int)session.getAttribute("id");
		
		//매수-매도 실행 
		String qty = request.getParameter("qty");
		if(qty != null) { 
			tradeProcess(memberId, request);
			JSONObject json = update(memberId, request);
			PrintWriter out = response.getWriter();
			out.print(json);
			return;
		}

		//for update
		String codeNum = request.getParameter("codeNum");
		if(codeNum != null) {
			Map<String, CurStock> stocks = AppContext.getStockMarket();
			CurStock curStock = null; 
			TradeCardData data = null;
			
			//크롤링 데이터가 유효한 경우 curStock 을 set 한다
			for(String getCodeNume : stocks.keySet()) {
				if(getCodeNume.equals(codeNum)) {
					curStock = new CurStock(); 
					curStock.setSellQuantityMap(stocks.get(getCodeNume).getSellQuantityMap());
					curStock.setBuyQuantityMap(stocks.get(getCodeNume).getBuyQuantityMap());
					break;
				}
			}

			//크롤링 데이터가 없는 경우, 가상머니와 보유수량만 반환한다
			if(curStock == null) {
				data = new TradeCardData();
				data.setvMoney(service.getAssets(memberId));
				data.setQuantity(service.getQty(memberId, codeNum));
			}
				
			//매도,매수 나우어 반환한다
			if(curStock != null) {
				data = new TradeCardData(
						curStock.getSellQuantityMap().size(), curStock.getBuyQuantityMap().size());
				data.setvMoney(service.getAssets(memberId));
				data.setQuantity(service.getQty(memberId, codeNum));
				int i = 0;
				for(Integer sell : curStock.getSellQuantityMap().keySet()) {
					data.setSellPrice(i, sell);
					data.setSellQuantity(i, curStock.getSellQuantityMap().get(sell));
					i++;
				}
				i = 0;
				for(Integer buy : curStock.getBuyQuantityMap().keySet()) {
					data.setBuyPrice(i, buy);
					data.setBuyQuantity(i, curStock.getBuyQuantityMap().get(buy));
					i++;
				}
//				System.out.println(data.toString());
			}
			
			Gson gson = new Gson();
			String json = gson.toJson(data);
			PrintWriter out = response.getWriter();
			out.print(json);
			return;
		}
		
		request.getRequestDispatcher("trading.jsp").forward(request, response);
	}

/////////////////////////////////////////////////////////
/////////////////// 매수 - 매도 관련 함수 /////////////////////
/////////////////////////////////////////////////////////
	
private void tradeProcess(int memberId, HttpServletRequest request) {
	String qty = request.getParameter("qty");
	String codeNum = request.getParameter("codeNum");
	String price = request.getParameter("price");
	
	if(service.checkHaveStock(memberId, codeNum) == false)
		service.addHaveStock(memberId, codeNum);	//DB 추가
	
	service.trade(memberId, codeNum, Integer.parseInt(qty), Integer.parseInt(price));
	
	if(service.checkZeroHaveStock(memberId, codeNum)) 
		service.delHaveStock(memberId, codeNum);	//DB 삭제
}

	

	private JSONObject update(int memberId,  HttpServletRequest request) {
		HashMap<Object, Object>map = new HashMap<>();
		String codeNum = request.getParameter("codeNum");
		int qty = service.getQty(memberId, codeNum);
		
		map.put("qty", qty);
		map.put("vMoney", (int) service.getAssets(memberId));
		JSONObject data = new JSONObject(map);
		return data;
	}
}
