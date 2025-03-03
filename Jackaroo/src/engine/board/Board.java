package engine.board;

import java.util.ArrayList;
import java.util.Random;

import model.Colour;
import engine.GameManager;

public class Board implements BoardManager{
	private final GameManager gameManager;
	private final ArrayList<Cell> track;
	private final ArrayList<SafeZone> safeZones;
	private int splitDistance;
	
	public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
		this.gameManager = gameManager;
		track = new ArrayList<Cell>();
		safeZones = new ArrayList<SafeZone>();
		splitDistance = 3;
		
		// Sets the track up with the correct cell types
		for(int i = 0; i < 100; i++) {
			if (i == 0 || i == 25 || i == 50 || i == 75) {
				track.add(new Cell(CellType.BASE));
			} else if (i == 23 || i == 48 || i == 73 || i == 98) {
				track.add(new Cell(CellType.ENTRY));
			} else {
				track.add(new Cell(CellType.NORMAL));
			}
		}
		
		// 8 random trap cells
		for (int i = 0; i<8; i++) {
			assignTrapCell();
		}
		
		// Creating SafeZones
		for(Colour colour : colourOrder) {
			safeZones.add(new SafeZone(colour));
		}
		
	}
	
	private void assignTrapCell() {
		Random rand = new Random();
		
		// Generating a random index
		int index = rand.nextInt(100);
		
		// Checking that the cell type is normal AND that the random cell is not already a trap otherwise a new index is generated
		while (!track.get(index).getCellType().equals(CellType.NORMAL) || track.get(index).isTrap()) {
			index = rand.nextInt(100);
		}
		
		// Flagging it as a trap
		track.get(index).setTrap(true);
	}
	
	public int getSplitDistance() {
		return splitDistance;
	}
	public void setSplitDistance(int splitDistance) {
		this.splitDistance = splitDistance;
	}
	public ArrayList<Cell> getTrack() {
		return track;
	}
	public ArrayList<SafeZone> getSafeZones() {
		return safeZones;
	}
}
