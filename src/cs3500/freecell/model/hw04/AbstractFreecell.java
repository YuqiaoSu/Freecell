package cs3500.freecell.model.hw04;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.Pile;
import cs3500.freecell.model.hw02.Suit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * design comment: Did not make Multi move extends previous SimpleFreecell because Multi-move game
 * is not a Simple Freecell game. Having a new interface that extends the FreecellModel interface
 * with another methods in it sounds plausible, however the handin test by calling move methods.
 * This means my new interface are having move() methods with same return type ,  otherwise it's
 * meaningless as private helper does not need to go into interface. Although java allow the
 * subclass that implements the interface use the move method from the child's first It's not a good
 * design in this case, since we are not having unlimited commands like command design pattern that
 * would needs this nor introducting new functionality like trace methods in turtle example.
 *
 * <p>So abstract class seems a good option as moving all common implementation to abstract class
 * and make each of its subclasses satisfy there own constraints seems fitting the scenario.
 *
 * <p>Meanwhile, moving private helper to protected does not affect client code and public methods
 * remain public. This even allow parts of the validation methods(in my code) reusable within
 * further free cell. As would expected, the free cell may have multiple constraints that cannot be
 * summerized within an interface, so we ask the subclasses to implements there own game rule and
 * move conditions. For example, if future game wants the constraints that only King can go to empty
 * cascade pile, then still, interface is not useful as the move methods don't change. However, as I
 * lifted my methods upward the future version of free cell can still reuse previous code just like
 * interface extends.
 *
 * <p>There is another solution that use delegation, however, that would make further implementation
 * of freecells depends on the correctness of SimpleFreecell model If something went wrong in my
 * first implementaion and I can't fix it from the beginning, delegation would corrupt at some
 * time in future.
 *
 * <p>In this abstract class, I set the vaildSource, validDestination and movingCard which
 * used to be my private methods in SimpleFreecellModel to abstract class in order not to confuse
 * future classes that extends this abstract class. Although there seems to be redundant code, but
 * they are actually giving clients the freedom of defining there own conditions on move methods.
 *
 * <p>If I moved all duplicate code to this abstract class, then my client must make sure they are
 * looking for the same condition as I did in previous assignment which would confuse their code.
 */
public abstract class AbstractFreecell implements FreecellModel<Card> {

  protected List<Card> moving = new ArrayList<>();
  protected final Random random = new Random();

  protected Pile[] open;
  protected Pile[] cascade;
  protected Pile[] foundation = new Pile[4];

  protected boolean startGame;


  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<>(52);

    for (int j = 12; j >= 0; j--) {
      for (Suit suit : Suit.values()) {
        Card card = new Card(j, suit);
        deck.add(card);
      }
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
      throws IllegalArgumentException {
    if (numCascadePiles < 4 || numOpenPiles < 1) {
      this.startGame = false;
      throw new IllegalArgumentException("Wrong number of pile");
    }
    if (deck.size() != 52 || !validDeck(deck) || deck == null) {
      this.startGame = false;
      throw new IllegalArgumentException("Invalid deck");
    }

    open = new Pile[numOpenPiles];
    cascade = new Pile[numCascadePiles];
    List<Card> copy = new ArrayList<>(deck);

    for (int i = 0; i < 4; i++) {
      foundation[i] = new Pile(PileType.FOUNDATION);
    }

    copy = shuffle ? shuffleDeck(copy, random) : copy;
    for (int i = 0; i < numCascadePiles; i++) {
      cascade[i] = new Pile(PileType.CASCADE);

    }
    for (int i = 0; i < numOpenPiles; i++) {
      open[i] = new Pile(PileType.OPEN);
    }

    for (int i = 0; i < deck.size(); i++) {
      int num = i % numCascadePiles;
      Card card = copy.get(i);
      cascade[num].addCard(card);
    }
    this.startGame = true;
  }

  /**
   * test if a deck has no duplicated card in it.
   *
   * @param deck a deck of cards
   * @return boolean
   */
  protected boolean validDeck(List<Card> deck) {
    Set<Card> set = new HashSet<>(deck.size());
    for (Card c : deck) {
      if (set.contains(c)) {
        return false;
      }
      set.add(c);
    }
    return true;
  }

  /**
   * shuffle the Deck with a Random sequence.
   *
   * @param deck the deck of 52 cards
   * @param rand a new Random
   * @return shuffled deck
   */
  protected List<Card> shuffleDeck(List<Card> deck, Random rand) {
    for (int i = 0; i < deck.size(); i++) {
      int a = rand.nextInt(52);
      Card temp = deck.get(a);
      deck.set(a, deck.get(i));
      deck.set(i, temp);
    }
    return deck;
  }


  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    if (source == null || destination == null) {
      throw new IllegalArgumentException("source or destination cannot be empty");
    }
    if (source == destination && pileNumber == destPileNumber) {
      //do nothing
    } else {

      if (!validSource(source, pileNumber, cardIndex)) {
        throw new IllegalArgumentException("not movable");
      }
      if (!startGame) {
        throw new IllegalStateException("Game not started");
      }
      movingCards(source, pileNumber, cardIndex);

      if (!validDestination(destination, destPileNumber)) {
        throw new IllegalArgumentException("not movable");
      }

      Pile[] src = thePileType(source);
      src[pileNumber].moveFrom(cardIndex);
      Pile[] des = thePileType(destination);
      des[destPileNumber].addListCards(this.moving);
      moving = new ArrayList<>();
    }
  }

