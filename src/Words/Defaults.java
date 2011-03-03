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
	 * Define if spell check considers only other words
	 * of the same length
	 * (Otherwise, we will use words of length +1 and -1)
	 */
	public static final Boolean matchOnlyExactLength = false;
}
