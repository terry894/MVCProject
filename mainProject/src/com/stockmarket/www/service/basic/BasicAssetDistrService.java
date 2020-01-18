package com.stockmarket.www.service.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.stockmarket.www.dao.HaveStockDao;
import com.stockmarket.www.dao.MemberDao;
import com.stockmarket.www.dao.jdbc.JdbcHaveStockDao;
import com.stockmarket.www.dao.jdbc.JdbcMemberDao;
import com.stockmarket.www.entity.HaveStockView;
import com.stockmarket.www.service.AssetDistrService;
import com.stockmarket.www.service.AssetTrendService;

public class BasicAssetDistrService implements AssetDistrService {

	private MemberDao memberDao;
	private HaveStockDao haveStockDao;
	private AssetTrendService assetTrendService;

	private static Map<String, Object> nonAraayList = new HashMap<>(); // 종목명 / 분포율 정렬 전
	private static Map<String, Object> ArrayList = new LinkedHashMap<>(); // 종목명 / 분포율 내림차순 정렬

	public BasicAssetDistrService() {
		haveStockDao = new JdbcHaveStockDao();
		assetTrendService = new BasicAssetTrendService();
		memberDao = new JdbcMemberDao();
	}

	/*
	 * 자산 분포도 ? 한 회원이 가지고 있는 모든 자산에서 회원이 보유하고 있는 주식 종목들의 자산 비율을 나타낸 것 한 종목의 자산 / 모든
	 * 종목의 자산을 더한 값 내림차순으로 정렬
	 */

	// 멤버의 자산을 정렬 후 맵 리스트로 반환
	@Override
	public List<HaveStockView> getHaveStockList(int memberId) {
		
		List<HaveStockView> arrayList = new ArrayList<>();
		float profits = getSumAll(memberId);
		// System.out.println("profits: "+profits);
		
		List<HaveStockView> list = new ArrayList<>();
		list.addAll(haveStockDao.getList(memberId));
		for (HaveStockView data : list) {
			HaveStockView haveStockView = new HaveStockView();
			float profit = getSumByStockId(data.getStockId(), memberId);
			// System.out.println(data.getStockName()+"profit: "+profit);
			float ratio = (profit / profits) * 100;
			// System.out.println(data.getStockName()+"ratio: "+ratio);
			
			haveStockView.setStockName(data.getStockName());
			haveStockView.setRatio(ratio);
			haveStockView.setAssetValue(((long)profit));
		
			arrayList.add(haveStockView);
		}
		Collections.sort(arrayList, new Comparator<HaveStockView>() {
			@Override
			public int compare(HaveStockView v1, HaveStockView v2) {
				return Float.valueOf(v2.getRatio()).compareTo(Float.valueOf(v1.getRatio()));
			}
		});
		cutListForFour(arrayList);
		
		return arrayList;
	}
	
	// 멤버의 하나의 보유 종목의 자산
	private float getSumByStockId(String stockId, int memberId) {

		float presentPrice = Float.parseFloat(haveStockDao.getView(memberId, stockId).getPrice().replaceAll(",", ""));
		int quantuty = haveStockDao.getView(memberId, stockId).getQuantity();

		return presentPrice * quantuty;
	}

	// 멤버의 모든 종목의 자산을 더한 값 = 현 보유 자산 - 주식 가상 머니
	private float getSumAll(int memberId) {

		long assetPresnt = assetTrendService.getAssetPresent(memberId);
		// System.out.println("assetPrsent: "+assetPresnt);
		long vMoney = memberDao.getMember(memberId).getvMoney();

		return assetPresnt - vMoney;
	}
	

	// 보유 주식이 5개 이상인 경우 4개 이후부터 기타로 묶고 4개 이후 리스트 삭제
	private void cutListForFour(List<HaveStockView> arrayList) {
		
		if(arrayList.size()>=5) {
			float ratio = 0;
			long assetValue = 0;
			for (int i = 0; i < arrayList.size()-3; i++) {
				ratio += arrayList.get(3+i).getRatio();
				assetValue += arrayList.get(3+i).getAssetValue();
			}
			while (arrayList.size()>4) {
				int i = 0;
				arrayList.remove(4+i);
				i++;
			}
			arrayList.get(3).setStockName("그 외");
			arrayList.get(3).setRatio(ratio);	
		}
	}

	/*
	 * =======================================================================
	 * ============================= for Test ================================
	 * =======================================================================
	 */
	public static void main(String[] args) {
		int testIndex = 0;
		BasicAssetDistrService assetDistr = new BasicAssetDistrService();

		Scanner sc = new Scanner(System.in);
		System.out.println("숫자를 입력하시오");
		testIndex = sc.nextInt();

		switch (testIndex) {
		case 1: // getAssetPresent용 테스트
			System.out.println(assetDistr.getSumByStockId("035420", 3));
			break;
		case 2: // getRecordAsset용 테스트
			System.out.println(assetDistr.getSumAll(3));
			break;
		case 3: // getRecordAsset용 테스트
			System.out.println(assetDistr.getHaveStockList(3));
			break;
		}
		System.out.println("종료");
	}

}
