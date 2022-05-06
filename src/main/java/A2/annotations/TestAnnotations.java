package A2.annotations;

import java.lang.reflect.Method;

/**
 * source: <a href="https://jenkov.com/tutorials/java-reflection/annotations.html">https://jenkov.com/tutorials/java-reflection/annotations.html</a>
 */
@StartClassDeclaration(arg2 = "Hello World")
public class TestAnnotations {

    @StartMethodDeclaration(arg1 = "bla", arg2 = "blub")
    public static void main(String[] args) {
        Class<TestAnnotations> aClass = TestAnnotations.class;
        StartClassDeclaration annotation = aClass.getAnnotation(StartClassDeclaration.class);

        if (annotation != null) {
            System.out.println("arg1: " + annotation.arg1());
            System.out.println("arg2: " + annotation.arg2());
        }

        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            StartMethodDeclaration annotation2 = method.getDeclaredAnnotation(StartMethodDeclaration.class);
            if (annotation2 != null) {
                System.out.println("arg1: " + annotation2.arg1());
                System.out.println("arg2: " + annotation2.arg2());
            }
        }

    }
}
