package com.stockmarket.www.controller.myAsset;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.stockmarket.www.entity.HaveStockView;
import com.stockmarket.www.entity.RecordAsset;
import com.stockmarket.www.service.AssetDistrService;
import com.stockmarket.www.service.AssetTrendService;
import com.stockmarket.www.service.basic.BasicAssetDistrService;
import com.stockmarket.www.service.basic.BasicAssetTrendService;

@WebServlet("/card/asset/myAsset-json")
public class MyAssetJSONController extends HttpServlet{
	
	private AssetTrendService assetTrendService;
	private AssetDistrService assetDistrService;
	
	public MyAssetJSONController() {
		assetTrendService = new BasicAssetTrendService();
		assetDistrService = new BasicAssetDistrService();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		int userId = (int)session.getAttribute("id");
		
		// 전날까지 또는 당일 오후 5시 이후의 자산 기록이 포함된 리스트
		List<RecordAsset> trendList = assetTrendService.getRecordAsset(userId);
		// 현재의 보유 자산
		long assetPesent = assetTrendService.getAssetPresent(userId);
		// 자산 비율 리스트
		List<HaveStockView> distrList = assetDistrService.getHaveStockList(userId);
	
		// String assetPesent = String.valueOf(assetTrendService.getAssetPresent(userId));
		
        Gson gson = new Gson();
		String distrJson = gson.toJson(distrList);
		String trendJson = gson.toJson(trendList);
		String trendPrsentJson = gson.toJson(assetPesent);
		String list = "[{\"distJson\":"+distrJson+"}, {\"trendJson\":"+trendJson+"}, {\"trendPrsentJson\":"+trendPrsentJson+"}]";

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
         PrintWriter out = response.getWriter();
         out.write(list);
        // out.write(trendJson);
			
       // request.setAttribute("assetPesent", assetPesent);
		// request.getRequestDispatcher("myAsset.jsp").forward(request, response);
		
	}

}
