import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw02.Suit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * test for the Simple free cell and its class.
 */
public class SimpleFreecellModelTest {

  private final Card ac = new Card(0, Suit.Club);
  private final Card ah = new Card(0, Suit.Heart);
  private final Card as = new Card(0, Suit.Spade);
  private final Card ad = new Card(0, Suit.Diamond);
  private final Card as2 = new Card(0, Suit.Spade);
  private final List<Card> deck = new ArrayList<>(Arrays.asList(ac, ah, as));
  private final List<Card> foodeck = new ArrayList<>(Arrays.asList(ac, ah, as, as2));
  protected FreecellModel<Card> sample;
  protected FreecellModel<Card> shuffled;
  protected FreecellModel<Card> smallgame;
  protected FreecellModel<Card> superBigGame;
  protected FreecellModel<Card> beforeStart = new SimpleFreecellModel();


  @Before
  public void initState() {
    sample = new SimpleFreecellModel();
    shuffled = new SimpleFreecellModel();
    smallgame = new SimpleFreecellModel();
    superBigGame = new SimpleFreecellModel();
    sample.startGame(sample.getDeck(), 8, 4, false);
    smallgame.startGame(smallgame.getDeck(), 4, 1, false);
    superBigGame.startGame(superBigGame.getDeck(), 53, 10, false);
  }


  @Test
  public void testgetDeck() {
    assertEquals(52, sample.getDeck().size());
    assertTrue(sample.getDeck().contains(ac));
    assertTrue(sample.getDeck().contains(ah));
    assertTrue(sample.getDeck().contains(as));
    assertTrue(sample.getDeck().contains(ad));
  }


  @Test
  public void testgetNumCascadePile() {
    assertEquals(8, sample.getNumCascadePiles());
    assertEquals(4, smallgame.getNumCascadePiles());
  }

  @Test
  public void testgetNumOpenPile() {
    assertEquals(4, sample.getNumOpenPiles());
    assertEquals(1, smallgame.getNumOpenPiles());
  }

  @Test
  public void testgetNumofCardsinOpenPile() {
    assertEquals(0, sample.getNumCardsInOpenPile(0));
    sample.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
    assertEquals(1, sample.getNumCardsInOpenPile(0));
  }

  @Test
  public void testgetNumofCardsinFoundationPile() {
    assertEquals(0, sample.getNumCardsInFoundationPile(0));
    sample.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    assertEquals(1, sample.getNumCardsInFoundationPile(0));
  }

  @Test
  public void testgetNumCardInCascade() {
    assertEquals(52,
        smallgame.getNumCardsInCascadePile(0) + smallgame.getNumCardsInCascadePile(1)
            + smallgame.getNumCardsInCascadePile(2) + smallgame.getNumCardsInCascadePile(3));
    assertEquals(7, sample.getNumCardsInCascadePile(1));
  }


  @Test
  public void testgetOpenCardAt() {
    assertEquals(null, sample.getOpenCardAt(0));
    sample.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
    assertEquals("A♣", sample.getOpenCardAt(0).toString());

  }

  @Test
  public void testgetFoundationCardAt() {

    sample.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    assertEquals("A♣", sample.getFoundationCardAt(0, 0).toString());
    sample.move(PileType.CASCADE, 4, 5, PileType.FOUNDATION, 0);
    assertEquals("2♣", sample.getFoundationCardAt(0, 1).toString());
  }

  @Test
  public void testgetCascadeCardAt() {
    assertEquals("A♥", superBigGame.getCascadeCardAt(51, 0).toString());
    assertEquals("A♦", superBigGame.getCascadeCardAt(50, 0).toString());
    assertEquals("A♠", superBigGame.getCascadeCardAt(49, 0).toString());
    assertEquals("A♣", superBigGame.getCascadeCardAt(48, 0).toString());
  }

