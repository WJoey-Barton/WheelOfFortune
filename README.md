
🎡 Wheel of Fortune

A multi-player, round-based word puzzle game built with JavaFX. Compete against friends or AI to spin the wheel, guess letters, and solve hidden phrases across three dynamic rounds.

🚀 Features
* **Multi-player Support:** Always supports 3 players. If fewer than 3 humans join, the game automatically initializes **Computer Players** to fill the remaining slots.
* **Dynamic Game Engine:** Manages complex turn logic, phrase progression, and state tracking using efficient `HashMap` data structures.
* **Interactive GUI:** A polished JavaFX interface featuring a puzzle board, an interactive wheel, and real-time score tracking.
* **AI Opponents:** Computer players can spin and guess letters, providing a challenging solo or duo experience.
* **Automated Logging:** Tracks "Bankrupt" and "Lose-a-Turn" states instantly through the backend engine.

🛠 Technical Architecture

The project follows a decoupled architecture to separate game logic from the visual interface:

* **GameEngine:** The "brain" of the app. It uses a `HashMap<Player, PlayerState>` for O(1) access to player scores and guess history. It also employs a custom hash function to map phrases to players.
* **SceneController:** The bridge between the backend and the UI. It listens for user actions (spins/guesses) and updates the JavaFX stage.
* **Phrase & LetterSquare:** The puzzle is broken down into a `PhraseLayout` grid, where individual `LetterSquare` objects manage the visibility of hidden characters.
* **Player vs. PlayerState:** `Player` stores persistent data (name, total points), while `PlayerState` handles volatile round data (current turn status, bankrupt status).

🎮 How to Play

1.  **Setup:** Launch the application and enter names for the human players. Any empty slots will be filled by AI.
2.  **The Turn:** When your name is highlighted, click the **Wheel** to spin.
3.  **Action:**
    * **Land on a Value:** Guess a consonant. If correct, your score increases by $(Value \times Occurrences)$. You then spin again.
    * **Bankrupt/Lose-a-Turn:** Your turn ends immediately (and your round score is cleared if you hit Bankrupt).
    * **Solve:** If you know the phrase, you can skip guessing a letter and attempt to solve the entire puzzle.
4.  **Winning:** The player with the highest accumulated points after three rounds is declared the winner!

**Note:** Computer players are programmed to spin and guess letters but will never attempt to solve the full phrase—that's up to the humans!

👥 Contributors
* Joey Barton – UI/UX Design & AI Logic
* Christina KC – Game Engine Logic & Data Structures
* **Partner Name** – Backend & Lead Integration
