package com.stockmarket.www.controller.captureMemo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stockmarket.www.service.CaptureMemoService;
import com.stockmarket.www.service.basic.BasicCaptureMemoService;

@WebServlet("/card/capturememo/captureMemo-delete-json")
public class CaptureMemoDeleteJSONController extends HttpServlet {
	private CaptureMemoService service;

	public CaptureMemoDeleteJSONController() {
		service = new BasicCaptureMemoService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String memoId = request.getParameter("memoId");
		
		int result = service.delete(Integer.parseInt(memoId));
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
	    
	    PrintWriter out = response.getWriter();
		out.print(result);
	}
}