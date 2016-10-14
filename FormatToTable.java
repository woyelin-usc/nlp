/*
 * This file transforms all relation sentences into Roth and Yin's format sentence table to train relation
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class FormatToTable {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("relation_sentence"));
			PrintWriter pw = new PrintWriter(new FileWriter("buffer"));
			
			// format to handle separation of ".";
			String line = "";
			while((line = br.readLine())!=null) {
				if(line.isEmpty())
					continue;
				StringBuilder sb = new StringBuilder();
				String[] tokens = line.split(" ");
				
				// format a line of sentence (may be sentences)
				for(int i=0; i<tokens.length; i++) {
					String token = tokens[i];
					int idx = token.indexOf('.');
					if(idx>0 && idx < token.length()-1) {
						sb.append(" " + token.substring(0, idx+1));
						pw.println(sb.toString());
						sb = new StringBuilder();
						sb.append(token.substring(idx+1));
					}
					else
						sb.append( (i==0?"":" ") + token);
					
				}
				pw.println(sb.toString());
			}
			
			
			
			
			br = new BufferedReader(new FileReader("buffer"));
			pw = new PrintWriter(new FileWriter("table1"));
			
			// TODO Auto-generated method stub
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			Annotation document;
			
			line = "";
			int sentence_count = 0;
			while((line = br.readLine())!=null) {
				if(!line.isEmpty()) {
					int token_count = 0;
					document = new Annotation(line);
					pipeline.annotate(document);
					List<CoreMap> sentences = document.get(SentencesAnnotation.class);
					
					for(CoreMap sentence: sentences) {
						
						// if a sentence have less than one 'PERSON' entity tag or do not have any 'NUMBER' tag, ignore that sentence
						int person_count = 0, number_count = 0;
						
						for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
							if (token.get(NamedEntityTagAnnotation.class).equalsIgnoreCase("PERSON")) {
								person_count++;
							}
							else if (token.get(NamedEntityTagAnnotation.class).equalsIgnoreCase("NUMBER")) {
								number_count++;
							}
						}
						if (!(number_count>0 && person_count>1)) continue;
						
						for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
							pw.print(sentence_count);
							pw.print("\t"+token.get(NamedEntityTagAnnotation.class));
							pw.print("\t"+token_count++);
							pw.print("\tO");
							pw.print("\t" + token.get(PartOfSpeechAnnotation.class));
							pw.print("\t" +token.get(TextAnnotation.class));
							pw.print("\tO");
							pw.print("\tO");
							pw.println("\tO");
							pw.flush();
						}
						pw.println();
						pw.println();
						pw.flush();
						sentence_count++;
						System.out.println(sentence_count);
					}
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
