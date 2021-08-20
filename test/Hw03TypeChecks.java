////
//// DO NOT MODIFY THIS FILE
////
//// You don't need to submit it, but you should make sure it compiles.
//// Further explanation appears below.
////


import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * This class is provided to check that your code implements the expected API. If your code compiles
 * with an unmodified version of this class, then it very likely will also compile with the tests
 * that we use to evaluate your code.
 */
public class Hw03TypeChecks {

  public static void main(String[] args) {
    checkSignatures();
  }

  // This doesn't really need to be a dynamic method, since it doesn't use `this`

  /**
   * What do you mean dynamic?.
   */
  public static void checkSignatures() {
    Reader stringReader;
    StringBuffer out;

    checkNewModel(new SimpleFreecellModel(), new SimpleFreecellModel().getDeck());
    stringReader = new StringReader("C1 8 F1 q");
    out = new StringBuffer();
    checkNewController(new SimpleFreecellModel(), new SimpleFreecellModel().getDeck(),
        new SimpleFreecellController(new SimpleFreecellModel(), stringReader, out));
  }

  // This doesn't really need to be a dynamic method, since it doesn't use `this`
  static <K> void checkNewController(FreecellModel<K> model, List<K> deck,
      FreecellController<K> controller) {
    String input = "4 3";

    try {
      controller.playGame(deck, 7, 4, false);
    } catch (IllegalStateException e) {
      //the input or output did not work
    }
  }

  static <K> void checkNewModel(FreecellModel<K> model, List<K> deck) {
    Appendable ap = new StringBuilder();
    FreecellView view = new FreecellTextView(model, ap);
    List<K> initialDeck = model.getDeck();
    model.startGame(initialDeck, 7, 4, false);
    model.move(PileType.CASCADE, 0, 7, PileType.FOUNDATION, 0);
    try {
      view.renderBoard();
      view.renderMessage("Some message");
    } catch (IOException e) {
      //IOException
    }
    boolean done = model.isGameOver();
  }

  private Hw03TypeChecks() {
    throw new RuntimeException("Don't instantiate this: use it as a reference");
  }
}
