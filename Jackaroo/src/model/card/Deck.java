package model.card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
			while ((row = br.readLine()) != null) { // Reads a line and stores it and checks if it is null
	            String[] values = row.split(","); // Splitting the values into an array of Strings

	            Card c;
	            String description = values[3].replaceAll("\"", "");
	            
	            // {0 code, 1 frequency, 2 name, 3 description, 4 rank, 5 suit}
	            switch(values[0]) { // Choosing the type of card
	            case "1":
	            	c = new Ace(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
	            case "13":
	            	c = new King(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
	            case "12":
	            	c = new Queen(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
	            case "11":
	            	c = new Jack(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "10":
					c = new Ten(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	        		break;
				case "7":
	            	c = new Seven(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "5":
	            	c = new Five(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "4":
	            	c = new Four(values[2], description, Suit.valueOf(values[5]), boardManager, gameManager);
	            	break;
				case "14":
	            	c = new Burner(values[2], description, boardManager, gameManager);
	            	break;
				case "15":
	            	c = new Saver(values[2], description, boardManager, gameManager);
	            	break;
	            default:
	            	c = new Standard(values[2], description, Integer.parseInt(values[4]), Suit.valueOf(values[5]), boardManager, gameManager);
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
		ArrayList<Card> newPool = new ArrayList<Card>();
		Random rand = new Random();
		
		// Shuffling the cardsPool
		for (int i = 0; i<cardsPool.size(); i++) {
			int newIndex = rand.nextInt(cardsPool.size());
			Card temp = cardsPool.get(i);
			cardsPool.set(i, cardsPool.get(newIndex));
			cardsPool.set(newIndex, temp);
		}
		
		// Removes First 4 cards from cardsPool if its not already empty then adds it to newPool
		int i = 0;
		while((!cardsPool.isEmpty()) && i<4) {
			newPool.add(cardsPool.remove(0));
			i++;
		}
		
		return newPool;
	}
}
