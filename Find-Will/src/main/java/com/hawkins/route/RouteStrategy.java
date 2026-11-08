package com.hawkins.route;

public interface RouteStrategy {
  record Crossing(int health, String line) {}

  String label();

  Crossing cross(int health);
}
