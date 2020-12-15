import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<List<String>> values = new ArrayList<>();
        for (T entity : entities) {
            values.add(getValuesFromEntity(entity));
        }
        return values;
    }

    private List<String> getValuesFromEntity(T entity) {
        List<String> currentValues = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                currentValues.add(field.get(entity).toString());
            } catch (Exception e) {
                currentValues.add("empty");
            }
        }
        return currentValues;
    }

    @Override
    public List<String> getFieldsNames() {
        return fields.stream()
                .map(field -> field.isAnnotationPresent(NameInReport.class) ? field.getAnnotation(NameInReport.class).name() : field.getName())
                .collect(Collectors.toList());
    }

    private List<Field> getClassFields() {
        List<Field> fields = new ArrayList<>();
        Class currentProcessedClass = clazz;
        while (currentProcessedClass != null) {
            fields.addAll(Arrays.asList(currentProcessedClass.getDeclaredFields()));
            currentProcessedClass = currentProcessedClass.getSuperclass();
        }
        return fields.stream()
                .filter(f -> !f.isSynthetic() && !Modifier.isStatic(f.getModifiers()))
                .collect(Collectors.toList());
    }
}
