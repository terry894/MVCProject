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

import org.json.simple.JSONObject;

import com.stockmarket.www.controller.system.AppContext;
import com.stockmarket.www.entity.CurStock;
import com.stockmarket.www.service.AnalysisService;
import com.stockmarket.www.service.basic.BasicAnalysisService;

@WebServlet("/card/trade/analysis")
public class AnalysisController extends HttpServlet{
	AnalysisService service;
	
	public AnalysisController() {
		service = new BasicAnalysisService();
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//캡쳐 버튼 Press 시
		String capture = request.getParameter("capture"); 
		if(capture != null) {  
			HttpSession session = request.getSession();
			int memberId = (int)session.getAttribute("id");
			
			String codeNum = request.getParameter("codeNum");
			String result = service.captureDataCrawling(codeNum, memberId);
			
			PrintWriter out = response.getWriter();
			out.print(result);      
			return;
		}
		
		String codeNum = request.getParameter("codeNum");
		if(codeNum != null) {
			HashMap<Object, Object>map = new HashMap<>();
			Map<String, CurStock> stocks = AppContext.getStockMarket();
			CurStock curStock = null; 
			for(String getCodeNume : stocks.keySet()) {
				if(getCodeNume.equals(codeNum)) {
//					System.out.println(stocks.get(getCodeNume).toString()); //for debugging
					curStock = new CurStock(
							codeNum,
							stocks.get(getCodeNume).getPrice(),
							stocks.get(getCodeNume).getGain(), 
							stocks.get(getCodeNume).getGainPrice(), 
							stocks.get(getCodeNume).getSignMark(), 
							stocks.get(getCodeNume).getPercent());
					break;
				} 
			}
			
			if(curStock == null)
				curStock = new CurStock(codeNum, "[데이터 수집중...]", "보합", "0", "none", "0");
			
//			System.out.println(curStock.toString());  //for debugging
			map.put("name", service.getStockName(codeNum));
			map.put("price", curStock.getPrice());
			map.put("status", curStock.getGain()); // 상승, 보합, 하락
			map.put("gain", curStock.getGainPrice());
			map.put("ratio", curStock.getPercent());
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(new JSONObject(map));
			return;
		}
		
		request.getRequestDispatcher("analysis.jsp").forward(request, response);
	}
}
