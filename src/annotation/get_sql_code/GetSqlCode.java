package annotation.get_sql_code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by Haochen on 2016/12/6.
 */
public class GetSqlCode {
    public static void main(String[] args) {
        Bean b = new Bean();
        try {
            System.out.println(new GetSqlCode(b).sqlCode());
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object bean;

    public GetSqlCode(Object bean) {
        this.bean = bean;
    }

    public String sqlCode() throws AnnotationNotFoundException {
        Class c = bean.getClass();
        if (!c.isAnnotationPresent(Table.class)) {
            throw new AnnotationNotFoundException("Table not found");
        }
        String result = "CREATE TABLE ";
        Table t = (Table) c.getAnnotation(Table.class);
        result += t.name() + " IF NOT EXISTS(";


        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Column.class)) {
                String type = type(f);
                if (type != null) {
                    Column column = f.getAnnotation(Column.class);
                    result += column.name() + " " + type + ",";
                } else {
                    throw new AnnotationNotFoundException("Column not found");
                }
            }
        }
        result = result.substring(0, result.length() - 1) + ");";
        return result;
    }

    private String type(Field field) {
        switch (field.getType().getSimpleName()) {
            case "int":
                return "INTEGER";
            case "String":
                return "TEXT";
        }
        return null;
    }

    @Table(name="Bean")
    private static class Bean {
        @Column(name="id")
        int id;
        @Column(name="value")
        String value;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Table {
        String name() default "";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Column {
        String name() default "";
    }

    private class AnnotationNotFoundException extends Exception {
        public AnnotationNotFoundException(String message) {
            super(message);
        }
    }
}
