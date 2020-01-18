package naverTrend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Program {
	private static String clientId;
	private static String clientSecret;
	private static String apiURL;
	private static String body;
	private static HttpURLConnection con;
	private static JSONObject json;
	private static JSONArray jarray;
	private static JSONParser jsonParser;
	private static InputData data;
	private static List<ResultData> resultData;
	
	public static void main(String[] args) throws Exception {
		resultData = new ArrayList<ResultData>();
		makeJsonFile();
		
		init();
		connect();
		response();
		parseJson();
		
		for(ResultData data : resultData) {
			System.out.println(data.getTitle());
			for(String period : data.getPeriod())
				System.out.println(period);
			for(int ratio : data.getRatio())
				System.out.println(ratio);
		}
	}

	@SuppressWarnings("unchecked")
	private static void makeJsonFile() {
		JSONObject obj = new JSONObject();
		JSONObject date = new JSONObject();
		JSONArray array = new JSONArray();

		data = new InputData();
		
		//init(true) => put the date info predefined.
		data.init(true);
		
		date.put("startDate", data.getStartDate());
		date.put("endDate", data.getEndDate());
		date.put("timeUnit", data.getTimeUnit());
		obj.putAll(date);
		
		for(int i = 0; i < data.getCnt(); i++) {
			JSONObject keyword = new JSONObject();
			JSONArray tempArray = new JSONArray();
			
			keyword.put("groupName", data.getGroupName(i));
			for(int j = 0; j < data.getKeyCnt(i); j++) {
				tempArray.add(data.getKeyWords(i, j));
			}
			if(data.getKeyCnt(i) == 0)
				tempArray.add(data.getGroupName(i));
				
			keyword.put("keywords", tempArray);
			array.add(keyword);
		}
		
		obj.put("keywordGroups", array);
		System.out.println(obj);
		body = obj.toString(); //fault toJsonString
	}

	private static void parseJson() {
		JSONParser parser = new JSONParser();
		JSONArray array = new JSONArray();
		JSONArray arrayDummy = new JSONArray();
		
		try {
			Object obj = parser.parse(new FileReader("src/result.json"));
			JSONObject jobj = (JSONObject) obj;
			array = (JSONArray) jobj.get("results");
			
			for(int i = 0 ; i < array.size(); i++) {
				ResultData temp = new ResultData();
				JSONObject data = (JSONObject) array.get(i);
//				System.out.println(data.get("title"));
				temp.setTitle((String) data.get("title"));
				arrayDummy = (JSONArray) data.get("data");
				for(int j = 0; j < arrayDummy.size(); j++) {
					data = (JSONObject) arrayDummy.get(j);
					temp.setPeriod((String) data.get("period"));
					//ratio 값이 double 과 long 이 섞여있어서 String 변환후 반올린하여 Int 로 반환
					String ratio = String.valueOf(data.get("ratio"));
					temp.setRatio((int)Math.round(Double.parseDouble(ratio)));
//					System.out.print(data.get("period"));
//					System.out.println(data.get("ratio"));
				}
				resultData.add(temp);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private static void response() throws Exception {
		int responseCode = con.getResponseCode();
		BufferedReader br;
		if (responseCode == 200) { // 정상 호출
			br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		} else { // 에러 발생
			br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
		}

		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close();
		
		System.out.println(response.toString());
		BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.json"));
		writer.write(response.toString());
		writer.close();
	}

	private static void connect() throws Exception {
		URL url = new URL(apiURL);

		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("X-Naver-Client-Id", clientId);
		con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		byte[] bytes = new byte[1000];
		bytes = body.getBytes();
		wr.write(bytes);
		wr.flush();
		wr.close();

	}

	private static void init() {
		clientId = "qcb_heUiOwEgQEvETpk8";// 애플리케이션 클라이언트 아이디값";
		clientSecret = "HopZWjJ6Qf";// 애플리케이션 클라이언트 시크릿값";
//		qnM5ckLBoL4MohCeIdp7 //검색 API 아이디
//		tfU_2RcZey			 //검색 API 비번
		apiURL = "https://openapi.naver.com/v1/datalab/search";

	}
}
