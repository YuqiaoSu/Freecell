package cs3500.freecell.model.hw02;

import cs3500.freecell.model.PileType;
import java.util.ArrayList;
import java.util.List;

/**
 * the Pile that contains one of three PileType and a list of cards.
 */
public class Pile {

  private List<Card> cards;

  /**
   * construct the pile using a designated PileType with a list of cards.
   *
   * @param pile  one of four PileType
   * @param cards a list of cards it contains
   * @throws IllegalArgumentException if pile is null
   */
  public Pile(PileType pile, List<Card> cards) {
    if (pile == null) {
      throw new IllegalArgumentException("Not a valid PileType");
    }
    this.cards = cards;
  }

  /**
   * construct the pile using a designated PileType and set the cards to empty list.
   *
   * @param pile one of four PileType
   * @throws IllegalArgumentException if pile is null
   */
  public Pile(PileType pile) {
    if (pile == null) {
      throw new IllegalArgumentException("Not a valid PileType");
    }
    this.cards = new ArrayList<>();
  }

  /**
   * the size of the list of cards.
   *
   * @return the number of cards in this pile
   */
  public int cardNum() {
    return cards.size();
  }

  /**
   * check for illegal index that is negative or out of bounds for List of cards then returns a.
   * card
   *
   * @param index the index starting at 0
   * @return Card desired at given index
   */
  public Card getCardAtIndex(int index) {
    try {
      return cards.get(index);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid index for cards");
    }

  }

  /**
   * add a card into this pile.
   *
   * @param card the card to add into this pile
   * @throws IllegalArgumentException if the cards is null
   */
  public void addCard(Card card) {

    if (card == null) {
      throw new IllegalArgumentException("Null Card");
    }
    this.cards.add(card);
  }

  /**
   * add multiple cards to this pile.
   *
   * @param cards a list of cards to add
   * @throws IllegalArgumentException if one of the cards is null
   */
  public void addListCards(List<Card> cards) {
    for (Card c : cards) {
      if (c == null) {
        throw new IllegalArgumentException("Null Card");
      }
      this.cards.add(c);
    }
  }

  /**
   * move from this Pile, starting From that index to the end of list.
   *
   * @param cardIndex move away the cards till the end of list
   */
  public void moveFrom(int cardIndex) {
    if (cardIndex < 0) {
      throw new IllegalArgumentException("Invalid Index for removing cards");
    }

    while (cardIndex < cards.size()) {
      cards.remove(cardIndex);
    }
  }


}
