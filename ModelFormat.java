import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ModelFormat {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("toTrain"));
			PrintWriter pw = new PrintWriter(new FileWriter("readyToTrain"));
			int idx = -1;
			int preCount = 0;
			String line = "";
			while(( line = br.readLine()) != null) {
				if(line.isEmpty()) {
					pw.println();
					pw.flush();
					preCount = 0;
				}
				else {
					String[] tmps = line.split("\t");
					if (tmps.length == 9) {
						if(preCount == 0)
							idx++;
						for(int i=0; i<tmps.length; i++) {
							if(i!=0) {
								pw.print("\t" + tmps[i]);
								pw.flush();
							}
							else {
								pw.print(idx);
								pw.flush();
							}
						}
						pw.println();
						preCount = 9;
					}	
					// here we meet a line of relationship
					else {
						for(int i=0; i<tmps.length; i++) {
							if(i!=0)
								pw.print("\t");
							pw.print(tmps[i]);
							pw.flush();
						}
						pw.println();
						pw.flush();
						preCount = 3;
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
