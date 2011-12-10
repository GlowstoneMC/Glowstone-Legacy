package net.glowstone.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectHelper {

    /**
     * Constructs an object of a give class
     * @param clazz the class type to construct
     * @param argTypes the types of constructor arguments
     * @param args the arguments to pass to the method
     * @return a constructed object of the class or null on failure
     */

    public static <T> T construct(Class<T> clazz, Class<?>[] argTypes, Object[] args) {

        Constructor<?>[] constructors;
        try {
            constructors = clazz.getConstructors();
        } catch (SecurityException se) {
            System.out.println("Security exception");
            return null;
        }

        for (Constructor<?> c : constructors) {

            Class<?>[] params = c.getParameterTypes();

            if (checkArgMatch(argTypes, params)) {
                try {
                    @SuppressWarnings("unchecked")
                    T newObject = (T)c.newInstance(args);
                    return newObject;
                } catch (IllegalAccessException iae) {
                    continue;
                } catch (IllegalArgumentException iae) {
                    continue;
                } catch (InstantiationException ie) {
                    continue;
                } catch (InvocationTargetException ie) {
                    continue;
                } catch (ExceptionInInitializerError eiie) {
                    continue;
                }
            }
        }
        System.out.println("No const match");
        return null;
    }

    /**
     * Helper method to check if argument arrays match
     * @param a1 the first argument array
     * @param a2 the second argument array
     * @return true if the arrays match
     */

    private static boolean checkArgMatch(Class<?>[] a1, Class<?>[] a2) {

        if (a1 == null || a2 == null) {
            return false;
        }

        if (a1.length != a2.length) {
            return false;
        }

        int size = a1.length;
        for (int i = 0; i < size; i++) {
            if (!a1[i].equals(a2[i])) {
                return false;
            }
        }

        return true;

    }

}
