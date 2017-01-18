import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		
		StringBuilder builder;
		File knownWordsFile = new File("knownWords.txt");
		File textFile = new File("string.txt");
		BufferedReader reader = null;
		
		String chapterMarker = "CHAPTER";
		String signalCharSequence = "!@!@!@!@!@";
		
		String knownWordsRaw;
		Matcher knownMatcher;
		
		HashSet<String> knownWords;
		LinkedHashMap<String, HashSet<String>> newWordsByChapter = new LinkedHashMap<String, HashSet<String>>();
		String currentChapter = "null chapter";
		
		//read known words file and store raw string
		System.out.println("@@loading known words");
		try {reader = new BufferedReader(new FileReader(knownWordsFile));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		builder = new StringBuilder();
		while (true) {
			try {
				String s = reader.readLine();
				if (s==null)
					break;
				builder.append(s);
			} catch (IOException e) {
				break;
			}
		}
		knownWordsRaw = builder.toString();
		System.out.println("@@done loading known words");
		
		knownMatcher = Pattern.compile(signalCharSequence).matcher(knownWordsRaw);
		knownWordsRaw = knownMatcher.replaceAll(signalCharSequence);
		knownWords = new HashSet<String>(Arrays.asList(knownWordsRaw.split(signalCharSequence)));
		
		Pattern occuredPattern = Pattern.compile("\\d|\\s|\\p{Punct}|\\u2014");
		
		//read text file and store raw string
		System.out.println("@@loading words");
		try {reader = new BufferedReader(new FileReader(textFile));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		builder = new StringBuilder();
		boolean doEnd = false;
		newWordsByChapter.put(currentChapter, new HashSet<String>());
		while (!doEnd) {
			while (true) {
				try {
					String s = reader.readLine();
					if (s==null) {
						doEnd = true;
						break;
					}else if (s.contains(chapterMarker)) {
						currentChapter = s;
						newWordsByChapter.put(s, new HashSet<String>());
						break;
					}else {
						s = s.toLowerCase();
						s = s.replaceAll("\\u201C|\\u201D|\\u2019|\\u2018", "\'");
						s = occuredPattern.matcher(s).replaceAll(signalCharSequence);
						LinkedList<String> list = new LinkedList<String>(Arrays.asList(s.split(signalCharSequence)));
						list.removeAll(knownWords);
						newWordsByChapter.get(currentChapter).addAll(list);
					}
				} catch (IOException e) {
					break;
				}
			}
		}
		
		System.out.println("@@writing new words");
		File newWordsFile = new File("newWords.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(newWordsFile));
			newWordsByChapter.forEach((chap, words) -> {
				try {writer.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+chap+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+"\n");
				} catch (IOException e) {e.printStackTrace();}
				words.forEach(word -> {
					try {writer.append(word+",\n");
					}catch(IOException e1){e1.printStackTrace();}
				});
			});
			try {writer.close();
			} catch (IOException e) {e.printStackTrace();}
		}catch (IOException e){e.printStackTrace();}
		System.out.println("@@done writing new words");
		
		////////////////////////////////////////////
		////////////////////////////////////////////
		////////////////////////////////////////////
		
//		StringBuilder builder;
//		
//		File knownWordsFile = new File("knownWords.txt");
//		File textFile = new File("string.txt");
//		
//		BufferedReader reader = null;
//		
//		String knownWordsRaw, occuredWordsRaw;
//		
//		String chapterMarker = "CHAPTER";
//		
//		//read known words file and store raw string
//		System.out.println("@@loading known words");
//		try {reader = new BufferedReader(new FileReader(knownWordsFile));
//		} catch (FileNotFoundException e) {e.printStackTrace();}
//		builder = new StringBuilder();
//		while (true) {
//			try {
//				String s = reader.readLine();
//				if (s==null)
//					break;
//				builder.append(s);
//			} catch (IOException e) {
//				break;
//			}
//		}
//		knownWordsRaw = builder.toString();
//		System.out.println("@@done loading known words");
//		
//		//read text file and store raw string
//		System.out.println("@@loading words");
//		try {reader = new BufferedReader(new FileReader(textFile));
//		} catch (FileNotFoundException e) {e.printStackTrace();}
//		builder = new StringBuilder();
//		while (true) {
//			try {
//				String s = reader.readLine();
//				if (s==null)
//					break;
//				builder.append(s+"\n");
//			} catch (IOException e) {
//				break;
//			}
//		}
//		occuredWordsRaw = builder.toString();
//		System.out.println("@@done loading words");
//		
//		HashSet<String> occuredWords;
//		HashSet<String> knownWords;
//		HashSet<String> newWords;
//		
//		Matcher knownMatcher;
//		Matcher occuredMatcher;
//		
//		String signalCharSequence = "!@!@!@!@!@";
//		
//		//process and split raw strings
//		occuredWordsRaw = occuredWordsRaw.toLowerCase();
//		occuredWordsRaw = occuredWordsRaw.replaceAll("\\u201C|\\u201D|\\u2019|\\u2018", "\'");
//		
//		knownMatcher = Pattern.compile(signalCharSequence).matcher(knownWordsRaw);
//		occuredMatcher = Pattern.compile("\\d|\\s|\\p{Punct}|\\u2014").matcher(occuredWordsRaw);
//		
//		knownWordsRaw = knownMatcher.replaceAll(signalCharSequence);
//		knownWords = new HashSet<String>(Arrays.asList(knownWordsRaw.split(signalCharSequence)));
//		occuredWordsRaw = occuredMatcher.replaceAll(signalCharSequence);
//		occuredWords = new HashSet<String>(Arrays.asList(occuredWordsRaw.split(signalCharSequence)));
//		
//		newWords = occuredWords;
//		newWords.removeAll(knownWords);
//		
//		System.out.println("@@writing new words");
//		File newWordsFile = new File("newWords.txt");
//		BufferedWriter writer = null;
//		try {writer = new BufferedWriter(new FileWriter(newWordsFile));
//		} catch (IOException e) {e.printStackTrace();}
//		LinkedList<String> tempWordList = new LinkedList<String>(newWords);
//		Collections.sort(tempWordList);
//		for (String word: tempWordList) {
//			try {writer.append("\n"+signalCharSequence+word);
//			} catch (IOException e) {e.printStackTrace();}
//		}
//		try {writer.close();
//		} catch (IOException e) {e.printStackTrace();}
//		System.out.println("@@done writing new words");
		
	}

}
