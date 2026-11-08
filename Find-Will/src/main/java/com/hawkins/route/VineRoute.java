package com.hawkins.route;

public final class VineRoute implements RouteStrategy {
  public String label() {
    return "Cut through vines";
  }

  public Crossing cross(int health) {
    return new Crossing(Math.max(1, health - 10), "The vines slow you down. Will’s signal fades.");
  }
}
