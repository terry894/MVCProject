package com.stockmarket.www.controller.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.RemoteEndpoint.Basic;

import com.stockmarket.www.controller.system.AppContext;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.service.basic.BasicLoginService;
import com.stockmarket.www.service.basic.BasicSystemService;

//TODO
//제재 회원 처리
//session 시간이 지날경우 화면상에 로그아웃 표시 또는 팝업창을 구현해야한다 (session 만료처리) 


@WebServlet("/login")
public class loginController extends HttpServlet{
	
	BasicLoginService loginService;
	
	public loginController() {
		loginService = new BasicLoginService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String logout = request.getParameter("loginStatus");

		if(logout == null) 
			response.sendRedirect("/main.jsp");
		
		//로그아웃 시도
		if(logout.equals("logout")) {
			if(request.isRequestedSessionIdValid()) {
				session.invalidate();
				response.sendRedirect("/main.jsp");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String form = request.getParameter("form");
				
		//회원가입
		if(form.equals("회원가입"))
			memberSignup(request, response);
		
		//로그인 시도
		//회원가입상태를 체크
		if(form.equals("로그인"))
			memberTryLogin(request, response);
		
		response.sendRedirect("/main.jsp");
	}

	private void memberSignup(HttpServletRequest request, HttpServletResponse response) {
		String userEmail = request.getParameter("userEmail");
		String pwd = request.getParameter("pwd");
		String nickName = request.getParameter("nickName");
		int vMoney = 1000000; //default 100만원
		
		Member member = new Member(userEmail, nickName, pwd, vMoney);
		int result = loginService.signUpMember(member);
		if(result == 0) 
			AppContext.setLog("회원가입 처리 오류", loginController.class.getName());
	}

	private void memberTryLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String userEmail = request.getParameter("userEmail");
		String pwd = request.getParameter("pwd");
		
		if(isValidLogInfo(userEmail, pwd)) {
			Member member = loginService.getMember(userEmail);
			if(member == null) {
				AppContext.setLog("LOGIN 오류", loginController.class.getName());
				return;
			}
			
			//id 값을 session 에 저장한다
			if(member.getId() != 0) {
				session.setAttribute("id", member.getId());
				session.setAttribute("nickName", member.getNickName());
			}
		} 
	}

	private boolean isValidLogInfo(String email, String pwd) {
		if(email == null || email.equals("")) 
			return false;

		if(loginService.isValidMember(email, pwd)) 
			return true;
		else 
			return false;
	}
}
