import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class SnookerSearch extends JFrame {

	JTabbedPane jtp;
	JPanel nameSearchPnl, NLSearchPnl;
	
	JPanel playerSearchPnl, firstPlayerSearchPnl, secondPlayerSearchPnl, nameBtnPnl, resultPnl, nameClearPnl;
	JPanel firstPlayerNamePnl,  secondPlayerNamePnl;
	JLabel firstPlayerNameLbl, secondPlayerNameLbl;
	JTextField firstPlayerNameTF,secondPlayerNameTF;
	JTextArea nameSearchTA, NLSearchTA;
	JButton nameSearchBtn,nameClearBtn;
	
	JPanel NLQuestionPnl, NLQuestionLblPnl, NLResultPnl, NLInputPnl, NLClearPnl;
	JTextArea NLResultTA;
	JLabel NLQuestionLbl;
	JTextField NLInputTF;
	JButton NLInputBtn, NLClearBtn;
	
	JMenuBar bar;
	JMenu menu;
	JMenuItem predict, exit;
	JFileChooser jfc;

	HashMap<String, HashMap<String, HashMap<String, Integer>>> rels1, rels2;
	
	public void configureNameSearchPanel() {
		
		nameSearchPnl = new JPanel();
		
		playerSearchPnl = new JPanel();
		playerSearchPnl.setLayout(new BoxLayout(playerSearchPnl, BoxLayout.X_AXIS));
		
		// Configure first player
		firstPlayerSearchPnl = new JPanel();
		firstPlayerSearchPnl.setLayout(new BoxLayout(firstPlayerSearchPnl, BoxLayout.Y_AXIS));
		firstPlayerNameLbl = new JLabel("Enter First Player's Name (Must)");
		firstPlayerNameTF = new JTextField("", 5);
		firstPlayerSearchPnl.add(firstPlayerNameLbl);
		firstPlayerSearchPnl.add(firstPlayerNameTF);
		
		// Configure second player
		secondPlayerSearchPnl = new JPanel();
		secondPlayerSearchPnl.setLayout(new BoxLayout(secondPlayerSearchPnl, BoxLayout.Y_AXIS));
		
		secondPlayerNameLbl = new JLabel("Enter Second Player's Name (Option)");
		secondPlayerNameTF = new JTextField("", 5);
		secondPlayerSearchPnl.add(secondPlayerNameLbl);
		secondPlayerSearchPnl.add(secondPlayerNameTF);

		// Configure button panel
		nameBtnPnl = new JPanel();
		nameBtnPnl.setLayout(new BorderLayout());
		nameSearchBtn = new JButton("Search");
		nameBtnPnl.add(nameSearchBtn, BorderLayout.SOUTH);
		
		playerSearchPnl.add(firstPlayerSearchPnl);
		playerSearchPnl.add(secondPlayerSearchPnl);
		playerSearchPnl.add(nameBtnPnl);
	
		nameClearPnl = new JPanel();
		nameClearBtn = new JButton("Clear Console");
		nameClearPnl.setLayout(new BorderLayout());
		nameClearPnl.add(nameClearBtn, BorderLayout.EAST);
		
		// configure result Panel
		resultPnl = new JPanel();
		resultPnl.setLayout(new BorderLayout());
		nameSearchTA = new JTextArea("", 50, 50);
		nameSearchTA.setEditable(true);
		JScrollPane jsp = new JScrollPane(nameSearchTA);
		resultPnl.add(jsp, BorderLayout.CENTER);
		
		
		nameSearchPnl.setLayout(new BorderLayout());
		nameSearchPnl.add(playerSearchPnl, BorderLayout.NORTH);
		nameSearchPnl.add(resultPnl, BorderLayout.CENTER);
		nameSearchPnl.add(nameClearPnl, BorderLayout.SOUTH);
	
		// set search btn action
		nameSearchBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			
				String firstPlayerName = firstPlayerNameTF.getText().trim(), secondPlayerName = secondPlayerNameTF.getText().trim();
				firstPlayerNameTF.setText("");
				secondPlayerNameTF.setText("");
				if(firstPlayerName.trim().isEmpty() && secondPlayerName.trim().isEmpty()) {
					JOptionPane.showMessageDialog(SnookerSearch.this, "You must input at least one player's name!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					if(!nameSearchTA.getText().trim().isEmpty())
						nameSearchTA.setText(nameSearchTA.getText() + "\n\n\n");
					StringBuilder searchInfo = new StringBuilder ("You are searching relations ");
					// search for only one player
					if (firstPlayerName.isEmpty() || secondPlayerName.isEmpty()) {
						searchInfo.append("for player ");
						if (!firstPlayerName.isEmpty())
							searchInfo.append("\"" + firstPlayerName + "\"");
						else
							searchInfo.append("\"" +secondPlayerName + "\"");
						searchInfo.append("\n--------------------------------------");
						nameSearchTA.setText(nameSearchTA.getText() + searchInfo.toString());
					}
					// search for relations between two players
					else {
						searchInfo.append("between \"" + firstPlayerName +"\" and \"" + secondPlayerName + "\"");
						searchInfo.append("\n--------------------------------------");
						nameSearchTA.setText(nameSearchTA.getText() + searchInfo.toString());
					}
					nameSearchTA.setText(nameSearchTA.getText() + search(firstPlayerName, secondPlayerName, SnookerSearch.this.rels1, SnookerSearch.this.rels2) );
				}
			}
		});
		
		
		// set clear console btn action
		nameClearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameSearchTA.setText("");
			}
		});
	}
	
	public void configureNaturalLanguagePanel() {
		NLSearchPnl = new JPanel();
		NLSearchPnl.setLayout(new BorderLayout());
		
		NLQuestionPnl = new JPanel();
		NLQuestionPnl.setLayout(new BoxLayout(NLQuestionPnl, BoxLayout.Y_AXIS));
		
		NLQuestionLbl = new JLabel("Please input your question below");
		NLQuestionLbl.setFont(new Font("Monaco", Font.BOLD, 13));
		NLQuestionPnl.add(NLQuestionLbl);
		
		NLInputPnl = new JPanel();
		NLInputPnl.setLayout(new BorderLayout());
		NLInputTF = new JTextField("", 15);
		NLInputBtn = new JButton("Ask");
		NLInputPnl.add(NLInputTF, BorderLayout.CENTER);
		NLInputPnl.add(NLInputBtn,  BorderLayout.EAST);
		
		NLQuestionPnl.add(NLQuestionLbl);
		NLQuestionPnl.add(NLInputPnl);
		NLSearchPnl.add(NLQuestionPnl, BorderLayout.NORTH);
		
		
		NLResultPnl = new JPanel();
		NLResultPnl.setLayout(new BorderLayout());
		NLResultTA = new JTextArea("", 50, 50);
		NLResultTA.setEditable(true);
		JScrollPane nljsp = new JScrollPane(NLResultTA);
		NLResultPnl.add(nljsp, BorderLayout.CENTER);
		
		NLClearPnl = new JPanel();
		NLClearPnl.setLayout(new BorderLayout());
		NLClearBtn = new JButton("Clear Console");
		NLClearPnl.add(NLClearBtn, BorderLayout.EAST);
		NLResultPnl.add(NLClearPnl,  BorderLayout.SOUTH);
		
		NLSearchPnl.add(NLResultPnl);
		
		NLClearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NLResultTA.setText("");   
			}
		});
		
		NLInputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ques = NLInputTF.getText().trim();
				NLInputTF.setText(""); 
				if(!NLResultTA.getText().trim().isEmpty())
					NLResultTA.setText(NLResultTA.getText() + "\n\n\n");
				NLResultTA.setText( NLResultTA.getText() + ques + "\n--------------------------");
				String[] tokens = ques.split(" ");
				if(!Character.isAlphabetic(tokens[tokens.length-1].charAt(tokens[tokens.length-1].length()-1)))
					tokens[tokens.length-1] = tokens[tokens.length-1].substring(0,  tokens[tokens.length-1].length()-1);
				
				if(ques.split(" ")[0].startsWith("what") || ques.split(" ")[0].startsWith("What") ) {
					String n = "";
					for(int i = 1; i<tokens.length; i++) {
						if(Character.isUpperCase(tokens[i].charAt(0))) {
							if(n.isEmpty()) {
								if(tokens[i].endsWith("'s"))
									n = tokens[i].substring(0,  tokens[i].length()-2);
								else
									n = tokens[i];
							}	
						}
					}
					if(n!=null) {
						NLResultTA.setText(NLResultTA.getText() + search(n, "", rels1, rels2));
					}	
				}
				else if(ques.split(" ")[0].startsWith("does") || ques.split(" ")[0].startsWith("Does") || ques.split(" ")[0].startsWith("Did") || ques.split(" ")[0].startsWith("did")
			||ques.split(" ")[0].startsWith("Has") || ques.split(" ")[0].startsWith("has") ) {
					String n1 = "", n2 = "", type = "";
					for(int i = 1; i<tokens.length; i++) {
						if(Character.isUpperCase(tokens[i].charAt(0))) {
							if(n1.isEmpty())
								n1 = tokens[i];
							else
								n2 = tokens[i];
						}
						else {
							if(tokens[i].equalsIgnoreCase("win") || tokens[i].equalsIgnoreCase("wins") || tokens[i].equalsIgnoreCase("won") || tokens[i].equalsIgnoreCase("winning")) {
								type = "WIN";
								//System.out.println("HERE");
							}
							else if(tokens[i].equalsIgnoreCase("lose") || tokens[i].equalsIgnoreCase("loses") || tokens[i].equalsIgnoreCase("lost") ) {
								type = "LOSE";
							}
							else if(tokens[i].equalsIgnoreCase("tie") || tokens[i].equalsIgnoreCase("ties") || tokens[i].equalsIgnoreCase("tied") ) {
								type = "TIE";
							}
							else;
						}
					}
					if(n1!=null || n2!=null) {
						NLResultTA.setText(NLResultTA.getText() + doesSearch(n1, n2, type, rels1, rels2));
					}
				}
				
				else {
					NLResultTA.setText(NLResultTA.getText() + "\nI'm sorry but I don't understand your question.");
				}
			}
		});
	}
	
	public String search (String name1, String name2, HashMap<String, HashMap<String, HashMap<String, Integer>>> rels1, HashMap<String, HashMap<String, HashMap<String, Integer>>> rels2 ) {
		StringBuilder res = new StringBuilder();
		// one player search
		if(name1.isEmpty() || name2.isEmpty()) {
			String name = name1.isEmpty()? name2: name1;
			if(rels1.containsKey(name)) {
				String n1 = name;
				for(Map.Entry<String, HashMap<String, Integer>> e1: rels1.get(n1).entrySet()) {
					String n2 = e1.getKey();
					for(Map.Entry<String, Integer> e2: e1.getValue().entrySet() ) {
						String type = e2.getKey();
						int times = e2.getValue();
						if(type.equalsIgnoreCase("TIE"))
							res.append("\n" + n1+ " "+ type +"s with " + n2 + " "+ times + (times==1? " time":" times"));
						else
							res.append("\n" + n1+ " " + type +"s " + n2 + " "+ times + (times==1? " time" : " times"));
					}
				}
			}
			if(rels2.containsKey(name)) {
				String n2 = name;
				for(Map.Entry<String, HashMap<String, Integer>> e1: rels2.get(n2).entrySet()) {
					String n1 = e1.getKey();
					for(Map.Entry<String, Integer> e2: e1.getValue().entrySet() ) {
						String type = e2.getKey();
						int times = e2.getValue();
						if(type.equalsIgnoreCase("TIE"))
							res.append("\n" + n2 + " "+ type +"s with " + n1 + " " +  times + (times==1? " time":" times"));
						else
							res.append("\n" + n2 + " "+ "LOSEs to " + n1 + " "+times +  (times==1? " time":" times"));
					}
				}
			}
		}
		
		// two player search
		else {
			String n1 = name1, n2 = name2;
			if(rels1.containsKey(n1) && rels1.get(n1).containsKey(n2)) {
				String type = rels1.get(n1).get(n2).entrySet().iterator().next().getKey();
				int times = rels1.get(n1).get(n2).entrySet().iterator().next().getValue();
				if(type.equalsIgnoreCase("TIE"))
					res.append("\n" + n1+ " "+ type +"s with " + n2 + " "+ times +  (times==1? " time":" times"));
				else
					res.append("\n" + n1+ " " + type +"s " + n2 + " "+ times +  (times==1? " time":" times"));
			}
			if(rels2.containsKey(n1) && rels2.get(n1).containsKey(n2)) {
				String type = rels2.get(n1).get(n2).entrySet().iterator().next().getKey();
				int times = rels2.get(n1).get(n2).entrySet().iterator().next().getValue();
				if(type.equalsIgnoreCase("TIE"))
					res.append("\n" + n1+ " "+ type +"s with " + n2 + " "+ times +  (times==1? " time":" times"));
				else
					res.append("\n" + n1+ " " + "LOSEs to " + n2 + " "+ times +  (times==1? " time":" times"));
			}
			
		}
		if(res.toString().trim().isEmpty())
			return "\nNo records found";
		return res.toString();
	}
	
	public String doesSearch(String n1, String n2, String type, HashMap<String, HashMap<String, HashMap<String, Integer>>> rels1, HashMap<String, HashMap<String, HashMap<String, Integer>>> rels2 ) {
		StringBuilder res = new StringBuilder();
		
		// search for 'WIN" relation
		if(type.equalsIgnoreCase("WIN")) {
			// only one has name, single search
			if(n1.isEmpty() || n2.isEmpty()) {
				// n is the winner, only search n in rels1
				String n = n1.isEmpty()? n2:n1;
				if(rels1.containsKey(n)) {
					for(Map.Entry<String, HashMap<String, Integer>> e1: rels1.get(n).entrySet()) {
						n2 = e1.getKey();
						for(Map.Entry<String, Integer> e2: e1.getValue().entrySet() ) {
							if(e2.getKey().equalsIgnoreCase("WIN")) {
								res.append("\n" + n+ " WINs " + n2 + " "+ e2.getValue() + (e2.getValue()==1? " time" : " times")); 
							}
						}
					}
				}
			}
			// both have name, double search
			else {
				// n1 wins n2, search n1 in rels1, find match for n2
				if(rels1.containsKey(n1) && rels1.get(n1).containsKey(n2)) {
					 if(rels1.get(n1).get(n2).entrySet().iterator().next().getKey().equalsIgnoreCase("WIN"))
						 res.append("\n" + n1+ " WINs " + n2 + " "+ rels1.get(n1).get(n2).entrySet().iterator().next().getValue() +  
						 (rels1.get(n1).get(n2).entrySet().iterator().next().getValue()==1? " time" : " times"));         
				}
			}
		}
		
		// search for "TIE" relations
		else if(type.equalsIgnoreCase("TIE")) {
			// only one has name, single search
			if(n1.isEmpty() || n2.isEmpty()) {
				// search n from both rels1 and rels2
				String n = n1.isEmpty()? n2:n1;
				if(rels1.containsKey(n)) {
					for(Map.Entry<String, HashMap<String, Integer>> e1: rels1.get(n).entrySet()) {
						n2 = e1.getKey();
						for(Map.Entry<String, Integer> e2: e1.getValue().entrySet() ) {
							if(e2.getKey().equalsIgnoreCase("TIE")) {
								res.append("\n" + n+ " TIEs with " + n2 + " "+ e2.getValue() + (e2.getValue()==1? " time" : " times")); 
							}
						}
					}
				}
				if(rels2.containsKey(n)) {
					for(Map.Entry<String, HashMap<String, Integer>> e1: rels2.get(n).entrySet()) {
						n2 = e1.getKey();
						for(Map.Entry<String, Integer> e2: e1.getValue().entrySet() ) {
							if(e2.getKey().equalsIgnoreCase("TIE")) {
								res.append("\n" + n+ " TIEs with " + n2 + " "+ e2.getValue() + (e2.getValue()==1? " time" : " times")); 
							}
						}
					}
				}
			}
			// both have names, double search
			else {
				// search n1 from rels1 and rels2
				if(rels1.containsKey(n1) && rels1.get(n1).containsKey(n2)) {
					 if(rels1.get(n1).get(n2).entrySet().iterator().next().getKey().equalsIgnoreCase("TIE"))
						 res.append("\n" + n1+ " TIEs with " + n2 + " "+ rels1.get(n1).get(n2).entrySet().iterator().next().getValue() + (rels1.get(n1).get(n2).entrySet().iterator().next().getValue()==1? "time" : "times")); 
				}
				if(rels2.containsKey(n1) && rels2.get(n1).containsKey(n2)) {
					 if(rels2.get(n1).get(n2).entrySet().iterator().next().getKey().equalsIgnoreCase("TIE"))
						 res.append("\n" + n1+ " TIEs with " + n2 + " "+ rels1.get(n2).get(n1).entrySet().iterator().next().getValue() + (rels1.get(n2).get(n1).entrySet().iterator().next().getValue()==1? "time" : "times")); 
				}
			}
		}

		// search for lose relation
		else {
			// only one has name, single search
			if(n1.isEmpty() || n2.isEmpty()) {
				// n losts, only search n in rels2
				String n = n1.isEmpty()? n2:n1;
				if(rels2.containsKey(n)) {
					for(Map.Entry<String, HashMap<String, Integer>> e1: rels2.get(n).entrySet()) {
						n1 = e1.getKey();
						for(Map.Entry<String, Integer> e2: e1.getValue().entrySet() ) {
							if(e2.getKey().equalsIgnoreCase("WIN")) {
								res.append("\n" + n+ " LOSEs to " + n1 + " "+ e2.getValue() +(e2.getValue()==1? " time" : " times"));
							}
						}
					}
				}
			}
			// has two names, double search
			else {
				// n2 wins n1, only search n2 in rels1, find match for n1
				if(rels1.containsKey(n2) && rels1.get(n2).containsKey(n1)) {
					 if(rels1.get(n2).get(n1).entrySet().iterator().next().getKey().equalsIgnoreCase("WIN"))
						 res.append("\n" + n1+ " LOSEs to " + n2 + " "+ rels1.get(n2).get(n1).entrySet().iterator().next().getValue() + (rels1.get(n2).get(n1).entrySet().iterator().next().getValue()==1? " time" : " times")); 
				}
			}
		}
		if(res.toString().trim().isEmpty())
			return "\nNo records found";
		return res.toString();
	}

	public SnookerSearch (HashMap<String, HashMap<String, HashMap<String, Integer>>> rels1, HashMap<String, HashMap<String, HashMap<String, Integer>>> rels2, String ser,String buf, String res) {
		super("SnookerSearch");
		setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		bar = new JMenuBar();
	    menu = new JMenu("Option");
	    predict = new JMenuItem("Predict");
	    exit = new JMenuItem("Exit");
	    menu.add(predict);
	    menu.add(exit);
	    exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
	    });
	    predict.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser();
				int result = jfc.showOpenDialog(new JFrame());
				if (result == JFileChooser.APPROVE_OPTION) {
					String filePath = jfc.getSelectedFile().getAbsolutePath();
					try {
						BufferedReader br = new BufferedReader(new FileReader(filePath));
						PrintWriter pw = new PrintWriter(new FileWriter(buf));
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
						br = new BufferedReader(new FileReader(buf));
						pw = new PrintWriter(new FileWriter(res));
						props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, relation");
						props.setProperty("sup.relation.model", ser);
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
						
					JOptionPane.showMessageDialog(SnookerSearch.this, "Extracting relations finished, now you can search!", "", JOptionPane.INFORMATION_MESSAGE);
						
					} catch (FileNotFoundException exc) {
						// TODO Auto-generated catch block
						exc.printStackTrace();
					} catch (IOException exc) {
						exc.printStackTrace();
					}
				}
			}
	    });
	   
		bar.add(menu);
		this.setJMenuBar(bar);
		
		jtp = new JTabbedPane();
	
		configureNameSearchPanel();
		configureNaturalLanguagePanel();
		
		jtp.add("Search by Natural Language", NLSearchPnl);
		jtp.add("Serach by Name", nameSearchPnl);
		

		this.add(jtp);		
		setVisible(true);
		
		this.rels1 = rels1;
		this.rels2 = rels2;
		
	}
	
	
	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			HashMap<String, HashMap<String, HashMap < String, Integer> > > rels1 = new HashMap<>(), rels2 = new HashMap<>();
			String line = "";
			while ( (line = br.readLine())!=null) {
				String[] tmps = line.split("\t");
				String n1 = tmps[0], n2 = tmps[1], type = tmps[2];
				if(!rels1.containsKey(n1))
					rels1.put(n1, new HashMap<>());
				if(!rels1.get(n1).containsKey(n2))
					rels1.get(n1).put(n2,  new HashMap<>());
				if(!rels1.get(n1).get(n2).containsKey(type))
					rels1.get(n1).get(n2).put(type, 1);
				else
					rels1.get(n1).get(n2).put(type,rels1.get(n1).get(n2).get(type)+1 );
			}
			
			for(Map.Entry<String, HashMap<String, HashMap<String, Integer>>> e1: rels1.entrySet() ) {
				String n1 = e1.getKey();
				for(Map.Entry<String, HashMap<String, Integer>> e2: e1.getValue().entrySet()) {
					String n2 = e2.getKey();
					for(Map.Entry<String, Integer> e3: e2.getValue().entrySet() ) {
						String type = e3.getKey();
						if(!rels2.containsKey(n2))
							rels2.put(n2, new HashMap<>());
						if(!rels2.get(n2).containsKey(n1))
							rels2.get(n2).put(n1, new HashMap<>());
						rels2.get(n2).get(n1).put(type, rels1.get(n1).get(n2).get(type));
					}
				}
			}
		
			new SnookerSearch(rels1, rels2, args[1], args[2], args[3]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}