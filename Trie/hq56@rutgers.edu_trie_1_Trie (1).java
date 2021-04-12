package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	  
	   // prevent instantiation
	   private Trie() { }
	  
	   /**
	   * Builds a trie by inserting all words in the input array, one at a time,
	   * in sequence FROM FIRST TO LAST */
	   public static TrieNode buildTrie(String[] allWord){
	       String [] aWord = allWord;
	       TrieNode root = new TrieNode(null, null, null);
	       if(aWord.length==0) return root;
	      
	       TrieNode fWord = new TrieNode(new Indexes(0, (short)0 , (short) (aWord[0].length()-1)), null, null); //Creates the first Node with full first word
	       root.firstChild = fWord;
	      
	       TrieNode ptr = root.firstChild;
	       TrieNode pptr= null;
	      
	       for(int i = 1; i < aWord.length; i++){
	           String prefix = "";
	           String insert = aWord[i];
	          
	           while(ptr != null){
	               int wIndex = ptr.substr.wordIndex;
	               int sIndex = ptr.substr.startIndex;
	               int eIndex = ptr.substr.endIndex;
	               String nodeP = aWord[wIndex].substring(sIndex, eIndex+1);
	               if(insert.indexOf(prefix+nodeP) == 0) { 
	                   prefix = prefix + nodeP;
	                   pptr = ptr;
	                   ptr = ptr.firstChild;
	                   continue;
	               }
	              
	               if(insert.charAt(sIndex) != nodeP.charAt(0)){
	                   pptr = ptr;
	                   ptr = ptr.sibling;
	                   continue;
	               }
	              
	               int count = 0;
	               for(int j = 0; j < nodeP.length(); j++){
	                   if(insert.charAt(sIndex+j) == nodeP.charAt(j)) count++;
	                   else break;
	               }
	               int clip = sIndex + count;
	              
	               TrieNode tempN = new TrieNode(new Indexes(wIndex, (short)clip, (short)eIndex), ptr.firstChild, null);
	               ptr.firstChild = tempN;
	               ptr.substr.endIndex = (short) (clip-1);
	               prefix = prefix + aWord[wIndex].substring(sIndex, clip);
	               pptr = ptr;
	               ptr = ptr.firstChild;
	           }
	           Indexes finalIndex = new Indexes(i, (short)(prefix.length()), (short)(insert.length()-1));
	           TrieNode add = new TrieNode(finalIndex, null, null);
	           pptr.sibling = add; 
	           ptr = root.firstChild; 
	           pptr = null;
	       }
	       return root;
	   }
	   public static ArrayList<TrieNode> completionList(TrieNode root,String[] allWords, String prefix){
	       /** COMPLETE THIS METHOD **/
	       ArrayList<TrieNode> cList = new ArrayList<>();
	       TrieNode ptr = root.firstChild, prev = null;
	       String totPrefix = "";
	      
	       while(prefix.length() > totPrefix.length() && ptr != null){
	           int wIndex = ptr.substr.wordIndex;
	           int sIndex = ptr.substr.startIndex;
	           int eIndex = ptr.substr.endIndex;
	           prev = ptr;
	           String curPre = allWords[wIndex].substring(sIndex, eIndex+1);
	           if(prefix.indexOf(totPrefix+curPre) == 0){
	               totPrefix = totPrefix + curPre;
	               ptr = ptr.firstChild;
	               continue;
	           }
	           if((totPrefix+curPre).indexOf(prefix) == 0){
	               ptr = ptr.firstChild;
	               break;
	           }
	          
	           ptr = ptr.sibling;
	       }
	       if(ptr == null){
	           if(allWords[prev.substr.wordIndex].indexOf(prefix) == 0){
	               cList.add(prev);
	               return cList;
	           }
	           return null;
	       }
	       addWords(cList, ptr);
	       return cList;
	   }
	   private static void addWords(ArrayList<TrieNode> completionList, TrieNode start){
	       if(start == null) return;
	       if(start.firstChild == null){
	           completionList.add(start);
	           addWords(completionList, start.sibling);
	           return;
	       }
	       addWords(completionList, start.firstChild);
	       addWords(completionList, start.sibling);
	      
	   }
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
