/**
 *
 * Copyright 2010, Lawrence McAlpin.
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package play.modules.scaffold.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Classes {
    public static List<Field> publicFields(Class<?> clazz) {
        final List<Field> output = new ArrayList<Field>();
        foreachSuperclass(clazz, false, new Executable<Class<?>>() {
            public void execute(Class<?> superclass) {
                Field[] fields = superclass.getDeclaredFields();
                for (Field field : fields) {
                    // include only public fields
                    if (Modifier.isPublic(field.getModifiers())) {
                        output.add(field);
                    }
                }
            }
        });
        return output;
    }

    public static List<String> superclasses(Class<?> clazz) {
        final List<String> output = new ArrayList<String>();
        foreachSuperclass(clazz, true, new Executable<Class<?>>() {
            public void execute(Class<?> superclass) {
                output.add(superclass.getName());
            }
        });
        return output;
    }

    public static List<String> annotations(Class<?> clazz) {
        List<String> output = new ArrayList<String>();
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation ann : annotations) {
            output.add(ann.annotationType().getName());
        }
        return output;
    }

    private static void foreachSuperclass(Class<?> clazz, boolean skipCurrent, Executable<Class<?>> block) {
        Class<?> superclass = clazz;
        if (skipCurrent) {
            superclass = superclass.getSuperclass();
        }
        if (superclass == null)
            return;
        do {
            if (superclass != null) {
                block.execute(superclass);
            }
            superclass = superclass.getSuperclass();
        } while (superclass != null);
    }

    public static String getPackageName(Class<?> clazz) {
        String fullName = clazz.getName();
        String packageName = "";
        int subpackageIdx = fullName.lastIndexOf('.');
        if (subpackageIdx >= 0) {
            packageName = fullName.substring(0, subpackageIdx);
        }
        return packageName;
    }

    public static boolean isNumeric(Class<?> type) {
        if (Number.class.isAssignableFrom(type))
            return true;
        return false;
    }
}
