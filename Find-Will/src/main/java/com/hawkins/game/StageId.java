package com.hawkins.game;

public enum StageId {
  PICK,
  HOUSE,
  BIKE,
  TRAIL,
  RIFT,
  LISTEN,
  FORK,
  ECHO,
  CAUGHT,
  FOUND;

  public boolean exposed() {
    return this == LISTEN || this == FORK || this == ECHO;
  }
}
