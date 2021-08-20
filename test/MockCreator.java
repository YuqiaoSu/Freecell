import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.hw02.Card;
import java.io.IOException;

/**
 * A mock factory class that is used for testing controller.
 */
public class MockCreator {

  private final StringBuilder ap;

  public MockCreator(StringBuilder ap) {
    this.ap = ap;
  }

  /**
   * this enum should be public as player can see whether multi move are supported.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE;
  }

  public FreecellModel<Card> create(FreecellModelCreator.GameType type) throws IOException {
    ap.append("Gametype is " + type);
    return new ConfirmFreecellModel(ap);
  }
}
