package cs3500.freecell.view;

import cs3500.freecell.model.FreecellModelState;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * View the Freecell game state by displaying in String.
 */
public class FreecellTextView implements FreecellView {

  private FreecellModelState<?> freeCell;
  private Appendable ap;

  /**
   * construct a Text viewer of Free cell.
   *
   * @param freeCell an ongoing game
   * @throws IllegalArgumentException if free cell is null
   */
  public FreecellTextView(FreecellModelState<?> freeCell) {
    if (freeCell == null) {
      throw new IllegalArgumentException("Invalid free cell");
    }
    this.freeCell = freeCell;
  }

  /**
   * construct a Text viewer of Free cell with appendable.
   *
   * @param freeCell an implementation of free cell
   * @param ap       an implementation of Appendable
   */
  public FreecellTextView(FreecellModelState<?> freeCell, Appendable ap) {
    if (freeCell == null || ap == null) {
      throw new IllegalArgumentException("Invalid Text View");
    }
    this.freeCell = freeCell;
    this.ap = ap;
  }

  @Override
  public String toString() {
    String str = "";
    try {
      int cascadeNum = freeCell.getNumCascadePiles();
      int openNum = freeCell.getNumOpenPiles();

      if (this.freeCell == null) {
        if (cascadeNum == -1) {
          return "";
        }
      }
      int i = 0;
      int j = 0;
      int totalCards = 0;

      for (i = 1; i < 5; i++) {
        str += "F" + i + ":";
        totalCards = freeCell.getNumCardsInFoundationPile(i - 1);
        if (totalCards == 0) {
          str += "\n";
        }
        for (j = 0; j < totalCards; j++) {

          if (j > 0 && j < totalCards) {
            str += ",";
          }

          str += " " + freeCell.getFoundationCardAt(i - 1, j);
          if (j == totalCards - 1) {
            str += "\n";
          }

        }
      }

      for (i = 1; i < openNum + 1; i++) {
        str += "O" + String.valueOf(i) + ":";
        totalCards = freeCell.getNumCardsInOpenPile(i - 1);
        if (totalCards == 0) {
          str += "\n";
          continue;
        }
        str += " " + freeCell.getOpenCardAt(i - 1) + "\n";
      }

      for (i = 1; i <= cascadeNum; i++) {
        str += "C" + String.valueOf(i) + ":";
        totalCards = freeCell.getNumCardsInCascadePile(i - 1);
        for (j = 0; j < totalCards; j++) {
          if (totalCards == 0) {
            str += "\n";
            break;
          }

          if (j > 0) {
            str += ",";
          }

          str += " " + freeCell.getCascadeCardAt(i - 1, j);

        }
        if (i != cascadeNum) {
          str += "\n";
        }
      }
    } catch (IllegalStateException e) {
      return "";
    }

    return str;
  }

  @Override
  public void renderBoard() throws IOException {
    ap.append(this.toString());

  }

  @Override
  public void renderMessage(String message) throws IOException {
    if (message == null) {
      message = "";
    }
    ap.append(message);
  }

}



