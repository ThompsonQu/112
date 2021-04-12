package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile)throws FileNotFoundException {
				if (docFile == null)
					throw new FileNotFoundException();

				HashMap<String, Occurrence> map = new HashMap<String, Occurrence>();

				Scanner sc = new Scanner(new File(docFile));

				while (sc.hasNext()){
					String keyw = getKeyword(sc.next());
					if (keyw != null){
						if (map.containsKey(keyw)){
							Occurrence oc = map.get(keyw);
							oc.frequency++;
						}
						else{
							Occurrence oc = new Occurrence(docFile, 1);
							map.put(keyw, oc);
						}
					}
				}
				return map;
			}
			
			/**
			 * Merges the keywords for a single document into the master keywordsIndex
			 * hash table. For each keyword, its Occurrence in the current document
			 * must be inserted in the correct place (according to descend
			 * ing order of
			 * frequency) in the same keyword's Occurrence list in the master hash table. 
			 * This is done by calling the insertLastOccurrence method.
			 * 
			 * @param kws Keywords hash table for a document
			 */
			public void mergeKeywords(HashMap<String,Occurrence> kws) {
				for (String k : kws.keySet())
				{
					ArrayList<Occurrence> oc = new ArrayList<Occurrence>();

					if (keywordsIndex.containsKey(k))
						oc = keywordsIndex.get(k);
					
					oc.add(kws.get(k));
					insertLastOccurrence(oc);
					keywordsIndex.put(k, oc);
				}
			}
			
			/**
			 * Given a word, returns it as a keyword if it passes the keyword test,
			 * otherwise returns null. A keyword is any word that, after being stripped of any
			 * trailing punctuation(s), consists only of alphabetic letters, and is not
			 * a noise word. All words are treated in a case-INsensitive manner.
			 * 
			 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
			 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
			 * 
			 * If a word has multiple trailing punctuation characters, they must all be stripped
			 * So "word!!" will become "word", and "word?!?!" will also become "word"
			 * 
			 * See assignment description for examples
			 * 
			 * @param word Candidate word
			 * @return Keyword (word without trailing punctuation, LOWER CASE)
			 */
			public String getKeyword(String word) {
				if ((word == null) || (word.equals(null))) {
					return null;
				}
				word = word.toLowerCase();

				if (containsc(word)) {
					return null;
				}
				word = removePunc(word);

				if (noiseWords.contains(word)) {
					return null;
				}
				if (word.length() <= 0) {
					return null;
				}
				return word;
			}
			private boolean containsc(String word)
			{
				int counter = 0;
				boolean f = false;
				while (counter < word.length())
				{
					char c = word.charAt(counter);
					
					if (!(Character.isLetter(c)))
						f = true;

					if ((f) && (Character.isLetter(c)))
						return true;

					counter++;
				}
				return false;
			}

			private String removePunc(String word)
			{
				int counter = 0;
				while (counter < word.length())
				{
					char c = word.charAt(counter);
					if (!(Character.isLetter(c)))
						break;
					counter++;
				}
				return word.substring(0, counter);
				
			}
			public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
				if (occs.size() < 2)
					return null;

				int left = 0;
				int right = occs.size()-2;
				int t = occs.get(occs.size()-1).frequency;
				int m = 0;
				ArrayList<Integer> mid = new ArrayList<Integer>();

				while (right >= left)
				{
					m = ((left + right) / 2);
					int d = occs.get(m).frequency;
					mid.add(m);

					if (d == t)
						break;

					else if (d < t)
					{
						right = m - 1;
					}

					else if (d > t)
					{
						left = m + 1;
						if (right <= m)
							m = m + 1;
					}
				}
				
				mid.add(m);

				Occurrence temp = occs.remove(occs.size()-1);
				occs.add(mid.get(mid.size()-1), temp);

				return mid;
			}
		
			public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
				Scanner sc = new Scanner(new File(noiseWordsFile));
				while (sc.hasNext()) {
					String word = sc.next();
					noiseWords.add(word);
				}
				sc = new Scanner(new File(docsFile));
				while (sc.hasNext()) {
					String docFile = sc.next();
					HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
					mergeKeywords(kws);
				}
				sc.close();
			}
			
			public ArrayList<String> top5search(String kw1, String kw2) {
				ArrayList<String> result = new ArrayList<String>();
				ArrayList<Occurrence> o1 = new ArrayList<Occurrence>();
				ArrayList<Occurrence> o2 = new ArrayList<Occurrence>();
				ArrayList<Occurrence> comb = new ArrayList<Occurrence>();
				
				if (keywordsIndex.containsKey(kw1))
					o1 = keywordsIndex.get(kw1);
				
				if (keywordsIndex.containsKey(kw2))
					o2 = keywordsIndex.get(kw2);
				
				comb.addAll(o1);
				comb.addAll(o2);
				
				if ((o1.isEmpty()==false) && (o2.isEmpty())==false){
					for (int i1 = 0; i1 < comb.size()-1; i1++)
					{
						for (int i2 = 1; i2 < comb.size()-i1; i2++)
						{
							if (comb.get(i2-1).frequency < comb.get(i2).frequency)
							{
								Occurrence temp = comb.get(i2-1);
								comb.set(i2-1, comb.get(i2));
								comb.set(i2,  temp);
							}
						}
					}

					for (int i3 = 0; i3 < comb.size()-1; i3++)
					{
						for (int i4 = i3 + 1; i4 < comb.size(); i4++)
						{
							if (comb.get(i3).document == comb.get(i4).document)
								comb.remove(i4);
						}
					}
				}

				while (comb.size() > 5)
					comb.remove(comb.size()-1);
				
				System.out.println(comb);
				
				for (Occurrence oc : comb)
					result.add(oc.document);

				return result;
			}
		}

