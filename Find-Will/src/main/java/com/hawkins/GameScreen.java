package com.hawkins;

import com.hawkins.game.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

final class GameScreen {
  private final Stage window;
  private final SearchFacade game;
  private final StackPane root = new StackPane();
  private final Region background = new Region(), veil = new Region();
  private final VBox center = new VBox();
  private final Label audioWarning = label("", "warning");
  private final Music music = new Music(audioWarning::setText);
  private final Map<String, Image> images = new HashMap<>();
  private final List<Animation> animations = new ArrayList<>();
  private final Timeline exposureClock = new Timeline();
  private final Label health = label("", "health");
  private final ProgressBar meter = new ProgressBar();
  private SearchView current;
  private boolean loading = true, compact;

  GameScreen(Stage stage, SearchFacade game) {
    window = stage;
    this.game = game;
    background.setMouseTransparent(true);
    veil.setMouseTransparent(true);
    veil.getStyleClass().add("veil");
    center.setAlignment(Pos.CENTER);
    center.setFillWidth(true);
    center.setMaxWidth(920);
    center.setMaxHeight(Region.USE_PREF_SIZE);
    StackPane.setAlignment(audioWarning, Pos.BOTTOM_CENTER);
    StackPane.setMargin(audioWarning, new Insets(5));
    root.getChildren().setAll(background, veil, center, audioWarning);
    Scene scene = new Scene(root, 980, 660);
    scene.getStylesheets().add(getClass().getResource("/game.css").toExternalForm());
    stage.setScene(scene);
    stage.setTitle("Hawkins • Find Will");
    stage.setMinWidth(480);
    stage.setMinHeight(440);
    game.events.subscribe(
        state -> {
          if (!state.entryEffect().isEmpty()
              && (current == null || current.stage() != state.stage()))
            music.effect(state.entryEffect());
        });
    exposureClock.getKeyFrames().add(new KeyFrame(Duration.seconds(3), e -> game.exposureTick()));
    exposureClock.setCycleCount(Animation.INDEFINITE);
    meter.setPrefWidth(180);
    meter.setMaxWidth(180);
    game.events.subscribe(
        s -> {
          if (s.stage().exposed()) exposureClock.play();
          else exposureClock.stop();
          if (current != null
              && s.stage() == current.stage()
              && s.health() != current.health()
              && s.line().equals(current.line())) {
            current = s;
            showHealth(s);
          } else render(s);
        });
    root.widthProperty().addListener((o, a, b) -> resize());
    root.heightProperty().addListener((o, a, b) -> resize());
  }

  void start() {
    current = game.view();
    backdrop(current.image());
    center
        .getChildren()
        .setAll(
            label("HAWKINS", "eyebrow"),
            label("Find Will", "title"),
            label("Tuning the radio…", "line"));
    window.show();
    Platform.runLater(
        () -> {
          music.play("session");
          for (Hero hero : Hero.values()) picture(hero.name());
          var opening = new PauseTransition(Duration.millis(950));
          opening.setOnFinished(
              e -> {
                loading = false;
                render(game.view());
              });
          animate(opening);
        });
  }

  private void resize() {
    boolean next = root.getWidth() < 740 || root.getHeight() < 560;
    if (compact != next) {
      compact = next;
      if (!loading && current != null) render(current);
    }
  }

  private static Label label(String text, String style) {
    Label l = new Label(text);
    l.getStyleClass().add(style);
    l.setWrapText(true);
    l.setAlignment(Pos.CENTER);
    l.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
    return l;
  }

  private Image picture(String key) {
    return images.computeIfAbsent(
        key,
        k ->
            new Image(
                Objects.requireNonNull(getClass().getResource("/images/" + k + ".jpg"))
                    .toExternalForm()));
  }

