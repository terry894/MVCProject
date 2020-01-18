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

@WebServlet("/card/board/community_board_list")
public class CommunityBoardJsonController extends HttpServlet {

	private CommunityBoardService communityBoardService;

	public CommunityBoardJsonController() {
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
		//로그인 세션을 불러온다.
		Object tempId = session.getAttribute("id");
		int loginId = -1;

		if (tempId != null)
			loginId = (Integer) tempId;
		MemberDao memberDao = new JdbcMemberDao();
		String loginUser = memberDao.getMember(loginId).getNickName();

		//게시글목록을 불러온다.
		int page = 1;
		String field = "TITLE";
		String query = "";
		String stockName = "";

		String page_ = request.getParameter("p");
		if (page_ != null && !page_.equals(""))
			page = Integer.parseInt(page_);

		String field_ = request.getParameter("f");
		if (field_ != null && !field_.equals(""))
			field = field_;

		String query_ = request.getParameter("q");
		if (query_ != null && !query_.equals(""))
			query = query_;

		if(query.equals("my"))
			query = loginUser;

		String stockName_ = request.getParameter("s");
		if (stockName_ != null && !stockName_.equals(""))
			stockName = stockName_;
		List<CommunityBoard> list = communityBoardService.getCommunityBoardList(page, field, query, stockName,loginId);

		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("loginUser", loginUser);
		hm.put("list", list);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
		String json = gson.toJson(hm);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(json);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		super.doPost(request, response);
	}
}
