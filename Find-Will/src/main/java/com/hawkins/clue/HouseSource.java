package com.hawkins.clue;

public final class HouseSource extends ClueSource {
  @Override
  protected Clue createClue() {
    return new HouseClue();
  }
}
