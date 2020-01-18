package com.stockmarket.www.service.basic;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.stockmarket.www.controller.system.AppContext;
import com.stockmarket.www.dao.CaptureMemoDao;
import com.stockmarket.www.dao.jdbc.JdbcCaptureMemoDao;
import com.stockmarket.www.entity.CaptureMemo;
import com.stockmarket.www.entity.CaptureMemoView;
import com.stockmarket.www.service.CaptureMemoService;

public class BasicCaptureMemoService implements CaptureMemoService {
	private CaptureMemoDao captureMemoDao;

	public BasicCaptureMemoService() {
		captureMemoDao = new JdbcCaptureMemoDao();	
	}

	@Override
	public List<CaptureMemoView> getList(int id) {
		return captureMemoDao.getList(id);
	}
	
	@Override
	public CaptureMemo get(int id) {
		return captureMemoDao.get(id);
	}

	@Override
	public int update(CaptureMemo captureMemo) {
		return captureMemoDao.update(captureMemo);
	}

	@Override
	public int delete(int id) {
		return captureMemoDao.delete(id);
	}

	@Override
	public int insert(CaptureMemoView memoView) {
		return captureMemoDao.insert(memoView);
	}

	@Override
	public String captureDataCrawling(String codeNum, int memberId) {
		Document doc = null;
		String url = "https://finance.naver.com/item/main.nhn?code=" + codeNum;
		
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).timeout(5000).get();
		} catch (IOException e) {
			AppContext.setLog("캡처 데이타 크롤링도중 IOException 발생", BasicAnalysisService.class.getName());
			e.printStackTrace();
		}
		//PER - PBR - ROE - 시가총액(억) - 외국인 지분율 - 부채비율
		String per = ".aside_invest_info em#_per";
		String pbr = ".aside_invest_info em#_pbr";
		double roe = 0.0; //계산
		String marketCap = ".aside_invest_info #_market_sum";
		String foreignInvestors = ".aside_invest_info .gray .strong:last-child td";
		String debtRatio = "div.section.cop_analysis div.sub_section tr:eq(6) td:nth-last-child(2)";
		Elements element = doc.select(per + "," + pbr  + "," +  roe  + "," + marketCap 
										+ "," + foreignInvestors + "," + debtRatio);

		String text = element.text().replace("%","").replace(",", "").replace("조 ","");
		//결과값 순서 : 부채비율 - 시가총액 - 외국인지분율 -  PER  - PBR    + ROE 계산
		//			data[0]- data[1]-  data[2]- data[3]-data[4] + @
		String[] data = text.split(" ");
		roe = Double.parseDouble(data[4]) / Double.parseDouble(data[3]) * 100;
		roe = Math.round(roe * 100) / 100.0;
		
		Gson gson = new Gson();
		CaptureMemo capture = new CaptureMemo(null, null, 
				Double.parseDouble(data[3]), 	//PER
				Double.parseDouble(data[4]), 	//PBR
				roe, 							//ROE
				Double.parseDouble(data[0]), 	//debtRatio
				Integer.parseInt(data[1]),	 	//marketCap
				Double.parseDouble(data[2]),	//foreignInvestors
				codeNum,					 	//codeNum
				memberId);    					//invalid memberId
		
		String json = gson.toJson(capture);
		return json;
	}
}
