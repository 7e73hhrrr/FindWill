package com.hawkins.world;

import com.hawkins.game.StageId;

public interface WorldFactory {
  interface Scenery {
    String image(StageId scene);
  }

  interface Soundscape {
    String entryEffect(StageId scene);
  }

  Scenery scenery();

  Soundscape soundscape();
}
