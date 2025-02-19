/*
 * Course: Software Tools & Process
 * Spring 2025
 * Group
 ** @author childressg
 ** @author OrugPeli
 ** @author Griffithjr
 ** @author kozakq
 * @version 1.0
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;


public class Dictionary {

	private List<String> wordList;

	/**
	 * no argument constructor for dictionary object
	 */
	public Dictionary(){
		loadToList(new File("data/wordle-full-1.txt"));
	}

	/**
	 * constructor for dictionary taking the txt file
	 * @param filePath file path of txt file
	 */
	public Dictionary(String filePath){
		loadToList(new File(filePath));
	}

	/**
	 * method to get a random word
	 */
	public String getRandomWord(){
		return wordList.get((int)(Math.random() * wordList.size()));
	}

	/**
	 * method to check if a word is contained inside the dictionary
	 * @param word word to check
	 */
	public boolean isValidWord(String word){
		return wordList.contains(word);
	}

	private void loadToList(File file) {
		try (Scanner scanner = new Scanner(new File("file.txt"))) {
			while (scanner.hasNextLine()) {
				wordList.add(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}