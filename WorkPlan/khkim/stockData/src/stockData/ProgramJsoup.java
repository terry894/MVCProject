package stockData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CORBA.TIMEOUT;

public class ProgramJsoup {
	// <th> 회사명|종목코드|업종|주요제품|상장일|결산월|대표자명|홈페이지|지역 </th>
	private static final int COMPANY_INFO_COLUMN = 9;    
	private static final String KOSPI = "stockMkt";
	private static final String KOSDAQ = "kosdaqMkt";
	String[] dataBuffer;
	List<String[]> companyList;
	
	public ProgramJsoup() {
		companyList = new ArrayList<String[]>();
		dataBuffer = new String[COMPANY_INFO_COLUMN];
	}
	
	public static void main(String[] args) throws IOException {
		ProgramJsoup pj = new ProgramJsoup();
		Document doc = null;
		String market = KOSPI;	//or KOSDAQ
		String url = "http://kind.krx.co.kr/corpgeneral/corpList.do"
				+ "?method=download"
				+ "&searchType=13"
				+ "&orderMode=3"
				+ "&orderStat=D"
				+ "&marketType=" + market; //stockMkt 코스피  kosdaqMkt 코스닥
		
		try {
			doc = Jsoup.connect(url).ignoreContentType(true)
									.timeout(5000)
									.post();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//tr Tag 이하를 선택
		Elements contents = doc.select("tr");
		System.out.println(doc.toString());

		CSVWrite cw = new CSVWrite("KOSPI"); //KOSPI.csv or KOSDAQ.csv 를 생성한다
		pj.makeCSV(contents, "th"); 
		pj.makeCSV(contents, "td"); 
		cw.writeCsv(pj.companyList);
	
		System.out.println("Finished");
		
	}
	
	private void makeCSV(Elements contents, String tag) {
		int cnt = 0;
		for(Element element : contents.select(tag)) {
			dataBuffer[cnt] = element.text();
			cnt++;
			if(cnt % COMPANY_INFO_COLUMN == 0) {
				cnt = 0;
				String[] data = new String[COMPANY_INFO_COLUMN];
				for(int i = 0; i < data.length; i++)
					data[i] = dataBuffer[i];
				
				companyList.add(data);
			}
		}
	}
}
