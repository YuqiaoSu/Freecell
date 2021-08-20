import static org.junit.Assert.assertEquals;

import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.Pile;
import cs3500.freecell.model.hw02.Suit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Test the pile class and its method.
 */
public class PileTest {

  private final Card clubAce = new Card(0, Suit.Club);
  private final Card queenC = new Card(11, Suit.Club);
  private final Card jackC = new Card(10, Suit.Club);
  private final List<Card> deck = new ArrayList<>(Arrays.asList(clubAce, jackC, queenC));
  private final List<Card> foodeck = new ArrayList<>(Arrays.asList(clubAce, jackC, queenC, null));
  private final Pile pile = new Pile(PileType.CASCADE, deck);
  private final Pile emptyPile = new Pile(PileType.OPEN);


  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullPileType() {
    Pile temp = new Pile(null, deck);
  }


  @Test
  public void testgetCardAt() {
    assertEquals(queenC, pile.getCardAtIndex(2));
    assertEquals(clubAce, pile.getCardAtIndex(0));
  }

  @Test
  public void testcardNum() {
    Pile temp = new Pile(PileType.CASCADE);
    assertEquals(0, temp.cardNum());
    assertEquals(3, pile.cardNum());
  }

  @Test
  public void testaddCard() {
    Pile temp = new Pile(PileType.CASCADE);
    temp.addCard(clubAce);
    assertEquals(1, temp.cardNum());
    assertEquals(clubAce, temp.getCardAtIndex(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void addNullCard() {
    pile.addCard(null);
  }

  @Test
  public void testaddListofCard() {
    Pile temp = new Pile(PileType.CASCADE);
    temp.addListCards(deck);
    assertEquals(3, temp.cardNum());
    assertEquals(clubAce, temp.getCardAtIndex(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void addNullListCard() {
    pile.addListCards(foodeck);
  }

  @Test
  public void testMoveFrom() {
    assertEquals(3, pile.cardNum());
    pile.moveFrom(0);
    assertEquals(0, pile.cardNum());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetCardAtNegative() {
    pile.getCardAtIndex(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetCardOutofBound() {
    pile.getCardAtIndex(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testgetFromEmptyList() {
    emptyPile.getCardAtIndex(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromNegIndex() {
    pile.moveFrom(-1);
  }
}
