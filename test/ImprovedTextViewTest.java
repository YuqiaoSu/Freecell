import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

/**
 * extends the text view test with multi move version of game.
 */
public class ImprovedTextViewTest extends TextViewTest {

  protected FreecellModel<Card> s = FreecellModelCreator
      .create(FreecellModelCreator.GameType.MULTIMOVE);
  protected FreecellModel<Card> manyCacadePile = FreecellModelCreator
      .create(FreecellModelCreator.GameType.MULTIMOVE);
  protected FreecellView viewer = new FreecellTextView(s);
  protected FreecellView manyCascade = new FreecellTextView(manyCacadePile);
  protected StringBuffer bf = new StringBuffer();
  protected FreecellView rdViewer = new FreecellTextView(s, bf);
}
