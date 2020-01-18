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

@WebServlet("/card/capturememo/captureMemo-json-update")
public class CaptureMemoUpdateJSONController extends HttpServlet {
	private CaptureMemoService service;

	public CaptureMemoUpdateJSONController() {
		service = new BasicCaptureMemoService();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// data를 받기 위한 버퍼
		StringBuffer jb = new StringBuffer();
		String line = null;
		
		// data를 받아서 버퍼에 저장
		BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	      jb.append(line);
	    
	    // json 형태의 data 파싱
	    Gson gson = new Gson();
	    CaptureMemo memo = gson.fromJson(jb.toString(), CaptureMemo.class);
	    
	    int result = service.update(memo);
	    
	    response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
	    
	    PrintWriter out = response.getWriter();
		out.print(result);
	}
}