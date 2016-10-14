/*
 * This file combine all "FirstName/LastName" into one line
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Reformat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new FileReader("table1"));
			PrintWriter pw = new PrintWriter(new FileWriter("table"));
			
			ArrayList<String> tokens = new ArrayList<>(), ners = new ArrayList<>(), poses = new ArrayList<>();
			String line = "";
			int sentence_count = 0;
			while((line = br.readLine())!=null) {
				
				if(line.isEmpty()) {	
					if(tokens.isEmpty())
						continue;
					// check whether we have a consecutive 'PERSON' ner tag, if yes, modify as such
					for(int i=0; i<ners.size()-2; i++) {
						if(ners.get(i).equalsIgnoreCase("PERSON") && ners.get(i+1).equalsIgnoreCase("PERSON") ) {
							tokens.set(i, tokens.get(i)+"/"+tokens.get(i+1));
							poses.set(i, poses.get(i)+"/"+poses.get(i+1));
							ners.set(i, ners.get(i)+"/"+ners.get(i+1));
							tokens.remove(i+1);
							poses.remove(i+1);
							ners.remove(i+1);
							i--;
						}
					}
					
					// after modify the sentence, print out the sentence
					for(int i=0; i<tokens.size(); i++) {
						pw.print(sentence_count);
						pw.print("\t" + ners.get(i));
						pw.print("\t" + i);
						pw.print("\tO");
						pw.print("\t" + poses.get(i));
						pw.print("\t" + tokens.get(i));
						pw.print("\tO"); pw.print("\tO"); pw.println("\tO");
						pw.flush();
					}
					pw.println(); pw.println();
					pw.flush();
					sentence_count++;

					
					tokens = new ArrayList<>();
					ners = new ArrayList<>();
					poses = new ArrayList<>();
				}
				
				else {
					String[] words = line.split("\t");
					tokens.add(words[5]);
					poses.add(words[4]);
					ners.add(words[1]);
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
