package com.hawkins.route;

public final class BridgeRoute implements RouteStrategy {
  public String label() {
    return "Take bridge";
  }

  public Crossing cross(int health) {
    return new Crossing(health, "Across the bridge. A red light in the trees.");
  }
}
