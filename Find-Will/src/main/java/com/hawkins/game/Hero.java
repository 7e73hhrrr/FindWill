package com.hawkins.game;

public enum Hero {
  Eleven("I can feel him past the trees."),
  Mike("I know the shortcut to his fort."),
  Dustin("My compass is spinning. He’s close."),
  Lucas("My binoculars caught his bike."),
  Max("Those tracks? Will’s back tire.");
  public final String line;

  Hero(String line) {
    this.line = line;
  }
}
