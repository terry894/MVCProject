package com.stockmarket.www.controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stockmarket.www.dao.MemberDao;
import com.stockmarket.www.dao.jdbc.JdbcMemberDao;
import com.stockmarket.www.entity.CommunityBoard;
import com.stockmarket.www.service.CommunityBoardService;
import com.stockmarket.www.service.basic.BasicCommunityBoardService;

@WebServlet("/card/board/detail")
public class DetailJsonController extends HttpServlet {

	private CommunityBoardService communityBoardService;

	public DetailJsonController() {
		communityBoardService = new BasicCommunityBoardService();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Object tempId = session.getAttribute("id");
		int writerId = -1;

		if (tempId != null)
			writerId = (Integer) tempId;
		MemberDao memberDao = new JdbcMemberDao();
		String writerNickname = memberDao.getMember(writerId).getNickName();

		int boardId = Integer.parseInt(request.getParameter("id"));
		CommunityBoard communityBoard = communityBoardService.getBoard(boardId);
		List<CommunityBoard> replyList = communityBoardService.getCommunityBoardReplyList(boardId);
		communityBoard.setHit(communityBoard.getHit()+1);
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("loginUser", writerNickname);
		hm.put("board", communityBoard);
		hm.put("replys", replyList);
		
		int result = communityBoardService.updateCommunityBoard(communityBoard);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String Json = gson.toJson(hm);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(Json);

//			
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
