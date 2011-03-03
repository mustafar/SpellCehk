package Words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class SpellChecker {
	
	Dictionary dict;
	Logger logger;
	
	/**
	 * @param d
	 * Constructor - save reference to passed dictionary
	 */
	public SpellChecker (Dictionary d){
		logger = Logger.getLogger(Defaults.logBundle);
		dict = d;
	}
	
	/**
	 * @param word - word to spell-check
	 * @return set of matches
	 */
	public Set<String> check (String word){
		
		Integer len = word.length();
		Set<String> initSet = dict.getWordsOfLength(len);
		Set<String> matches = new HashSet<String>();
		
		// if exact match found, just return it
		if (initSet.contains(word)){
			matches.add(word);
			return matches;
		}
		
		// add different length word sets
		// as per Defaults.matchOnlyExactLength flag
		List<String> wordList = new ArrayList<String>();
		wordList.addAll(initSet);
		if (!Defaults.matchOnlyExactLength){
			if (len>1){
				wordList.addAll(dict.getWordsOfLength(len-1));
			}
			wordList.addAll(dict.getWordsOfLength(len+1));
		}
		
		// sort list to facilitate binary search
		Collections.sort(wordList);
		
		// get the position of the first word beginning with
		// charAt(word_to_search, 0)
		// ---- RULE ----> you don't misspell a word by typing
		// ---- RULE ----> the first letter wrong 
		Integer beginPos = this.binarySearch(wordList, word.charAt(0));
		System.out.println(wordList.get(beginPos));
		if (beginPos == 0){
			return matches;
		}
		wordList = wordList.subList(beginPos, wordList.size()-1);
		
		
		return matches;
	}
	
	private Integer binarySearch(List<String> list, Character c) {
		
		Integer pos = 0;
		Boolean found = false;
		
		Integer first = 0;
		Integer last = list.size() - 1;
		Integer mid = (last-first)/2;
		Integer co = 0;
		
		while (!found){
		co++;if(co>100){break;}	
			if (mid==0 || mid==(list.size()-1)){
				pos = mid;
				break;
			}
						
			if (last < first){
				pos = 0;
				break;
			}
			
			
			Character midChar = list.get(mid).charAt(0);
			Character lastChar = list.get(mid-1).charAt(0);
			if (midChar==c && lastChar!=c){
				pos = mid;
				break;
			}
			
			if (midChar >= c){
				last = mid - 1;
			} else {
				first = mid + 1;
			}
			mid = first + ((last - first) / 2);
			
		}
		
		return pos;
	}
}
