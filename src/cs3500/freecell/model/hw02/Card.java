package cs3500.freecell.model.hw02;

/**
 * the Card that has a value and its suit for Free cell.
 */
public class Card {

  private final int value;
  private final Suit suit;

  /**
   * Create a card with value and its suit.
   *
   * @param value the value of the card, 0 is Ace, 10,11,12 is J,Q,K
   * @param suit  one of the four SuitType
   */
  public Card(int value, Suit suit) {
    if (value < 0 || value >= 13 || suit == null) {
      throw new IllegalArgumentException("Illegal input for a card");
    }
    this.value = value;
    this.suit = suit;
  }

  @Override
  public String toString() {
    switch (this.value) {
      case (0):
        return "A" + suit.drawSuit();
      case (10):
        return "J" + suit.drawSuit();
      case (11):
        return "Q" + suit.drawSuit();
      case (12):
        return "K" + suit.drawSuit();
      default:
        return (this.value + 1) + suit.drawSuit();
    }

  }

  /**
   * Are the two cards having same suit.
   *
   * @param that the card that is comparing
   * @return boolean
   * @throws IllegalArgumentException if that is null
   */
  public boolean sameSuit(Card that) {
    if (that == null) {
      throw new IllegalArgumentException("Not a Card");
    }
    return this.suit == that.suit;
  }

  /**
   * whether this card is Ace.
   *
   * @return boolean
   */
  public boolean ace() {
    return this.value == 0;
  }


  /**
   * check if this value is exactly one more above the other.
   *
   * @param that the card to compare with
   * @return boolean
   * @throws IllegalArgumentException if that is null
   */
  public boolean oneMore(Card that) {
    if (that == null) {
      throw new IllegalArgumentException("Not a Card");
    }
    return this.value == that.value + 1;
  }

  /**
   * check if this card can be attached to that card in an cascade pile.
   *
   * @param that the card to attach
   * @return boolean
   * @throws IllegalArgumentException if that is null
   */
  public boolean differentColor(Card that) {
    if (that == null) {
      throw new IllegalArgumentException("Not a Card");
    }
    return this.suit.differentColor(that.suit);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Card)) {
      return false;
    }
    Card that = (Card) obj;
    return this.value == that.value && this.suit == that.suit;
  }

  @Override
  public int hashCode() {
    return value + suit.hashCode();
  }
}




