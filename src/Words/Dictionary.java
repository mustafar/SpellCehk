package Words;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Mustafa
 * Dictionary Entity
 * Structure -->	[1]	=> (a, i, ...)
 * 					[2]	=> (is, of, ...)
 * 					[3] => (fee, you, ...)
 * 					......
 * 					[n] => (...)
 */
public class Dictionary {
	
		Map<Integer, Set<String>> dict;
		Logger logger;

		/**
		 * @param docs - List of document URLs
		 * Constructor - adds all words in provided list of docs to the dictionary
		 */
		public Dictionary (List<URL> docs){
			
			logger = Logger.getLogger(Defaults.logBundle);
			
			dict = this.read();
			if (dict == null)
				dict = new HashMap<Integer, Set<String>>();
			
			for (URL doc: docs){
				this.teach(doc);
			}
			
			logger.log(Level.FINE, "Dictionary Size: "+dict.size());
		}
		
		/**
		 * @param doc - URL of a single doc
		 * Constructor - ads all words in provided doc to the dictionary
		 */
		public Dictionary (URL doc){
			
			logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			
			dict = this.read();
			if (dict == null)
				dict = new HashMap<Integer, Set<String>>();
			
			this.teach(doc);
			
			logger.log(Level.FINE, "Dictionary Size: "+dict.size());
		}
		
		/**
		 * Constructor - instantiate blank dictionary
		 */
		public Dictionary() {
			
			logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			
			dict = this.read();
			if (dict == null)
				dict = new HashMap<Integer, Set<String>>();

			logger.log(Level.FINE, "Dictionary Size: "+dict.size());
		}
		
		/**
		 * @param docUrl - document to teach
		 * teach - parse the document at provided URL and learn every word
		 */
		public void teach(URL doc){
						
			BufferedReader in;
			String inputLine;
			try {
				in = new BufferedReader(
							new InputStreamReader(
							doc.openStream()));
				
				while ((inputLine = in.readLine()) != null){
					String[] words = inputLine.split(" ");
					for (String word: words){
						word = word.toLowerCase();
						Integer len = word.length();
						Set<String> sizeBasedDict = dict.get(len);
						if (sizeBasedDict == null){
							sizeBasedDict = new HashSet<String>();
							dict.put(len, sizeBasedDict);
						}
						sizeBasedDict.add(word);
					}
				}
				
				in.close();
				
			} catch (IOException e) {
				logger.log(Level.WARNING, "IO Exception: \""+doc+"\"");
				return;
			}
			
			logger.log(Level.INFO, "Learned new document: \""+doc+"\"");

		}
		
		/**
		 * 
		 * @param word - word to add to dictionary
		 * teach - learn a single word
		 */
		public void teach (String word) {
			word = word.toLowerCase();
			Integer len = word.length();
			Set<String> sizeBasedDict = dict.get(len);
			if (sizeBasedDict == null){
				sizeBasedDict = new HashSet<String>();
				dict.put(len, sizeBasedDict);
			}
			sizeBasedDict.add(word);
		}
		
		
		/**
		 * @param len - required length of returned words
		 * @return set of all words of length=len
		 */
		public Set<String> getWordsOfLength(Integer len){
			return dict.get(len);
		}
		
		/**
		 * @return full dictionary
		 */
		public Map<Integer, Set<String>> getFullDictionary(){
			return dict;
		}
		
		/**
		 * @return set of distinct lengths of saved words
		 */
		public Set<Integer> getWordLengthSet(){
			return dict.keySet();
		}
		
		/**
		 * Save dict object to disk
		 */
		public void save(){
			
			try{
				FileOutputStream fout = new FileOutputStream(Defaults.dictFile);
			    ObjectOutputStream oos = new ObjectOutputStream(fout);
			    oos.writeObject(this.dict);
			    oos.close();				
			} catch (Exception e){
				logger.log(Level.WARNING, "Cannot save dictionary: \""+Defaults.dictFile+"\"");
			}
			
			logger.log(Level.INFO, "Saved to dictinary: \""+Defaults.dictFile+"\"");
	
		}
		
		/**
		 * Open dictionary file and copy to dict object
		 */
		@SuppressWarnings("unchecked")
		private Map<Integer, Set<String>> read(){
			Map<Integer, Set<String>> readDict = null;
			try{
			    FileInputStream fin = new FileInputStream(Defaults.dictFile);
			    ObjectInputStream ois = new ObjectInputStream(fin);
			    readDict = (Map<Integer, Set<String>>) ois.readObject();
			    ois.close();
			} catch (Exception e){
				logger.log(Level.WARNING, "Cannot read dictionary: \""+Defaults.dictFile+"\"");
				return null;
			}
			
			logger.log(Level.INFO, "Opened saved dictinary: \""+Defaults.dictFile+"\"");
			return readDict;
		}
}
