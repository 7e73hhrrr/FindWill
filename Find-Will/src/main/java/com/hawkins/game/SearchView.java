package com.hawkins.game;

import com.hawkins.clue.Clue;

public record SearchView(
    StageId stage,
    Hero hero,
    int health,
    String line,
    String image,
    String entryEffect,
    Clue clue,
    String lead,
    String toolAction) {}
