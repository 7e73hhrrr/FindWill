# Find Will
Astana IT University · SE-2532 · Software Design Patterns  
Kashtayeva Yasmina and Byerik Farida

## Play on Ubuntu (Intel/AMD)
Open a terminal inside the extracted **Find-Will** folder and run:

```bash
bash Play-on-Ubuntu.sh
```

Requires a desktop session, Java 17 or newer and curl. The first launch downloads four OpenJFX libraries from Maven Central and checks their SHA-256 hashes. Later launches use the local cache. This script launches the included build in launch/find-will.jar.

## Run or edit in IntelliJ
Open pom.xml as a project, choose JDK 17, load Maven, then double-click **Maven → Plugins → javafx → javafx:run**. With Maven installed, run **mvn javafx:run**. This version runs your current source edits. Use **mvn test** to run the tests and **mvn package** to rebuild. To refresh the Ubuntu launch build after editing, copy target/find-will-1.0.0.jar to launch/find-will.jar.

## Play
1. Choose a friend. At the Byers house, inspect either the walkie or the blinking bulbs. Both reveal a different lead. Your lead reappears at the fork.
2. Find Will’s bicycle and take one tool. The bell tests whether the tunnel copies sounds. The flashlight lets you see what is waiting inside.
3. Choose a way through the woods, cross the gate and use your tool. Compare what you discover with your first clue.
4. Follow the double flash to Castle Byers or the voice into the tunnel. At the fort, tap **Knock** three times to bring Will home. The tunnel leads to capture. Either ending lets you play again.

The meter tracks Will. The slow vine route costs 10 points; the bridge saves that time. Crossing the gate costs 5, and reaching the fort costs 3. While you wait at either Upside Down location, Will loses 2 more every 3 seconds. Health stops at 1 so you can read and still choose either ending. The timer stops as soon as the search ends.

## Sound
The supplied Running Up That Hill recording starts on the loading screen and loops throughout the session, including replay. Tool sounds, knocks and the capture cue play separately. The app first tries Java Sound PCM, then JavaFX Media and finally ffplay when installed. It prints the selected backend to the terminal. If the computer stays silent, check the operating system’s selected output and application volume while the game is open. The program reports when all playback backends fail.

## Defense materials
See docs/Defense.md, docs/Report.pdf and docs/Patterns.puml. The source code demonstrates six patterns. Artwork is original AI-generated project art; the background recording was supplied by the team and the short effects were synthesized for the game. The fan setting and character names are used in an unofficial academic project.
