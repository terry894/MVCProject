package stockData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) throws Exception {
		String url = "http://kind.krx.co.kr/corpgeneral/corpList.do?"
				+ "method=download"
				+ "&searchType=13"
				+ "&orderMode=3"
				+ "&orderStat=D"
				+ "&marketType=stockMkt";
		//stockMkt 코스피
		//kosdaqMkt 코스닥
		urlConnection(url);
	}

	private static void urlConnection(String _url) throws Exception {
		URL url = null;
		BufferedReader reader = null;

		url = new URL(_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
//		con.setReadTimeout(1000);
		con.connect();

		reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "EUC-KR"));

		String inputLine = null;
		Scanner sc = new Scanner(System.in);
		while ((inputLine = reader.readLine()) != null) {
			sc.nextInt();
			System.out.println(inputLine);
		}

		reader.close();
		
	}
}