  @Test
  public void testgetNumsAfterShuffle() {
    sample.startGame(sample.getDeck(), 8, 4, true);
    assertEquals(8, sample.getNumCascadePiles());
    assertEquals(4, sample.getNumOpenPiles());
    assertEquals(0, sample.getNumCardsInOpenPile(0));
    assertEquals(0, sample.getNumCardsInFoundationPile(0));
    assertEquals(52,
        smallgame.getNumCardsInCascadePile(0) + smallgame.getNumCardsInCascadePile(1)
            + smallgame.getNumCardsInCascadePile(2) + smallgame.getNumCardsInCascadePile(3));
    assertEquals(7, sample.getNumCardsInCascadePile(1));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetCardAtFoundationWhenEmpty() {
    sample.getFoundationCardAt(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetCardAtCascadeWhenEmpty() {
    superBigGame.getFoundationCardAt(52, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetCardAtCascadeNotExist() {
    superBigGame.getFoundationCardAt(53, 0);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testtwoCardsinOpenPile() {
    sample.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
    sample.move(PileType.CASCADE, 0, 5, PileType.OPEN, 0);
  }


  @Test
  public void testgetNumOfBeforeGameStart() {
    assertEquals(-1, beforeStart.getNumOpenPiles());
    assertEquals(-1, beforeStart.getNumCascadePiles());
  }


  //the three getCardAt and three getNumCards use same helper to check game state and Index,
  // so is that test one means tested all of them?
  @Test(expected = IllegalStateException.class)
  public void testgetCardAtCascadeBeforeGameStart() {
    beforeStart.getCascadeCardAt(0, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testgetCardAtFoundationBeforeGameStart() {
    beforeStart.getFoundationCardAt(0, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testgetCardAtOpenBeforeGameStart() {
    beforeStart.getOpenCardAt(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testgetNumsCardAtCascadeBeforeGameStart() {
    beforeStart.getNumCardsInCascadePile(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testgetNumsCardAtFoundationBeforeGameStart() {
    beforeStart.getNumCardsInFoundationPile(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testgetNumsCardAtOpenBeforeGameStart() {
    beforeStart.getNumCardsInOpenPile(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetCardAtIllegalIndex() {
    sample.getOpenCardAt(100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetCardAtNegIndex() {
    sample.getFoundationCardAt(-1, 6);
  }

  //invalid because of repetition
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDecktoStart() {
    sample.startGame(foodeck, 8, 4, true);
  }

  //invalid because of not enough cards
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDecktoStart2() {
    sample.startGame(deck, 8, 4, true);
  }

  @Test
  public void testInvalidDecktoStartGameStateShuffle() {

    try {
      sample.startGame(foodeck, 8, 4, true);
    } catch (IllegalArgumentException e) {
      assertFalse(sample.isGameOver());
      assertEquals(-1, sample.getNumCascadePiles());
    }
  }

  @Test
  public void testInvalidDecktoStartGameStateNoShuffle() {

    try {
      sample.startGame(foodeck, 8, 4, false);
    } catch (IllegalArgumentException e) {
      assertFalse(sample.isGameOver());
      assertEquals(-1, sample.getNumCascadePiles());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCascadePiletoStart() {
    sample.startGame(sample.getDeck(), 3, 4, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidOpenPiletoStart() {
    sample.startGame(sample.getDeck(), 8, 0, true);
  }

  @Test
  public void testisGameOverBeforeStart() {
    assertFalse(beforeStart.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveBeforeGameStart() {
    beforeStart.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromInvalidIndex() {
    sample.move(PileType.CASCADE, -1, 0, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromNullPile() {
    sample.move(null, 0, 0, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToInvalidIndex() {
    sample.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToNullPile() {
    sample.move(PileType.CASCADE, 0, 0, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveIllegallCardsInput() {
    sample.move(PileType.CASCADE, 0, 100, PileType.FOUNDATION, 1);
  }


  //the game should not start if either the deck or num of piles is invalid
  @Test
  public void testGameStateAfterFailedStart() {
    try {
      sample.startGame(foodeck, 0, 0, false);
    } catch (IllegalArgumentException e) {
      assertEquals(-1, sample.getNumCascadePiles());
    }
  }

  @Test
  public void testStartGameState() {
    beforeStart.startGame(beforeStart.getDeck(), 8, 4, false);
    assertEquals(8, beforeStart.getNumCascadePiles());
    assertEquals(7, beforeStart.getNumCardsInCascadePile(0));
    assertEquals(null, beforeStart.getOpenCardAt(0));
  }

  @Test
  public void testShuffle() {
    shuffled.startGame(shuffled.getDeck(), 8, 4, true);
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 5; j++) {
        assertFalse(shuffled.getCascadeCardAt(i, j).equals(sample.getCascadeCardAt(i, j))
            && shuffled.getCascadeCardAt(i + 1, j).equals(sample.getCascadeCardAt(i + 1, j)));
      }
    }
  }


  @Test(expected = IllegalArgumentException.class)
  public void testMoveNotInDifferentColor() {
    sample.move(PileType.CASCADE, 0, 6, PileType.CASCADE, 4);
    //I know these won't execute, but its shown to explain
    assertEquals("A♣", sample.getCascadeCardAt(0, 6));
    assertEquals("2♣", sample.getCascadeCardAt(4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToOccupiedOpenPile() {
    sample.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
    sample.move(PileType.CASCADE, 0, 5, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAceFirstInFoundation() {
    sample.move(PileType.CASCADE, 4, 5, PileType.FOUNDATION, 0);
    assertEquals("2♣", sample.getCascadeCardAt(4, 5));
  }

  @Test
  public void testMoveToFoundationPile() {
    sample.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    sample.move(PileType.CASCADE, 4, 5, PileType.FOUNDATION, 0);
    assertEquals(2, sample.getNumCardsInFoundationPile(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromFoundationPile() {
    sample.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    sample.move(PileType.FOUNDATION, 0, 0, PileType.CASCADE, 0);
  }

  //i.e an 2Club and A Club cannot be moved at same time
  @Test(expected = IllegalArgumentException.class)
  public void testMoveCascadeCardsNotAttached() {
    sample.move(PileType.CASCADE, 0, 4, PileType.CASCADE, 2);
  }


  @Test
  public void testPutitBack() {
    assertEquals("A♣", sample.getCascadeCardAt(0, 6).toString());
    sample.move(PileType.CASCADE, 0, 6, PileType.CASCADE, 0);
    assertEquals("A♣", sample.getCascadeCardAt(0, 6).toString());
  }

  @Test
  public void testGameNotOver() {

    assertFalse(sample.isGameOver());
    sample.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    assertFalse(sample.isGameOver());
  }

  @Test
  public void testGameOver() {
    for (int i = 51; i >= 0; i--) {
      int j = i % 4;
      superBigGame.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, j);
    }
    assertTrue(superBigGame.isGameOver());

  }


}
