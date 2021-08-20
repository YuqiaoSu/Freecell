
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw02.Suit;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the controller of free cell about its play game method and other funcions.
 */
public class ControllerTest {

  protected FreecellModel<Card> model;
  protected  FreecellModel<Card> shuffle = new SimpleFreecellModel();
  protected  Appendable out = new StringBuilder();
  protected  Appendable shuffleOut = new StringBuilder();
  protected Readable in;
  protected FreecellController<Card> controller;

  protected FreecellModel<Card> superBigGame = new SimpleFreecellModel();


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

  @Before
  public void initState() {
    model = new SimpleFreecellModel();
    in = new StringReader("C1 7 C8 Q");
    controller = new SimpleFreecellController(model, in, out);
  }

  @Test
  public void testInputManyStartGame() {
    int max = 1000;
    Random random = new Random(2);
    for (int i = 0; i < 100; i++) {
      int numCascadePiles = random.nextInt(max) % (max - 4) + 4;
      int numOpenPiles = random.nextInt(max) % (max - 1) + 1;
      boolean shuffle = random.nextBoolean();
      Reader in = new StringReader("Q");
      StringBuilder log = new StringBuilder();
      FreecellModel<Card> mock = new ConfirmFreecellModel(log);
      controller = new SimpleFreecellController(mock, in, out);
      controller.playGame(model.getDeck(), numCascadePiles, numOpenPiles, shuffle);
      assertEquals("Game started with " + numCascadePiles + " Cascade Piles, " + numOpenPiles
          + " Open Piles, and shuffle is " + shuffle + "\n", log.toString());
    }
  }


