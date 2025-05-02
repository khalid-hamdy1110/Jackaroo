package model.card;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

public abstract class Card {
	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;
	
	public Card(String name, String description, BoardManager boardManager,
			GameManager gameManager) {
		this.name = name;
		this.description = description;
		this.boardManager = boardManager;
		this.gameManager = gameManager;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	// Milestone 2 Methods
	
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		 if (marbles.size() == 1) 
			 return true;
		 return false;
	}
	
	public boolean validateMarbleColours(ArrayList<Marble> marbles) {
		 if (marbles.size() != 0 && gameManager.getActivePlayerColour() != marbles.get(0).getColour()) {
			return false;
		 }
		 return true;
	 }
	 
	 public abstract void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException;
}
