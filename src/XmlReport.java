import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XmlReport implements Report{
    private final Workbook workbook;

    public XmlReport(Workbook workbook) {
        this.workbook = workbook;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    @Override
    public byte[] asBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.workbook.write(baos);
        return baos.toByteArray();
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        this.workbook.write(os);
    }
}
