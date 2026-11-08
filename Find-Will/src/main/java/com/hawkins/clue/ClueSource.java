package com.hawkins.clue;

public abstract class ClueSource {
  protected abstract Clue createClue();

  public final Clue discover() {
    var clue = createClue();
    System.out.println(
        "[Factory Method] "
            + getClass().getSimpleName()
            + " created "
            + clue.getClass().getSimpleName());
    return clue;
  }
}
