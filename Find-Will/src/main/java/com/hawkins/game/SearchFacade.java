package com.hawkins.game;

import com.hawkins.clue.*;
import java.util.List;

public final class SearchFacade {
  private final RescueMission mission;
  public final SearchEvents events = new SearchEvents();
  private StageId stage = StageId.PICK;
  private Hero hero;
  private int health = 100;
  private String line = "";
  private Clue clue;
  private String lead = "";

  public enum Tool {
    BELL("Ring the bell", "You ring once. The tunnel rings once. It copies everything."),
    LAMP("Sweep the beam", "Your beam catches teeth in the tunnel. The trees flash twice.");
    final String action, result;

    Tool(String action, String result) {
      this.action = action;
      this.result = result;
    }
  }

  private Tool tool;

  public SearchFacade(RescueMission mission) {
    this.mission = mission;
  }

  public SearchView view() {
    var world = (stage.exposed() || stage == StageId.CAUGHT) ? mission.otherSide() : mission.town();
    return new SearchView(
        stage,
        hero,
        health,
        line,
        world.scenery().image(stage),
        world.soundscape().entryEffect(stage),
        clue,
        lead,
        tool == null ? "" : tool.action);
  }

  public List<String> routes() {
    return mission.routes().stream().map(r -> r.label()).toList();
  }

  private void require(StageId expected) {
    if (stage != expected) throw new IllegalStateException("Expected " + expected);
  }

  private void update() {
    events.publish(view());
  }

  public void choose(Hero hero) {
    require(StageId.PICK);
    this.hero = java.util.Objects.requireNonNull(hero);
    clue = mission.firstSignal().discover();
    stage = StageId.HOUSE;
    line = clue.prompt();
    update();
  }

  public void answer(int choice) {
    if (stage != StageId.HOUSE && stage != StageId.ECHO)
      throw new IllegalStateException("No signal here.");
    var result = clue.interact(choice);
    line = result.line();
    if (result.complete()) {
      if (stage == StageId.HOUSE) {
        stage = StageId.BIKE;
        lead = choice == 0 ? "Will warned you: voices can be copied." : "Will’s code: two flashes.";
      } else {
        stage = StageId.FOUND;
      }
      clue = null;
    }
    update();
  }

  public void take(Tool chosen) {
    require(StageId.BIKE);
    tool = java.util.Objects.requireNonNull(chosen);
    stage = StageId.TRAIL;
    line = hero.line;
    update();
  }

  public void probe() {
    require(StageId.LISTEN);
    stage = StageId.FORK;
    line = tool.result;
    update();
  }

  public void cross(int route) {
    require(StageId.TRAIL);
    var path = mission.routes().get(route);
    var result = path.cross(health);
    System.out.println("[Strategy] " + path.getClass().getSimpleName());
    health = result.health();
    line = result.line();
    stage = StageId.RIFT;
    update();
  }

  public void enterGate() {
    require(StageId.RIFT);
    System.out.println("[Facade] Enter gate");
    System.out.println("[Abstract Factory] " + mission.otherSide().getClass().getSimpleName());
    stage = StageId.LISTEN;
    health = Math.max(1, health - 5);
    line = "A light blinks twice in the trees. “Over here!” calls the tunnel.";
    update();
  }

  public void exposureTick() {
    if (stage.exposed() && health > 1) {
      health = Math.max(1, health - 2);
      update();
    }
  }

  public void followVoice() {
    require(StageId.FORK);
    stage = StageId.CAUGHT;
    line = "The Demogorgon caught you.";
    update();
  }

  public void followLight() {
    require(StageId.FORK);
    health = Math.max(1, health - 3);
    stage = StageId.ECHO;
    clue = mission.finalSignal().discover();
    line = clue.prompt();
    update();
  }

  public void replay() {
    if (stage != StageId.FOUND && stage != StageId.CAUGHT)
      throw new IllegalStateException("Finish the search first.");
    stage = StageId.PICK;
    hero = null;
    tool = null;
    lead = "";
    health = 100;
    line = "";
    update();
  }
}
