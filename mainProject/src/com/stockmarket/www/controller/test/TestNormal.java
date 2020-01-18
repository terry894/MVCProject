package com.stockmarket.www.controller.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.stockmarket.www.dao.StockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbcStockDetailDao;
import com.stockmarket.www.entity.CurStock;
import com.stockmarket.www.service.basic.BasicSystemService;

public class TestNormal {
	public void test() {
		int testIndex = 0;
		BasicSystemService sys = new BasicSystemService();
		StockDetailDao stockDetailDao = new JdbcStockDetailDao();
		
		while (true) {
			Scanner sc = new Scanner(System.in);
			viewPrint();
			testIndex = sc.nextInt();	
			
			switch (testIndex) {
			case 1:
				Map<Integer, Integer>map = new HashMap<Integer, Integer>();
				map.put(1,2);
				map.put(1,3);
				map.put(1,5);
				map.put(2,2);
				map.put(2,2);
				map.put(3,2);
				System.out.println(map.toString());
				break;

			}
		}
	}
	
	private void viewPrint() {
		System.out.println("-----------------------------");
		System.out.println("1.map test");
		System.out.println("-----------------------------");
		System.out.println("숫자를 입력하시오");
	}
	
	
}

