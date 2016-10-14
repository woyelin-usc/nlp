import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class SentenceByLine {
	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(new FileReader("output"));
			PrintWriter pw = new PrintWriter(new FileWriter("sentence_by_line"));
			
			// TODO Auto-generated method stub
			String line = "";
			ArrayList<String> sentence = new ArrayList<>();
			
			while( scan.hasNextLine() ) {
				line = scan.nextLine();
				if(line.isEmpty()) {
					for(int i=0; i<sentence.size(); i++) {
						System.out.println(sentence.get(i)+" ");
						if(i==sentence.size()-1) {
							pw.println(sentence.get(i));
							pw.flush();
						}
						else {
							pw.print(sentence.get(i)+" ");
							pw.flush();
						}
					}
					System.out.println();
					sentence = new ArrayList<>();
				}
				else {
					sentence.add(line.split("\t")[5]);
				}
			}
			
			scan.close();
			pw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
