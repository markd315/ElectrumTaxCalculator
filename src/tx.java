import java.util.Date;

public class TX {
	private Date timestamp;
	private double value;
	private boolean wasMined;
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public double getValue() {
		return value;
	}
	public boolean wasMined() {
		return wasMined;
	}
	public void setWasMined(boolean wasMined) {
		this.wasMined = wasMined;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public TX(Date timestamp, double value, boolean wasMined) {
		super();
		this.timestamp = timestamp;
		this.value = value;
		this.wasMined = wasMined;
		if(value < 0.0) {
			//Mining transactions cannot be negative.
			this.wasMined = false;
		}
	}
	
}
