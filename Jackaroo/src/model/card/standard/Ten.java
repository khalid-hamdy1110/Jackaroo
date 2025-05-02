package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

public class Ten extends Standard{
	public Ten(String name, String description, Suit suit,
			BoardManager boardManager, GameManager gameManager) {
		super(name, description, 10, suit, boardManager, gameManager);
	}
	
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		 if (marbles.size() == 0 || marbles.size() == 1) 
			 return true;
		 return false;
	 }
	 
	public boolean validateMarbleColours(ArrayList<Marble> marbles) {
		if (marbles.size() != 0 && gameManager.getActivePlayerColour() != marbles.get(0).getColour()) {
			 return false;
		}
		return true;
	 }
	 
	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		
		// Discarding a random card from the next player
		if (marbles.size() == 0) {
			gameManager.discardCard(gameManager.getNextPlayerColour());
		} 
		
		// Standard moving of a marble
		if (marbles.size() == 1) {
			boardManager.moveBy(marbles.get(0), 10, false);
		}
	}
}