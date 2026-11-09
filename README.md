
Find Will

Byerik Farida and Kashtayeva Yasmina  · SE-2532 · Astana IT University

For our Software Design Patterns final project, we made a small JavaFX game based on Stranger Things. You play as one of Will’s friends and try to bring him home. We gave all five characters the same search so we could focus on the choices along the way.
Playing the game

Pick Eleven, Mike, Dustin, Lucas, or Max. You start at the Byers house, where you can listen to the walkie-talkie or watch the Christmas lights. Either choice gives you something to remember later.

At Will’s bicycle, you can take the bell or the flashlight. If you take the bell, you can ring it at the tunnel and hear something ring back. If you take the flashlight, you get a look inside. When you finally hear a voice calling for help, you have to decide whether you believe it. There are two endings, so you can play again and try the other path.

The health bar belongs to Will. Cutting through the vines costs him 10 points, and he loses 2 more every 3 seconds you spend in the Upside Down. We stop the bar at 1 so you can finish reading a clue before you choose. Running Up That Hill plays throughout the game, with the bell, knocks, and other sounds over it.
Running it

In IntelliJ: Open the Find-Will folder that contains pom.xml, select JDK 17, and let Maven load the project. In the Maven window, double-click Plugins → javafx → javafx:run. You can also start it from that folder with mvn javafx:run.

On Ubuntu: To play the included build on a 64-bit Intel or AMD laptop, open a terminal in the same folder and run:
bash Play-on-Ubuntu.sh

You’ll need Java 17 or newer and curl. The first launch needs internet access to download JavaFX.
How we used the patterns
Pattern	What we used it for
Builder	We use RescueMission.Builder at startup to put the clues, routes, and two worlds into one mission.
Factory Method	HouseSource creates the clue you inspect in the house. KnockSource creates a fresh door clue each time you reach the fort, so the knock count starts at zero.
Strategy	The bridge and the vines each have their own crossing rule. The player picks the route, and that route decides how much health Will keeps.
Abstract Factory	Hawkins and the Upside Down each supply their own scenery and scene sounds. For example, the Upside Down supplies the tunnel background and the sound you hear if the Demogorgon catches you.
Observer	When Will loses health, SearchEvents tells the screen to update the bar. Another listener handles the sound when you enter a scene.
Facade	The Step through button calls SearchFacade.enterGate(). The facade checks where you are, updates Will’s health, and moves the search into the Upside Down.
Tests and report

Run mvn test in the folder with pom.xml. All 24 tests passed in the packaged version. One test checks that Will’s health stops dropping after the rescue. Another checks that starting again after capture restores his health to 100.

GitHub repository ·
Project report ·
UML
