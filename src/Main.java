import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
	// Three topline numbers to report: hobbyist mining income, deductible
	// electrical cost, and capital gain.
	// format is "5/30/2017 9:47"
	static DateFormat myFormat = new SimpleDateFormat("M/d/yyyy HH:mm");

	public static void main(String args[]) throws ParseException, InterruptedException, IOException {
		String fn = args[0];
		String miningPercentageOfTime = args[1];
		String miningStart = args[2];
		String miningEnd = args[3];
		String taxYear = args[4];
		Scanner fi = new Scanner(new File(fn));
		List<TX> arr = new ArrayList<TX>();
		String topline = fi.nextLine(); // Scrap the header line.
		while (fi.hasNextLine()) {// parsing
			String[] parts = fi.nextLine().split(",");
			// format: 0transaction_hash,1label,2confirmations,3value,4timestamp,5mining
			double val = Double.parseDouble(parts[3]);
			Date date = toDate(parts[4]);
			boolean wasMined = wasMined(parts[5]);
			TX newtx = new TX(date, val, wasMined);
			arr.add(newtx);

		}
		double miningRevenue = 0;
		double balance = 0;
		for (TX tx : arr) { // Run calculation.
			long unixTimestamp = tx.getTimestamp().getTime() / 1000; // seconds UNIX time from ms UNIX time.
			String response = makeRequest(unixTimestamp);
			// https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=USD&limit=1&aggregate=1&toTs=[insert]&extraParams=ElectrumTaxCalculator
			Thread.sleep(100); // API limit is 15/sec
			System.out.println(response);
			//Response format:
			//{"Response":"Success","Type":100,"Aggregated":false,"Data":[{"time":1496145600,"close":2289.96,"high":2307.07,"low":2279.17,"open":2300.73,"volumefrom":6024.7,"volumeto":13629703.54},{"time":1496149200,"close":2266.12,"high":2294.73,"low":2259.23,"open":2289.96,"volumefrom":7559.15,"volumeto":17102527.7}],"TimeTo":1496149200,"TimeFrom":1496145600,"FirstValueInArray":true,"ConversionType":{"type":"direct","conversionSymbol":""}}
			//Let's use the closing price for our quote.
			String[] responseBroken = response.split(",");// look for "close":2220.82
			String closingPrice = responseBroken[11].substring(8); //strip static chars to get the value only.
			double price = Double.parseDouble(closingPrice);
			if(tx.getValue() > 0 && tx.wasMined()) {
				miningRevenue += price * (tx.getValue());
			}
			else if(tx.getValue() < 0){
				double soldPercentage = (-1.0* tx.getValue()) / balance;
				double soldAmount = (-1.0* tx.getValue()) * price;
				//TODO
			}
			//Calculate capital gain on all sales.
			//For any sale, look at the percentage of mining revenue being dumped and compare the price here.
			balance += tx.getValue();//Always do this step.
			
		}
		//Then, factor in the capital gains of the ending balance.

	}

	private static String makeRequest(long unixTimestamp) throws IOException {
		StringBuilder result = new StringBuilder();
		String urlToRead = "https://min-api.cryptocompare.com/data/histohour?fsym=BTC&tsym=USD&limit=1&toTs=" + unixTimestamp + "&extraParams=ElectrumTaxCalculator";
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	private static boolean wasMined(String string) {
		if (string.equalsIgnoreCase("yes")) {
			return true;
		}
		if (string.equalsIgnoreCase("y")) {
			return true;
		}
		if (string.equalsIgnoreCase("true")) {
			return true;
		}
		if (string.equalsIgnoreCase("t")) {
			return true;
		}
		return false;
	}

	private static Date toDate(String string) throws ParseException {
		Date ret = myFormat.parse(string);
		return ret;
	}

}
