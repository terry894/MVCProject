package com.stockmarket.www.controller.test;

import java.util.Scanner;

import com.stockmarket.www.dao.StockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbcStockDetailDao;
import com.stockmarket.www.service.basic.BasicSystemService;

public class TestForm {
	public void test() {
		int testIndex = 0;
		BasicSystemService sys = new BasicSystemService();
		StockDetailDao stockDetailDao = new JdbcStockDetailDao();
		
		while (true) {
			Scanner sc = new Scanner(System.in);
			viewPrint();
			testIndex = sc.nextInt();	
			
			switch (testIndex) {
			case 0:
				break;
			case 1:
				break;
			}
		}
	}
	
	private void viewPrint() {
		System.out.println("-----------------------------");
		System.out.println("1.코스피 코스닥 전종목 현재가 갱신");
		System.out.println("-----------------------------");
		System.out.println("숫자를 입력하시오");
	}
}
