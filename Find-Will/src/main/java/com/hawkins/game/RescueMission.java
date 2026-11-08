package com.hawkins.game;

import com.hawkins.clue.*;
import com.hawkins.route.*;
import com.hawkins.world.*;
import java.util.*;

public record RescueMission(
    ClueSource firstSignal,
    ClueSource finalSignal,
    List<RouteStrategy> routes,
    WorldFactory town,
    WorldFactory otherSide) {
  public static final class Builder {
    private ClueSource first, last;
    private WorldFactory town, other;
    private final List<RouteStrategy> routes = new ArrayList<>();

    public Builder signals(ClueSource first, ClueSource last) {
      this.first = first;
      this.last = last;
      return this;
    }

    public Builder route(RouteStrategy route) {
      routes.add(Objects.requireNonNull(route));
      return this;
    }

    public Builder worlds(WorldFactory town, WorldFactory other) {
      this.town = town;
      this.other = other;
      return this;
    }

    public RescueMission build() {
      if (first == null || last == null || town == null || other == null || routes.isEmpty())
        throw new IllegalStateException(
            "Configure both signals, both worlds and at least one route.");
      System.out.println("[Builder] Search for Will");
      return new RescueMission(first, last, List.copyOf(routes), town, other);
    }
  }

  public static RescueMission standard() {
    return new Builder()
        .signals(new HouseSource(), new KnockSource())
        .worlds(new HawkinsWorld(), new UpsideDownWorld())
        .route(new BridgeRoute())
        .route(new VineRoute())
        .build();
  }
}
