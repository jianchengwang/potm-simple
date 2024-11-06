package org.example.potm.framework.pojo;

import org.example.potm.framework.exception.FrameworkErrorCode;
import org.example.potm.framework.exception.ServerException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class PojoConverter {
    /**
     * 转换对象
     *
     * @param from 原始对象
     * @param toClass 新对象Class
     * @param <T> 对象类型
     * @return 新对象
     */
    public static <T> T convert(Object from, Class<T> toClass) {
        return convert(from, toClass, false);
    }

    /**
     * 转换对象集合
     *
     * @param collect 原始对象集合
     * @param toClass 新对象Class
     * @param <T> 对象类型
     * @return 新对象集合
     */
    public static <T> List<T> conventList(Collection<?> collect, Class<T> toClass) {
        return conventList(collect, toClass, false);
    }

    /**
     * 转换对象集合
     *
     * @param collect 原始对象集合
     * @param toClass 新对象Class
     * @param <T> 对象类型
     * @param sameNameDiffTypeConvert 字段名一样，类型不一样是否也开启转换
     * @return 新对象集合
     */
    public static <T> List<T> conventList(Collection<?> collect, Class<T> toClass, boolean sameNameDiffTypeConvert) {
        if (collect == null || collect.isEmpty()) {
            return Collections.emptyList();
        }

        return collect.stream()
                .map(o -> convert(o, toClass, sameNameDiffTypeConvert))
                .collect(Collectors.toList());
    }

    /**
     * 转换对象
     *
     * @param from 原始对象
     * @param toClass 新对象Class
     * @param <T> 对象类型
     * @param sameNameDiffTypeConvert 字段名一样，类型不一样是否也开启转换
     * @return 新对象
     */
    public static <T> T convert(Object from, Class<T> toClass, boolean sameNameDiffTypeConvert) {
        if (from == null) {
            return null;
        }
        T to = BeanUtils.instantiateClass(toClass);
        try {
            // 设置类型相同属性
            BeanUtils.copyProperties(from, to);

            if(sameNameDiffTypeConvert) {
                List<Field> fromFields = Arrays.stream(from.getClass().getDeclaredFields()).collect(Collectors.toList());
                List<Field> toFields = Arrays.stream(to.getClass().getDeclaredFields()).collect(Collectors.toList());
                for (Field toField : toFields) {
                    toField.setAccessible(true);

                    // 如果不为null则说明值已经设置，无需重新设置
                    if (toField.get(to) == null) {
                        Optional<Field> fromFieldOptional = fromFields.stream().filter(ff -> ff.getName().equals(toField.getName())).findFirst();
                        if (fromFieldOptional.isPresent()) {
                            final Field fromField = fromFieldOptional.get();
                            fromField.setAccessible(true);

                            // 类型不一样，名称一样的属性再转换一次
                            if (fromField.getType() != toField.getType()) {
                                toField.set(to, convert(fromField.get(from), toField.getType(), true));
                            } else {
                                toField.set(to, fromField.get(from));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServerException(FrameworkErrorCode.POJO_CONVERT, e, from, to);
        }
        return to;
    }
}
