package com.hawkins.world;

public final class HawkinsWorld implements WorldFactory {
  public Scenery scenery() {
    return stage ->
        switch (stage) {
          case BIKE -> "bike";
          case HOUSE -> "BYERS_normal";
          case RIFT -> "HUB_normal";
          case FOUND -> "rescue";
          default -> "FOREST_normal";
        };
  }

  public Soundscape soundscape() {
    return stage -> "";
  }
}
