import java.net.MalformedURLException;
import java.net.URL;

import Words.Dictionary;


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
		d.save();
		
		
		
	}

}
