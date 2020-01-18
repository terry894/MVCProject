package com.stockmarket.www.controller.list;

import java.io.IOException;
import java.io.PrintWriter;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stockmarket.www.entity.CommunityBoard;
import com.stockmarket.www.service.CompanyService;
import com.stockmarket.www.service.basic.BasicCompanyService;

@WebServlet("/card/company/list-json")
public class ListControllerJSONController extends HttpServlet{
	private CompanyService companyService;
	
	public ListControllerJSONController() {
		companyService = new BasicCompanyService();
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
		
		//System.out.println("불러지니?");
		
		HttpSession session = request.getSession();
		Object tempId = session.getAttribute("id");
		int id = -1;
		
		if(tempId != null)
			id = (Integer)tempId;
		
		String attention = request.getParameter("attention");
		//System.out.println(attention);
		String status = request.getParameter("status");
		
		//System.out.println("status : " + status);
		
		if (status.equals("insert")) {
			int result  = 0;
			result =companyService.insertInterest(id, attention);
			
			//System.out.println("즐겨찾기 추가" + result);
			//System.out.println("attention : " +attention );
			
		} else if (status.equals("delete")) {
			int result =0;
			
			result = companyService.deleteInterest(id, attention);
			//System.out.println("즐겨찾기 삭제 " + result);
			//System.out.println("attention : " + attention );
		};
	}
	
	
	
}
