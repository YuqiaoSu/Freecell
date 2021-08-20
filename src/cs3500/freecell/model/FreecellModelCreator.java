package cs3500.freecell.model;

import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.FreecellMultiMoveModel;

/**
 * Determines which Free cell model to be created given by a GameType.
 */
public class FreecellModelCreator {

  /**
   * The game type of free cell, multi move or single move.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE;
  }

  /**
   * creates a SimpleFreecellModel for SINGLEMOVE creates a FreecellMultiMoveModel for MULTIMOVE.
   *
   * @param type one of two gametype
   * @return freecell model
   * @throws IllegalArgumentException if type is null
   */
  public static FreecellModel<Card> create(GameType type) {
    if (type == null) {
      throw new IllegalArgumentException("null game type");
    }
    switch (type) {
      case SINGLEMOVE:
        return new SimpleFreecellModel();
      case MULTIMOVE:
        return new FreecellMultiMoveModel();
      default:
        throw new IllegalArgumentException("Illegal game type");
    }
  }
}
