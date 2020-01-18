package com.stockmarket.www.controller.test;

import java.util.Scanner;

import com.google.gson.Gson;
import com.stockmarket.www.dao.StockDetailDao;
import com.stockmarket.www.dao.jdbc.JdbcStockDetailDao;
import com.stockmarket.www.service.basic.BasicSystemService;

public class TestJson {
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
				implJson();
				break;
			case 1:
				break;
			}
		}
	}

	private void viewPrint() {
		System.out.println("-----------------------------");
		System.out.println("1.json 사용법");
		System.out.println("-----------------------------");
		System.out.println("숫자를 입력하시오");
	}
	
	private void implJson() {
		int[] numbers = {1, 1, 2, 3, 5, 8, 13};
		String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

		// Create a new instance of Gson
		Gson gson = new Gson();

		// Convert numbers array into JSON string.
		String numbersJson = gson.toJson(numbers);

		// Convert strings array into JSON string
		String daysJson = gson.toJson(days);
		System.out.println("numbersJson = " + numbersJson);
		System.out.println("daysJson = " + daysJson);

		// Convert from JSON string to a primitive array of int.
		int[] fibonacci = gson.fromJson(numbersJson, int[].class);
		for (int number : fibonacci) {
		    System.out.print(number + " ");
		}
		System.out.println("");

		// Convert from JSON string to a string array.
		String[] weekDays = gson.fromJson(daysJson, String[].class);
		for (String weekDay : weekDays) {
		    System.out.print(weekDay + " ");
		}
		System.out.println("");

		// Converting multidimensional array into JSON
		int[][] data = {{1, 2}, {3, 4}, {4, 5}};
		String json = gson.toJson(data);
		System.out.println("Data = " + json);

		// Convert JSON string into multidimensional array of int.
		int[][] dataMap = gson.fromJson(json, int[][].class);
		for (int[] i : dataMap) {
		    for (int j : i) {
		        System.out.print(j + " ");
		    }
		    System.out.println("");
		}
	}
}



