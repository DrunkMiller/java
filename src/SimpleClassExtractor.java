import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SimpleClassExtractor<T> implements ClassExtractor<T> {
    private final Class<T> clazz;
    private final List<Field> fields;

    public SimpleClassExtractor(Class<T> clazz) throws IllegalAccessException {
        if (clazz == null) {
            throw new IllegalAccessException("Class type not be null");
        }
        this.clazz = clazz;
        this.fields = getClassFields();
    }

    @Override
    public List<List<String>> getFieldsValues(List<T> entities) {
        List<List<String>> values = new LinkedList<>();
        for (T entity : entities) {
            List<String> currentValues = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    currentValues.add(field.get(entity).toString());
                } catch (Exception e) {
                    currentValues.add("empty");
                }
            }
            values.add(currentValues);
        }
        return values;
    }

    @Override
    public List<String> getFieldsNames() {
        List<String> names = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(NameInReport.class)) {
                names.add(field.getAnnotation(NameInReport.class).name());
            }
            else {
                names.add(field.getName());
            }
        }
        return names;
    }

    private List<Field> getClassFields() {
        List<Field> fields = new ArrayList<>();
        Class currentProcessedClass = clazz;
        while (currentProcessedClass != null) {
            fields.addAll(Arrays.asList(currentProcessedClass.getDeclaredFields()));
            currentProcessedClass = currentProcessedClass.getSuperclass();
        }
        return fields;
    }
}
