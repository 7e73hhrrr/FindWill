package com.hawkins.world;

public final class UpsideDownWorld implements WorldFactory {
  public Scenery scenery() {
    return stage ->
        switch (stage) {
          case LISTEN, FORK -> "FOREST_inverted";
          case CAUGHT -> "HUB_inverted";
          default -> "fort";
        };
  }

  public Soundscape soundscape() {
    return stage -> stage == com.hawkins.game.StageId.CAUGHT ? "caught" : "";
  }
}
