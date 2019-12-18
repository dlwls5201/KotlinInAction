package chapter10;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

//https://elfinlas.github.io/2017/12/14/java-custom-anotation-01/
public class AnnotationExam {

    public static void main(String[] args) {
        Field[] fields = AnnotationExam01.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(InsertIntData.class)) {

                InsertIntData insertIntData = field.getAnnotation(InsertIntData.class);
                System.out.println("[" + field.getName() + "] -> [" + insertIntData.data() + "]");

            }
        }

        System.out.println();

        Method[] methods = AnnotationExam02.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(InsertStringData.class)) {

                InsertStringData insertStringData = method.getAnnotation(InsertStringData.class);
                System.out.println("[" + method.getName() + "] -> [" + insertStringData.data() + "]");

            }
        }
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InsertIntData {
        int data() default 0;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InsertStringData {
        String data() default "default";
    }

    public class AnnotationExam01 {
        @InsertIntData(data = 30)
        private int myAge;

        @InsertIntData
        private int defaultAge;

        public AnnotationExam01() {
            this.myAge = -1;
            this.defaultAge = -1;
        }

        public int getMyAge() {
            return myAge;
        }

        public int getDefaultAge() {
            return defaultAge;
        }
    }

    public class AnnotationExam02 {

        private String myData;

        private String defaultData;

        public AnnotationExam02() {
            myData = "No data";
            defaultData = "No data";
        }

        @InsertStringData(data = "MHLab")
        public String getMyData() {
            return myData;
        }

        @InsertStringData
        public String getDefaultData() {
            return defaultData;
        }
    }
}
