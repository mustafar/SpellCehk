import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import Words.Dictionary;
import Words.SpellChecker;

/**
 * 
 * @author Mustafa
 * 
 */
public class Sample {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		URL u = new URL("http://people.sc.fsu.edu/~jburkardt/data/txt/dictionary.txt");
		Dictionary d = new Dictionary(); //"http://people.sc.fsu.edu/~jburkardt/data/txt/dictionary.txt"
		d.teach("hello");
		//d.save();
		
		SpellChecker s = new SpellChecker(d);
		List<String> res = s.check("hexlo");
		System.out.println("hexlo: "+java.util.Arrays.asList(res).toString());
		res = s.check("xav");
		System.out.println("xav: "+java.util.Arrays.asList(res).toString());
		res = s.check("aa");		
		System.out.println("aa: "+java.util.Arrays.asList(res).toString());
		//System.exit(0);
		
        BufferedReader reader;

        //specify the reader variable 
        //to be a standard input buffer
        reader = new BufferedReader(new InputStreamReader(System.in));

        String word = "hey";
        while (!word.equals("_exit")){
        //ask the user for their name
        	System.out.print("Word: ");
        	word = reader.readLine();
        	if (word.equals("_exit")){
        		continue;
        	}
        	res = s.check(word);		
    		System.out.println("Res: "+java.util.Arrays.asList(res).toString());
        }
	}

}
