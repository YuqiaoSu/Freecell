package cs3500.freecell.model.hw02;

import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw04.AbstractFreecell;

/**
 * A simple free cell model implementation that covers what needed for playing a simple free cell.
 * Covers moving and returning numbers and index of cards and determine whether game is over during
 * play Changes: lifted redundant public methods to abstract class, setting implement detail to
 * abstract methods and inherited by whichever class that extends it (easier for changing rules).
 * Private fields are lifted as well and changed to protected since redundant and leaving new
 * classes implements there own private fields. Design thinking process is presented above
 * AbstractFreecell
 */
public final class SimpleFreecellModel extends AbstractFreecell {

  protected boolean validSource(PileType pileType, int num, int cardIndex) {
    switch (pileType) {
      case CASCADE:
        return getNumCardsInCascadePile(num) > cardIndex;
      case OPEN:
        return getNumCardsInOpenPile(num) == cardIndex + 1;
      case FOUNDATION:
        return false;
      default:
        throw new IllegalArgumentException("PileType not exist");
    }
  }

  @Override
  protected void movingCards(PileType source, int pileNumber, int cardIndex) {
    if (source == PileType.OPEN) {
      moving.add(getOpenCardAt(pileNumber));
    } else if (source == PileType.CASCADE) {

      if (cardIndex == getNumCardsInCascadePile(pileNumber) - 1) {
        moving.add(getCascadeCardAt(pileNumber, cardIndex));

      } else {
        Card pre = getCascadeCardAt(pileNumber, cardIndex);
        Card cur = getCascadeCardAt(pileNumber, cardIndex + 1);
        for (int i = cardIndex + 1; i < getNumCardsInCascadePile(pileNumber); i++) {
          if (!pre.differentColor(cur) && !pre.oneMore(cur)) {
            throw new IllegalArgumentException("not moveable");
          }
          moving.add(getCascadeCardAt(pileNumber, i));
        }
      }
    }

  }

  @Override
  protected boolean validDestination(PileType pileType, int num) {
    switch (pileType) {
      case CASCADE:
        return getCascadeCardAt(num, getNumCardsInCascadePile(num) - 1).oneMore(moving.get(0))
            && getCascadeCardAt(num, getNumCardsInCascadePile(num) - 1)
            .differentColor(moving.get(0));
      case OPEN:
        return getNumCardsInOpenPile(num) == 0 && moving.size() == 1;
      case FOUNDATION:
        return addingToFoundationPile(num);
      default:
        throw new IllegalArgumentException("PileType not exist");
    }
  }

  protected boolean addingToFoundationPile(int numFoundationPile) {
    if (moving.size() > 1) {
      return false;
    }

    if (getNumCardsInFoundationPile(numFoundationPile) == 0) {
      return moving.get(0).ace();
    } else {
      return moving.get(0).oneMore(
          getFoundationCardAt(numFoundationPile,
              getNumCardsInFoundationPile(numFoundationPile) - 1))
          && getFoundationCardAt(numFoundationPile,
          getNumCardsInFoundationPile(numFoundationPile) - 1)
          .sameSuit(moving.get(0));
    }
  }

}

