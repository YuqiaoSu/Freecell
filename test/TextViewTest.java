import static org.junit.Assert.assertEquals;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.io.IOException;
import org.junit.Test;

/**
 * test the viewer of Freecell.
 */
public class TextViewTest {

  protected FreecellModel<Card> s = new SimpleFreecellModel();
  protected FreecellModel<Card> manyCacadePile = new SimpleFreecellModel();
  protected FreecellView viewer = new FreecellTextView(s);
  protected FreecellView manyCascade = new FreecellTextView(manyCacadePile);
  protected StringBuffer bf = new StringBuffer();
  protected FreecellView rdViewer = new FreecellTextView(s, bf);

  @Test(expected = IllegalArgumentException.class)
  public void testNullFreeCell() {
    FreecellTextView temp = new FreecellTextView(null);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    FreecellTextView temp = new FreecellTextView(s, null);
  }

  @Test
  public void testEmptyMessage() throws IOException {
    rdViewer.renderMessage(null);
    assertEquals("", bf.toString());
  }

  @Test
  public void testEmptyBoard() throws IOException {
    try {
      s.startGame(s.getDeck(), 8, 0, false);
    } catch (IllegalArgumentException e) {
      rdViewer = new FreecellTextView(s, bf);
      assertEquals("", bf.toString());
    }

  }

  @Test
  public void testtoStringEmpty() {
    try {
      s.startGame(s.getDeck(), 8, 0, false);
    } catch (IllegalArgumentException e) {
      viewer = new FreecellTextView(s);
      assertEquals("", viewer.toString());
    }
  }

  @Test
  public void testToString() {
    s.startGame(s.getDeck(), 8, 4, false);
    viewer = new FreecellTextView(s);
    s.move(PileType.CASCADE, 0, 6, PileType.OPEN, 2);
    assertEquals("F1:\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3: A♣\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠, A♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣, 2♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥", viewer.toString());

    s.move(PileType.OPEN, 2, 0, PileType.FOUNDATION, 0);

    assertEquals("F1: A♣\n"
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
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥", viewer.toString());

    s.move(PileType.CASCADE, 4, 5, PileType.FOUNDATION, 0);
    assertEquals("F1: A♣, 2♣\n"
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
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥", viewer.toString());
    s.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 7);
    assertEquals("F1: A♣, 2♣\n"
        + "F2:\n"
        + "F3:\n"
        + "F4:\n"
        + "O1:\n"
        + "O2:\n"
        + "O3:\n"
        + "O4:\n"
        + "C1: K♣, J♣, 9♣, 7♣, 5♣, 3♣\n"
        + "C2: K♠, J♠, 9♠, 7♠, 5♠, 3♠\n"
        + "C3: K♦, J♦, 9♦, 7♦, 5♦, 3♦, A♦\n"
        + "C4: K♥, J♥, 9♥, 7♥, 5♥, 3♥, A♥\n"
        + "C5: Q♣, 10♣, 8♣, 6♣, 4♣\n"
        + "C6: Q♠, 10♠, 8♠, 6♠, 4♠, 2♠\n"
        + "C7: Q♦, 10♦, 8♦, 6♦, 4♦, 2♦\n"
        + "C8: Q♥, 10♥, 8♥, 6♥, 4♥, 2♥, A♠", viewer.toString());

  }

  //test to see if \n between Piles and Cards works
  @Test
  public void testToStringLongCascade() {
    manyCacadePile.startGame(manyCacadePile.getDeck(), 53, 10, false);
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
        + "O10:\n"
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
        + "C53:", manyCascade.toString());
  }

  @Test
  public void testFinishingGame() {
    manyCacadePile.startGame(manyCacadePile.getDeck(), 53, 10, false);
    for (int i = 51; i >= 0; i--) {
      int j = i % 4;
      manyCacadePile.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, j);
    }
    assertEquals("F1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
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
        + "C53:", manyCascade.toString());
  }

  @Test(expected = IOException.class)
  public void testRenderBoardIO() throws IOException {
    Appendable brokenAp = new MyAppendable();
    s.startGame(s.getDeck(), 8, 4, false);
    rdViewer = new FreecellTextView(s, brokenAp);
    rdViewer.renderBoard();
  }

  @Test(expected = IOException.class)
  public void testRenderMessageIO() throws IOException {

    Appendable brokenAp = new MyAppendable();
    s.startGame(s.getDeck(), 8, 4, false);
    rdViewer = new FreecellTextView(s, brokenAp);
    rdViewer.renderMessage("Message");
    assertEquals("Message", bf.toString());
  }


  @Test
  public void testRenderBoard() throws IOException {
    s.startGame(s.getDeck(), 8, 4, false);
    rdViewer.renderBoard();
    assertEquals(viewer.toString(), bf.toString());
  }

  @Test
  public void testRenderMessage() throws IOException {
    rdViewer.renderMessage("Message");
    assertEquals("Message", bf.toString());
  }

  @Test
  public void testRenderNullMessage() throws IOException {
    rdViewer.renderMessage(null);
    assertEquals("", bf.toString());
  }

}

