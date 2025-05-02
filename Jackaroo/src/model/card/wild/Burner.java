package model.card.wild;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

public class Burner extends Wild{
	public Burner(String name, String description, BoardManager boardManager,
			GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}
	
	@Override
	public boolean validateMarbleColours(ArrayList<Marble> marbles) {
		 if (gameManager.getActivePlayerColour() == marbles.get(0).getColour()) {
			return false;
		 }
		 return true;
	 }
	 
	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		
		// Burn a selected marble
		boardManager.destroyMarble(marbles.get(0));
	}
}