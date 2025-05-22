package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;
import engine.board.Board;
import engine.board.SafeZone;
import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;

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
	
	
	// Milestone 2 methods
	
	public void selectCard(Card card) throws InvalidCardException {
		players.get(currentPlayerIndex).selectCard(card);
	}
	
	public void selectMarble(Marble marble) throws InvalidMarbleException {
		players.get(currentPlayerIndex).selectMarble(marble);
	}
	
	public void deselectAll() {
		players.get(currentPlayerIndex).deselectAll();
	}
	
	public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException {
		if (splitDistance < 1 || splitDistance > 6) {
			throw new SplitOutOfRangeException("Split distance is not within range.");
		}
		
		board.setSplitDistance(splitDistance);
	}
	
	public boolean canPlayTurn() {
		Player player = players.get(currentPlayerIndex);
		
		return player.getHand().size() == 4-turn;
	}
	
	public void playPlayerTurn() throws GameException {
		// checking if he can play turn
		players.get(currentPlayerIndex).play();
	}
	
	public void endPlayerTurn() {
        Card selected = players.get(currentPlayerIndex).getSelectedCard();
        players.get(currentPlayerIndex).getHand().remove(selected);
        firePit.add(selected);
        players.get(currentPlayerIndex).deselectAll();
        
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
        
        if(currentPlayerIndex == 0 && turn < 3) 
            turn++;
        
        else if (currentPlayerIndex == 0 && turn == 3) {
        	turn = 0;
        	for (Player p : players) {
              if(Deck.getPoolSize() < 4) {
	              Deck.refillPool(firePit);
	              firePit.clear();
              }
              ArrayList<Card> newHand = Deck.drawCards();
              p.setHand(newHand);
        	}
        		
        }
        
    }
	
	public Colour checkWin() {
		ArrayList<SafeZone> safeZones = board.getSafeZones();
		for (int i = 0; i < safeZones.size(); i++) {
			if (safeZones.get(i).isFull()) {
				return safeZones.get(i).getColour();
			}
		}
		return null;
	}
	
	public void sendHome(Marble marble) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getColour() == marble.getColour()) {
				players.get(i).regainMarble(marble);
				break;
			}
		}
	}
	
	public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
		Marble marble = players.get(currentPlayerIndex).getOneMarble();
		if (marble == null) {
			throw new CannotFieldException("No marbles left in the Home Zone to field.");
		}
		
		// Sends that marble from the home zone to the base
		board.sendToBase(marble);
		
		// Removes the marble from the home zone
		ArrayList<Marble> marbles = players.get(currentPlayerIndex).getMarbles();
		marbles.remove(marbles.indexOf(marble));
	}
	
	public void discardCard(Colour colour) throws CannotDiscardException {
        for (Player player : players) {
            if (player.getColour() == colour) {
                int handSize = player.getHand().size();
                if(handSize == 0)
                    throw new CannotDiscardException("Player has no cards to discard.");
                int randIndex = (int) (Math.random() * handSize);
                this.firePit.add(player.getHand().remove(randIndex));
            }
        }
    }

    public void discardCard() throws CannotDiscardException {
        int randIndex = (int) (Math.random() * 4);
        while(randIndex == currentPlayerIndex)
            randIndex = (int) (Math.random() * 4);

        discardCard(players.get(randIndex).getColour());
    }
	
	public Colour getActivePlayerColour() {
		return players.get(currentPlayerIndex).getColour();
	}
	
	public Colour getNextPlayerColour() {
		int nextPlayerIndex = currentPlayerIndex+1;
		
		if (nextPlayerIndex > 3) {
			nextPlayerIndex = 0;
		}
		
		return players.get(nextPlayerIndex).getColour();
	}
}
