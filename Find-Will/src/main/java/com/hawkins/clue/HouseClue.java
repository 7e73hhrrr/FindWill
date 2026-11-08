package com.hawkins.clue;

import java.util.List;

public final class HouseClue implements Clue {
  public String prompt() {
    return "Will’s missing. The radio crackles. Two bulbs blink.";
  }

  public List<String> options() {
    return List.of("Listen to radio", "Watch the lights");
  }

  public Result interact(int option) {
    return switch (option) {
      case 0 -> new Result("Will whispers, “Mirkwood. Something copies my voice.”", true);
      case 1 -> new Result("Mirkwood is circled on Will’s map: “Two flashes means it’s me.”", true);
      default -> throw new IllegalArgumentException("Unknown house object");
    };
  }
}
