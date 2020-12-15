import java.util.List;

public interface ClassExtractor<T> {
    List<List<String>> getFieldsValues(List<T> entities);
    List<String> getFieldsNames();
}
