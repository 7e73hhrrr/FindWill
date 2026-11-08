package com.hawkins;

import com.hawkins.game.*;
import javafx.application.Application;
import javafx.stage.Stage;

public final class MainApp extends Application {
  private GameScreen screen;

  public void start(Stage stage) {
    screen = new GameScreen(stage, new SearchFacade(RescueMission.standard()));
    screen.start();
  }

  public void stop() {
    if (screen != null) screen.close();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
