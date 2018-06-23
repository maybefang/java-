package chatRoom4_0;

import java.io.*;

public class test {
	public static void main(String [] args) throws IOException {
		
	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
	    String in ;
	    in = br.readLine();
	    File fi = new File(in);
	    FileReader fr = new FileReader(fi);
	    char[] achars = new char[10];
	    fr.read(achars, 0, 10);
	    for(int i=0;i<10;i++) {
	    	System.out.println(achars[i]);
	    }
	    /*File fi = new File(in);
	    FileWriter fw = new FileWriter(fi);
	    char[] achars = new char[] {'a','b','c','k'};
	    fw.write(achars, 0, achars.length);
	    fw.flush();*/
	}
	
}
