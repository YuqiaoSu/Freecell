package cs3500.freecell.model.hw04;

import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.Pile;
import java.util.ArrayList;

/**
 * Free cell model that supports the version of multi move with an additional condition that enough
 * empty piles must be provided for multi move.
 */
public final class FreecellMultiMoveModel extends AbstractFreecell {

  public FreecellMultiMoveModel() {
    super();
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) {
    if (source == null || destination == null) {
      throw new IllegalArgumentException("source or destination cannot be empty");
    }
    if (!startGame) {
      throw new IllegalStateException("Game not started");
    }

    if (pileNumber < 0 || cardIndex < 0 || destPileNumber < 0) {
      throw new IllegalArgumentException("Negative Index");
    }

    if (source == destination && pileNumber == destPileNumber) {
      //do nothing
    } else {

      if (!validSource(source, pileNumber, cardIndex)) {
        moving = new ArrayList<>();
        throw new IllegalArgumentException("not movable source");
      }
      movingCards(source, pileNumber, cardIndex);

      if (!validDestination(destination, destPileNumber)) {
        moving = new ArrayList<>();
        throw new IllegalArgumentException(
            "not movable destination");
      }
      if (validEnoughEmpty()) {
        moving = new ArrayList<>();
        throw new IllegalArgumentException("Invalid Move, Not Enough Space");
      }

      Pile[] src = thePileType(source);
      src[pileNumber].moveFrom(cardIndex);
      Pile[] des = thePileType(destination);
      des[destPileNumber].addListCards(this.moving);
      moving = new ArrayList<>();
    }
  }


  private boolean validEnoughEmpty() {
    int emptyCascade = 0;
    int emptyOpen = 0;
    for (int i = 0; i < this.getNumCascadePiles(); i++) {
      if (getNumCardsInCascadePile(i) == 0) {
        emptyCascade += 1;
      }
    }
    for (int j = 0; j < this.getNumOpenPiles(); j++) {
      if (getNumCardsInOpenPile(j) == 0) {
        emptyOpen += 1;
      }
    }
    int allowedIndex = (int) ((emptyOpen + 1) * Math.pow(2, emptyCascade));

    return moving.size() > allowedIndex;
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
        for (int i = cardIndex; i <= getNumCardsInCascadePile(pileNumber) - 1; i++) {
          if (!pre.differentColor(cur) || !pre.oneMore(cur)) {
            throw new IllegalArgumentException("not movable moving Card");
          }
          if (i == getNumCardsInCascadePile(pileNumber) - 2) {
            moving.add(getCascadeCardAt(pileNumber, i));
            moving.add(getCascadeCardAt(pileNumber, i + 1));
            break;
          }
          moving.add(getCascadeCardAt(pileNumber, i));
          pre = cur;
          cur = getCascadeCardAt(pileNumber, i + 2);

        }
      }
    }

  }

  @Override
  protected boolean validDestination(PileType pileType, int num) {
    if (pileType == PileType.CASCADE) {
      if (getNumCardsInCascadePile(num) == 0) {
        return true;
      }
    }
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
      if (!moving.get(0).ace()) {
        throw new IllegalArgumentException("Ace");
      }
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

  private int numOfCard(PileType source, int pileNumber) {
    switch (source) {
      case CASCADE:
        return getNumCardsInCascadePile(pileNumber);
      case OPEN:
        return getNumCardsInOpenPile(pileNumber);
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
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
}
