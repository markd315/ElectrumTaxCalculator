import java.util.Date;

public class tx {
	private Date timestamp;
	private double value;
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public tx(Date timestamp, double value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
	}
	
}
