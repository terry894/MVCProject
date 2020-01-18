package com.stockmarket.www.service.basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.stockmarket.www.dao.KoreaStocksDao;
import com.stockmarket.www.dao.StockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbcStockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbckoreaStocksDao;
import com.stockmarket.www.entity.KoreaStocks;

public class BasicSystemAnalysis {
	
	KoreaStocksDao koreaStockDao; 
	StockDetailDao stockDeatailDao;
	
	public BasicSystemAnalysis() {
		koreaStockDao = new JdbckoreaStocksDao();
		stockDeatailDao = new JdbcStockDetailDao();
		
	}
	
	public void algorismImpl() {
		List<KoreaStocks> stocks = new ArrayList<>();
		List<String> codeNum = new ArrayList<>();
		int result = 0; 
		int influence = influence();
		
		stocks = koreaStockDao.getList();
//		for(KoreaStocks stock : stocks) {
//			String code = stock.getStockCode();
			String code = "095660";
//			result += trend(code) * 0.2;
			result += contents("머큐리") * 0.3;
//			result += supply(code) * 0.3;
//			result += strength(code) * 0.1;
//			result += influence * 0.1;
			
			System.out.println(result);
//		}
	}
	

	
	private int trend(String code) {
		
		return 0;
	}
	
	private int contents(String name) {
		Document doc = null;
		String url = "https://search.naver.com/search.naver?where=news&sm=tab_jum&query=" + name + "&sm=tab_opt&sort=0&photo=0&field=0&reporter_article=&pd=2";
		
		doc = crawling(url);
		String month = doc.select(".title_desc.all_my").text().replace("1-10 / ","").replace(",","").replace("건","");
		
		url = "https://search.naver.com/search.naver?where=news&sm=tab_jum&query=" + name + "&sm=tab_opt&sort=0&photo=0&field=0&reporter_article=&pd=4";
		doc = crawling(url);
		String oneDay = doc.select(".title_desc.all_my").text().replace("1-10 / ","").replace(",","").replace("건","");
		
		//한달기준 1일분량의 기사 검색. 1/30(일) = 3% 기준 0 점 - 세분화 필요
		// 0점   	  20점	  	  40점		60점			80점 	 	100점
		//0~2%	3% ~ 6%,   7% ~ 12%, 13% ~ 23%,  24% ~ 50%,  60% ~ 
		
		double result = Double.parseDouble(oneDay) / Double.parseDouble(month);
		result = result * 100;

		if(result <= 2)
			return 0;
		else if(result <= 6)
			return 20;
		else if(result <= 12)
			return 40;
		else if(result <= 23)
			return 60;
		else if(result <= 50)
			return 80;
		else 
			return 100;
	}
	
	private int supply(String code) {

		return 0;
	}
	
	private int strength(String code) {
		return 0;
	}
	
	private int influence() {
		//코스피 코스닥 동향
		return 0;
	}

	private Document crawling(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).timeout(60000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static void main(String[] args) {
		BasicSystemAnalysis sys = new BasicSystemAnalysis();
		
		sys.algorismImpl();
	}
		
}
