/*
 * This file select among the whole corpus all sentences that are most likely to contain the target relationship
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import edu.stanford.nlp.util.StringUtils;

public class SelectSentence {
	
	public static boolean validPattern(String str) {
		if(!StringUtils.isNumeric(str))
			return false;
		if(str.charAt(0)=='0' && str.length()>1)
			return false;
		if(str.contains("."))
			return false;
		if(str.length()==4)
			return false;
		return true;
	}
	
	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(new File("sentence_by_line"));
			PrintWriter pw = new PrintWriter(new FileWriter("relation_sentence"));
			String line = "";
			int lineCount = 0;
			while(scan.hasNextLine()) {
				lineCount++;
				line = scan.nextLine();
				boolean print = false;
				String[] words = line.split(" ");
				
				// check whether the sentence contains the keyword "tie" or "tied", or contains a valid score pattern "-"
				for(int i=0; i<words.length; i++) {
					String word = words[i];
					if(word.equalsIgnoreCase("tie") || word.equalsIgnoreCase("tied")) {
						pw.println(line);
						pw.flush();
						break;
					}
					
					if(word.contains("-")) {
						int idx = word.indexOf('-');
						if(idx==0 || idx == word.length()-1) {
							continue;
						}
						if(validPattern(word.substring(0,idx)) && validPattern(word.substring(idx+1))) {
							pw.println(line);
							pw.flush();
							break;
						}
					}
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
