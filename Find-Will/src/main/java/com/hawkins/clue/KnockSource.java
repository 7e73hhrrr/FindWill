package com.hawkins.clue;

public final class KnockSource extends ClueSource {
  @Override
  protected Clue createClue() {
    return new KnockClue();
  }
}
