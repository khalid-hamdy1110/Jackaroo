package model.card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import model.card.standard.*;
import model.card.wild.*;
import engine.GameManager;
import engine.board.BoardManager;

public class Deck {
	private static final String CARDS_FILE = "Cards.csv";
	private static ArrayList<Card> cardsPool;
	
	public static void loadCardPool(BoardManager boardManager, GameManager gameManager) 
			throws IOException {
		
		BufferedReader br = null;
		try {
			
			// Initializing the cardsPool array
			cardsPool = new ArrayList<Card>();
			
			br = new BufferedReader(new FileReader(CARDS_FILE)); 
			String row; // To store the line read from the CSV file
			while ((row = br.readLine()) != null) { // Reads a line, stores it and checks if it is null
	            String[] values = rowSplitter(row); // Splitting the values into an array of Strings
	            
	            Card c;
	            
	            // {0 code, 1 frequency, 2 name, 3 description, 4 rank, 5 suit}
	            switch(values[0]) { // Choosing the type of card
	            case "1":
	            	c = new Ace(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
	            case "13":
	            	c = new King(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
	            case "12":
	            	c = new Queen(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
	            case "11":
	            	c = new Jack(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "10":
					c = new Ten(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	        		break;
				case "7":
	            	c = new Seven(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "5":
	            	c = new Five(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "4":
	            	c = new Four(values[2], values[3], Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "14":
	            	c = new Burner(values[2], values[3], boardManager, gameManager);
	            	break;
				case "15":
	            	c = new Saver(values[2], values[3], boardManager, gameManager);
	            	break;
	            default:
	            	c = new Standard(values[2], values[3], Integer.parseInt(values[4]), Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
	            }
	            
	            // Adds to the cardsPool the cards
	            for(int i = 0; i<Integer.parseInt(values[1]); i++) {
	            	cardsPool.add(c);
	           }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}
	
	public static ArrayList<Card> drawCards() {
		// Shuffling using Collections
		Collections.shuffle(cardsPool);
        ArrayList<Card> cards = new ArrayList<>(cardsPool.subList(0, 4));
        cardsPool.subList(0, 4).clear();
        return cards;
	}
	
	// Used to account for the cases where the CSV contains <"> and commas inside
	private static String[] rowSplitter(String row) {
		ArrayList<String> result = new ArrayList<String>();	
		String currentItem = "";
		boolean quotation = false;
		
		// Iterates through each character in the row and adds to the String list
		for (int i = 0; i < row.length(); i++) {
			if (row.charAt(i) == '"') {
				quotation = !quotation;
			} else if (row.charAt(i) == ',' && !quotation) {
				result.add(currentItem);
				currentItem = "";
			} else {
				currentItem += row.charAt(i);
			}
		}
		
		result.add(currentItem);
		
		// Moving the items in the String list into the string array
		String[] resultArr = new String[result.size()];
		for(int i = 0; i < result.size(); i++) {
			resultArr[i] = result.get(i);
		}
		
		return resultArr;
	}
	
	
	// Milestone 2 methods
	
	public static void refillPool(ArrayList<Card> cards) {
		cardsPool.addAll(cards);
	}
	
	public static int getPoolSize() {
		 // if we use .remove then this is correct
		 return cardsPool.size();
	 }
}