package com.stockmarket.www.controller.board;

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
import com.stockmarket.www.entity.CommunityBoard;
import com.stockmarket.www.service.CommunityBoardService;
import com.stockmarket.www.service.basic.BasicCommunityBoardService;

@WebServlet("/card/board/stock_reg_board")
public class RegBoardJsonController extends HttpServlet {

	private CommunityBoardService communityBoardService;

	public RegBoardJsonController() {
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
		// TODO Auto-generated method stub
		super.doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String status = request.getParameter("status");
		String boardIds = request.getParameter("boardId");
		String stockCode = request.getParameter("stockCode");

		// 상태값이 reg면 등록
		if (status.equals("reg")) {
			Object tempId = session.getAttribute("id");
			int writerId = -1;

			if (tempId != null)
				writerId = (Integer) tempId;
			MemberDao memberDao = new JdbcMemberDao();
			String writerNickname = memberDao.getMember(writerId).getNickName();

			CommunityBoard insertBoard = new CommunityBoard(title, content, writerNickname, stockCode);

			int result = communityBoardService.insertCommunityBoard(insertBoard);

			response.setCharacterEncoding("UTF-8"); // UTP-8로 보내는 코드
			response.setContentType("text/html;charset=UTF-8"); // UTP-8로 보내는 코드
			PrintWriter out = response.getWriter();
			out.print(result);

			// 상태값이del이면 삭제
		} else if (status.equals("del")) {
			int boardId = -1;
			boardId = Integer.parseInt(boardIds);
			int resultInterest = communityBoardService.deleteinterestBoards(boardId);
			int result = communityBoardService.deleteCommunityBoard(boardId);
			int resultReply = communityBoardService.deleteReplys(boardId);

			response.setCharacterEncoding("UTF-8"); // UTP-8로 보내는 코드
			response.setContentType("text/html;charset=UTF-8"); // UTP-8로 보내는 코드
			PrintWriter out = response.getWriter();

			out.print(result);

			// 상태값이 modi면 수정
		} else if (status.equals("modi")) {
			int boardId = -1;
			boardId = Integer.parseInt(boardIds);

			CommunityBoard updateCommunityBoard = new CommunityBoard(boardId, title, content, "modi");
			int result = communityBoardService.updateCommunityBoard(updateCommunityBoard);

			response.setCharacterEncoding("UTF-8"); // UTP-8로 보내는 코드
			response.setContentType("text/html;charset=UTF-8"); // UTP-8로 보내는 코드
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
