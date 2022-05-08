package A3.playground;

import A3.Inject;
import A3.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PersonReflectionTest {

    public static void main(String[] args) {
        try {
            Class<?> aClass = Class.forName("A3.playground.Person");
            Constructor<?>[] constructors = aClass.getConstructors();

//            Inject annotation = aClass.getAnnotation(Inject.class);

            Method getFirstnameMethod = aClass.getMethod("getFirstname");

            for (Constructor<?> constructor : constructors) {
//                System.out.println(constructor);
                int countArg = constructor.getParameterCount();
                if (countArg != 2) continue;

//                System.out.println(countArg);
                PersonInterface person = (PersonInterface) constructor.newInstance("Max", "Mustermann");

                Arrays.stream(aClass.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(Inject.class))
                        .forEach(field -> {
                            try {
                                field.set(person, new Logger());
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        });

                String firstname = (String) getFirstnameMethod.invoke(person);
                System.out.println(firstname);
                System.out.println(person.getLastname());

                person.log("bla");
//                System.out.println(person);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
