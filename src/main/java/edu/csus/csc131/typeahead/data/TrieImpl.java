package edu.csus.csc131.typeahead.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//ToDo: provide real implementation for this class
@Component
public class TrieImpl extends Trie {
	private static Logger logger = LoggerFactory.getLogger(TrieImpl.class);	
	
	
	public TrieImpl() {
		super();
	}
	
	public TrieImpl(@Value("${backup.file}") String backupFilePath) throws IOException {
		super(backupFilePath);
	}
	
	public Node buildTestTree(String text) {
		Node root = buildTree(text);
		return root;
	}
	
	@Override
	Node buildTree(String str) {
		logger.trace("buildTree started");
		NodeImpl root = new NodeImpl();
		StringTokenizer st = new StringTokenizer(str," ");
		int wordCount = st.countTokens();
		//logger.trace("Number of words in the document: " + words);
		//Loads the Tokens into an array
		for(int i =0; i < wordCount; i++) {
			String current = st.nextToken();
			current = current.replaceAll("[,\t\n \".;!?0-9]", "");
			if(current.length()>2){
				this.addWord(current.toLowerCase(), root); /////HEY MATT ADDED TOLOWERCASE
			}
		}
		/*
		int freq = 0;
		String res = null;
		String topWord[];
		topWord = new String[5];
		int topWordCount = 0;
		
		for (int i = 0; i < wordCount; i++) {
	        int count = 0;
	        for (int j = i + 1; j < wordCount; j++) {
	            if (arr[j] == arr[i]) {
	                count++;
	            }
	        }
		
	        if (count >= freq) {
	            res = arr[i];
	            freq = count;
	            
	            if(topWordCount <= 4) {
	            	topWord[topWordCount] = res;
	            }
	            else {
	            	topWordCount = 0;
	            	topWord[topWordCount] = res;
	            }
	        }
	    }
		
		
		logger.trace("The Most common words are: "+ topWord[0]+ topWord[1]+ topWord[2]+ topWord[3]+ topWord[3]);
		*/
		logger.trace("buildTree completed");
		return root;
	}
	
	private void addWord(String word,NodeImpl root) {
		char letter = word.charAt(0);
		word= word.substring(1,word.length());
		root.addChild(letter);
		NodeImpl next = root.getChild(letter);
		if(word.length()==0) {					///////HEY MATT CHANGED 1 to 0
			next.incrementCount();
			next.setWord(true);
			return;
		}
		this.addWord(word,next);
	}

	@Override
	String serialize() {
		logger.trace("serialize started");		

		logger.trace("serialize completed");		
		return "test" + System.currentTimeMillis();
	}

	@Override
	Node deSerialize(String str) {
		logger.trace("deSerialize started");		
		
		logger.trace("deSerialize completed");	
		return new NodeImpl('\0');
	}

	@Override
	public List<String> getSuggestions(String prefix) {
		logger.trace("getSuggestions started");
		
		List <String> suggestions = new ArrayList<String>();
		Node current = this.getRoot();
		
		// Iterate though the given prefix, searching for the parent node to dfs upon.
		for (int i = 0; i < prefix.length(); i++) {
			current = current.getChild(prefix.charAt(i));
			if (current == null) return suggestions;  // There are no words starting with the given prefix, return.
		}
		
		suggestions = current.getSuggestions();
		
		if(prefix.length()>1) {
			String prepre = prefix.substring(0,prefix.length()-1);
			for(String i : suggestions) {
				i = prepre + i;
			}
		}
		//sort suggestions return top 5
		
		logger.trace("getSuggestions completed");		
		return suggestions;
	}


}