  @Test(expected = IllegalArgumentException.class)
  public void testInitialControllerNullModel() {
    controller = new SimpleFreecellController(null, new InputStreamReader(System.in), System.out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitialControllerNullReadable() {
    controller = new SimpleFreecellController(model, null, System.out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitialControllerNullAppendable() {
    controller = new SimpleFreecellController(model, new InputStreamReader(System.in), null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameWithNullDeck() {
    controller.playGame(null, 8, 4, false);
  }

  @Test
  public void testPlayGameWithLessCascade() {
    controller.playGame(model.getDeck(), 3, 4, false);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameWithLessOpen() {
    controller.playGame(model.getDeck(), 8, 0, false);
    assertEquals("Could not start game.", out.toString());
  }

  @Test()
  public void testIllegalStartGameNegatively() {
    in = new StringReader("C1 -7 C2 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), -8, 4, false);
    assertEquals("Could not start game.", out.toString());
  }

  @Test()
  public void testIllegalStartGameNegativelyOpen() {
    in = new StringReader("C1 -7 C2 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, -4, false);
    assertEquals("Could not start game.", out.toString());
  }

  @Test
  public void testPlayGameWithInvalidDeck() {

    Card ac = new Card(0, Suit.Club);
    Card ah = new Card(0, Suit.Heart);
    Card as = new Card(0, Suit.Spade);
    List<Card> invalidDeck = new ArrayList<>(Arrays.asList(ac, ah, as));
    controller.playGame(invalidDeck, 8, 4, false);
    assertEquals("Could not start game.", out.toString());
  }


  @Test
  public void testNoGamePlay() {

    assertEquals("", out.toString());
  }

  //nothing is called
  @Test
  public void testNoGamePlayWithMoves() {
    in = new StringReader("C1 7 C8 q");
    StringBuilder log = new StringBuilder();
    FreecellModel<Card> mock = new ConfirmFreecellModel(log);
    controller = new SimpleFreecellController(mock, in, out);
    assertEquals("", log.toString());

  }

  @Test(expected = IllegalStateException.class)
  public void tesIllegalReadable() {
    Readable in = new StringReader("");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void tesIllegalReadableSpace() {
    Readable in = new StringReader(" ");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  //a normal move is made
  @Test
  public void testPlayGameNormalMove() {
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "F1:\n"
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
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♣\n" +
        "Game quit prematurely.", out.toString());
  }

  @Test
  public void testPlayGameWithGivenDeck() {
    in = new StringReader("C1 7 C8 q");
    StringBuilder log = new StringBuilder();
    FreecellModel<Card> mock = new ConfirmFreecellModel(log);
    controller = new SimpleFreecellController(mock, in, out);
    List<Card> deck = new ArrayList<>(Arrays.asList(new Card(1, Suit.Club)));
    controller.playGame(deck, 8, 4, false);
    assertEquals("Game started with 8 Cascade Piles, 4 Open Piles, and shuffle is false\n"
        + "Move from CASCADE0 at 6 to CASCADE7\n"
        + "GameOver tested\n", log.toString());
  }

  //To show that shuffle is true in model
  @Test
  public void testPlayGameWithShuffledMockIsDifferent() {
    Readable shuffleIn = new StringReader("C1 7 C8 q");
    StringBuilder log = new StringBuilder();
    FreecellModel<Card> mock = new ConfirmFreecellModel(log);
    FreecellController<Card> shuffledController = new SimpleFreecellController(mock, shuffleIn,
        shuffleOut);
    shuffledController.playGame(model.getDeck(), 8, 4, true);
    assertEquals("Game started with 8 Cascade Piles, 4 Open Piles, and shuffle is true\n"
        + "Move from CASCADE0 at 6 to CASCADE7\n"
        + "GameOver tested\n", log.toString());
  }

  //also showed that is human friendly
  @Test
  public void testPlayGameWithMultipleMove() {
    in = new StringReader("C1 7 C8 C2 7 C7 Q");
    StringBuilder log = new StringBuilder();
    FreecellModel<Card> mock = new ConfirmFreecellModel(log);
    controller = new SimpleFreecellController(mock, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals("Game started with 8 Cascade Piles, 4 Open Piles, and shuffle is false\n"
        + "Move from CASCADE0 at 6 to CASCADE7\n"
        + "GameOver tested\n"
        + "Move from CASCADE1 at 6 to CASCADE6\n"
        + "GameOver tested\n", log.toString());
  }


  @Test
  public void testInputq() {
    in = new StringReader("q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testInputQ() {
    in = new StringReader("Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Game quit prematurely.", out.toString());
  }

  //only the first will work
  @Test
  public void testNQuit() {
    in = new StringReader("Q q Q q q qwqwq q Q Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testInputQuitAfterMove() {
    in = new StringReader("C1 7 C8 Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "F1:\n"
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
  public void testInputQuitDuringMove() {
    in = new StringReader("C1 7 q C8 Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testInputQuitATiNDEX() {
    in = new StringReader("C1 Q C7 C8 Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testInputQuitInsideMove() {
    in = new StringReader("Q 7 C8 Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Game quit prematurely.", out.toString());
  }

  //q and Q quits game only as single letter
  @Test
  public void testInputQuitIllegallyAsDest() {
    in = new StringReader("C1 7 qQ C8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. Try again. Please reenter the pile to move to\n"
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
  public void testInputQuitIllegally2AsSrc() {
    in = new StringReader("q1 7 Q8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. Try again. Please reenter the pile to move from\n"
        + "Invalid move. Try again. Please reenter the pile to move from\n"
        + "Invalid move. Try again. Please reenter the pile to move from\n"
        + "Game quit prematurely.", out.toString());
  }


  //test continue moving after one set of input failed
  @Test
  public void testInputMoveFromFail() {
    in = new StringReader("c1 7 C1 C1 7 C8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move from\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move from\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
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
  public void testInputCardIndexFail() {
    in = new StringReader("C1 C C8 7 C8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
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
  public void testInputDestinationFail() {
    in = new StringReader("C1 7 c8 C8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move to\n"
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
  public void testInputDestinationIndexFail() {
    in = new StringReader("C1 7 CC C8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Illegal pile number, please reenter the number of pile to move to\n"
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
  public void testInputMoveFromIndexFail() {
    in = new StringReader("Cc 7 Cc C1 7 C8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move from\n"
        + "Invalid move. "
        + "Try again. Illegal pile number, please reenter the number of pile to move from\n"
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
  public void testEveryInputFailed() {
    in = new StringReader("Cc C c7 C1 adwaw C2 8 asdaw C3 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. "
        + "Try again. Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move from\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move to\n"
        + "Invalid move. "
        + "Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testReEnterDestination() {
    in = new StringReader("C1 7 c7 adwaw CC C3 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move to\n"
        + "Invalid move. "
        + "Try again. Please reenter the pile to move to\n"
        + "Invalid move. "
        + "Try again. Illegal pile number, please reenter the number of pile to move to\n"
        + "Invalid move. "
        + "Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testReEnterCardIndex() {
    in = new StringReader("C1 A C7 rua C7 3 C7 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. "
        + "Try again. Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. "
        + "Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  //none of them are getting into source pile
  @Test
  public void testAllIlegalInput() {
    in = new StringReader("Cc 7 ab rua 4 ;' <> 71123 FOC ocf o c f F O C FAQ q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move from\n"
        + "Game quit prematurely.", out.toString());
  }


  @Test(expected = IllegalStateException.class)
  public void testNotEnoughInput() {
    in = new StringReader("C1 A C7 rua C7 3 C7 C1 ");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testOnlySrc() {
    in = new StringReader("C1");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testSrcandIndex() {
    in = new StringReader("C1 7");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testRunningOutWhenReenteringSrc() {
    in = new StringReader("Cc 7 C2 C1");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testRunningOutWhenReenteringiNDEX() {
    in = new StringReader("C1 C C2 8");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testRunningOutWhenReenteringDestination() {
    in = new StringReader("C1 7 Cc ");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testRunningOutWhenPassedOne() {
    in = new StringReader("C1 7 C8 C2 ");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }


  @Test
  public void testReEnterSourceToCorrectState() {
    in = new StringReader("Cc 7 C1 rua C C7 c1 7 CQ 13 7 C8 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move from\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move from\n"
        + "Invalid move. Try again. "
        + "Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. Try again. "
        + "Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. Try again. "
        + "Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. Try again. "
        + "Illegal card index, please reenter the number of card to be moved\n"
        + "Invalid move. Try again. "
        + "Illegal pile number, please reenter the number of pile to move to\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move to\n"
        + "Invalid move. Try again. "
        + "Please reenter the pile to move to\n"
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
  public void testMoveToFoundationPile() {
    in = new StringReader("C1 7 F1 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "F1: A♣\n"
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
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥\n" + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testMoveToOpenPile() {
    in = new StringReader("C1 7 O1 Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "F1:\n"
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
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥" + "\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveToFoundationPile() {
    in = new StringReader("C8 7 F1 Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveToOpenPile() {
    in = new StringReader("C8 7 O1 C8 6 O1 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n" + "F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1: 2♥\n"
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
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveFromFoundationPile() {
    in = new StringReader("F1 7 O1 Q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveToCascadePileIllegaly() {
    in = new StringReader("C1 7 C2 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveNegativePileNum() {
    in = new StringReader("C-1 7 C-2 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveNegativeIndex() {
    in = new StringReader("C1 -7 C2 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveBigIndex() {
    in = new StringReader("C1 100 C2 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }

  @Test
  public void testIllegalMoveInvalidPileNum() {
    in = new StringReader("C100 7 C20 q");
    controller = new SimpleFreecellController(model, in, out);
    controller.playGame(model.getDeck(), 8, 4, false);
    assertEquals(initialDeck + "\n" + "Invalid move. Try again. Move not permitted\n"
        + "Game quit prematurely.", out.toString());
  }


  //an actual finished game, I only use contains here cause its too much, or I shall use substring
  @Test
  public void testGameOver() {

    String cheatCode = "";
    for (int i = 51; i >= 0; i--) {
      int j = i % 4;
      cheatCode += "C" + (i + 1) + " 1" + " F" + (j + 1) + " ";
    }
    Readable input = new StringReader(cheatCode);
    FreecellController<Card> superBigGameController = new SimpleFreecellController(superBigGame,
        input, out);
    superBigGameController.playGame(superBigGame.getDeck(), 53, 10, false);
    assertTrue(out.toString().contains("F1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "F2: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "F3: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "F4: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "O10:\n"
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
        + "C52:\n"
        + "C53:\n"
        + "Game over."));
  }

  @Test
  public void testGameOverWithInvalidInput() {

    String cheatCodeWithMessStart = "C1 3 C8 C1 2 O1 C4 2 O1 ";
    for (int i = 51; i >= 0; i--) {
      int j = i % 4;
      cheatCodeWithMessStart += "C" + (i + 1) + " 1" + " F" + (j + 1) + " ";
    }
    Readable input = new StringReader(cheatCodeWithMessStart);
    FreecellController<Card> superBigGameController = new SimpleFreecellController(superBigGame,
        input, out);
    superBigGameController.playGame(superBigGame.getDeck(), 53, 10, false);
    assertTrue(out.toString().contains("F1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
        + "F2: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
        + "F3: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
        + "F4: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "O5:\n"
        + "O6:\n"
        + "O7:\n"
        + "O8:\n"
        + "O9:\n"
        + "O10:\n"
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
        + "C52:\n"
        + "C53:\n"
        + "Game over."));
  }


  @Test(expected = IllegalStateException.class)
  public void testIllegalAppendable() {
    Appendable brokenAp = new MyAppendable();
    controller = new SimpleFreecellController(model, in, brokenAp);
    controller.playGame(model.getDeck(), 8, 4, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalReadable() {
    Readable brokenRd = new MyReadable();
    controller = new SimpleFreecellController(model, brokenRd, out);
    controller.playGame(model.getDeck(), 8, 4, false);
  }


}


