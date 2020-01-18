package com.stockmarket.www.controller.system;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stockmarket.www.service.SystemService;
import com.stockmarket.www.service.basic.BasicSystemAnalysis;
import com.stockmarket.www.service.basic.BasicSystemService;

@WebServlet("/main")
public class SystemController extends HttpServlet {
	//thread 함수를 한번만 실행시키기 위한 flag
    
	static boolean oneShotFlag;
	static String preHour; 
	SystemService service;
	BasicSystemAnalysis analysisService;
	
	public SystemController() {
		oneShotFlag = false;
		service = new BasicSystemService();
		analysisService = new BasicSystemAnalysis();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//검색했을 때 키워드 값 넘겨받기
		request.setCharacterEncoding("UTF-8");
		String keyword = request.getParameter("k");
		Cookie cookie = new Cookie("keyword", keyword);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		if(oneShotFlag == true) {
			request.setAttribute("keyword", keyword);
			request.getRequestDispatcher("main.jsp").forward(request, response);
			return;
		}
		oneShotFlag = true;
		
		Thread thread = new Thread(()->{
			try {
				while(!Thread.currentThread().isInterrupted())
					systemThread();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("Thread is dead");
			}
		});
		thread.setDaemon(true);
		thread.start();
		
		request.getRequestDispatcher("main.jsp").forward(request, response);
	}

	private void systemThread() throws InterruptedException, IOException {
		SimpleDateFormat date = new SimpleDateFormat("HH"); //HH : 0~23시  기타형식예 "yyyy-MM-dd HH:mm:ss"
		String curHour = date.format(System.currentTimeMillis());
		
		SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //HH : 0~23시  기타형식예 "yyyy-MM-dd HH:mm:ss"
		System.out.println(date1.format(System.currentTimeMillis()));
		//10분주기 - refreshStockPrice 함수실행시 약 7분소요로 10분주기로 변경
		
		//오전 5시 하루에 한번 KOSPI.csv KOSDAQ.csv 파일을 갱신한다.
		if(curHour.equals("5") && preHour.equals("4")) {
			service.updateMarket("KOSPI");
			service.updateMarket("KOSDAQ");
		}
			
		//주식가격 refresh by 크롤링 9시 ~ 20시까지 실행
		if(Integer.parseInt(curHour) >= 9 && Integer.parseInt(curHour) <= 19) {
			service.refreshStockPrice();
		}
		//18시 장종료후 19시 주식데이터 갱신 stockDetail 데이터
		if(curHour.equals("20") && preHour.equals("19")) {
//			TODO
		}
		
		if(curHour.equals("19") && preHour.equals("18")) {
			service.insertRecordAsset();
		}
		
		//매일 오전 8시 분석데이터 갱신
		if(curHour.equals("8") && preHour.equals("7")) {
			analysisService.algorismImpl();
		}
			
	
		//현재 시간을 preHour flag 에 저장
		preHour = curHour;
		
		Thread.sleep(1000 * 60 * 1); //1분의 대기 - 반복적인 crawling 에 대한 우려
	}

/*
 * =======================================================================
 * ============================= for Test ================================
 * =======================================================================
 */
	void timeTest(String mm) {
		String curHour = mm;
		
		//오전 5시 하루에 한번 KOSPI.csv KOSDAQ.csv 파일을 갱신한다.
		if(curHour.equals("5") && preHour.equals("4"))  
			System.out.println("success-1");

		//주식가격 refresh by 크롤링 9시 ~ 18시까지 실행
		if(Integer.parseInt(curHour) >= 9 && Integer.parseInt(curHour) <= 19)
			System.out.println("success-2");
			
		//18시 장종료후 19시에 주식데이터 갱신
		if(curHour.equals("19") && preHour.equals("18"))  
			System.out.println("success-3");
		
		if(curHour.equals("18") && preHour.equals("17")) {
			System.out.println("success-4");
			service.insertRecordAsset();
		}
		preHour = curHour;
	}
	
	public static void main(String[] args) {
		int testIndex = 0;
		SystemController system = new SystemController();
		
		Scanner input = new Scanner(System.in);
		System.out.println("숫자를 입력하시오");
		testIndex = input.nextInt();
		
		switch(testIndex) {
		case 1:	//시간 조건문 Test
			while(true) {
				Scanner sc = new Scanner(System.in);
				System.out.println("시간을 입력하시오 : ");
				system.timeTest(sc.next());
			}
		case 2:
			break;
		}
		
	}
}
