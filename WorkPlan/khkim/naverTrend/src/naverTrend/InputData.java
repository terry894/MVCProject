package naverTrend;

import java.util.Scanner;

public class InputData {
	private String startDate;
	private String endDate;
	private String timeUnit;
	private String groupName[];
	private String keyWords[][];
	private int cnt, keyCnt[];
	
	private static String START_DATE = "2017-01-01";
	private static String END_DATE = "2019-01-01";
	private static String TIMEUNIT = "month";
	private static String GROUP_NAME = "한글";
	private static String KEYWORDS = "korean";
	
	public InputData() {
		groupName = new String[5];
		keyWords = new String[5][20];
		keyCnt = new int[5];
	}
	
	public InputData init(boolean mode) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("┌───────────┐");
		if(mode)
			startDate = START_DATE;
		else {
			System.out.print("Input START_DATE(20xx-0x-0x) : ");
			startDate = sc.next();
		}
		
		if(mode)
			endDate = END_DATE;
		else {
			System.out.print("Input END_DATE(20xx-0x-0x) : ");
			endDate = sc.next();
		}
		
		if(mode)
			timeUnit = TIMEUNIT;
		else {
			timeUnit = sc.next();
			System.out.print("Input TIMEUNIT(date, week, month) : ");
		}
		
		System.out.println("Input Subject Number : ");
		cnt = sc.nextInt();
		
		for(int i = 0; i < cnt; i++) {
			System.out.print("Input GROUP_NAME : ");
			groupName[i] = sc.next();
		
			System.out.println("Input Subject KeyWord Num : ");
			keyCnt[i] = sc.nextInt();
			for(int j = 0; j < keyCnt[i]; j++) {
				System.out.print("Input KEYWORDS : ");
				keyWords[i][j] = sc.next();
			}
		}
		return this;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getKeyCnt(int i) {
		return keyCnt[i];
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public String getGroupName(int i) {
		return groupName[i];
	}

	public void setGroupName(String[] groupName) {
		this.groupName = groupName;
	}

	public int getKeyWordsCnt(int i) {
		System.out.println("--------------");
		System.out.println(i + " " + keyWords[i].length);

		
		return keyWords[i].length;
	}

	public String getKeyWords(int i, int j) {
		return keyWords[i][j];
	}

	public void setKeyWords(String[][] keyWords) {
		this.keyWords = keyWords;
	}
	
	
}
