import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import java.util.List;
import java.util.Objects;

/**
 * A mock model for Free cell game that is used for testing controller It is package- private so
 * only Controller test class can access it.
 */
class ConfirmFreecellModel implements FreecellModel<Card> {

  private final StringBuilder log;

  public ConfirmFreecellModel(StringBuilder log) {

    if (log == null) {
      throw new IllegalArgumentException("null string builder");
    }
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public List<Card> getDeck() {
    log.append("Deck created");
    return null;
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
      throws IllegalArgumentException {
    log.append(String
        .format("Game started with %d Cascade Piles, %d Open Piles, and shuffle is %b\n",
            numCascadePiles, numOpenPiles, shuffle));

  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    log.append(String
        .format("Move from %s%d at %d to %s%d\n", source, pileNumber, cardIndex, destination,
            destPileNumber));
  }

  @Override
  public boolean isGameOver() {
    log.append("GameOver tested\n");
    return false;
  }

  @Override
  public int getNumCardsInFoundationPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public int getNumCascadePiles() {
    return 1;
  }

  @Override
  public int getNumCardsInCascadePile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return 2;
  }

  @Override
  public int getNumCardsInOpenPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    return 3;
  }

  @Override
  public int getNumOpenPiles() {
    return 4;
  }

  @Override
  public Card getFoundationCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getCascadeCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getOpenCardAt(int pileIndex) throws IllegalArgumentException, IllegalStateException {
    return null;
  }
}