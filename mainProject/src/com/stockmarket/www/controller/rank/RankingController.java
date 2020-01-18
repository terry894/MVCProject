package com.stockmarket.www.controller.rank;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stockmarket.www.entity.Member;
import com.stockmarket.www.entity.MemberView;
import com.stockmarket.www.service.RankingService;
import com.stockmarket.www.service.basic.BasicRankingService;

@WebServlet("/card/rank/ranking")
public class RankingController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RankingService service = new BasicRankingService();
		
		// 가상머니 소유자를 상위 50위까지 가져온다.
		List<MemberView> rankers = service.getMemberList();
		rankers.sort(new Comparator<MemberView>() {
			@Override
			public int compare(MemberView o1, MemberView o2) {
				long x = o1.getTotalAsset();
				long y = o2.getTotalAsset();
				
				if(x>y) return -1;
				else if(x<y) return 1;
				else return 0;
			}
		});
		request.setAttribute("rankers", rankers);
		
		// 세션을 이용하여 현재 사용자의 아이디를 가져온다.
		HttpSession session = request.getSession();
		Object tempId = session.getAttribute("id");
		
		int id = -1;
		
		if(tempId != null)
			id = (Integer)tempId;
		
		// 본인의 랭킹을 가져온다.
		MemberView myInfo = service.getMember(id);
		
		request.setAttribute("myRank", service.getMemberRank(id));
		request.setAttribute("myInfo", myInfo);
		
		// jsp 파일을 요청하여 브라우저에 보여준다.
		request.getRequestDispatcher("ranking.jsp").forward(request, response);
	}
}