  /**
   * test if the source is valid and able to be moved from.
   *
   * @param pileType  the type of pile
   * @param num       index of that pile in list of pile
   * @param cardIndex the wanted card
   * @return boolean
   */
  protected abstract boolean validSource(PileType pileType, int num, int cardIndex);

  /**
   * test if the destination is valid and able to be moved for.
   *
   * @param pileType one of three PileType
   * @param num      the num of the pile, starting from 0
   * @return boolean
   */
  protected abstract boolean validDestination(PileType pileType, int num);


  /**
   * add the moving with the cards about to move and check if they are validate as a Cascade pile
   * cannot move those that are not one more and in different color.
   *
   * @param source     the source to move cards
   * @param pileNumber the number of the given pile
   * @param cardIndex  the index of that card to be moved
   */
  protected abstract void movingCards(PileType source, int pileNumber, int cardIndex);

  /**
   * helper method to determine the source and destinationed PileType.
   *
   * @param pileType the given type of pile
   * @return Pile[] an array of pile.
   */
  protected Pile[] thePileType(PileType pileType) {
    switch (pileType) {
      case CASCADE:
        return this.cascade;
      case OPEN:
        return this.open;
      case FOUNDATION:
        return this.foundation;
      default:
        throw new IllegalArgumentException("PileType not exist");
    }
  }


  @Override
  public boolean isGameOver() {
    if (!startGame) {
      return false;
    }
    for (int i = 0; i < 4; i++) {
      if (getNumCardsInFoundationPile(i) != 13) {
        return false;
      }
    }
    return true;

  }

  /**
   * check the exception for illegal Index input that is either negative or out of bounds refuse the
   * get when game not started.
   *
   * @param piles the array that contains PileType
   * @param index the index to be checked
   */
  private void illegalPileIndex(Pile[] piles, int index) {
    if (!startGame) {
      throw new IllegalStateException("Game not started");
    }
    if (index < 0 || index > piles.length - 1) {
      throw new IllegalArgumentException("Invalid Index for pile");
    }

  }

  @Override
  public int getNumCardsInFoundationPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    illegalPileIndex(this.foundation, index);
    return this.foundation[index].cardNum();
  }

  @Override
  public int getNumCascadePiles() {
    if (startGame) {
      return cascade.length;
    } else {
      return -1;
    }
  }

  @Override
  public int getNumCardsInCascadePile(int index)
      throws IllegalArgumentException, IllegalStateException {
    illegalPileIndex(this.cascade, index);
    return this.cascade[index].cardNum();
  }

  @Override
  public int getNumCardsInOpenPile(int index)
      throws IllegalArgumentException, IllegalStateException {
    illegalPileIndex(this.open, index);
    return this.open[index].cardNum();
  }

  @Override
  public int getNumOpenPiles() {
    if (startGame) {
      return open.length;
    } else {
      return -1;
    }
  }

  @Override
  public Card getFoundationCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    illegalPileIndex(foundation, pileIndex);

    Pile pile = foundation[pileIndex];

    return pile.getCardAtIndex(cardIndex);
  }

  @Override
  public Card getCascadeCardAt(int pileIndex, int cardIndex)
      throws IllegalArgumentException, IllegalStateException {
    illegalPileIndex(cascade, pileIndex);

    Pile pile = cascade[pileIndex];

    return pile.getCardAtIndex(cardIndex);
  }

  @Override
  public Card getOpenCardAt(int pileIndex) throws IllegalArgumentException, IllegalStateException {
    illegalPileIndex(open, pileIndex);

    Pile pile = open[pileIndex];
    if (pile.cardNum() == 0) {
      return null;
    }
    return pile.getCardAtIndex(0);
  }

}



