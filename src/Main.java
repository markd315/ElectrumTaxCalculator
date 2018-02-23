import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	//Three topline numbers to report: hobbyist mining income, deductible electrical cost, and capital gain.
	public static void main(String args[]) throws FileNotFoundException {
			String fn = args[0];
			Scanner fi = new Scanner(new File(fn));
			List<tx> arr = new ArrayList<tx>();
			String line = fi.nextLine();
			while(fi.hasNextLine()) {//parsing
				
			}
			
	}
	
}
