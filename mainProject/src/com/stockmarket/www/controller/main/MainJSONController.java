package com.stockmarket.www.controller.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.service.MainService;
import com.stockmarket.www.service.SystemService;
import com.stockmarket.www.service.basic.BasicMainService;
import com.stockmarket.www.service.basic.BasicSystemService;

@WebServlet("/main-json")
public class MainJSONController extends HttpServlet {
	MainService service;
	
	public MainJSONController() {
		service = new BasicMainService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object tempId = session.getAttribute("id");
		
		int id = -1;
		
		if(tempId != null)
			id = (Integer)tempId;
		
		Member member = service.getMember(id);
		
		Gson gson = new Gson();
		String json;
		if(id != -1) {
			json = gson.toJson(member.getCardPos());
		} else {
			String defultPos = "1,2,3,4,5,6,0,0,7";
			json = gson.toJson(defultPos);
		}
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.print(json);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object tempId = session.getAttribute("id");
		
		int id = -1;
		
		if(tempId != null)
			id = (Integer)tempId;
		
		String cardPos = request.getParameter("cardPos");
		cardPos = cardPos.replaceAll("[\\[|\\]|\"]", "");
		
		int result = service.updateCardPos(id, cardPos);
		
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.print(result);
	}
}
