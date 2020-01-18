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
import com.stockmarket.www.entity.InterestView;
import com.stockmarket.www.service.InterestStocksService;
import com.stockmarket.www.service.InterestViewService;
import com.stockmarket.www.service.basic.BasicInterestStocksService;
import com.stockmarket.www.service.basic.BasicInterestViewService;

@WebServlet("/card/managestocks/interestlist-json")
public class InterestStockJSONController extends HttpServlet{
	
	
	private static final long serialVersionUID = -794157807520021418L;
	private InterestStocksService interestStocksInterface;
	private InterestViewService interestViewInterface;
	
	
	public InterestStockJSONController() {
		interestStocksInterface =
				new BasicInterestStocksService();
		interestViewInterface = new BasicInterestViewService();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
		HttpSession session = request.getSession();
		
	
		int userId = (int)session.getAttribute("id");
	    updateCurrentPrice(request,response,userId);
		
	}
	
	private void updateCurrentPrice(HttpServletRequest request,HttpServletResponse response , int userId) throws IOException {
		
		if(interestViewInterface.getInterestViewList(userId).isEmpty()) {
		    Gson gson = new Gson();
			String json = gson.toJson(-1);
	        PrintWriter out = response.getWriter();
	        out.write(json);
		}
		else {
		List<InterestView> interestlist = new ArrayList<InterestView>();
		interestlist = interestViewInterface.getInterestViewList(userId);
        Gson interestGson = new Gson();
		String interestJson = interestGson.toJson(interestlist);
        PrintWriter out = response.getWriter();
		out.write(interestJson);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
		
        HttpSession session = request.getSession();
        
		int userId = (int)session.getAttribute("id");
		String delStockName = request.getParameter("delStockName");

		int result = interestStocksInterface.deleteStock(userId,delStockName);	
        PrintWriter out = response.getWriter();
		out.write(result);
	}
	

}
