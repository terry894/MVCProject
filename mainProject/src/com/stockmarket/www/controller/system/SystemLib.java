package com.stockmarket.www.controller.system;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class SystemLib {
	//네이버 크롤링 GET 방식 LIB 
	public static Document naverCrawling(String url) {
		Document doc = null;	//크롤링 결과를 담는 Document
		Response response = null; //jsoup connect 결과 반환 
		
		try {
			response = Jsoup.connect(url)
					.ignoreContentType(true)
					.method(Connection.Method.GET)
					.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			doc = response.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
