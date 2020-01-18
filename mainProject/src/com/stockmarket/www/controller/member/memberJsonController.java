package com.stockmarket.www.controller.member;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stockmarket.www.dao.MemberDao;
import com.stockmarket.www.dao.jdbc.JdbcMemberDao;
import com.stockmarket.www.entity.Member;
import com.stockmarket.www.service.MemberService;
import com.stockmarket.www.service.basic.BasicMemberService;

@WebServlet("/member-profile")
public class memberJsonController extends HttpServlet {

	private MemberService memberService;

	public memberJsonController() {
		memberService = new BasicMemberService();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//닉네임 중복확인
		String nickname = request.getParameter("nickname");
		Boolean result = memberService.isDuplicatedId(nickname);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String loginNickname = request.getParameter("loginNickname");
		String profileImg = request.getParameter("profileImg");
		String currentPwd = request.getParameter("currentPwd");
		String newPwd = request.getParameter("newPwd");
		
		Object tempId = session.getAttribute("id");
		
		int id = -1;

		if (tempId != null)
			id = (Integer) tempId;
		
		if (loginNickname != null) {

			MemberDao memberDao = new JdbcMemberDao();
			int ProfileImgNum = memberDao.getMember(id).getProfileImg();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(ProfileImgNum);
			// 프로필이미지값이 널이면 비밀번호 수정
		} else if (profileImg == null) {

			MemberDao memberDao = new JdbcMemberDao();
			String serverPwd = memberDao.getMember(id).getPassword();
			
			// 사용자가 입력한 현재 비밀번호와  서버의 비밀번호가 같지 않으면
			if(!currentPwd.equals(serverPwd)) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("wrong");
				
			//사용자가 입력한 새 비밀번호와 서버의 비밀번호가 같으면
			} else if(newPwd.equals(serverPwd)) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("same");
				
			//사용자가 입력한 현재 비밀번호와 서버의 비밀번호가 같으면
			} else if(currentPwd.equals(serverPwd)) {
				int result = memberService.updateMember(id, newPwd, "pwdChange");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print(result);
			}

			// 프로필이미지값이 들어있으면 프로필 이미지 수정
		} else if (profileImg != null) {

			int profileImg_ = Integer.parseInt(profileImg);
			int result = memberService.updateMember(id, profileImg_, "imgChange");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
