import com.github.jinahya.database.metadata.bind.Attribute;
import org.jspecify.annotations.Nullable;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

public class DebugNullable {
    public static void main(String[] args) throws Exception {
        Field field = Attribute.class.getDeclaredField("typeCat");
        System.out.println("Field: " + field.getName());
        for (Annotation ann : field.getAnnotations()) {
            System.out.println("  Annotation: " + ann.annotationType().getName());
        }
    }
}
