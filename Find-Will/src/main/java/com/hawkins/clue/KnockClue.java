package com.hawkins.clue;

import java.util.List;

public final class KnockClue implements Clue {
  private int count;

  public String prompt() {
    return "Three knocks from inside. Answer with three of your own.";
  }

  public List<String> options() {
    return List.of("Knock");
  }

  public Result interact(int option) {
    if (option != 0 || count >= 3) throw new IllegalArgumentException("Knock unavailable");
    count++;
    return new Result(
        switch (count) {
          case 1 -> "One knock. Footsteps stop inside.";
          case 2 -> "Two knocks. Someone lifts the latch.";
          default -> "The door opens. Will takes your hand.";
        },
        count == 3);
  }
}
