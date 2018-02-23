import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
	//Three topline numbers to report: hobbyist mining income, deductible electrical cost, and capital gain.
	//format is "5/30/2017 9:47"
	static DateFormat myFormat = new SimpleDateFormat("M/d/yyyy HH:mm");
	public static void main(String args[]) throws FileNotFoundException, ParseException {
			String fn = args[0];
			String miningPercentageOfTime = args[1];
			String miningStart = args[2];
			String miningEnd = args[3];
			Scanner fi = new Scanner(new File(fn));
			List<TX> arr = new ArrayList<TX>();
			String topline = fi.nextLine(); //Scrap the header line.
			while(fi.hasNextLine()) {//parsing
				String[] parts = fi.nextLine().split(",");
				//format: 0transaction_hash,1label,2confirmations,3value,4timestamp,5mining
				double val = Double.parseDouble(parts[3]);
				Date date = toDate(parts[4]);
				boolean wasMined = wasMined(parts[5]);
				TX newtx = new TX(date, val, wasMined);
				arr.add(newtx);
			}
			
			//Run calculation.
	}

	private static boolean wasMined(String string) {
		if(string.equalsIgnoreCase("yes")) {
			return true;
		}
		if(string.equalsIgnoreCase("y")) {
			return true;
		}
		if(string.equalsIgnoreCase("true")) {
			return true;
		}
		if(string.equalsIgnoreCase("t")) {
			return true;
		}
		return false;
	}

	private static Date toDate(String string) throws ParseException {
		Date ret = myFormat.parse(string);
		return ret;
	}
	
}
