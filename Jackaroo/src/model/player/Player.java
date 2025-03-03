package model.player;

import java.util.ArrayList;

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
	
	
}