  private void backdrop(String key) {
    background.setBackground(
        new Background(
            new BackgroundImage(
                picture(key),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true))));
  }

  private Button button(String text, Runnable action) {
    Button b = new Button(text);
    b.setOnAction(e -> action.run());
    return b;
  }

  private void animate(Animation a) {
    animations.add(a);
    a.play();
  }

  private void clearAnimations() {
    animations.forEach(Animation::stop);
    animations.clear();
  }

  private void render(SearchView s) {
    current = s;
    clearAnimations();
    backdrop(s.image());
    center.getChildren().clear();
    center.setSpacing(compact ? 7 : 12);
    center.setPadding(new Insets(compact ? 12 : 26));
    center.getStyleClass().setAll(compact ? "compact" : "wide");
    if (s.stage() == StageId.PICK) picker();
    else field(s);
    var fade = new FadeTransition(Duration.millis(170), center);
    fade.setFromValue(.63);
    fade.setToValue(1);
    animate(fade);
  }

  private void picker() {
    center
        .getChildren()
        .addAll(
            label("HAWKINS   /   1983", "eyebrow"),
            label("Find Will", "title"),
            label("Choose who goes looking.", "line"));
    FlowPane crew = new FlowPane(8, 8);
    crew.setAlignment(Pos.CENTER);
    crew.setPrefWrapLength(compact ? 450 : 820);
    for (var hero : Hero.values()) {
      var portrait = new ImageView(picture(hero.name()));
      portrait.setFitWidth(compact ? 58 : 114);
      portrait.setPreserveRatio(true);
      var card = button(hero.name(), () -> game.choose(hero));
      card.getStyleClass().add("portrait");
      card.setContentDisplay(ContentDisplay.TOP);
      card.setGraphic(portrait);
      crew.getChildren().add(card);
    }
    center.getChildren().add(crew);
  }

  private void showHealth(SearchView s) {
    health.setText("Will’s health   " + s.health() + "/100");
    meter.setProgress(s.health() / 100.0);
  }

  private void field(SearchView s) {
    String title =
        switch (s.stage()) {
          case HOUSE -> "Byers house";
          case BIKE -> "Will’s bicycle";
          case LISTEN -> "Something answers";
          case TRAIL -> "Mirkwood trail";
          case RIFT -> "The gate";
          case FORK -> "Two signals";
          case CAUGHT -> "Search failed.";
          case ECHO -> "Castle Byers";
          case FOUND -> "Will is home.";
          default -> throw new IllegalStateException();
        };
    String caption =
        s.stage() == StageId.FOUND
            ? "HAWKINS   /   DAWN"
            : (s.stage().exposed() || s.stage() == StageId.CAUGHT)
                ? "THE UPSIDE DOWN"
                : "HAWKINS   /   AFTER DARK";
    center.getChildren().addAll(label(caption, "eyebrow"), label(title, "title"));
    if (s.stage() == StageId.FOUND || s.stage() == StageId.CAUGHT) {
      var will = new ImageView(picture(s.stage() == StageId.CAUGHT ? "Demogorgon" : "Will"));
      will.setFitWidth(compact ? 64 : 92);
      will.setPreserveRatio(true);
      center.getChildren().add(will);
    }
    VBox panel = new VBox(compact ? 7 : 12);
    panel.setAlignment(Pos.CENTER);
    panel.getStyleClass().add("panel");
    panel.setMaxWidth(580);
    showHealth(s);
    panel.getChildren().addAll(health, meter);
    if (s.stage() == StageId.LISTEN || s.stage() == StageId.FORK)
      panel.getChildren().add(label(s.lead(), "eyebrow"));
    String speaker = s.stage() == StageId.TRAIL ? s.hero() + " · " : "";
    panel.getChildren().add(label(speaker + s.line(), "line"));
    switch (s.stage()) {
      case HOUSE -> {
        var objects = actions();
        for (int i = 0; i < s.clue().options().size(); i++) {
          int choice = i;
          var inspect = button(s.clue().options().get(i), () -> game.answer(choice));
          var art = new ImageView(picture(i == 0 ? "walkie" : "lights"));
          art.setFitWidth(compact ? 95 : 150);
          art.setPreserveRatio(true);
          inspect.setGraphic(art);
          inspect.setContentDisplay(ContentDisplay.TOP);
          objects.getChildren().add(inspect);
        }
        panel.getChildren().add(objects);
      }
      case BIKE -> {
        panel.getChildren().add(label("His bike lies on the trail. Take one tool.", "line"));
        var tools = actions();
        tools
            .getChildren()
            .addAll(
                button("Take bell", () -> game.take(SearchFacade.Tool.BELL)),
                button("Take flashlight", () -> game.take(SearchFacade.Tool.LAMP)));
        panel.getChildren().add(tools);
      }
      case LISTEN ->
          panel
              .getChildren()
              .add(
                  button(
                      s.toolAction(),
                      () -> {
                        music.effect(s.toolAction().equals("Ring the bell") ? "bell" : "click");
                        game.probe();
                      }));
      case ECHO -> {
        panel.getChildren().add(knocks());
        panel
            .getChildren()
            .add(
                button(
                    s.clue().options().get(0),
                    () -> {
                      music.effect("knock");
                      game.answer(0);
                    }));
      }
      case TRAIL -> {
        var choices = actions();
        for (int i = 0; i < game.routes().size(); i++) {
          int index = i;
          choices.getChildren().add(button(game.routes().get(i), () -> game.cross(index)));
        }
        panel.getChildren().add(choices);
      }
      case RIFT -> panel.getChildren().add(button("Step through", game::enterGate));
      case FORK -> panel.getChildren().add(actionsWithPaths());
      case CAUGHT -> panel.getChildren().add(button("Try again", game::replay));
      case FOUND -> panel.getChildren().add(button("Play again", game::replay));
      default -> throw new IllegalStateException();
    }
    center.getChildren().add(panel);
  }

  private FlowPane actionsWithPaths() {
    var choices = actions();
    choices
        .getChildren()
        .addAll(
            button("Follow the light", game::followLight),
            button("Follow the voice", game::followVoice));
    return choices;
  }

  private FlowPane actions() {
    var flow = new FlowPane(8, 6);
    flow.setPrefWrapLength(400);
    flow.setAlignment(Pos.CENTER);
    return flow;
  }

  private HBox knocks() {
    var row = new HBox(11);
    row.setAlignment(Pos.CENTER);
    var pulse = new Timeline();
    for (int i = 0; i < 3; i++) {
      Circle lamp = new Circle(5, Color.web("#ffb992"));
      lamp.setOpacity(.2);
      row.getChildren().add(lamp);
      pulse
          .getKeyFrames()
          .addAll(
              new KeyFrame(Duration.millis(i * 310 + 100), e -> lamp.setOpacity(1)),
              new KeyFrame(Duration.millis(i * 310 + 300), e -> lamp.setOpacity(.2)));
    }
    pulse.getKeyFrames().add(new KeyFrame(Duration.millis(2000)));
    pulse.setCycleCount(Animation.INDEFINITE);
    animate(pulse);
    return row;
  }

  void close() {
    clearAnimations();
    exposureClock.stop();
    music.close();
  }
}
