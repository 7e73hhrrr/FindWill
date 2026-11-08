package com.hawkins;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.scene.media.*;
import javafx.util.Duration;
import javax.sound.sampled.*;

/** One soundtrack at a time. PCM playback avoids depending on a particular Linux media backend. */
public final class Music implements AutoCloseable {
  private final Consumer<String> warning;
  private Clip clip;
  private MediaPlayer media;
  private Process external;
  private final Map<String, Path> extracted = new HashMap<>();
  private PauseTransition watchdog;
  private String current = "";

  public Music(Consumer<String> warning) {
    this.warning = warning;
  }

  public void play(String name) {
    if (current.equals(name)) return;
    stop();
    current = name;
    warning.accept("");
    URL url = getClass().getResource("/audio/" + name + ".wav");
    if (url == null) {
      warning.accept("Missing audio: " + name);
      return;
    }
    try (AudioInputStream input =
        AudioSystem.getAudioInputStream(new BufferedInputStream(url.openStream()))) {
      clip = AudioSystem.getClip();
      clip.open(input);
      if (name.equals("caught")) clip.start();
      else clip.loop(Clip.LOOP_CONTINUOUSLY);
      System.out.println("[Audio] Java Sound: " + name);
      return;
    } catch (Exception e) {
      if (clip != null) {
        clip.close();
        clip = null;
      }
      System.err.println("[Audio] PCM output unavailable: " + e.getMessage());
    }
    try {
      Path wav = extracted.get(name);
      if (wav == null) {
        wav = Files.createTempFile("find-will-", ".wav");
        wav.toFile().deleteOnExit();
        try (InputStream stream = url.openStream()) {
          Files.copy(stream, wav, StandardCopyOption.REPLACE_EXISTING);
        }
        extracted.put(name, wav);
      }
      startMedia(wav);
    } catch (Exception e) {
      warning.accept("Sound unavailable · check your output device");
      System.err.println(e);
    }
  }

  private void startMedia(Path wav) {
    try {
      var player = new MediaPlayer(new Media(wav.toUri().toString()));
      media = player;
      player.setCycleCount(current.equals("caught") ? 1 : MediaPlayer.INDEFINITE);
      player.setVolume(.72);
      Runnable failed =
          () -> {
            if (media == player) {
              stopMedia();
              startExternal(wav);
            }
          };
      player.setOnError(failed);
      watchdog = new PauseTransition(Duration.seconds(2.5));
      watchdog.setOnFinished(
          e -> {
            if (player.getStatus() != MediaPlayer.Status.PLAYING) failed.run();
          });
      player.setOnPlaying(
          () -> {
            if (watchdog != null) watchdog.stop();
            System.out.println("[Audio] JavaFX: " + current);
          });
      watchdog.play();
      player.play();
    } catch (RuntimeException e) {
      stopMedia();
      startExternal(wav);
    }
  }

  private void startExternal(Path wav) {
    try {
      Process process =
          new ProcessBuilder(
                  "ffplay",
                  "-nodisp",
                  "-loglevel",
                  "error",
                  "-loop",
                  current.equals("caught") ? "1" : "0",
                  "-autoexit",
                  "-volume",
                  "70",
                  wav.toString())
              .redirectOutput(ProcessBuilder.Redirect.DISCARD)
              .redirectError(ProcessBuilder.Redirect.DISCARD)
              .start();
      external = process;
      process
          .onExit()
          .thenRun(
              () ->
                  javafx.application.Platform.runLater(
                      () -> {
                        if (external == process && process.exitValue() != 0)
                          warning.accept("Sound unavailable · check your output device");
                      }));
      System.out.println("[Audio] ffplay: " + current);
    } catch (IOException e) {
      System.err.println("[Audio] " + e.getMessage());
      warning.accept("Sound unavailable · check your output device");
    }
  }

  private void stopMedia() {
    if (watchdog != null) {
      watchdog.stop();
      watchdog = null;
    }
    if (media != null) {
      var old = media;
      media = null;
      old.dispose();
    }
  }

  private void stop() {
    if (clip != null) {
      clip.stop();
      clip.close();
      clip = null;
    }
    stopMedia();
    if (external != null) {
      external.destroy();
      external = null;
    }
  }

  private Clip effect;

  public void effect(String key) {
    if (effect != null) {
      effect.close();
      effect = null;
    }
    try (var input =
        AudioSystem.getAudioInputStream(
            new BufferedInputStream(
                Objects.requireNonNull(
                    getClass().getResourceAsStream("/audio/" + key + ".wav"))))) {
      effect = AudioSystem.getClip();
      effect.open(input);
      effect.start();
      System.out.println("[Effect] " + key);
    } catch (Exception e) {
      System.err.println("[Effect] " + e.getMessage());
    }
  }

  public void close() {
    if (effect != null) effect.close();
    stop();
    current = "";
  }
}
