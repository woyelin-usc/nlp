import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class TestModel {

	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("test"));
			PrintWriter pw = new PrintWriter(new FileWriter("buf"));
			// TODO Auto-generated method stub
			Properties props = new Properties();
			
			// First part: solve "firstName_lastName" issue by combinging them with an "_"
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			Annotation doc;
			String line = "";
			while((line = br.readLine())!=null) {
				doc = new Annotation (line);
				pipeline.annotate(doc);
				List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
				for(CoreMap sentence: sentences) {
					ArrayList<String> tokens = new ArrayList<>(), ners = new ArrayList<>();
					for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
						tokens.add(token.get(TextAnnotation.class));
						ners.add(token.get(NamedEntityTagAnnotation.class));
						
					
					}
					// check the sentence whether they have "firstName lastName" pair
					for(int i=0; i<tokens.size()-1; i++) {
						if(ners.get(i).equalsIgnoreCase("PERSON") && ners.get(i).equalsIgnoreCase("PERSON") ) {
							if(i!=0) {
								pw.print(" ");
								pw.flush();
							}
							// here print the firstName_lastName as a one word
							pw.print(tokens.get(i));
							pw.flush();
							// skip next token because already printed it
							i++;
						}
						else {
							if (i!=0) {
								pw.print(" ");
								pw.flush();
							}
							pw.print(tokens.get(i));
							pw.flush();
						}
						
					}
					pw.println();
					pw.flush();
				}
			}
			
			// after solve the "firstName_lastName" issue, output the relation in the test
			br = new BufferedReader(new FileReader("buf"));
			pw = new PrintWriter(new FileWriter("result"));
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, relation");
			props.setProperty("sup.relation.model", "./tmp/roth_relation_model_pipeline.ser");
			pipeline = new StanfordCoreNLP(props);
			Annotation document;
			
			while((line = br.readLine())!=null) {
				document = new Annotation (line);
				pipeline.annotate(document);
				List<CoreMap> sentences = document.get(SentencesAnnotation.class);
				for(CoreMap sentence: sentences) {
		            
		            List<RelationMention> relations = sentence.get(MachineReadingAnnotations.RelationMentionsAnnotation.class);
		            for(RelationMention rel: relations) {
		            	if((rel.getType().equalsIgnoreCase("WIN") || rel.getType().equalsIgnoreCase("TIE")) 
		            			&& rel.getArg(0).getType().equalsIgnoreCase("PEOPLE") && rel.getArg(1).getType().equalsIgnoreCase("PEOPLE")) {
		            		pw.println(rel.getArg(0).getValue() + "\t" + rel.getArg(1).getValue() + "\t" + rel.getType());
		            		pw.flush();
		            		break;
		            	}
		            }
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
