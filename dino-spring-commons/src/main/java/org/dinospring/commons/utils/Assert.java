// Copyright 2024 dinosdev.cn.
// SPDX-License-Identifier: Apache-2.0

package org.dinospring.commons.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dinospring.commons.exception.BusinessException;
import org.dinospring.commons.response.Status;
import org.slf4j.helpers.MessageFormatter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Cody LU
 */

@UtilityClass
public class Assert {

  /**
   * Assert a boolean expression, throwing an {@code IllegalStateException}
   * if the expression evaluates to {@code false}.
   * <p>Call {@link #isTrue} if you wish to throw an {@code IllegalArgumentException}
   * on an assertion failure.
   * <pre class="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
   * @param expression a boolean expression
   * @param message the exception message pattern to use if the assertion fails. pattern is same as SLF4J
   * @param args The argument to be substituted in place of the formatting anchor
   * @throws IllegalStateException if {@code expression} is {@code false}
   */
  public static void state(boolean expression, String message, Object... args) {
    if (!expression) {
      throw new IllegalStateException(MessageFormatter.arrayFormat(message, args).getMessage());
    }
  }

  /**
   * Assert a boolean expression, throwing an {@code IllegalStateException}
   * if the expression evaluates to {@code false}.
   * <p>Call {@link #isTrue} if you wish to throw an {@code IllegalArgumentException}
   * on an assertion failure.
   * <pre class="code">
   * Assert.state(entity.getId() == null,
   *     () -&gt; "ID for entity " + entity.getName() + " must not already be initialized");
   * </pre>
   * @param expression a boolean expression
   * @param messageSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws IllegalStateException if {@code expression} is {@code false}
   */
  public static void state(boolean expression, Supplier<String> messageSupplier) {
    if (!expression) {
      throw new IllegalStateException(nullSafeGet(messageSupplier));
    }
  }

  /**
   * Assert a boolean expression, throwing an {@code BusinessException}
   * if the expression evaluates to {@code false}.
   * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
   * @param expression a boolean expression
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if {@code expression} is {@code false}
   */
  public static void isTrue(boolean expression, String message) {
    isTrue(expression, Status.fail(message));
  }

