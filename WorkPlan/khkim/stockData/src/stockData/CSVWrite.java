package stockData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVWriter;

public class CSVWrite {
	private String filename;

	public CSVWrite(String filename) {
		this.filename = filename + ".csv";
	}

	public void writeCsv(List<String[]> data) throws IOException {
		CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
		Iterator<String[]> it = data.iterator();
		
		while (it.hasNext()) {
			String[] s = (String[]) it.next();
			cw.writeNext(s);
		}
		cw.close();
	}
}
