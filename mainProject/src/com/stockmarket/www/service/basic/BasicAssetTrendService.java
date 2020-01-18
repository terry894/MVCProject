package com.stockmarket.www.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.stockmarket.www.dao.HaveStockDao;
import com.stockmarket.www.dao.MemberDao;
import com.stockmarket.www.dao.RecordAssetDao;
import com.stockmarket.www.dao.jdbc.JDBCRecordAssetDao;
import com.stockmarket.www.dao.jdbc.JdbcHaveStockDao;
import com.stockmarket.www.dao.jdbc.JdbcMemberDao;
import com.stockmarket.www.entity.HaveStockView;
import com.stockmarket.www.entity.RecordAsset;
import com.stockmarket.www.service.AssetTrendService;

public class BasicAssetTrendService implements AssetTrendService {
	
	private RecordAssetDao recordAssetDao; 
	private HaveStockDao haveStockDao; 
	private MemberDao memberDao;
	
	
	public BasicAssetTrendService() {
		recordAssetDao = new JDBCRecordAssetDao();
		haveStockDao = new JdbcHaveStockDao();
		memberDao = new JdbcMemberDao();
	}

	@Override
	public List<RecordAsset> getRecordAsset(int memberId) {
		
		return recordAssetDao.getList(memberId);
	}

	@Override
	public long getAssetPresent(int memberId) {
		// 현재 보유 자산
		long sum = 0;
		sum = memberDao.getMember(memberId).getvMoney();
		
		List<HaveStockView> list = new ArrayList<>();
		list.addAll(haveStockDao.getList(memberId));
		for (HaveStockView data : list) {
			// (보유종목당 현재가 및 보유수량 확인용)
			// System.out.println(data.getPrice()+","+data.getQuantity()); 
			long presentValue = Long.parseLong(data.getPrice().replaceAll(",",""));
			long quantity = data.getQuantity();
			// System.out.println(data.getStockName()+": "+presentValue*quantity);
			sum += presentValue*quantity;
		}
		// 가상머니+(현재가*보유수량)+(현재가*보유수량).....
		// List<HaveStock> quantHaveStocks = haveStockDao.getQuantity(memberId);
		return sum;
	}
	
	/*
	 * =======================================================================
	 * ============================= for Test ================================
	 * =======================================================================
	 */	
	public static void main(String[] args) {
		int testIndex = 0;
		AssetTrendService assetTrend = new BasicAssetTrendService();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("숫자를 입력하시오");
		testIndex = sc.nextInt();

		switch(testIndex) {
		case 1:	// getAssetPresent용 테스트
			System.out.println(assetTrend.getAssetPresent(3));
			break;
		case 2:	// getRecordAsset용 테스트
			System.out.println(assetTrend.getRecordAsset(3));
			break;
		}
		System.out.println("종료");
	}

}
