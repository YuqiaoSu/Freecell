import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModelCreator;
import java.io.StringReader;
import org.junit.Before;

/**
 * extends the controller test and override the instance to multi move version.
 */
public class ImprovedControllerTest extends ControllerTest {

  @Before
  @Override
  public void initState() {
    model = FreecellModelCreator
        .create(FreecellModelCreator.GameType.MULTIMOVE);
    shuffle = FreecellModelCreator
        .create(FreecellModelCreator.GameType.MULTIMOVE);
    out = new StringBuilder();
    shuffleOut = new StringBuilder();
    superBigGame = FreecellModelCreator
        .create(FreecellModelCreator.GameType.MULTIMOVE);
    in = new StringReader("C1 7 C8 Q");
    controller = new SimpleFreecellController(model, in, out);
  }
}
