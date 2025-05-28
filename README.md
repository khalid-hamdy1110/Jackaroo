# Jackaroo: A New Game Spin 🎮🎯🧩

## Description 📜🧠🎲

**Jackaroo** is a single-player adaptation of the classic marbles-and-cards game Jackaroo, built in Java with JavaFX. You play against three CPU opponents, each controlling their own colored marbles. Draw cards, move your marbles into the safe zone, avoid trap cells, and be the first to get all four of your marbles home! 🎯🚩🏁

## Features 💡✨🛠️

- **Complete game engine** implementing all 15 card types and their actions:
  - Standard cards: move exactly N steps where some have special moves
  - Ace & King: field new marbles or move existing ones
  - Jack: swap your marble with any opponent marble on track
  - Seven: split seven steps between two marbles
  - Queen & Ten: discard an opponent’s card and skip their turn
  - Wild cards: burn (destroy) or save marbles
- **100-cell circular track** with 8 random trap cells that respawn
- **Safe zones**, **base cells**, and **home zones** for each player
- **Human vs. CPU** play: CPU uses a randomized valid strategy
- **Interactive GUI** with:
  - Click-to-select cards and marbles
  - Highlighted active/next player indicators
  - Live firepit display and split-distance control
  - Victory and trap notifications

## Gameplay Rules 📏🎴🏁

1. **Fielding**  
   - Play an **Ace** or **King** to bring a marble from your home into your base cell (if free).

2. **Movement**  
   - Play a numbered card to advance one marble exactly *N* spaces (wrapping the track), then into your safe zone if you reach your entry.

3. **Swapping**  
   - Play a **Jack** to swap one of your marbles with any opponent’s track marble (not in their base).

4. **Burning**  
   - Play the **MarbleBurner** wild card to send any opponent’s track marble back to their home. 

5. **Saving**  
   - Play the **MarbleSaver** wild card to move one of your track marbles into a random empty safe-zone cell.

6. **Discard & Skip**  
   - Play a **Queen** (next player) or **Ten** (any player) to discard one card from their hand and skip their turn.

7. **Split Seven** 
   - Play a **Seven** to split seven steps between two marbles (choose split distance 1–6).

8. **Trap Cells**  
   - Landing on a trap cell destroys your marble back to home; that trap then relocates.

9. **Winning**
   - First to move all four marbles into your safe zone wins.
  
## Installation & Running 🧱🛠️⚙️

1. **Clone** the repo:  
   ```bash
   git clone https://github.com/your-username/jackaroo.git
   cd jackaroo
   ```
2. **Build** with Maven or your IDE (requires JDK 11+).

3. **Run** controller.Main.

4. Enter your name, then click Play Turn to select cards and marbles!

## Project Structure 🗂️📁🧱

```
src/
 ├─ controller/       # JavaFX UI controller & FXML
 ├─ engine/           # Game engine interfaces & implementation
 │   └─ board/        # Board, Cell, SafeZone, BoardManager
 ├─ model/            # Domain models: Player, Marble, Colour
 │   └─ card/         # Card hierarchy (Standard, Wild, subtypes)
 ├─ exception/        # Custom exception classes
 └─ view/             # FXML layouts & CSS
```

## Skills & Technologies 💼📚🧠

- **Java 11+** & **JavaFX**
- **Object-Oriented Design**: interfaces, inheritance, encapsulation
- **Custom Exception Handling** for game-rule enforcement
- **File I/O**: loading card definitions from CSV
- **FXML & CSS** for responsive, styled GUIs
- **MVC Pattern**: clear separation between engine and UI
- **Java Collections & Streams**: filtering, sorting, mapping
- **Lambda Expressions** & **Functional Interfaces**

---

> Developed as part of CSEN 401 Computer Programming Lab, Spring 2025 (German University in Cairo) 🎓 © 2025 Khalid Hamdy, Mohamed Mostafa, Youssef Tamer
