package com.stockmarket.www.controller.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.stockmarket.www.controller.system.AppContext;
import com.stockmarket.www.dao.KoreaStocksDao;
import com.stockmarket.www.dao.StockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbcStockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbcUpjongDao;
import com.stockmarket.www.dao.jdbc.JdbckoreaStocksDao;
import com.stockmarket.www.entity.CurStock;
import com.stockmarket.www.entity.StockDetail;
import com.stockmarket.www.entity.KoreaStocks;
import com.stockmarket.www.service.basic.BasicSystemService;

public class TestSystemService {

	public void test() {
		int testIndex = 0;
		BasicSystemService sys = new BasicSystemService();
		StockDetailDao stockDetailDao = new JdbcStockDetailDao();
		
		while (true) {
			Scanner sc = new Scanner(System.in);
			viewPrint();
			testIndex = sc.nextInt();	
			
			switch (testIndex) {
			case 1: // 코스피 코스닥 전종목 현재가 갱신
				// TEST - KOSPI, KOSDAQ 데이터 크롤링 및 callback
				sys.refreshStockPrice();
				break;

			case 2:
				// refreshStockPrice 함수 처리 시간 체크(100Mbps 이하 환경(기현집) 에서 약 7분 소요) 2019-11-24
				// 20:37:54 ~ 2019-11-24 20:44:53
				// refreshStockPrice 함수 처리 시간 체크(100Mbps 환경에서 약 4~5분 소요) 2019-11-28 16:21:19 ~
				// 2019-11-28 16:17:33
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println(date.format(System.currentTimeMillis()));

				sys.refreshStockPrice();
				System.out.println(date.format(System.currentTimeMillis()));
				break;
			case 3: // single tone Test for 코스피/코스닥 크롤링 데이터
//				List<CurStock> stockMarket;
//				stockMarket = AppContext.getStockMarket();
//
//				if (stockMarket != null) {
//					for (CurStock cur : stockMarket) {
//						System.out.println(cur.toString());
//					}
//				}
//				System.out.println("finished-3");
				break;
			case 4: //호가창 TODO
				List<CurStock> testMarket;
				CurStock test = null;
				List<String> codeNum = new ArrayList<>();
				codeNum.add("095660");
			
				
				System.out.println(test.toString());
				break;
			case 5: // 보유 자산 리스트 업로드 테스트
				System.out.println("케이스 5");
				sys.insertRecordAsset();
				break;
			case 7:	//stockDetailDao 에 크롤링 데이터를 저장하다
//				sys.setStockDataAll("095660");
				sys.setStockDataAll("005930");
//				service.getAllDailyPrice("004170");
				return;
			case 8:
//				stockDetailDao.deleteAll();
				return;
			case 9: // stockdetailDao 의 저장된 데이터를 가져온다
				List<StockDetail> stock = sys.getStockDetail("095660");
				for (StockDetail obj : stock)
					System.out.println(obj.toString());

				return;
			case 10:
				sys.upjongCrawling();
				return;
			case 11:
				JdbcUpjongDao upjongDao = new JdbcUpjongDao();
				upjongDao.delete();

			
			case 12:
				JdbckoreaStocksDao koreaDao = new JdbckoreaStocksDao();
				List<KoreaStocks> list = koreaDao.getList();
				int cnt = 0;
				for(KoreaStocks korea : list) {
					cnt++;
					System.out.println(korea.getCompanyName());
				}
				System.out.println("koreaDao.getList 종료 cnt : " + cnt);
				break;
			case 13://koreaStockDao insert
				sys.updateMarket("KOSPI");
				sys.updateMarket("KOSDAQ");
				break;
			case 14:	//케이티 -> KT 로 수작업
				KoreaStocksDao koreaStocksDao = new JdbckoreaStocksDao();
				koreaStocksDao.update("KT","케이티");;
				break;
			case 99:
				System.out.println("Test - SystemService 종료");
				return;
			}
		}
	}

	private void viewPrint() {
		System.out.println("-----------------------------");
		System.out.println("1.코스피 코스닥 전종목 현재가 갱신");
		System.out.println("2.함수 처리 시간 체크 ");
//		System.out.println("3.single tone Test for 코스피/코스닥 크롤링 데이터 ");
		System.out.println("4.호가창 테스트");
		System.out.println("5. ");
		System.out.println("6. ");
		System.out.println("7. stockDetailDao 에 크롤링 데이터를 저장하다 in:codeNum");
		System.out.println("8. ");
		System.out.println("9. stockdetailDao 의 저장된 데이터를 가져온다 in:codeNum");
		System.out.println("13. koreaStockDao insert");
		System.out.println("14. update 케이티 -> KT ");
		System.out.println("99. 종료");
		System.out.println("-----------------------------");
		System.out.println("숫자를 입력하시오");
	}

}
