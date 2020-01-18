package com.stockmarket.www.controller.test;

import java.util.Scanner;

public class TestMain {
	public static void main(String[] args) {
		int testIndex = 0;

		while (true) {
			Scanner sc = new Scanner(System.in);
			viewPrint();
			testIndex = sc.nextInt();

			switch (testIndex) {
			case 1:
				clearScreen();
				SystemServiceTest();
				break;
			case 2:
				clearScreen();
				JsonTest();
				break;
			case 3:
				clearScreen();
				NormalTest();
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				System.out.println("TEST END");
				return;
			}
		}

	}

	private static void viewPrint() {
		clearScreen();
		System.out.println("-----------------------------");
		System.out.println("1.SystemService Test");
		System.out.println("2.JSON Test ");
		System.out.println("3.Java grammar Test  ");
		System.out.println("4. ");
		System.out.println("5. ");
		System.out.println("6. ");
		System.out.println("7. ");
		System.out.println("8. ");
		System.out.println("9. ");
		System.out.println("10. 종료");
		System.out.println("-----------------------------");
		System.out.println("숫자를 입력하시오");
	}

	public static void clearScreen() {
		for (int i = 0; i < 80; i++)
			System.out.println("");
	}

	private static void SystemServiceTest() {
		TestSystemService service = new TestSystemService();
		service.test();
	}
	private static void JsonTest() {
		TestJson service = new TestJson();
		service.test();
	}
	private static void NormalTest() {
		TestNormal service = new TestNormal();
		service.test();
	}

}
