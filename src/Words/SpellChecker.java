package Words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


/**
 * 
 * @author Mustafa
 * Spell Checking entity. It accepts a dictionary and 
 * tries to match input words against it.
 */
public class SpellChecker {
	
	Dictionary dict;
	Logger logger;
	LDCalculator ld;
	
	/**
	 * @param d
	 * Constructor - save reference to passed dictionary
	 */
	public SpellChecker (Dictionary d){
		logger = Logger.getLogger(Defaults.logBundle);
		dict = d;
		ld = new LDCalculator();
	}
	
	/**
	 * @param word - word to spell-check
	 * @return set of matches
	 */
	public List<String> check (String word){
		
		Integer len = word.length();
		Set<String> initSet = dict.getWordsOfLength(len);
		List<String> matches = new ArrayList<String>();
		Boolean isListSorted = false;
		
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
			
			// Apply RULE-WS
			// RULE-WS: the difference in length with the
			// word you are looking for is never more than 1
			if (Defaults.useRuleWS){
				if (len > 1){
					wordList.addAll(dict.getWordsOfLength(len-1));
				}
				wordList.addAll(dict.getWordsOfLength(len+1));
				
			} else {
			    Set<Integer> keySet = dict.getWordLengthSet();
			    for (Integer i: keySet){
			    	if (i==len){
			    		continue;
			    	}
			    	wordList.addAll(dict.getWordsOfLength(len));
			    }
			}
			
		}
				

		// Apply RULE-FL: you don't misspell a word by typing
		// the first letter wrong 
		if (Defaults.useRuleFL) {
			
			// sort list to facilitate binary search
			Collections.sort(wordList);
			isListSorted = true;
			
			// get the position of the first word beginning with
			// charAt(word_to_search, 0)
			Integer beginPos = this.binarySearch(wordList, word.charAt(0));
			if (beginPos == 0){
				return matches;
			}
			wordList = wordList.subList(beginPos, wordList.size()-1);
		}
		
		
		// iterate over words and calculate Levenshtein distances
		Map<String, Integer> ldMatches = new HashMap<String, Integer>();
		Integer worstScore = 0;
		
		for (String possibleMatch: wordList){
			
			if (isListSorted && Defaults.useRuleFL && (word.charAt(0)!=possibleMatch.charAt(0))){
				break;
			}
			
			Integer thisScore = ld.calc(word, possibleMatch);
			if (thisScore > worstScore){
				worstScore = thisScore;
			}
			
			if (ldMatches.size() < Defaults.resultSize){
				ldMatches.put(possibleMatch, thisScore);
			} else {
				Set<String> keyWords = ldMatches.keySet();
				Set<String> toEvict = new HashSet<String>();
				for (String currWord: keyWords){
					Integer currScore = ldMatches.get(currWord);
					if (thisScore < currScore){ //evict someone
						toEvict.add(currWord);
						break;
					}
				}
				if (toEvict.size() > 0){
					ldMatches.put(possibleMatch, thisScore);
				}
				for (String evictThisWord: toEvict){
					ldMatches.remove(evictThisWord);
				}
			}
						
			// check if we have good enough results
			if (ldMatches.values().size()==1 && ldMatches.values().contains((Integer)1)){
				break;
			}

		}
		Set<String>matchedWords = ldMatches.keySet();
		List<String>sortedMatchedWordsTmp = new ArrayList<String>();
		List<String>sortedMatchedWords = new ArrayList<String>();
		for (String matchedWord: matchedWords){
			String tmp = new String (ldMatches.get(matchedWord)+":"+matchedWord);
			sortedMatchedWordsTmp.add(tmp);
		}
		
		Collections.sort(sortedMatchedWordsTmp);
		for (String matchedWord: sortedMatchedWordsTmp){
			String tmpArr[] = matchedWord.split(":");
			sortedMatchedWords.add(tmpArr[1]);
		}

		matches.addAll(sortedMatchedWordsTmp);
		
		
		return matches;
	}
	
	/**
	 * @param list - sorted list of words for search
	 * @param c - the character of which the first
	 * 			  word in list is to be found
	 * @return position of the word found
	 */
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
