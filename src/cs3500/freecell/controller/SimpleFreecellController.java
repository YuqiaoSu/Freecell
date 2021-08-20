package cs3500.freecell.controller;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.FreecellModelCreator.GameType;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

/**
 * The simple controller for running a free cell model. throw and IllegalArgument exception if null.
 * Plays the game with three input: in sequence of Source, CardIndex and Destination. Determine
 * whether player has won after each move is made
 */
public class SimpleFreecellController implements FreecellController<Card> {

  private FreecellModel<Card> model;
  private final Readable rd;
  private final Appendable ap;

  private int cardIndex;

  /**
   * construct a free cell controller with given model, a Readable and Appendable. All inputs must
   * not be null
   *
   * @param model a given free cell model
   * @param rd    implementation of readable
   * @param ap    implementation of appendable
   * @throws IllegalArgumentException if any of inputs is null
   */
  public SimpleFreecellController(FreecellModel<Card> model, Readable rd, Appendable ap)
      throws IllegalArgumentException {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Illegal Controller Input");
    }

    this.rd = rd;
    this.model = model;
    this.ap = ap;
    this.cardIndex = Integer.MIN_VALUE;


  }

  @Override
  public void playGame(List<Card> deck, int numCascades, int numOpens, boolean shuffle)
      throws IllegalStateException, IllegalArgumentException {
    if (deck == null) {
      throw new IllegalArgumentException("Null deck");
    }
    FreecellTextView view = new FreecellTextView(model, ap);
    try {
      try {
        model.startGame(deck, numCascades, numOpens, shuffle);
      } catch (IllegalArgumentException e) {
        view.renderMessage("Could not start game.");
        return;
      }
      view.renderBoard();
      view.renderMessage("\n");
      Scanner scan = new Scanner(this.rd);
      PileType src = null;
      PileType des = null;
      int from = 0;
      int to = 0;
      int count = 0;
      if (!scan.hasNext()) {
        throw new IllegalStateException("null input");
      }

      while (scan.hasNext()) {
        String in = scan.next();

        if (isQuit(in, view)) {
          return;
        }
        if (in.equals("render")) {
          view.renderBoard();
          view.renderMessage("\n");
          continue;
        }

        if (src == null) {
          try {
            String str = in.substring(0, 1);
            src = whichPile(str);
            from = Integer.valueOf(in.substring(1)) - 1;
          } catch (NumberFormatException e) {
            src = null;

            view.renderMessage("Invalid move. Try again."
                + " Illegal pile number, please reenter the number of pile to move from" + "\n");
            continue;
          } catch (IllegalArgumentException e) {
            src = null;
            view.renderMessage(
                "Invalid move. Try again." + " Please reenter the pile to move from" + "\n");
            continue;
          }
        } else if (src != null && this.cardIndex == Integer.MIN_VALUE) {
          try {
            this.cardIndex = Integer.valueOf(in) - 1;
          } catch (NumberFormatException e) {
            this.cardIndex = Integer.MIN_VALUE;
            view.renderMessage("Invalid move. Try again."
                + " Illegal card index, please reenter the number of card to be moved" + "\n");
            continue;
          }

        } else if (src != null && this.cardIndex != Integer.MIN_VALUE && des == null) {
          try {
            String str = String.valueOf(in.charAt(0));
            des = whichPile(str);
            to = Integer.valueOf(in.substring(1)) - 1;
          } catch (NumberFormatException e) {
            des = null;
            view.renderMessage("Invalid move. Try again."
                + " Illegal pile number, please reenter the number of pile to move to" + "\n");
            continue;
          } catch (IllegalArgumentException e) {
            des = null;
            view.renderMessage(
                "Invalid move. Try again." + " Please reenter the pile to move to" + "\n");
            continue;
          }
        }

        if (src != null && des != null && this.cardIndex != Integer.MIN_VALUE) {
          try {
            model.move(src, from, cardIndex, des, to);

            view.renderBoard();
            view.renderMessage("\n");
            src = null;
            des = null;
            this.cardIndex = Integer.MIN_VALUE;
            if (model.isGameOver()) {
              view.renderBoard();
              view.renderMessage("\n");
              view.renderMessage("Game over.");
              return;
            }

          } catch (IllegalArgumentException e) {
            view.renderMessage("Invalid move. Try again." + " Move not permitted" + "\n");
            src = null;
            des = null;
            cardIndex = Integer.MIN_VALUE;
          }
        }


      }

      if (!scan.hasNext()) {
        throw new IllegalStateException("Run out of Input");
      }

    } catch (IOException e) {
      throw new IllegalStateException("Cannot read or write, please reenter the move");
    }
  }


  /**
   * determine from one of three Piletype, which is the String represent for.
   *
   * @param s the string to check
   * @return Piletype
   * @throws IllegalArgumentException if s is neither of three Piletype
   */
  private static PileType whichPile(String s) {
    switch (s) {
      case "C":
        return PileType.CASCADE;
      case "O":
        return PileType.OPEN;
      case "F":
        return PileType.FOUNDATION;
      default:
        throw new IllegalArgumentException("Invalid Pile Type");
    }
  }

  /**
   * check if the given string is q or Q, if it is , return true.
   *
   * @param str  the checking str
   * @param view appendable instance to append on
   * @return boolean
   */
  private boolean isQuit(String str, FreecellView view) {
    try {

      if (str.equals("q") || str.equals("Q")) {

        view.renderMessage("Game quit prematurely.");
        this.cardIndex = Integer.MIN_VALUE;
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      throw new IllegalStateException("Run out of Input");
    }
  }

  /**
   * The main methods designed for human player, play it if you wish.
   *
   * @param args main args
   */
  public static void main(String[] args) {
    Readable rad = new InputStreamReader(System.in);
    Appendable ap = System.out;
    Readable rd = new StringReader(" C1 7 C8 C8 6 C1 C1 6 C8 C8 5 C1 C1 5 C8 q");
    FreecellModel model = FreecellModelCreator.create(GameType.MULTIMOVE);
    SimpleFreecellController controller = new SimpleFreecellController(model, rad, ap);
    controller.playGame(model.getDeck(), 10, 1, false);
  }
}
