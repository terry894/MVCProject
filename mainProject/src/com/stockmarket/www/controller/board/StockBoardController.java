package com.stockmarket.www.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stockmarket.www.service.CommunityBoardService;
import com.stockmarket.www.service.basic.BasicCommunityBoardService;

@WebServlet("/card/board/stock_board")
public class StockBoardController extends HttpServlet {
	private CommunityBoardService communityBoardService;
	public StockBoardController() {
		communityBoardService = new BasicCommunityBoardService();
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 세션을 이용하여 현재 사용자의 아이디를 가져온다.
		HttpSession session = request.getSession();
		Object tempId = session.getAttribute("id");
		int id = -1;

		if (tempId != null)
			id = (Integer) tempId;


		int page = 1;
		String field = "title";
		String query = "";
		String stockCode = "";

		String page_ = request.getParameter("p");
		if (page_ != null && !page_.equals(""))
			page = Integer.parseInt(page_);

		String field_ = request.getParameter("f");
		if (field_ != null && !field_.equals(""))
			field = field_;

		String query_ = request.getParameter("q");
		if (query_ != null && !query_.equals(""))
			query = query_;

		String stockCode_ = request.getParameter("s");
		if (stockCode_ != null && !stockCode_.equals(""))
			stockCode = stockCode_;

		request.setAttribute("CommunityBoard", communityBoardService.getCommunityBoardList(page, field, query, stockCode, id)); // 컨트롤러가 할 일은 데이터를 준비하는 일
		request.setAttribute("loginId", id);

		request.getRequestDispatcher("/card/board/stock_board.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		super.doPost(request, response);
	}
}
