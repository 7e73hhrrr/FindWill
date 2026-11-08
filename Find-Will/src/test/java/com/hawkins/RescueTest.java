package com.hawkins;

import static org.junit.jupiter.api.Assertions.*;

import com.hawkins.clue.*;
import com.hawkins.game.*;
import com.hawkins.route.*;
import com.hawkins.world.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RescueTest {
  final SearchFacade game = new SearchFacade(RescueMission.standard());

  @Test
  void builderRequiresSignalsRoutesAndWorlds() {
    assertThrows(IllegalStateException.class, () -> new RescueMission.Builder().build());
  }

  @Test
  void builtRouteListIsReadOnly() {
    assertThrows(
        UnsupportedOperationException.class, () -> RescueMission.standard().routes().clear());
  }

  @Test
  void signalCreatorsSupplyDifferentClueTypes() {
    assertInstanceOf(HouseClue.class, new HouseSource().discover());
    assertInstanceOf(KnockClue.class, new KnockSource().discover());
  }

  @Test
  void bothHouseObjectsGiveDistinctUsefulLeads() {
    var house = new HouseSource().discover();
    var radio = house.interact(0);
    var bulbs = house.interact(1);
    assertTrue(radio.complete());
    assertTrue(bulbs.complete());
    assertNotEquals(radio.line(), bulbs.line());
  }

  @Test
  void theDoorRespondsToEachKnock() {
    var door = new KnockSource().discover();
    assertFalse(door.interact(0).complete());
    assertFalse(door.interact(0).complete());
    assertTrue(door.interact(0).complete());
  }

  @Test
  void worldsSupplyRelatedArtworkAndSound() {
    var town = new HawkinsWorld();
    var other = new UpsideDownWorld();
    assertNotEquals(town.scenery().image(StageId.TRAIL), other.scenery().image(StageId.ECHO));
    assertNotEquals(
        town.soundscape().entryEffect(StageId.TRAIL),
        other.soundscape().entryEffect(StageId.CAUGHT));
  }

  @Test
  void everyWorldResourceExists() {
    for (var world : List.of(new HawkinsWorld(), new UpsideDownWorld()))
      for (var s : StageId.values()) {
        assertNotNull(getClass().getResource("/images/" + world.scenery().image(s) + ".jpg"));
        String cue = world.soundscape().entryEffect(s);
        if (!cue.isEmpty()) assertNotNull(getClass().getResource("/audio/" + cue + ".wav"));
      }
  }

  @Test
  void crossingTheBridgePreservesHealth() {
    assertEquals(100, new BridgeRoute().cross(100).health());
  }

  @Test
  void vinesCanScratchButNeverKill() {
    assertEquals(90, new VineRoute().cross(100).health());
    assertEquals(1, new VineRoute().cross(1).health());
  }

  @Test
  void observersReceiveUpdatedState() {
    List<SearchView> updates = new ArrayList<>();
    game.events.subscribe(updates::add);
    game.choose(Hero.Mike);
    game.answer(1);
    assertEquals(
        List.of(StageId.HOUSE, StageId.BIKE), updates.stream().map(SearchView::stage).toList());
  }

  @Test
  void removedObserverStopsReceivingScenes() {
    List<SearchView> updates = new ArrayList<>();
    SearchEvents.Listener l = updates::add;
    game.events.subscribe(l);
    game.events.unsubscribe(l);
    game.choose(Hero.Max);
    assertTrue(updates.isEmpty());
  }

  @Test
  void theToolAndLeadCarryAcrossTheGate() {
    for (int i = 0; i < 2; i++) {
      game.choose(Hero.Dustin);
      game.answer(i);
      String lead = game.view().lead();
      game.take(SearchFacade.Tool.values()[i]);
      game.cross(0);
      game.enterGate();
      assertEquals(lead, game.view().lead());
      assertEquals(i == 0 ? "Ring the bell" : "Sweep the beam", game.view().toolAction());
      game.probe();
      assertTrue(game.view().line().contains(i == 0 ? "copies" : "teeth"));
      game.followVoice();
      game.replay();
      assertTrue(game.view().lead().isEmpty());
      assertTrue(game.view().toolAction().isEmpty());
    }
  }

  @Test
  void facadeRejectsOutOfSequenceActions() {
    assertThrows(IllegalStateException.class, game::enterGate);
    assertThrows(IllegalStateException.class, () -> game.answer(2));
  }

  @ParameterizedTest
  @EnumSource(Hero.class)
  void bothRoutesRescueWillWithEveryHero(Hero hero) {
    for (int i = 0; i < 2; i++) {
      game.choose(hero);
      game.answer(1);
      game.take(SearchFacade.Tool.LAMP);
      assertEquals(hero.line, game.view().line());
      game.cross(i);
      game.enterGate();
      game.probe();
      game.followLight();
      knockThreeTimes();
      assertEquals(StageId.FOUND, game.view().stage());
      assertEquals(i == 0 ? 92 : 82, game.view().health());
      game.replay();
      assertEquals(100, game.view().health());
      assertEquals(StageId.PICK, game.view().stage());
    }
  }

  @Test
  void healthDrainsOnlyWhileExposedAndStopsAtRescue() {
    game.choose(Hero.Max);
    game.answer(1);
    game.take(SearchFacade.Tool.LAMP);
    game.cross(0);
    game.exposureTick();
    assertEquals(100, game.view().health());
    game.enterGate();
    game.probe();
    assertEquals(95, game.view().health());
    game.exposureTick();
    assertEquals(93, game.view().health());
    game.followLight();
    assertEquals(90, game.view().health());
    game.exposureTick();
    assertEquals(88, game.view().health());
    for (int i = 0; i < 60; i++) game.exposureTick();
    assertEquals(1, game.view().health());
    knockThreeTimes();
    game.exposureTick();
    assertEquals(1, game.view().health());
    assertEquals(StageId.FOUND, game.view().stage());
  }

  @ParameterizedTest
  @EnumSource(Hero.class)
  void wrongTurnEndsInCaptureAndAllowsRetry(Hero hero) {
    game.choose(hero);
    game.answer(1);
    game.take(SearchFacade.Tool.LAMP);
    game.cross(1);
    game.enterGate();
    game.probe();
    game.followVoice();
    assertEquals(StageId.CAUGHT, game.view().stage());
    assertEquals("caught", game.view().entryEffect());
    game.exposureTick();
    assertEquals(85, game.view().health());
    assertThrows(IllegalStateException.class, game::followLight);
    game.replay();
    assertEquals(100, game.view().health());
    assertEquals(StageId.PICK, game.view().stage());
  }

  private void knockThreeTimes() {
    for (int i = 0; i < 3; i++) game.answer(0);
  }
}
