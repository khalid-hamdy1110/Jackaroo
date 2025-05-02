package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

public class Ace extends Standard{
	public Ace(String name, String description, Suit suit,
			BoardManager boardManager, GameManager gameManager) {
		super(name, description, 1, suit, boardManager, gameManager);
	}
	
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		 if (marbles.size() == 0 || marbles.size() == 1) 
			 return true;
		 return false;
	 }
	 
	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		
		// Fielding a marble
		if (marbles.size() == 0) {
			gameManager.fieldMarble();
		}
		
		// Standard moving of a marble
		if (marbles.size() == 1) {
			boardManager.moveBy(marbles.get(0), 1, false);
		}
	}
}