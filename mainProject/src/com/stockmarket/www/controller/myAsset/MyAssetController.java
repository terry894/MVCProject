package com.stockmarket.www.controller.myAsset;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stockmarket.www.service.AssetDistrService;
import com.stockmarket.www.service.AssetTrendService;
import com.stockmarket.www.service.basic.BasicAssetDistrService;
import com.stockmarket.www.service.basic.BasicAssetTrendService;

@WebServlet("/card/asset/myAsset")
public class MyAssetController extends HttpServlet {

	private AssetTrendService assetTrendService;
	private AssetDistrService assetDistrService;

	public MyAssetController() {
		assetTrendService = new BasicAssetTrendService();
		assetDistrService = new BasicAssetDistrService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("id");

		// 현재의 보유 자산
		long assetPresent = assetTrendService.getAssetPresent(userId);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		request.setAttribute("assetPresent", assetPresent);

		request.getRequestDispatcher("myAsset.jsp").forward(request, response);

	}

	public static void main(String[] args) {
		Locale locales[] = SimpleDateFormat.getAvailableLocales();

		// iterate through each locale and print
		// locale code, display name and country
		for (int i = 0; i < locales.length; i++) {

			System.out.print(i);
			System.out.printf("%10s - %s, %s \n", locales[i].toString(), locales[i].getDisplayName(),
					locales[i].getDisplayCountry());
		}
	}
}
