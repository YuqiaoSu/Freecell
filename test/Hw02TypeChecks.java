import cs3500.freecell.model.PileType;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import java.util.List;

/**
 * Do not modify this file. This file should compile correctly with your code!
 */
public class Hw02TypeChecks {

  public static void main(String[] args) {
    helper(new cs3500.freecell.model.hw02.SimpleFreecellModel());

  }

  private static <T> void helper(cs3500.freecell.model.FreecellModel<T> model) {
    FreecellView view = new FreecellTextView(model);
    List<T> deck = model.getDeck();
    model.startGame(deck, 8, 4, false);
    model.move(PileType.CASCADE, 0, 6, PileType.CASCADE, 6);
    String output = view.toString();
    System.out.println(output);
  }
}
