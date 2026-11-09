# 🔦 FIND WILL
### A Stranger Things Inspired Game

**Software Design Patterns — Final Project**  
Astana IT University | SE-2532

**Developed by:** Byerik Farida & Kashtayeva Yasmina

---

## 🎮 About the Game

**Find Will** is a story-based adventure game inspired by *Stranger Things*, developed in Java using JavaFX.

Will Byers has disappeared, and it's up to you to find him.

Explore Hawkins, collect clues, make decisions, and enter the Upside Down to bring Will back. Your choices affect what happens during the search, and not every decision leads to a happy ending.

## 🕹️ Gameplay

You can play as one of five characters:

- Eleven
- Mike
- Dustin
- Lucas
- Max

All characters follow the same main storyline, but the decisions you make determine how the adventure ends.

**What you can do:**
- Explore different locations in Hawkins.
- Investigate clues and collect useful items.
- Choose between different paths.
- Enter the Upside Down.
- Keep an eye on Will's health.
- Discover two possible endings.

The game includes background music, sound effects, and interactive scenes.

## 💻 How to Run

### Option 1: Ubuntu

**Requirements:**
- Ubuntu (64-bit Intel/AMD)
- Java 17 or newer
- Internet connection for the first launch
- `curl`

Download the project and open the `Find-Will` folder in your terminal.

Run:

```bash
bash Play-on-Ubuntu.sh
```

The script downloads the required JavaFX files on the first launch and starts the game.

### Option 2: IntelliJ IDEA

1. Download or clone the repository.
2. Open the `Find-Will` folder containing `pom.xml`.
3. Select **JDK 17**.
4. Wait for Maven to load the dependencies.
5. Open the Maven panel.
6. Go to **Plugins → javafx → javafx:run**.

Or run the following command in the project folder:

```bash
mvn javafx:run
```

## 🛠️ Technologies

| Technology | Purpose |
|---|---|
| Java 17 | Game logic |
| JavaFX | Game interface |
| Maven | Project dependencies and build |
| JUnit 5 | Unit testing |
| Git & GitHub | Version control |

## 🧩 Design Patterns

We used **6 design patterns** in this project as part of our Software Design Patterns course.

| Pattern | Implementation |
|---|---|
| **Builder** | Creates the rescue mission with its locations, clues, and routes. |
| **Factory Method** | Creates clues and interactive objects during the game. |
| **Abstract Factory** | Provides different backgrounds and sounds for Hawkins and the Upside Down. |
| **Strategy** | Handles different routes and their effects on Will's health. |
| **Observer** | Updates the health bar and triggers sounds when game events happen. |
| **Facade** | Connects the game actions and manages transitions between scenes. |

These patterns help us organize the game logic and keep different parts of the project separate.

## ❤️ Will's Health

Will starts with **100 health points**.

His health changes depending on your decisions:

- Taking the vines route costs 10 health points.
- While in the Upside Down, Will loses 2 health points every 3 seconds.
- Health cannot drop below 1 during the search.
- After rescuing Will, the health loss stops.

Choose carefully!

## 🧪 Testing

We used JUnit 5 to test the main game logic.

To run the tests:

```bash
mvn test
```

**24 tests passed** in our packaged version.

The tests cover game decisions, health changes, scene transitions, and restarting the game.

## 📁 Project

The main project files are inside the `Find-Will` folder.

```text
FindWill/
│
├── Find-Will/
│   ├── src/
│   ├── launch/
│   ├── pom.xml
│   └── Play-on-Ubuntu.sh
│
└── README.md
```

## 👩‍💻 Authors

**Byerik Farida**  
**Kashtayeva Yasmina**

Astana IT University  
School of Software Engineering  
Group SE-2532

**Course:** Software Design Patterns

---

*Inspired by Stranger Things. Created as a university final project.*
