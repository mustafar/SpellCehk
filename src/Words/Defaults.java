package Words;

import java.util.logging.Logger;

/**
 * 
 * @author Mustafa
 * Define all the configurable values of the package
 */
public class Defaults {
	
	/**
	 * Path of serialized dictionary
	 */
	public static final String dictFile = "diction.ary";
	
	/**
	 * Log Bundle Name
	 */
	public static final String logBundle = Logger.GLOBAL_LOGGER_NAME;
	
	
	/**
	 * the number of results needed
	 */
	public static final Integer resultSize = 3;
	
	/**
	 * Define if spell check considers only other words
	 * of the same length
	 * (Otherwise, we will use words of length +1 and -1)
	 */
	public static final Boolean matchOnlyExactLength = false;
	
	/**
	 * Flag to apply RULE-WS (Word Sizes)
	 * RULE-WS: the difference in length with the
	 * word you are looking for is never more than 1
	 */	
	public static final Boolean useRuleWS = true;
	
	/**
	 * Flag to apply RULE-FL (First Letter)
	 * RULE-FL: you don't misspell a word by typing
	 * the first letter wrong 
	 */
	public static final Boolean useRuleFL = false;
}
