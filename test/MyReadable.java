import java.io.IOException;
import java.nio.CharBuffer;

/**
 * specially designed readable for testing IOException.
 * Warning, only throw IOException
 */
class MyReadable implements Readable {

  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException("IO");
  }
}
