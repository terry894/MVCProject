package com.stockmarket.www.controller.captureMemo;

import java.io.BufferedReader;
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
import com.google.gson.GsonBuilder;
import com.stockmarket.www.entity.CaptureMemo;
import com.stockmarket.www.entity.CaptureMemoView;
import com.stockmarket.www.service.CaptureMemoService;
import com.stockmarket.www.service.basic.BasicCaptureMemoService;

@WebServlet("/card/capturememo/captureMemo-detail-json")
public class CaptureMemoDetailJSONController extends HttpServlet {
	private CaptureMemoService service;

	public CaptureMemoDetailJSONController() {
		service = new BasicCaptureMemoService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String temp = request.getParameter("memoId");
		
		if(!temp.equals("undefined"));
			int id = Integer.parseInt(temp);

		CaptureMemo captureMemo = service.get(id);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String json = gson.toJson(captureMemo);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
	    
	    PrintWriter out = response.getWriter();
		out.print(json);
	}
}