  /**
   * Assert a boolean expression, throwing an {@code BusinessException}
   * if the expression evaluates to {@code false}.
   * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
   * @param expression a boolean expression
   * @param status the exception status to use if the assertion fails
   * @throws BusinessException if {@code expression} is {@code false}
   */
  public static void isTrue(boolean expression, Status status) {
    if (!expression) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert a boolean expression, throwing an {@code BusinessException}
   * if the expression evaluates to {@code false}.
   * <pre class="code">
   * Assert.isTrue(i &gt; 0, () -&gt; "The value '" + i + "' must be greater than zero");
   * </pre>
   * @param expression a boolean expression
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if {@code expression} is {@code false}
   */
  public static void isTrue(boolean expression, @Nonnull Supplier<Status> statusSupplier) {
    if (!expression) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that an object is {@code null}.
   * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
   * @param object the object to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the object is not {@code null}
   */
  public static void isNull(@Nullable Object object, String message) {
    isNull(object, Status.fail(message));
  }

  /**
   * Assert that an object is {@code null}.
   * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
   * @param object the object to check
   * @param status the exception status to use if the assertion fails
   * @throws BusinessException if the object is not {@code null}
   */
  public static void isNull(@Nullable Object object, Status status) {
    if (!Objects.isNull(object)) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that an object is {@code null}.
   * <pre class="code">
   * Assert.isNull(value, () -&gt; "The value '" + value + "' must be null");
   * </pre>
   * @param object the object to check
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if the object is not {@code null}
   */
  public static void isNull(@Nullable Object object, @Nonnull Supplier<Status> statusSupplier) {
    if (!Objects.isNull(object)) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that an object is not {@code null}.
   * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
   * @param object the object to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the object is {@code null}
   */
  public static void notNull(@Nullable Object object, String message) {
    notNull(object, Status.fail(message));
  }

  /**
  * Assert that an object is not {@code null}.
  * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
  * @param object the object to check
  * @param status the exception status to use if the assertion fails
  * @throws BusinessException if the object is {@code null}
  */
  public static void notNull(@Nullable Object object, Status status) {
    if (Objects.isNull(object)) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that an object is not {@code null}.
   * <pre class="code">
   * Assert.notNull(entity.getId(),
   *     () -&gt; "ID for entity " + entity.getName() + " must not be null");
   * </pre>
   * @param object the object to check
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if the object is {@code null}
   */
  public static void notNull(@Nullable Object object, @Nonnull Supplier<Status> statusSupplier) {
    if (Objects.isNull(object)) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that the given String is not empty; that is,
   * it must not be {@code null} and not the empty String.
   * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
   * @param text the String to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the text is empty
   * @see StringUtils#hasLength
   */
  public static void hasLength(@Nullable String text, String message) {
    hasLength(text, Status.fail(message));
  }

  /**
   * Assert that the given String is not empty; that is,
   * it must not be {@code null} and not the empty String.
   * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
   * @param text the String to check
   * @param status the exception status to use if the assertion fails
   * @throws BusinessException if the text is empty
   * @see StringUtils#hasLength
   */
  public static void hasLength(@Nullable String text, Status status) {
    if (StringUtils.isEmpty(text)) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that the given String is not empty; that is,
   * it must not be {@code null} and not the empty String.
   * <pre class="code">
   * Assert.hasLength(account.getName(),
   *     () -&gt; "Name for account '" + account.getId() + "' must not be empty");
   * </pre>
   * @param text the String to check
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if the text is empty
   * @see StringUtils#hasLength
   */
  public static void hasLength(@Nullable String text, @Nonnull Supplier<Status> statusSupplier) {
    if (StringUtils.isEmpty(text)) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that the given String contains valid text content; that is, it must not
   * be {@code null} and must contain at least one non-whitespace character.
   * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
   * @param text the String to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the text does not contain valid text content
   * @see StringUtils#hasText
   */
  public static void hasText(@Nullable String text, String message) {
    hasText(text, Status.fail(message));
  }

  /**
   * Assert that the given String contains valid text content; that is, it must not
   * be {@code null} and must contain at least one non-whitespace character.
   * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
   * @param text the String to check
   * @param status the exception status to use if the assertion fails
   * @throws BusinessException if the text does not contain valid text content
   * @see StringUtils#hasText
   */
  public static void hasText(@Nullable String text, Status status) {
    if (StringUtils.isBlank(text)) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that the given String contains valid text content; that is, it must not
   * be {@code null} and must contain at least one non-whitespace character.
   * <pre class="code">
   * Assert.hasText(account.getName(),
   *     () -&gt; "Name for account '" + account.getId() + "' must not be empty");
   * </pre>
   * @param text the String to check
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if the text does not contain valid text content
   * @see StringUtils#hasText
   */
  public static void hasText(@Nullable String text, @Nonnull Supplier<Status> statusSupplier) {
    if (StringUtils.isBlank(text)) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that the given text does not contain the given substring.
   * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
   * @param textToSearch the text to search
   * @param substring the substring to find within the text
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the text contains the substring
   */
  public static void doesNotContain(@Nullable String textToSearch, String substring, String message) {
    doesNotContain(textToSearch, substring, Status.fail(message));
  }

  /**
   * Assert that the given text does not contain the given substring.
   * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
   * @param textToSearch the text to search
   * @param substring the substring to find within the text
   * @param status the exception status to use if the assertion fails
   * @throws BusinessException if the text contains the substring
   */
  public static void doesNotContain(@Nullable String textToSearch, String substring, Status status) {
    if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) && textToSearch.contains(substring)) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that the given text does not contain the given substring.
   * <pre class="code">
   * Assert.doesNotContain(name, forbidden, () -&gt; "Name must not contain '" + forbidden + "'");
   * </pre>
   * @param textToSearch the text to search
   * @param substring the substring to find within the text
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if the text contains the substring
   */
  public static void doesNotContain(@Nullable String textToSearch, String substring,
      @Nonnull Supplier<Status> statusSupplier) {
    if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) && textToSearch.contains(substring)) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that an array contains elements; that is, it must not be
   * {@code null} and must contain at least one element.
   * <pre class="code">Assert.notEmpty(array, "The array must contain elements");</pre>
   * @param array the array to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the object array is {@code null} or contains no elements
   */
  public static void notEmpty(@Nullable Object[] array, String message) {
    notEmpty(array, Status.fail(message));
  }

  /**
   * Assert that an array contains elements; that is, it must not be
   * {@code null} and must contain at least one element.
   * <pre class="code">Assert.notEmpty(array, "The array must contain elements");</pre>
   * @param array the array to check
   * @param status the exception status to use if the assertion fails
   * @throws BusinessException if the object array is {@code null} or contains no elements
   */
  public static void notEmpty(@Nullable Object[] array, Status status) {
    if (array == null || array.length == 0) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that an array contains elements; that is, it must not be
   * {@code null} and must contain at least one element.
   * <pre class="code">
   * Assert.notEmpty(array, () -&gt; "The " + arrayType + " array must contain elements");
   * </pre>
   * @param array the array to check
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if the object array is {@code null} or contains no elements
   */
  public static void notEmpty(@Nullable Object[] array, @Nonnull Supplier<Status> statusSupplier) {
    if (array == null || array.length == 0) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that an array contains no {@code null} elements.
   * <p>Note: Does not complain if the array is empty!
   * <pre class="code">Assert.noNullElements(array, "The array must contain non-null elements");</pre>
   * @param array the array to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the object array contains a {@code null} element
   */
  public static void noNullElements(@Nullable Object[] array, String message) {
    noNullElements(array, Status.fail(message));
  }

  /**
   * Assert that an array contains no {@code null} elements.
   * <p>Note: Does not complain if the array is empty!
   * <pre class="code">Assert.noNullElements(array, "The array must contain non-null elements");</pre>
   * @param array the array to check
   * @param status the exception status to use if the assertion fails
   * @throws BusinessException if the object array contains a {@code null} element
   */
  public static void noNullElements(@Nullable Object[] array, Status status) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          throw BusinessException.of(status);
        }
      }
    }
  }

  /**
   * Assert that an array contains no {@code null} elements.
   * <p>Note: Does not complain if the array is empty!
   * <pre class="code">
   * Assert.noNullElements(array, () -&gt; "The " + arrayType + " array must contain non-null elements");
   * </pre>
   * @param array the array to check
   * @param statusSupplier a supplier for the exception status to use if the
   * assertion fails
   * @throws BusinessException if the object array contains a {@code null} element
   */
  public static void noNullElements(@Nullable Object[] array, @Nonnull Supplier<Status> statusSupplier) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          throw BusinessException.of(nullSafeGet(statusSupplier));
        }
      }
    }
  }

  /**
   * Assert that a collection contains elements; that is, it must not be
   * {@code null} and must contain at least one element.
   * <pre class="code">Assert.notEmpty(collection, "Collection must contain elements");</pre>
   * @param collection the collection to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the collection is {@code null} or
   * contains no elements
   */
  public static void notEmpty(@Nullable Collection<?> collection, String message) {
    notEmpty(collection, Status.fail(message));
  }

  /**
   * Assert that a collection contains elements; that is, it must not be
   * {@code null} and must contain at least one element.
   * <pre class="code">Assert.notEmpty(collection, "Collection must contain elements");</pre>
   * @param collection the collection to check
   * @param status the exception message to use if the assertion fails
   * @throws BusinessException if the collection is {@code null} or
   * contains no elements
   */
  public static void notEmpty(@Nullable Collection<?> collection, Status status) {
    if (CollectionUtils.isEmpty(collection)) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that a collection contains elements; that is, it must not be
   * {@code null} and must contain at least one element.
   * <pre class="code">
   * Assert.notEmpty(collection, () -&gt; "The " + collectionType + " collection must contain elements");
   * </pre>
   * @param collection the collection to check
   * @param statusSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws BusinessException if the collection is {@code null} or
   * contains no elements
   */
  public static void notEmpty(@Nullable Collection<?> collection, @Nonnull Supplier<Status> statusSupplier) {
    if (CollectionUtils.isEmpty(collection)) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that a collection contains no {@code null} elements.
   * <p>Note: Does not complain if the collection is empty!
   * <pre class="code">Assert.noNullElements(collection, "Collection must contain non-null elements");</pre>
   * @param collection the collection to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the collection contains a {@code null} element
   */
  public static void noNullElements(@Nullable Collection<?> collection, String message) {
    noNullElements(collection, Status.fail(message));
  }

  /**
   * Assert that a collection contains no {@code null} elements.
   * <p>Note: Does not complain if the collection is empty!
   * <pre class="code">Assert.noNullElements(collection, "Collection must contain non-null elements");</pre>
   * @param collection the collection to check
   * @param status the exception message to use if the assertion fails
   * @throws BusinessException if the collection contains a {@code null} element
   */
  public static void noNullElements(@Nullable Collection<?> collection, Status status) {
    if (collection != null) {
      for (Object element : collection) {
        if (element == null) {
          throw BusinessException.of(status);
        }
      }
    }
  }

  /**
   * Assert that a collection contains no {@code null} elements.
   * <p>Note: Does not complain if the collection is empty!
   * <pre class="code">
   * Assert.noNullElements(collection, () -&gt; "Collection " + collectionName + " must contain non-null elements");
   * </pre>
   * @param collection the collection to check
   * @param statusSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws BusinessException if the collection contains a {@code null} element
   */
  public static void noNullElements(@Nullable Collection<?> collection, @Nonnull Supplier<Status> statusSupplier) {
    if (collection != null) {
      for (Object element : collection) {
        if (element == null) {
          throw BusinessException.of(nullSafeGet(statusSupplier));
        }
      }
    }
  }

  /**
   * Assert that a Map contains entries; that is, it must not be {@code null}
   * and must contain at least one entry.
   * <pre class="code">Assert.notEmpty(map, "Map must contain entries");</pre>
   * @param map the map to check
   * @param message the exception message to use if the assertion fails
   * @throws BusinessException if the map is {@code null} or contains no entries
   */
  public static void notEmpty(@Nullable Map<?, ?> map, String message) {
    notEmpty(map, Status.fail(message));
  }

  /**
   * Assert that a Map contains entries; that is, it must not be {@code null}
   * and must contain at least one entry.
   * <pre class="code">Assert.notEmpty(map, "Map must contain entries");</pre>
   * @param map the map to check
   * @param status the exception message to use if the assertion fails
   * @throws BusinessException if the map is {@code null} or contains no entries
   */
  public static void notEmpty(@Nullable Map<?, ?> map, Status status) {
    if (MapUtils.isEmpty(map)) {
      throw BusinessException.of(status);
    }
  }

  /**
   * Assert that a Map contains entries; that is, it must not be {@code null}
   * and must contain at least one entry.
   * <pre class="code">
   * Assert.notEmpty(map, () -&gt; "The " + mapType + " map must contain entries");
   * </pre>
   * @param map the map to check
   * @param statusSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws BusinessException if the map is {@code null} or contains no entries
   */
  public static void notEmpty(@Nullable Map<?, ?> map, @Nonnull Supplier<Status> statusSupplier) {
    if (MapUtils.isEmpty(map)) {
      throw BusinessException.of(nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that the provided object is an instance of the provided class.
   * <pre class="code">Assert.instanceOf(Foo.class, foo, "Foo expected");</pre>
   * @param type the type to check against
   * @param obj the object to check
   * @param message a message which will be prepended to provide further context.
   * If it is empty or ends in ":" or ";" or "," or ".", a full exception message
   * will be appended. If it ends in a space, the name of the offending object's
   * type will be appended. In any other case, a ":" with a space and the name
   * of the offending object's type will be appended.
   * @throws BusinessException if the object is not an instance of type
   */
  public static void isInstanceOf(@Nonnull Class<?> type, @Nullable Object obj, String message) {
    isInstanceOf(type, obj, Status.fail(message));
  }

  /**
   * Assert that the provided object is an instance of the provided class.
   * <pre class="code">Assert.instanceOf(Foo.class, foo, "Foo expected");</pre>
   * @param type the type to check against
   * @param obj the object to check
   * @param status a message which will be prepended to provide further context.
   * If it is empty or ends in ":" or ";" or "," or ".", a full exception message
   * will be appended. If it ends in a space, the name of the offending object's
   * type will be appended. In any other case, a ":" with a space and the name
   * of the offending object's type will be appended.
   * @throws BusinessException if the object is not an instance of type
   */
  public static void isInstanceOf(@Nonnull Class<?> type, @Nullable Object obj, Status status) {
    if (!type.isInstance(obj)) {
      instanceCheckFailed(type, obj, status);
    }
  }

  /**
   * Assert that the provided object is an instance of the provided class.
   * <pre class="code">
   * Assert.instanceOf(Foo.class, foo, () -&gt; "Processing " + Foo.class.getSimpleName() + ":");
   * </pre>
   * @param type the type to check against
   * @param obj the object to check
   * @param statusSupplier a supplier for the exception message to use if the
   * assertion fails. See {@link #isInstanceOf(Class, Object, String)} for details.
   * @throws BusinessException if the object is not an instance of type
   */
  public static void isInstanceOf(@Nonnull Class<?> type, @Nullable Object obj,
      @Nonnull Supplier<Status> statusSupplier) {
    if (!type.isInstance(obj)) {
      instanceCheckFailed(type, obj, nullSafeGet(statusSupplier));
    }
  }

  /**
   * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
   * <pre class="code">Assert.isAssignable(Number.class, myClass, "Number expected");</pre>
   * @param superType the super type to check against
   * @param subType the sub type to check
   * @param message a message which will be prepended to provide further context.
   * If it is empty or ends in ":" or ";" or "," or ".", a full exception message
   * will be appended. If it ends in a space, the name of the offending sub type
   * will be appended. In any other case, a ":" with a space and the name of the
   * offending sub type will be appended.
   * @throws BusinessException if the classes are not assignable
   */
  public static void isAssignable(@Nonnull Class<?> superType, @Nullable Class<?> subType, String message) {
    isAssignable(superType, subType, Status.fail(message));
  }

  /**
   * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
   * <pre class="code">Assert.isAssignable(Number.class, myClass, "Number expected");</pre>
   * @param superType the super type to check against
   * @param subType the sub type to check
   * @param status a message which will be prepended to provide further context.
   * If it is empty or ends in ":" or ";" or "," or ".", a full exception message
   * will be appended. If it ends in a space, the name of the offending sub type
   * will be appended. In any other case, a ":" with a space and the name of the
   * offending sub type will be appended.
   * @throws BusinessException if the classes are not assignable
   */
  public static void isAssignable(@Nonnull Class<?> superType, @Nullable Class<?> subType, Status status) {
    if (subType == null || !superType.isAssignableFrom(subType)) {
      assignableCheckFailed(superType, subType, status);
    }
  }

  /**
   * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
   * <pre class="code">
   * Assert.isAssignable(Number.class, myClass, () -&gt; "Processing " + myAttributeName + ":");
   * </pre>
   * @param superType the super type to check against
   * @param subType the sub type to check
   * @param statusSupplier a supplier for the exception message to use if the
   * assertion fails. See {@link #isAssignable(Class, Class, String)} for details.
   * @throws BusinessException if the classes are not assignable
   */
  public static void isAssignable(@Nonnull Class<?> superType, @Nullable Class<?> subType,
      @Nonnull Supplier<Status> statusSupplier) {
    if (subType == null || !superType.isAssignableFrom(subType)) {
      assignableCheckFailed(superType, subType, nullSafeGet(statusSupplier));
    }
  }

  private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nonnull Status status) {
    String msg = status.getMsg();
    String className = (obj != null ? obj.getClass().getName() : "null");
    String result = "";
    boolean defaultMessage = true;
    if (StringUtils.isNotEmpty(msg)) {
      if (endsWithSeparator(msg)) {
        result = msg + " ";
      } else {
        result = messageWithTypeName(msg, className);
        defaultMessage = false;
      }
    }
    if (defaultMessage) {
      result = result + ("Object of class [" + className + "] must be an instance of " + type);
    }
    throw BusinessException.of(Status.fail(status.getCode(), result));
  }

  private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nonnull Status status) {
    String msg = status.getMsg();
    String result = "";
    boolean defaultMessage = true;
    if (StringUtils.isNotEmpty(msg)) {
      if (endsWithSeparator(msg)) {
        result = msg + " ";
      } else {
        result = messageWithTypeName(msg, subType);
        defaultMessage = false;
      }
    }
    if (defaultMessage) {
      result = result + (subType + " is not assignable to " + superType);
    }
    throw BusinessException.of(Status.fail(status.getCode(), result));
  }

  private static boolean endsWithSeparator(String msg) {
    return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
  }

  private static String messageWithTypeName(String msg, @Nullable Object typeName) {
    return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
  }

  @Nullable
  private static <T> T nullSafeGet(@Nullable Supplier<T> messageSupplier) {
    return (messageSupplier != null ? messageSupplier.get() : null);
  }

}
