import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * This file coverts the ner tags produced by Stanford CoreNLP 3.6.0 to Roth and Yihs' ner tags
 * but still remains the same format (sentence talbe and relation block)
 */

public class ToRothTih {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new FileReader("readyToTrain"));
			PrintWriter pw = new PrintWriter(new FileWriter("RothYih"));
			
			String line = "";
			while( (line = br.readLine()) != null) {
				if(line.isEmpty())
					pw.println();
				else {
					String[] tmps = line.split("\t");
					if(tmps.length==3)
						pw.print(line);
					else 
						for(int i=0; i<tmps.length; i++) {
							if (i==0) {
								pw.print(tmps[i]);
								pw.flush();
							}
							// convert "ner" tags here
							else if(i==1) {
								if(tmps[i].equals("PERSON") || tmps[i].equals("PERSON/PERSON"))
									pw.print("\tPeop");
								else if(tmps[i].equals("LOCATION"))
									pw.print("\tLoc");
								else if(tmps[i].equals("ORGANIZATION"))
									pw.print("\tOrg");
								else
									pw.print("\tOther");
								pw.flush();
							}
							else {
								pw.print("\t" + tmps[i]);
								pw.flush();
							}
						}
					pw.println();
					pw.flush();
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
