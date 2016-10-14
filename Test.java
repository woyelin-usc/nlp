import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


public class Test {

	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("corpus_raw"));
			//PrintWriter pw = new PrintWriter(new FileWriter("output"));
			
			// TODO Auto-generated method stub
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			Annotation document;
			
			document = new Annotation ("`` I don't often go mad like that at the end of a match , I do n't know what happened to me , I think this place and the nerves means you forget everything else .");
			pipeline.annotate(document);
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			for(CoreMap sentence: sentences) {
				for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
					System.out.print(token.get(TextAnnotation.class));
					System.out.print("\t" + token.get(PartOfSpeechAnnotation.class));
					System.out.println("\t" + token.get(NamedEntityTagAnnotation.class));
				}
			}
			
			
			
//			String line = "";
//			int lineCount = 1;
//			// parse line by line
//			while((line = br.readLine())!=null) {
//				System.out.println("Line Number: " + lineCount++);
//				document = new Annotation (line) ;
//				pipeline.annotate(document);
//				List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//				System.out.println("COUNT: " + sentences.size());
//				int sentence_idx = 0;
//				for(CoreMap sentence: sentences) {
//					int token_idx = 0;
//					for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
//						pw.print(sentence_idx);
//						pw.print("\t" + token.get(NamedEntityTagAnnotation.class));
//						pw.print("\t"+token_idx++);
//						pw.print("\tO");
//						pw.print("\t" + token.get(PartOfSpeechAnnotation.class));
//						pw.print("\t" + token.get(TextAnnotation.class));
//						pw.print("\tO");
//						pw.print("\tO");
//						pw.print("\tO");
//						pw.println();
//						pw.flush();
//					}
//					pw.println();
//					pw.println();
//					sentence_idx++;
//				}
				br.close();
				//pw.close();
//			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
