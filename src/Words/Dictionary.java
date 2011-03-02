package Words;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Dictionary {
	
		Set<String> dict;
		Logger logger;
		final String dictFile = "diction.ary";

		/**
		 * @param docs - List of document URLs
		 * Constructor - adds all words in provided list of docs to the dictionary
		 */
		public Dictionary (List<URL> docs){
			
			logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			
			dict = this.read();
			if (dict == null)
				dict = new HashSet<String>();
			
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
				dict = new HashSet<String>();
			
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
				dict = new HashSet<String>();

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
						dict.add(word);
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
			dict.add(word);
		}
		
		/**
		 * Save dict object to disk
		 */
		public void save(){
			
			try{
				FileOutputStream fout = new FileOutputStream(this.dictFile);
			    ObjectOutputStream oos = new ObjectOutputStream(fout);
			    oos.writeObject(this.dict);
			    oos.close();				
			} catch (Exception e){
				logger.log(Level.WARNING, "Cannot save dictionary: \""+this.dictFile+"\"");
			}
			
			logger.log(Level.INFO, "Saved to dictinary: \""+this.dictFile+"\"");
	
		}
		
		/**
		 * Open dictionary file and copy to dict object
		 */
		@SuppressWarnings("unchecked")
		private Set<String> read(){
			Set<String> readDict = null;
			try{
			    FileInputStream fin = new FileInputStream(this.dictFile);
			    ObjectInputStream ois = new ObjectInputStream(fin);
			    readDict = (Set<String>) ois.readObject();
			    ois.close();
			} catch (Exception e){
				logger.log(Level.WARNING, "Cannot read dictionary: \""+this.dictFile+"\"");
				return null;
			}
			
			logger.log(Level.INFO, "Opened saved dictinary: \""+this.dictFile+"\"");
			return readDict;
		}
}
