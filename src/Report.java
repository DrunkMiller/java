import java.io.IOException;
import java.io.OutputStream;

public interface Report {
    byte[] asBytes() throws IOException;
    void writeTo(OutputStream os) throws IOException;
}
