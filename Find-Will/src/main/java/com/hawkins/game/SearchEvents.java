package com.hawkins.game;

import java.util.*;

public final class SearchEvents {
  @FunctionalInterface
  public interface Listener {
    void changed(SearchView state);
  }

  private final Set<Listener> listeners = new LinkedHashSet<>();

  public void subscribe(Listener l) {
    listeners.add(l);
  }

  public void unsubscribe(Listener l) {
    listeners.remove(l);
  }

  void publish(SearchView state) {
    System.out.println("[Observer] " + state.stage());
    for (var l : List.copyOf(listeners)) l.changed(state);
  }
}
