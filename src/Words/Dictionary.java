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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Mustafa Dictionary Entity Structure --> [1] => (a->500, i->300, ...)
 *         [2] => (is->250, of->250, ...) [3] => (fee->20, you->50, ...) ......
 *         [n] => (...)
 */
public class Dictionary {

    Map<Integer, Map<String, Integer>> dict;
    Logger logger;

    /**
     * @param docs
     *            List of document URLs Constructor - adds all words in
     *            provided list of docs to the dictionary
     */
    public Dictionary(List<URL> docs) {

        logger = Logger.getLogger(Defaults.logBundle);

        dict = this.read();
        if (dict == null)
            dict = new HashMap<Integer, Map<String, Integer>>();

        for (URL doc : docs) {
            this.teach(doc);
        }

        logger.log(Level.FINE, "Dictionary Size: " + dict.size());
    }

    /**
     * @param doc
     *            URL of a single doc Constructor - ads all words in provided
     *            doc to the dictionary
     */
    public Dictionary(URL doc) {

        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        dict = this.read();
        if (dict == null)
            dict = new HashMap<Integer, Map<String, Integer>>();

        this.teach(doc);

        logger.log(Level.FINE, "Dictionary Size: " + dict.size());
    }

    /**
     * Constructor - instantiate blank dictionary
     */
    public Dictionary() {

        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        dict = this.read();
        if (dict == null)
            dict = new HashMap<Integer, Map<String, Integer>>();

        logger.log(Level.FINE, "Dictionary Size: " + dict.size());
    }

    /**
     * @param docUrl
     *            document to teach teach - parse the document at provided URL
     *            and learn every word
     */
    public void teach(URL doc) {

        BufferedReader in;
        String inputLine;
        Pattern wordPattern = Pattern
                .compile("[^a-z]*([a-z]+[^a-z]*[a-z]+)[^a-z]*");
        Matcher wordMatcher;

        try {
            in = new BufferedReader(new InputStreamReader(doc.openStream()));

            while ((inputLine = in.readLine()) != null) {
                String[] words = inputLine.split(" ");
                for (String tmpWord : words) {
                    tmpWord = tmpWord.toLowerCase();
                    String word = "";

                    wordMatcher = wordPattern.matcher(tmpWord);
                    if (wordMatcher.find()) {
                        word = wordMatcher.group(1);
                    } else {
                        if (tmpWord.length() == 1 && tmpWord != " ") {
                            word = tmpWord;
                        }
                    }

                    if (word == "") {
                        continue;
                    }

                    Integer len = word.length();
                    Map<String, Integer> sizeBasedDict = dict.get(len);
                    if (sizeBasedDict == null) {
                        sizeBasedDict = new HashMap<String, Integer>();
                        dict.put(len, sizeBasedDict);
                    }
                    if (sizeBasedDict.containsKey(word)) {
                        Integer count = sizeBasedDict.get(word);
                        sizeBasedDict.put(word, count + 1);
                    } else {
                        sizeBasedDict.put(word, 1);
                    }
                }

            }

            in.close();

        } catch (IOException e) {
            logger.log(Level.WARNING, "IO Exception: \"" + doc + "\"");
            return;
        }

        logger.log(Level.INFO, "Learned new document: \"" + doc + "\"");

    }

    /**
     * 
     * @param word
     *            word to add to dictionary teach - learn a single word
     */
    public void teach(String word) {
        word = word.toLowerCase();
        Integer len = word.length();
        Map<String, Integer> sizeBasedDict = dict.get(len);
        if (sizeBasedDict == null) {
            sizeBasedDict = new HashMap<String, Integer>();
            dict.put(len, sizeBasedDict);
        }
        if (sizeBasedDict.containsKey(word)) {
            Integer count = sizeBasedDict.get(word);
            sizeBasedDict.put(word, count + 1);
        } else {
            sizeBasedDict.put(word, 1);
        }

    }

    /**
     * @param len
     *            - required length of returned words
     * @return set of all words of length=len
     */
    public Map<String, Integer> getWordsOfLength(Integer len) {
        return dict.get(len);
    }

    /**
     * @return full dictionary
     */
    public Map<Integer, Map<String, Integer>> getFullDictionary() {
        return dict;
    }

    /**
     * @return set of distinct lengths of saved words
     */
    public Set<Integer> getWordLengthSet() {
        return dict.keySet();
    }

    /**
     * 
     * @param word
     *            - word to search
     * @return frequency of the supplied word
     */
    public Integer getWordFrequency(String word) {
        return dict.get(word.length()).get(word);
    }

    /**
     * Save dict object to disk
     */
    public void save() {

        try {
            FileOutputStream fout = new FileOutputStream(Defaults.dictFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this.dict);
            oos.close();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Cannot save dictionary: \""
                    + Defaults.dictFile + "\"");
        }

        logger.log(Level.INFO, "Saved to dictinary: \"" + Defaults.dictFile
                + "\"");

    }

    /**
     * Open dictionary file and copy to dict object
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, Map<String, Integer>> read() {
        Map<Integer, Map<String, Integer>> readDict = null;
        try {
            FileInputStream fin = new FileInputStream(Defaults.dictFile);
            ObjectInputStream ois = new ObjectInputStream(fin);
            readDict = (Map<Integer, Map<String, Integer>>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            logger.log(Level.INFO, "Cannot read dictionary: \""
                    + Defaults.dictFile + "\"");
            return null;
        }

        logger.log(Level.INFO, "Opened saved dictinary: \"" + Defaults.dictFile
                + "\"");
        return readDict;
    }

}
