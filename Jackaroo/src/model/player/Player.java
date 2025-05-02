package model.player;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import model.Colour;
import model.card.Card;

public class Player {
	private final String name;
	private final Colour colour;
	private ArrayList<Card> hand;
	private final ArrayList<Marble> marbles;
	private Card selectedCard;
	private final ArrayList<Marble> selectedMarbles;
	
	public Player(String name, Colour colour) {
		this.name = name;
		this.colour = colour;
		hand = new ArrayList<Card>();
		marbles = new ArrayList<Marble>();
		selectedMarbles = new ArrayList<Marble>();
		
		for(int i = 0; i<4; i++) {
			marbles.add(new Marble(colour));
		}
		
		selectedCard = null;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}
	public String getName() {
		return name;
	}
	public Colour getColour() {
		return colour;
	}
	public ArrayList<Marble> getMarbles() {
		return marbles;
	}
	public Card getSelectedCard() {
		return selectedCard;
	}
	
	
	// Milestone 2 methods
	
	public void regainMarble(Marble marble) {
		marbles.add(marble);
	}
	
	public Marble getOneMarble() {
		try {
			Marble marble = marbles.getFirst();
			return marble;
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public void selectCard(Card card) throws InvalidCardException {
		if (hand.indexOf(card) != -1) {
			selectedCard = card;
		} else {
			throw new InvalidCardException("Card not in hand.");
		}
	}
	
	public void selectMarble(Marble marble) throws InvalidMarbleException {
		if (selectedMarbles.size() >= 2) {
			throw new InvalidMarbleException("Can't select more than two marbles.");
		} else if (selectedMarbles.indexOf(marble) == -1){
			selectedMarbles.add(marble);
		}
	}
	
	public void deselectAll() {
		selectedCard = null;
		selectedMarbles.clear();
	}
	
	public void play() throws GameException {
		if (selectedCard == null) {
			throw new InvalidCardException("No card selected");
		}
		if(!selectedCard.validateMarbleSize(selectedMarbles)) {
			throw new InvalidMarbleException("Invalid marble size");
		}
		if(!selectedCard.validateMarbleColours(selectedMarbles)) {
			throw new InvalidMarbleException("Invalid marble colours");
		}
		
		selectedCard.act(selectedMarbles);
	}
}
