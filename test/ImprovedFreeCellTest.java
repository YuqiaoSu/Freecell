import static org.junit.Assert.assertEquals;

import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.FreecellModelCreator.GameType;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for multi-move free cell model, extends the simple free cell model test for previous
 * test cases.
 */
public class ImprovedFreeCellTest extends SimpleFreecellModelTest {

  private final FreecellModel<Card> model = new SimpleFreecellModel();
  private final Appendable out = new StringBuilder();
  private Readable in;
  private FreecellModel improved;


  FreecellController impControl;
  String initialDeck = "F1:\n"
      + "F2:\n"
      + "F3:\n"
      + "F4:\n"
      + "O1:\n"
      + "O2:\n"
      + "O3:\n"
      + "O4:\n"
      + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, A♣\n"
      + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
      + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
      + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
      + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
      + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
      + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
      + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥";

  /**
   * override the initState to multimove and test mainly for game state and start game method.
   */
  @Before
  @Override
  public void initState() {
    super.initState();
    improved = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    sample = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    shuffled = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    smallgame = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    superBigGame = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    sample.startGame(sample.getDeck(), 8, 4, false);
    smallgame.startGame(smallgame.getDeck(), 4, 1, false);
    superBigGame.startGame(superBigGame.getDeck(), 53, 10, false);
  }


  @Test
  public void testFactoryMethod() {
    FreecellModel test = FreecellModelCreator.create(GameType.MULTIMOVE);
    assertEquals(improved.getClass(), test.getClass());
    test = FreecellModelCreator.create(GameType.SINGLEMOVE);
    assertEquals(model.getClass(), test.getClass());
  }

