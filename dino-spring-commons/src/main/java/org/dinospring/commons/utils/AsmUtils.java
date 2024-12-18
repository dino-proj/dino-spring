// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.utils;

import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;
import org.springframework.cglib.core.ReflectUtils;

/**
 * asm工具类
 * @author Cody Lu
 * @date 2022-04-13 02:25:45
 */

public interface AsmUtils {

  /**
   * 定义一个泛型类的子类
   *
   * @param className 类名
   * @param superClass 父类
   * @param parameterClass 第一个泛型参数的实参类型
   * @return
   * @throws Exception
   */
  public static <T, U extends T> Class<U> defineGenericClass(String className, Class<T> superClass,
      Class<?> parameterClass) throws Exception {
    var classLoader = Thread.currentThread().getContextClassLoader();

    try {
      return TypeUtils.cast(classLoader.loadClass(className));
    } catch (ClassNotFoundException e) {
      // ignore
    }

    var param1Name = Type.getInternalName(parameterClass);
    var superClassName = Type.getInternalName(superClass);

    var sigStr = "L" + superClassName + "<+L" + param1Name + ";>;";

    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER + Opcodes.ACC_FINAL, className.replace('.', '/'),
        sigStr,
        superClassName, null);

    // Create default constructor
    MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
    mv.visitCode();
    mv.visitVarInsn(Opcodes.ALOAD, 0);
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superClassName,
        "<init>", "()V", false);
    mv.visitInsn(Opcodes.RETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
    // default constructor end

    cw.visitEnd();
    var code = cw.toByteArray();
    var newClass = ReflectUtils.defineClass(className, code, classLoader);
    return TypeUtils.cast(newClass);
  }

  /**
   * 生成类名，类名为：baseClass类名+"$"+postfix
   * @param baseClass 类名
   * @param postfix 后缀
   * @return
   */
  public static String className(Class<?> baseClass, String postfix) {
    return baseClass.getName() + "$" + postfix;
  }

  /**
   * 生成类名，类名为：baseClass类名+"$"+typeClass.getSimpleName()
   * @param baseClass   类名
   * @param typeClass   类型
   * @return
   */
  public static String className(Class<?> baseClass, Class<?> typeClass) {
    return baseClass + "$" + typeClass.getSimpleName();
  }

}
