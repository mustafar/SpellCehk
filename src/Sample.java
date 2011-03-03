import java.net.MalformedURLException;
import java.net.URL;
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
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub

		URL u = new URL("http://people.sc.fsu.edu/~jburkardt/data/txt/dictionary.txt");
		Dictionary d = new Dictionary(); //"http://people.sc.fsu.edu/~jburkardt/data/txt/dictionary.txt"
		d.teach("hello");
		//d.save();
		
		SpellChecker s = new SpellChecker(d);
		Set<String> res = s.check("hexlo");
		System.out.println(java.util.Arrays.asList(res).toString());
		res = s.check("xav");
		System.out.println(java.util.Arrays.asList(res).toString());
		res = s.check("aabb");		
		System.out.println(java.util.Arrays.asList(res).toString());
	}

}
