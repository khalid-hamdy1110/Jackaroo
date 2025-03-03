package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Player;
import engine.board.Board;

public class Game implements GameManager{
	private final Board board;
	private final ArrayList<Player> players;
	private final ArrayList<Card> firePit;
	private int currentPlayerIndex;
	private int turn;
	
	public Game(String playerName) throws IOException {
		// Making an Array with the colours
		ArrayList<Colour> colourOrder = new ArrayList<Colour>();
		colourOrder.add(Colour.RED);
		colourOrder.add(Colour.BLUE);
		colourOrder.add(Colour.GREEN);
		colourOrder.add(Colour.YELLOW);
		
		// Shuffling the colours randomly
		Random rand = new Random();
		for (int i = 0; i<colourOrder.size(); i++) {
			int newIndex = rand.nextInt(colourOrder.size());
			Colour temp = colourOrder.get(i);
			colourOrder.set(i, colourOrder.get(newIndex));
			colourOrder.set(newIndex, temp);
		}
		
		// BoardManager
		board = new Board(colourOrder, this);
		
		// Load Card Pool
		Deck.loadCardPool(board, this);
		
		// Creating Players
		players = new ArrayList<Player>();
		players.add(new Player(playerName, colourOrder.get(0)));
		players.add(new CPU("CPU 1", colourOrder.get(1), board));
		players.add(new CPU("CPU 2", colourOrder.get(2), board));
		players.add(new CPU("CPU 3", colourOrder.get(3), board));
		
		// Drawing Cards
		players.get(0).setHand(Deck.drawCards());
		players.get(1).setHand(Deck.drawCards());
		players.get(2).setHand(Deck.drawCards());
		players.get(3).setHand(Deck.drawCards());
		
		currentPlayerIndex = 0;
		turn = 0;
		firePit = new ArrayList<Card>();
	}
	
	public Board getBoard() {
		return board;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public ArrayList<Card> getFirePit() {
		return firePit;
	}
}
