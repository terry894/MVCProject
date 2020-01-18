package naverTrend;

import java.util.ArrayList;
import java.util.List;

public class ResultData {
	private String title;
	private List<String> period;
	private List<Integer> ratio;
	
	public ResultData() {
		period = new ArrayList<String>();
		ratio = new ArrayList<Integer>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period.add(period);
	}

	public List<Integer> getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio.add(ratio);
	}
	
}


