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
	// Three topline numbers to report: hobbyist mining income, and capital gain.
	// date format is "5/30/2017 9:47"
	static DateFormat myFormat = new SimpleDateFormat("M/d/yyyy HH:mm");

	public static void main(String args[]) throws ParseException, InterruptedException, IOException {
		String fn = args[0];
		int fileYear = Integer.parseInt(args[1]);
		Scanner fi = new Scanner(new File(fn));
		List<TX> arr = new ArrayList<TX>();
		String topline = fi.nextLine(); // Scrap the header line.
		int startingBalance = 0;
		while (fi.hasNextLine()) {// parsing
			String[] parts = fi.nextLine().split(",");
			// format: 0transaction_hash,1label,2confirmations,3value,4timestamp,5mining
			double val = Double.parseDouble(parts[3]);
			int txYear = Integer.parseInt(parts[4].split("/")[2].substring(0,4));//Grabs just the year.
			if(txYear > fileYear) {//We disregard it, as it is in the future. Also warn console.
				System.err.println("Warning: ledger contains transactions from future years.");
				continue;
			}
			if(txYear < fileYear) {
				startingBalance +=val;
				continue;
			}
			Date date = toDate(parts[4]);
			boolean wasMined = wasMined(parts[5]);
			TX newtx = new TX(date, val, wasMined);
			arr.add(newtx);

		}
		System.out.println("Finished parsing transaction log, making API calls.");
		double miningRevenue = 0;
		double balance = startingBalance;
		double capitalGain = 0;
		double costBasis = 0;
		for (TX tx : arr) { // Run calculation.
			long unixTimestamp = tx.getTimestamp().getTime() / 1000; // seconds UNIX time from ms UNIX time.
			// https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=USD&limit=1&aggregate=1&toTs=[insert]&extraParams=ElectrumTaxCalculator
			//Response format:
			//{"Response":"Success","Type":100,"Aggregated":false,"Data":[{"time":1496145600,"close":2289.96,"high":2307.07,"low":2279.17,"open":2300.73,"volumefrom":6024.7,"volumeto":13629703.54},{"time":1496149200,"close":2266.12,"high":2294.73,"low":2259.23,"open":2289.96,"volumefrom":7559.15,"volumeto":17102527.7}],"TimeTo":1496149200,"TimeFrom":1496145600,"FirstValueInArray":true,"ConversionType":{"type":"direct","conversionSymbol":""}}
			//Let's use the closing price for our quote.
			double price = getQuote(unixTimestamp);
			if(tx.wasMined()) {
				miningRevenue += price * (tx.getValue());
			}
			if(tx.getValue() > 0) {
				costBasis += price * (tx.getValue());
			}
			else if(tx.getValue() < 0){//On sales we calculate a gain or loss from the basis.
				double avgCostBasis = costBasis / balance; //So like .5 bought@1000, .5 bought@2000 = basis of (500+1000)/1  = 1500 avg
				//capital gain = (avgCostBasis - saleCost) * soldShares
				capitalGain+= (avgCostBasis-price)*tx.getValue();
			}
			//Calculate capital gain on all sales.
			//For any sale, look at the percentage of mining revenue being dumped and compare the price here.
			balance += tx.getValue();//Always do this step.
			Thread.sleep(100); // API limit is 15/sec
		}
		System.out.println("Mining revenue: $" + miningRevenue);
		System.out.println("Cost basis for capital gains: $" + costBasis/balance);
		System.out.println("Capital gain (loss shown negative): $" + capitalGain);
	}

	private static String makeRequest(long unixTimestamp) throws IOException {
		System.out.println("Still working: takes time to talk to server.");
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
	private static double getQuote(long unixTimestamp) throws IOException {
		String response = makeRequest(unixTimestamp);
		String[] responseBroken = response.split(",");// look for "close":2220.82
		String closingPrice = responseBroken[11].substring(8); //strip static chars to get the value only.
		return Double.parseDouble(closingPrice);
	}
}

//TODO remove FOR ETH:
//395.4223 avg cost basis
//sale 1.95@531.31 = 264.98 gain
//sale .35@1203 = 282.76 gain
//547.74 ETH capital gain.
