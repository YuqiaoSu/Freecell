import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.Suit;
import org.junit.Test;

/**
 * Test the card class and its method.
 */
public class CardTest {

  private final Card clubAce = new Card(0, Suit.Club);
  private final Card jackC = new Card(11, Suit.Club);
  private final Card tenC = new Card(10, Suit.Club);
  private final Card tenD = new Card(10, Suit.Diamond);

  @Test(expected = IllegalArgumentException.class)
  public void testNegCardNum() {
    Card foo = new Card(-1, Suit.Diamond);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBigNumber() {
    Card foo = new Card(14, Suit.Diamond);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSuit() {
    Card foo = new Card(14, null);
  }

  @Test
  public void testtoString() {
    assertEquals("A♣", clubAce.toString());
    assertEquals("Q♣", jackC.toString());
    assertEquals("J♣", tenC.toString());
  }

  @Test
  public void testAce() {
    assertTrue(clubAce.ace());
    assertFalse(jackC.ace());
  }


  @Test
  public void testsameSuit() {
    assertTrue(clubAce.sameSuit(tenC));
    assertFalse(tenC.sameSuit(tenD));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCardInputsamSuit() {
    jackC.sameSuit(null);
  }

  @Test
  public void testOneMore() {
    assertTrue(jackC.oneMore(tenD));
    assertFalse(jackC.oneMore(clubAce));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCardInputOneMore() {
    jackC.oneMore(null);
  }

  @Test
  public void attachable() {
    assertTrue(jackC.differentColor(tenD));
    assertFalse(jackC.differentColor(clubAce));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalCardInputdifferentColor() {
    jackC.differentColor(null);
  }

  @Test
  public void testHashCodeandEqual() {
    Card temp = new Card(10, Suit.Club);
    assertTrue(temp.hashCode() == tenC.hashCode());
    assertFalse(tenD.hashCode() == tenC.hashCode());
    assertTrue(temp.equals(tenC));
    assertFalse(tenC.equals(tenD));
  }
}
