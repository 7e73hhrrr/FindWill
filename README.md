
Find Will

We built Find Will for our Software Design Patterns course at Astana IT University. It’s a short JavaFX mystery set in Hawkins. Will is missing, the walkie-talkie is picking up his voice, and two lights are blinking in his house. The first clue you choose comes back later, when you have to decide which signal to trust.

Team: Byerik Farida and Kashtayeva Yasmina · Group: SE-2532

GitHub repository ·
Project report on GitHub ·
UML source

In IntelliJ IDEA: Open Find-Will/pom.xml as a project, choose JDK 17, and let Maven finish loading. In the Maven window, double-click Plugins → javafx → javafx:run. You can also run the current source from a terminal with mvn javafx:run.
How to play

    Pick Eleven, Mike, Dustin, Lucas, or Max. They follow the same story, but each has a different reaction on the trail.
    At the Byers house, listen to the radio or watch the lights. Both choices give you a clue. At Will’s bicycle, take the bell or the flashlight.
    Choose a route through the woods. The bridge keeps Will’s health steady. The vines slow you down and cost him 10 points.
    Cross the gate and try your tool. If you ring the bell, listen to what the tunnel does. If you use the flashlight, look at what the beam catches. Your first clue appears again just before you choose a direction.

<details> <summary>Both endings (spoilers)</summary>

Follow the light to reach Castle Byers. Click Knock three times: the footsteps stop, someone lifts the latch, and Will opens the door. Follow the voice instead and the Demogorgon catches you in the tunnel. You can play again after either ending.
</details>
Will’s health and sound

We gave Will the health bar because the search is about reaching him in time. He starts at 100. Crossing the gate costs 5 points, reaching the fort costs 3, and he loses 2 more every 3 seconds you spend in the Upside Down. We set the minimum to 1 so players can take time to read the clues. The timer stops at either ending.

Our recording of Running Up That Hill begins on the loading screen and loops through both endings and replay. The bell, flashlight click, knocks, and capture cue play over the song. The terminal shows which playback system handled the music. If you hear silence on Ubuntu, check the selected output device and the game’s volume while its window is open.
Where we used the six patterns
Pattern	Where it appears in our game
Builder	RescueMission.Builder puts the two clue sources, the worlds, and the available routes into one playable mission.
Factory Method	HouseSource creates the house clue. KnockSource creates a fresh clue that counts the taps on the fort door.
Strategy	BridgeRoute and VineRoute each decide what happens to Will’s health when the player crosses.
Abstract Factory	HawkinsWorld and UpsideDownWorld supply scenery and scene-specific sound cues as a pair.
Observer	SearchEvents sends the latest game state to listeners that update the screen and play an entry cue.
Facade	Buttons call SearchFacade to take a tool, cross the gate, or follow a signal. The facade checks the current scene before applying the action.
Tests and build

Run mvn test from the folder containing pom.xml. The packaged version passed all 24 JUnit test invocations. They cover both endings, all five characters, health changes, clue interactions, and replay.

After changing the code, run mvn javafx:run to try your edits. To update the version used by the Ubuntu launcher:
mvn package && cp target/find-will-1.0.0.jar launch/find-will.jar

The
defense notes include our two-person walkthrough. This is an unofficial academic fan project. The character and place names come from Stranger Things. We used project-specific generated illustrations, a team-supplied music recording, and synthesized sound effects.
