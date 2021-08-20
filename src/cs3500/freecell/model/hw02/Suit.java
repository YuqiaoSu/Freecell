package cs3500.freecell.model.hw02;

/**
 * A suit that is one of  Club, Spade, Diamond, Heart.
 */
public enum Suit {
  Club, Spade, Diamond, Heart;

  /**
   * draw the suit with its corresponding symbol.
   * @return String
   */
  public String drawSuit() {
    switch (this) {
      case Club:
        return "♣";
      case Spade:
        return "♠";
      case Diamond:
        return "♦";
      case Heart:
        return "♥";
      default:
        return "";
    }
  }

  /**
   * Check if the two suit are in different color.
   *
   * @param that the Suit to attach on
   * @return boolean
   */
  public boolean differentColor(Suit that) {
    if (this == Club || this == Spade) {
      return that == Diamond || that == Heart;
    } else {
      return that == Club || that == Spade;
    }
  }

}