  @Test
  public void testControllerToFactory() throws IOException {
    in = new StringReader("C1 7 C8 q");
    StringBuilder log = new StringBuilder();
    MockCreator mock = new MockCreator(log);
    impControl = new SimpleFreecellController(mock.create(GameType.MULTIMOVE), in, out);
    assertEquals("Gametype is MULTIMOVE", log.toString());
    StringBuilder log2 = new StringBuilder();
    MockCreator mock2 = new MockCreator(log2);
    impControl = new SimpleFreecellController(mock2.create(GameType.SINGLEMOVE), in, out);
    assertEquals("Gametype is SINGLEMOVE", log2.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFactoryMethodNullInput() {
    FreecellModel test = FreecellModelCreator.create(null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testMultiMovetoFoundationFromCascade() {
    in = new StringReader("C1 7 C8 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    improved.move(PileType.CASCADE, 7, 5, PileType.FOUNDATION, 0);
  }

  //Move destination not allowed
  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToCascadeFromCascadeIllegalBuild() {
    in = new StringReader("C1 7 C8 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    improved.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 8);
  }

  //Move source not allowed
  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToCascadeFromCascadeIllegalBuild2() {
    in = new StringReader("C1 7 C8 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    improved.move(PileType.CASCADE, 7, 6, PileType.CASCADE, 0);
  }

  @Test
  public void testSingleMovetoFoundationFromCascade() {
    in = new StringReader("C1 7 F1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    assertEquals(initialDeck + ""
        + "\nF1: A♣\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥\n"
        + "Game quit prematurely.", out.toString());
  }

  //move from foundation pile is not allowed
  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromFoundationToCascade() {
    in = new StringReader("C1 7 F1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    improved.move(PileType.FOUNDATION, 1, 1, PileType.CASCADE, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromFoundationToOpen() {
    in = new StringReader("C1 7 F1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    improved.move(PileType.FOUNDATION, 1, 1, PileType.OPEN, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromFoundationToFoundation() {
    in = new StringReader("C1 7 F1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    improved.move(PileType.FOUNDATION, 1, 1, PileType.FOUNDATION, 7);
  }


  @Test
  public void testMultiMoveToCascadePileFromCascade() {
    in = new StringReader("C1 7 C8 C8 6 C1 C1 6 C8 C8 5 C1 C1 5 C8 C8 4 C1 C1 4 C8 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 9, false);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♣\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, 2♥, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 3♣, 2♥, A♣\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 4♥, 3♣, 2♥, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣, 7♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 5♣, 4♥, 3♣, 2♥, A♣\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 6♥, 5♣, 4♥, 3♣, 2♥, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "C1: K♣, J♣, 9♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 7♣, 6♥, 5♣, 4♥, 3♣, 2♥, A♣\n"
        + "Game quit prematurely.", out.toString());
  }


  //If falied a multimove, the previous moving shouldn't exist something left behind
  @Test
  public void testMultiMoveFailedNoDuplicate() {
    in = new StringReader("C1 7 C8 C8 6 C1 C1 3 C8 C1 6 F1 C1 6 O1 C1 6 C8 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\nF1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♣\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, 2♥, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 3♣, 2♥, A♣\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testMultiMoveFailedOnOpen() {
    in = new StringReader("C1 7 C8 C8 6 O1 C8 6 C1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\nF1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♣\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, 2♥, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testMultiMoveFailedOnCascade() {
    in = new StringReader("C1 7 C8 C8 6 C2 C8 6 C1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\nF1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♣\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, 2♥, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testMultiMoveFailedOnFoundation() {
    in = new StringReader("C1 7 C8 C8 6 F1 C8 6 C1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\nF1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♣\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, 2♥, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testMultiMoveWeirdCascadeInput() {
    in = new StringReader("C1 6 C8 C1 6 O1 C1 6 F1 C1 7 O1 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\nInvalid move. Try again. Move not permitted\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "Invalid move. Try again. Move not permitted\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: A♣\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToCascadeWithNotEnoughEmptyOpenPile() {
    improved.startGame(improved.getDeck(), 8, 1, false);
    improved.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 7);
    improved.move(PileType.CASCADE, 7, 5, PileType.CASCADE, 1);
    improved.move(PileType.CASCADE, 1, 5, PileType.CASCADE, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToCascadeWithNotEnoughEmptyCascadePile() {
    improved.startGame(improved.getDeck(), 50, 1, false);

    improved.move(PileType.CASCADE, 0, 1, PileType.CASCADE, 45);
    improved.move(PileType.CASCADE, 45, 0, PileType.CASCADE, 43);
    improved.move(PileType.CASCADE, 1, 1, PileType.CASCADE, 45);
    improved.move(PileType.CASCADE, 43, 0, PileType.CASCADE, 37);
  }

  @Test
  public void testMultiMoveToCascadeWithEnoughEmptyOpenPile() {
    improved.startGame(improved.getDeck(), 8, 2, false);
    improved.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 7);
    improved.move(PileType.CASCADE, 7, 5, PileType.CASCADE, 1);
    improved.move(PileType.CASCADE, 1, 5, PileType.CASCADE, 7);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣, A♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 3♠, 2♥, A♠", new FreecellTextView(improved).toString());
  }

  @Test
  public void testMultiMoveToCascadeWithEnoughEmptyCascadePile() {
    improved.startGame(improved.getDeck(), 53, 1, false);
    improved.move(PileType.CASCADE, 51, 0, PileType.CASCADE, 44);
    improved.move(PileType.CASCADE, 44, 0, PileType.CASCADE, 43);
    improved.move(PileType.CASCADE, 43, 0, PileType.CASCADE, 51);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "C1: K♣\n"
        + "C2: K♠\n"
        + "C3: K♦\n"
        + "C4: K♥\n"
        + "C5: Q♣\n"
        + "C6: Q♠\n"
        + "C7: Q♦\n"
        + "C8: Q♥\n"
        + "C9: J♣\n"
        + "C10: J♠\n"
        + "C11: J♦\n"
        + "C12: J♥\n"
        + "C13: 10♣\n"
        + "C14: 10♠\n"
        + "C15: 10♦\n"
        + "C16: 10♥\n"
        + "C17: 9♣\n"
        + "C18: 9♠\n"
        + "C19: 9♦\n"
        + "C20: 9♥\n"
        + "C21: 8♣\n"
        + "C22: 8♠\n"
        + "C23: 8♦\n"
        + "C24: 8♥\n"
        + "C25: 7♣\n"
        + "C26: 7♠\n"
        + "C27: 7♦\n"
        + "C28: 7♥\n"
        + "C29: 6♣\n"
        + "C30: 6♠\n"
        + "C31: 6♦\n"
        + "C32: 6♥\n"
        + "C33: 5♣\n"
        + "C34: 5♠\n"
        + "C35: 5♦\n"
        + "C36: 5♥\n"
        + "C37: 4♣\n"
        + "C38: 4♠\n"
        + "C39: 4♦\n"
        + "C40: 4♥\n"
        + "C41: 3♣\n"
        + "C42: 3♠\n"
        + "C43: 3♦\n"
        + "C44:\n"
        + "C45:\n"
        + "C46: 2♠\n"
        + "C47: 2♦\n"
        + "C48: 2♥\n"
        + "C49: A♣\n"
        + "C50: A♠\n"
        + "C51: A♦\n"
        + "C52: 3♥, 2♣, A♥\n"
        + "C53:", new FreecellTextView(improved).toString());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveToOpenPile() {
    improved.startGame(improved.getDeck(), 8, 4, false);
    improved.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 7);
    improved.move(PileType.CASCADE, 7, 5, PileType.CASCADE, 1);
    improved.move(PileType.CASCADE, 1, 5, PileType.OPEN, 1);
  }

  @Test
  public void testSingleMovetoOpenFromCascade() {
    in = new StringReader("C1 7 O1 O1 1 C8 q");
    impControl = new SimpleFreecellController(improved, in, out);
    impControl.playGame(improved.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\nF1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: A♣\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♣\n"
        + "Game quit prematurely.", out.toString());
  }


  @Test
  public void testMultiMoveToEmptyCascade() {
    FreecellModel improved = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    Readable stringReader = new StringReader("C52 1 C45 C45 1 C53 q");
    StringBuffer out = new StringBuffer();
    FreecellController impControl = new SimpleFreecellController(improved, stringReader, out);
    impControl.playGame(improved.getDeck(), 53, 4, false);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣\n"
        + "C2: K♠\n"
        + "C3: K♦\n"
        + "C4: K♥\n"
        + "C5: Q♣\n"
        + "C6: Q♠\n"
        + "C7: Q♦\n"
        + "C8: Q♥\n"
        + "C9: J♣\n"
        + "C10: J♠\n"
        + "C11: J♦\n"
        + "C12: J♥\n"
        + "C13: 10♣\n"
        + "C14: 10♠\n"
        + "C15: 10♦\n"
        + "C16: 10♥\n"
        + "C17: 9♣\n"
        + "C18: 9♠\n"
        + "C19: 9♦\n"
        + "C20: 9♥\n"
        + "C21: 8♣\n"
        + "C22: 8♠\n"
        + "C23: 8♦\n"
        + "C24: 8♥\n"
        + "C25: 7♣\n"
        + "C26: 7♠\n"
        + "C27: 7♦\n"
        + "C28: 7♥\n"
        + "C29: 6♣\n"
        + "C30: 6♠\n"
        + "C31: 6♦\n"
        + "C32: 6♥\n"
        + "C33: 5♣\n"
        + "C34: 5♠\n"
        + "C35: 5♦\n"
        + "C36: 5♥\n"
        + "C37: 4♣\n"
        + "C38: 4♠\n"
        + "C39: 4♦\n"
        + "C40: 4♥\n"
        + "C41: 3♣\n"
        + "C42: 3♠\n"
        + "C43: 3♦\n"
        + "C44: 3♥\n"
        + "C45: 2♣\n"
        + "C46: 2♠\n"
        + "C47: 2♦\n"
        + "C48: 2♥\n"
        + "C49: A♣\n"
        + "C50: A♠\n"
        + "C51: A♦\n"
        + "C52: A♥\n"
        + "C53:\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣\n"
        + "C2: K♠\n"
        + "C3: K♦\n"
        + "C4: K♥\n"
        + "C5: Q♣\n"
        + "C6: Q♠\n"
        + "C7: Q♦\n"
        + "C8: Q♥\n"
        + "C9: J♣\n"
        + "C10: J♠\n"
        + "C11: J♦\n"
        + "C12: J♥\n"
        + "C13: 10♣\n"
        + "C14: 10♠\n"
        + "C15: 10♦\n"
        + "C16: 10♥\n"
        + "C17: 9♣\n"
        + "C18: 9♠\n"
        + "C19: 9♦\n"
        + "C20: 9♥\n"
        + "C21: 8♣\n"
        + "C22: 8♠\n"
        + "C23: 8♦\n"
        + "C24: 8♥\n"
        + "C25: 7♣\n"
        + "C26: 7♠\n"
        + "C27: 7♦\n"
        + "C28: 7♥\n"
        + "C29: 6♣\n"
        + "C30: 6♠\n"
        + "C31: 6♦\n"
        + "C32: 6♥\n"
        + "C33: 5♣\n"
        + "C34: 5♠\n"
        + "C35: 5♦\n"
        + "C36: 5♥\n"
        + "C37: 4♣\n"
        + "C38: 4♠\n"
        + "C39: 4♦\n"
        + "C40: 4♥\n"
        + "C41: 3♣\n"
        + "C42: 3♠\n"
        + "C43: 3♦\n"
        + "C44: 3♥\n"
        + "C45: 2♣, A♥\n"
        + "C46: 2♠\n"
        + "C47: 2♦\n"
        + "C48: 2♥\n"
        + "C49: A♣\n"
        + "C50: A♠\n"
        + "C51: A♦\n"
        + "C52:\n"
        + "C53:\n"
        + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣\n"
        + "C2: K♠\n"
        + "C3: K♦\n"
        + "C4: K♥\n"
        + "C5: Q♣\n"
        + "C6: Q♠\n"
        + "C7: Q♦\n"
        + "C8: Q♥\n"
        + "C9: J♣\n"
        + "C10: J♠\n"
        + "C11: J♦\n"
        + "C12: J♥\n"
        + "C13: 10♣\n"
        + "C14: 10♠\n"
        + "C15: 10♦\n"
        + "C16: 10♥\n"
        + "C17: 9♣\n"
        + "C18: 9♠\n"
        + "C19: 9♦\n"
        + "C20: 9♥\n"
        + "C21: 8♣\n"
        + "C22: 8♠\n"
        + "C23: 8♦\n"
        + "C24: 8♥\n"
        + "C25: 7♣\n"
        + "C26: 7♠\n"
        + "C27: 7♦\n"
        + "C28: 7♥\n"
        + "C29: 6♣\n"
        + "C30: 6♠\n"
        + "C31: 6♦\n"
        + "C32: 6♥\n"
        + "C33: 5♣\n"
        + "C34: 5♠\n"
        + "C35: 5♦\n"
        + "C36: 5♥\n"
        + "C37: 4♣\n"
        + "C38: 4♠\n"
        + "C39: 4♦\n"
        + "C40: 4♥\n"
        + "C41: 3♣\n"
        + "C42: 3♠\n"
        + "C43: 3♦\n"
        + "C44: 3♥\n"
        + "C45:\n"
        + "C46: 2♠\n"
        + "C47: 2♦\n"
        + "C48: 2♥\n"
        + "C49: A♣\n"
        + "C50: A♠\n"
        + "C51: A♦\n"
        + "C52:\n"
        + "C53: 2♣, A♥\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testMultiMoveToGameOver() {
    improved.startGame(improved.getDeck(), 52, 4, false);

    for (int i = 51; i - 4 >= 0; i -= 4) {
      improved.move(PileType.CASCADE, i, 0, PileType.CASCADE, i - 7);
      improved.move(PileType.CASCADE, i - 1, 0, PileType.CASCADE, i - 6);
      improved.move(PileType.CASCADE, i - 2, 0, PileType.CASCADE, i - 5);
      improved.move(PileType.CASCADE, i - 3, 0, PileType.CASCADE, i - 4);

    }
    improved.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 4);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1:\n"
        + "C2: K♠, Q♦, J♠, 10♦, 9♠, 8♦, 7♠, 6♦, 5♠, 4♦, 3♠, 2♦, A♠\n"
        + "C3: K♦, Q♠, J♦, 10♠, 9♦, 8♠, 7♦, 6♠, 5♦, 4♠, 3♦, 2♠, A♦\n"
        + "C4: K♥, Q♣, J♥, 10♣, 9♥, 8♣, 7♥, 6♣, 5♥, 4♣, 3♥, 2♣, A♥\n"
        + "C5: K♣, Q♥, J♣, 10♥, 9♣, 8♥, 7♣, 6♥, 5♣, 4♥, 3♣, 2♥, A♣\n"
        + "C6:\n"
        + "C7:\n"
        + "C8:\n"
        + "C9:\n"
        + "C10:\n"
        + "C11:\n"
        + "C12:\n"
        + "C13:\n"
        + "C14:\n"
        + "C15:\n"
        + "C16:\n"
        + "C17:\n"
        + "C18:\n"
        + "C19:\n"
        + "C20:\n"
        + "C21:\n"
        + "C22:\n"
        + "C23:\n"
        + "C24:\n"
        + "C25:\n"
        + "C26:\n"
        + "C27:\n"
        + "C28:\n"
        + "C29:\n"
        + "C30:\n"
        + "C31:\n"
        + "C32:\n"
        + "C33:\n"
        + "C34:\n"
        + "C35:\n"
        + "C36:\n"
        + "C37:\n"
        + "C38:\n"
        + "C39:\n"
        + "C40:\n"
        + "C41:\n"
        + "C42:\n"
        + "C43:\n"
        + "C44:\n"
        + "C45:\n"
        + "C46:\n"
        + "C47:\n"
        + "C48:\n"
        + "C49:\n"
        + "C50:\n"
        + "C51:\n"
        + "C52:", new FreecellTextView(improved).toString());

    try {
      improved.move(PileType.CASCADE, 1, 0, PileType.OPEN, 1);
    } catch (IllegalArgumentException e) {
      //A failed move
    }
    for (int j = 12; j >= 0; j--) {
      for (int i = 1; i <= 4; i++) {
        if (j % 2 == 0) {
          improved.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i - 1);
        } else if (j % 2 == 1) {
          if (i == 1 || i == 3) {
            improved.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i);
          } else {
            improved.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i - 2);
          }
        }
      }
    }
    assertEquals("F1: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "F2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "F3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "F4: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1:\n"
        + "C2:\n"
        + "C3:\n"
        + "C4:\n"
        + "C5:\n"
        + "C6:\n"
        + "C7:\n"
        + "C8:\n"
        + "C9:\n"
        + "C10:\n"
        + "C11:\n"
        + "C12:\n"
        + "C13:\n"
        + "C14:\n"
        + "C15:\n"
        + "C16:\n"
        + "C17:\n"
        + "C18:\n"
        + "C19:\n"
        + "C20:\n"
        + "C21:\n"
        + "C22:\n"
        + "C23:\n"
        + "C24:\n"
        + "C25:\n"
        + "C26:\n"
        + "C27:\n"
        + "C28:\n"
        + "C29:\n"
        + "C30:\n"
        + "C31:\n"
        + "C32:\n"
        + "C33:\n"
        + "C34:\n"
        + "C35:\n"
        + "C36:\n"
        + "C37:\n"
        + "C38:\n"
        + "C39:\n"
        + "C40:\n"
        + "C41:\n"
        + "C42:\n"
        + "C43:\n"
        + "C44:\n"
        + "C45:\n"
        + "C46:\n"
        + "C47:\n"
        + "C48:\n"
        + "C49:\n"
        + "C50:\n"
        + "C51:\n"
        + "C52:", new FreecellTextView(improved).toString());
  }
}


