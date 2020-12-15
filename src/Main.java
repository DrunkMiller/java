import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Account> accounts = Arrays.asList(
                new Account(1, "James", "Gosling", 100),
                new Account(2, "Donald", "Knuth", 134.77),
                new Account(3, "Linus", "Torvalds", 73.11)
        );
        try {
            FileOutputStream outputStream = new FileOutputStream("report.xls");
            ReportGenerator reportGenerator = new ExcelReportGenerator(new SimpleClassExtractor(Account.class));
            reportGenerator.generate(accounts).writeTo(outputStream);
            outputStream.close();
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
