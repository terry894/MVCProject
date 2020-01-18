package com.stockmarket.www.controller.managestocks;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.stockmarket.www.entity.HaveStockView;
import com.stockmarket.www.service.HoldingStocksService;
import com.stockmarket.www.service.basic.BasicHoldingStocksService;

@WebServlet("/card/managestocks/holdinglist-json")
public class HoldingStockJSONController extends HttpServlet{
	
	
	private static final long serialVersionUID = -1822211939058642758L;
	private HoldingStocksService holdingStocksInterface;

	public HoldingStockJSONController(){
		holdingStocksInterface = new BasicHoldingStocksService();
	}
//	
//	@Override
//	public void init() throws ServletException {
//		super.init();
//	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

		HttpSession session = request.getSession();
		int userId = (int)session.getAttribute("id");
//		System.out.println("holding");
	
		updateCurrentPrice(request,response,userId);
		
	}
	
	private void updateCurrentPrice(HttpServletRequest request,HttpServletResponse response , int userId) throws IOException {
		
		if(holdingStocksInterface.getInterestHoldingList(userId).isEmpty()) {
	   
        Gson gson = new Gson();
		String json = gson.toJson(-1);
        PrintWriter out = response.getWriter();
		out.write(json);
		}
		else{
			List<HaveStockView> list = new ArrayList<HaveStockView>();
			list = holdingStocksInterface.getInterestHoldingList(userId);
	        Gson gson = new Gson();
			String json = gson.toJson(list);
	        PrintWriter out = response.getWriter();
			out.write(json);
		}
	}
}
