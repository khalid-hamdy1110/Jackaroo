# Jackaroo: A New Game Spin

![Jackaroo Screenshot](./assets/screenshot.png)

## Description

**Jackaroo** is a single-player adaptation of the classic marbles-and-cards game Jackaroo, built in Java with JavaFX. You play against three CPU opponents, each controlling their own colored marbles. Draw cards, move your marbles into the safe zone, avoid trap cells, and be the first to get all four of your marbles home!

## Features

- **Full game engine** implementing all card actions (movement, swap, burn, save, split, discard, etc.)
- **100-cell circular track** with random trap cells that respawn on each destruction
- **Safe zones** and **base cells** for each player
- **Human vs. CPU** play: CPU players employ random-but-valid strategies
- **Animated turn loop** with 2-second CPU delays
- **Interactive GUI** with:
  - Click-to-select cards and marbles  
  - Highlighted active and next player  
  - Live firepit and split-distance controls  
  - Victory and trap notifications  

## Gameplay Rules

1. **Fielding** — Play an Ace or King to bring a marble from your home into your base cell.  
2. **Movement** — Play a numbered card to advance one marble exactly *N* spaces, wrapping around the track, then into your safe zone if you reach your entry cell.  
3. **Swap** — Play a Jack to swap one of your marbles with any opponent marble on the track.  
4. **Burn** — Play the “MarbleBurner” wild card to send any opponent’s track marble back to their home.  
5. **Save** — Play the “MarbleSaver” wild card to move one of your track marbles into a random empty safe-zone cell.  
6. **Discard & Skip** — Play a Queen or Ten to discard a card from the next (Queen) or any (Ten) opponent’s hand and skip their turn.  
7. **Split Seven** — Play a Seven to split seven steps between two marbles (1–6 and 6–1).  
8. Land on a **trap cell**, and your marble goes back to your home — traps reassign after each destruction.  
9. **Win** by getting all four of your marbles into your safe zone before your opponents.
  
## Installation & Running 🧱🛠️⚙️

Build with Maven or your preferred IDE (requires JDK 11+).

Run the JavaFX application:

- **From your IDE**: run `controller.MainApp#main()`
- **Or via Maven**:

  ```bash
  mvn javafx:run
  ```

Enter your name in the prompt, then click Play Turn to select cards and marbles! 🎮🧠🎯😃

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

> Developed as part of CSEN 401 Computer Programming Lab, Spring 2025 (German University in Cairo) 👨‍💻🎓📘 © 2025 Khalid Hamdy, Mohamed Mostafa

```
```
