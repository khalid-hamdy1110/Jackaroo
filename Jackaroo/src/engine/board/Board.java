package engine.board;

import java.util.ArrayList;
import java.util.Random;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.IllegalSwapException;
import exception.InvalidMarbleException;

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
	
	
	// Milestone 2 methods
	
	// Searches for a certain list of cells of a safe zone given its colour
	private ArrayList<Cell> getSafeZone(Colour colour) {
		for (int i = 0; i<safeZones.size(); i++) {
			if (safeZones.get(i).getColour() == colour)
				return safeZones.get(i).getCells();
		}
		return null;
	}
	
	// Gets the position of a marble in the provided path
	private int getPositionInPath(ArrayList<Cell> path, Marble marble) {
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).getMarble() == marble) {
				return i;
			}
		}
		return -1;
	}
	
	// Gets the position of the base of a given colour
	private int getBasePosition(Colour colour) {
		int i;
		for(i = 0; i<safeZones.size(); i++) {
			if(safeZones.get(i).getColour() == colour)
				break;
		}
		if (i == 0) {
			return 0;
		} else if (i == 1) {
			return 25;
		} else if (i == 2) {
			return 50;
		} else if (i == 3) {
			return 75;
		} else {
			return -1;
		}
		
	}
	
	// Gets the position of the entry of a given colour
	private int getEntryPosition(Colour colour) {
		int i;
		for(i = 0; i<safeZones.size(); i++) {
			if(safeZones.get(i).getColour() == colour)
				break;
		}
		if (i == 0) {
			return 98;
		} else if (i == 1) {
			return 23;
		} else if (i == 2) {
			return 48;
		} else if (i == 3) {
			return 73;
		} else {
			return -1;
		}
	}
	
	// Validates the number of steps a marble can take and returns the path that it WILL be taking in ORDER
	private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
		// Full path that will be returned
		ArrayList<Cell> fullPath = new ArrayList<Cell>();
		
		// First check if the marble is on the track
		int currPos = getPositionInPath(track, marble);
		int entry = getEntryPosition(marble.getColour());
		ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
		boolean inSafeZone = false;
		
		// On track
		if (currPos != -1) {
			
			// Valid movement
				
			// will loop over every step and add it to the full path that the marble will move
			for (int i = 0; i < Math.abs(steps)+1; i++) {
				
				// Ensuring circular wrapping
				if (steps >= 0 && currPos > 99) { // When moving forward
					currPos = 0;
				} else if (steps < 0 && currPos < 0) { // When moving backward
					currPos = 99;
				}
				
				// If on the safe zone entry
				if (steps >= 0 && currPos == entry && marble.getColour() == gameManager.getActivePlayerColour()) { // Update the full path and set up the loop for the inside of the safe zone conditions
					fullPath.add(track.get(currPos));
					inSafeZone = true;
					currPos = -1;
				} else if (inSafeZone) { // If inside the safe zone the safe zone cells will be added to the path
					try { // if the steps exceed the safe zone then an error will be thrown
						fullPath.add(safeZone.get(currPos));
					} catch (IndexOutOfBoundsException e) {
						throw new IllegalMovementException("Rank of the card played is too high.");
					}
				} else { // In normal cases where the marble is on track we just add to the full path
					fullPath.add(track.get(currPos));
				}
				
				// Updating the current position
				if (steps >= 0) {
					currPos++;
				} else if (steps < 0) {
					currPos--;
				}
				
			}		
			
		} else { // Not on track
			
			// Check if marble is in the safe zone
			currPos = getPositionInPath(safeZone, marble);
			
			// In safe zone
			if (currPos != -1) {
				
				if (steps < 0) {
					throw new IllegalMovementException("Cannot move backwards in a safe zone.");
				}
				for (int i = 0; i < steps+1; i++) {
					try {
						fullPath.add(safeZone.get(currPos));
						currPos++;
					} catch (IndexOutOfBoundsException e) {
						throw new IllegalMovementException("Rank of the card played is too high.");
					}
				}
				
			} else { // Not in either so we throw an exception
				throw new IllegalMovementException("Marble cannot be moved.");
			}
		}
		
		return fullPath;
	}
	
	 private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalMovementException {
		 int marbleCount = 0;
		 for (int i = 1; i < fullPath.size(); i++) {
			 
			 // Current cell properties
			 Marble pathMarble = fullPath.get(i).getMarble();
			 Colour pathMarbleColour = null;
			 
			 if (pathMarble != null) {
				 pathMarbleColour = pathMarble.getColour();
				 if (marble.getColour() != pathMarbleColour)
					 marbleCount++;
			 }
			 
			 // Self Blockage
			 if (!destroy && pathMarbleColour != null && gameManager.getActivePlayerColour() == pathMarbleColour) {
				 throw new IllegalMovementException("Self Blockage.");
			 }
			 
			 // Path Blockage
			 if (!destroy && i != fullPath.size()-1 && marbleCount >= 2) {
				 throw new IllegalMovementException("Path Blockage.");
			 }
			 
			 // Safe Zone Entry
			 if (!destroy && pathMarble != null && i+1 < fullPath.size() && fullPath.get(i+1).getCellType() == CellType.SAFE) {
				 throw new IllegalMovementException("Safe Zone Entry.");
			 }
			 
			 // Base cell Blockage
			 if (pathMarble != null && getPositionInPath(track, pathMarble) == getBasePosition(pathMarbleColour)) {
				 throw new IllegalMovementException("Base Cell Blockage.");
			 }
			 
			 // No king interference if in safe zone
			 if (destroy && pathMarble != null && fullPath.get(i).getCellType() == CellType.SAFE) {
				 throw new IllegalMovementException("King interference in safe zone.");
			 }
		 }
	 }
	 
	 private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
		 
		 // Remove marble from current cell
		 Cell currCell = fullPath.get(0);
		 currCell.setMarble(null);
		 
		 if (!destroy) { // If the card played is not KING then only destroy target
			 Cell targetCell = fullPath.getLast();
			 if (targetCell.getMarble() != null)
				 destroyMarble(targetCell.getMarble());
		 } else { // If the card played is KING then destroy all marbles in its path if there is any
			 for (int i = 1; i < fullPath.size(); i++) {
				 if (fullPath.get(i).getMarble() != null) {
					 destroyMarble(fullPath.get(i).getMarble());
				 }
			 }
		 }
		 
		 // Place the marble in the target cell
		 fullPath.getLast().setMarble(marble);
		 
		 // If the target cell is a trap then destroy the marble and assign a new trap cell
		 if (fullPath.getLast().isTrap()) {
			 destroyMarble(fullPath.getLast().getMarble());
			 fullPath.getLast().setTrap(false);
			 assignTrapCell();
		 }
	 }
	 
	 private void validateSwap(Marble marble_1, Marble marble_2) throws IllegalSwapException {
		 if (getPositionInPath(track, marble_1) == -1 || getPositionInPath(track, marble_2) == -1) {
			 throw new IllegalSwapException("One of the marbles aren't on track.");
		 }
		 
		 Marble opponentMarble = (marble_1.getColour() == gameManager.getActivePlayerColour())?marble_2:marble_1;
		 if (getPositionInPath(track, opponentMarble) == getBasePosition(opponentMarble.getColour())) {
			 throw new IllegalSwapException("One of the marbles are on the base cell.");
		 }
	 }
	 
	 private void validateDestroy(int positionInPath) throws IllegalDestroyException {

		 if (positionInPath == -1) {
			 throw new IllegalDestroyException("Marble not on track.");
		 }
		 
		 Marble pathMarble = track.get(positionInPath).getMarble();
		 if (pathMarble != null && positionInPath == getBasePosition(pathMarble.getColour())) {
			 throw new IllegalDestroyException("Marble on Base cell."); 
		 }
	 }
	 
	 private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException {
		 if(occupiedBaseCell.getMarble().getColour() == gameManager.getActivePlayerColour()) {
			 throw new CannotFieldException("Your marble is already in the base cell");
		 }
	 }
	 
	 private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException {
		 if (positionInSafeZone != -1) {
			 throw new InvalidMarbleException("Already in Safe Zone");
		 }
		 
		 if (positionOnTrack == -1) {
			 throw new InvalidMarbleException("Not on track.");
		 }
	 }
	 
	 public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException {
		 ArrayList<Cell> fullPath = validateSteps(marble, steps);
		 validatePath(marble, fullPath, destroy);
		 move(marble, fullPath, destroy);
	 }
	 
	 public void swap(Marble marble_1, Marble marble_2) throws IllegalSwapException {
		 validateSwap(marble_1, marble_2);
		 int pos1 = getPositionInPath(track, marble_1);
		 int pos2 = getPositionInPath(track, marble_2);
		 
		 Cell temp = track.get(pos1);
		 track.set(pos1, track.get(pos2));
		 track.set(pos2, temp);
	 }
	
	 public void destroyMarble(Marble marble) throws IllegalDestroyException {

		 int positionInPath = getPositionInPath(track, marble);
		 
		 if (marble.getColour() != gameManager.getActivePlayerColour())
			 validateDestroy(positionInPath);
		 
		 if (positionInPath != -1)
			 track.get(positionInPath).setMarble(null);
		 
		 gameManager.sendHome(marble);
	 }
	 
	 public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {
		 Marble marbleOnTrack = track.get(getBasePosition(marble.getColour())).getMarble();
		 if (marbleOnTrack != null) {
			 validateFielding(track.get(getBasePosition(marble.getColour())));
			 destroyMarble(marbleOnTrack);
		 }
		 
		 track.get(getBasePosition(marble.getColour())).setMarble(marble);
	 }
	 
	 public void sendToSafe(Marble marble) throws InvalidMarbleException {
		 
		 // Validating Saving
		 ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
		 int positionInSafeZone = getPositionInPath(safeZone, marble);
		 int positionOnTrack = getPositionInPath(track, marble);
		 validateSaving(positionInSafeZone, positionOnTrack);
		 
		 track.get(positionOnTrack).setMarble(null);
		 Random rand = new Random();
		 while (true) {
			 int curr = rand.nextInt(4);
			 if (safeZone.get(curr).getMarble() == null) {
				 safeZone.get(curr).setMarble(marble);
				 break;
			 }
		 }
	 }
	 
	 public ArrayList<Marble> getActionableMarbles() {
		 ArrayList<Marble> actionableMarbles = new ArrayList<Marble>();
		 
		 // Track marbles
		 for (int i = 0; i < track.size(); i++) {
			 if (track.get(i).getMarble() != null) {
				 actionableMarbles.add(track.get(i).getMarble());
			 }
		 }
		 
		 // Safe zone marbles
		 ArrayList<Cell> safeZone = getSafeZone(gameManager.getActivePlayerColour());
		 for (int i = 0; i < safeZone.size(); i++) {
			 if (safeZone.get(i).getMarble() != null) {
				 actionableMarbles.add(safeZone.get(i).getMarble());
			 }
		 }
		 
		 return actionableMarbles;
	 }
	 
}

