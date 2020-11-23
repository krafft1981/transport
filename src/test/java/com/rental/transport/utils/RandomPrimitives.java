package com.rental.transport.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.assertj.core.util.Lists;

/**
 * Класс, который генерирует простые рандомные значения для тестов.
 */
public final class RandomPrimitives {

    /**
     * Генератор случайных чисел. */
    private static final ThreadLocalRandom RANDOM =
        ThreadLocalRandom.current();

    /**
     * Нет необходимости в конструкторах.
     */
    private RandomPrimitives() { }

    /**
     * @param objects объекты для случайного выбора.
     * @param <ObjectsType> тип объектов.
     * @return случайный объект из заданных объектов.
     * @throws IllegalArgumentException если objects пуст.
     */
    public static <ObjectsType> ObjectsType getRandomOf(final ObjectsType... objects)
        throws IllegalArgumentException
    {
        RandomPrimitives.validateVarArgNotEmpty(objects);
        final Integer randomIndex = RandomPrimitives.getRandomAbsIntTo(objects.length);
        return objects[randomIndex];
    }

    /**
     * @param suppliers лямбды, предоставляющие значения.
     * @param <ObjectsType> тип объектов.
     * @return значение, полученное из случайно выбранной лямбды из числа заданных.
     * @throws IllegalArgumentException если suppliers пуст.
     */
    public static <ObjectsType> ObjectsType getRandomOf(final Supplier<ObjectsType>... suppliers)
        throws IllegalArgumentException
    {
        RandomPrimitives.validateVarArgNotEmpty(suppliers);
        final Integer randomIndex = RandomPrimitives.getRandomAbsIntTo(suppliers.length);
        return suppliers[randomIndex].get();
    }

    /**
     * @param collection объекты для случайного выбора.
     * @param <ObjectsType> тип объектов.
     * @return случайный объект из заданной коллекции.
     * @throws IllegalArgumentException если collection пуст.
     */
    public static <ObjectsType> ObjectsType getRandomOf(@NonNull final Collection<ObjectsType> collection)
        throws IllegalArgumentException
    {
        RandomPrimitives.validateVarArgNotEmpty(collection);

        int randomIndex = RandomPrimitives.getRandomAbsIntTo(collection.size());
        for (ObjectsType t: collection) {
            if (--randomIndex < 0) {
                return t;
            }
        }

        throw new RuntimeException("randomIndex > objects.size()");
    }

    /**
     * @return случайное неотрицательное целое число, <code>0 <= x < {@link Integer#MAX_VALUE}</code>.
     */
    public static Integer getRandomInt() {
        return Math.abs(RANDOM.nextInt());
    }

    /**
     * @return null или случайное неотрицательное целое число, <code>0 <= x < {@link Integer#MAX_VALUE}</code>.
     */
    public static Integer getRandomIntOrNull() {
        return RandomPrimitives.getFromLambdaOrNull(RandomPrimitives::getRandomInt);
    }

    /**
     * @param max максимальное возвращаемое значение.
     * @return случайное положительное целое число, <code>0 <= x < max</code>.
     */
    public static Integer getRandomAbsIntTo(final int max) {
        return Math.abs(RANDOM.nextInt(max));
    }

    /**
     * @param min минимальное возвращаемое значение.
     * @param max верхняя граница возвращаемого значения.
     * @return случайное положительное целое число, <code>min <= x < max</code>.
     */
    public static Integer getRandomAbsInt(final int min, final int max) {
        return Math.abs(RANDOM.nextInt(min, max));
    }


    /**
     * @param max максимальное возвращаемое значение.
     * @return случайное положительное целое число, <code>0 <= x < max</code>.
     */
    public static Long getRandomAbsLongTo(final long max) {
        return Math.abs(RANDOM.nextLong(max));
    }

    /**
     * @param min минимальное возвращаемое значение.
     * @param max верхняя граница возвращаемого значения.
     * @return случайное положительное целое число, <code>min <= x < max</code>.
     */
    public static Long getRandomAbsLong(final long min, final long max) {
        return Math.abs(RANDOM.nextLong(min, max));
    }

    /**
     * @param min минимальное возвращаемое значение.
     * @return случайное положительное целое число, <code>min <= x < {@link Integer#MAX_VALUE}</code>.
     */
    public static Integer getRandomAbsIntFrom(final int min) {
        return getRandomAbsInt(min, Integer.MAX_VALUE);
    }

    /**
     * @param existingInts коллекция с уже существующими числами.
     * @param max верхняя граница возвращаемого значения.
     * @return случайный положительное целое число <code>0 <= x < max</code>, отсутствующее в заданной коллекции.
     * Возвращаемое значение также автоматически добавляется в existingInts.
     */
    public static Integer getNewRandomInt(final Collection<Integer> existingInts, final Integer max) {
        if (max == existingInts.size()) {
            throw new RuntimeException("Использованы все возможные значения в заданном диапазоне.");
        }

        Integer newInt;
        do {
            newInt = RandomPrimitives.getRandomAbsIntTo(max);
        } while (existingInts.contains(newInt));
        existingInts.add(newInt);

        return newInt;
    }

    /**
     * @param existingInts коллекция с уже существующими числами.
     * @return случайный положительное целое число <code>0 <= x < {@link Integer#MAX_VALUE}</code>,
     * отсутствующее в заданной коллекции. Возвращаемое значение также автоматически добавляется в existingInts.
     */
    public static Integer getNewRandomInt(final Collection<Integer> existingInts) {
        return RandomPrimitives.getNewRandomInt(existingInts, Integer.MAX_VALUE);
    }

    /**
     * @return случайный положительный Short.
     */
    public static Short getRandomShort() {
        return (short) Math.abs(RANDOM.nextInt(Short.MAX_VALUE + 1));
    }

    /**
     * @param max максимальное возвращаемое значение.
     * @return случайное число, <code>0 <= x < max</code>.
     */
    public static Short getRandomShort(final Short max) {
        return (short) Math.abs(RANDOM.nextInt(max));
    }

    /**
     * @return случайный положительный Float.
     */
    public static Float getRandomFloat() {
        return Math.abs(RANDOM.nextFloat());
    }

    /**
     * @return случайный положительный Double.
     */
    public static Double getRandomDouble() {
        return Math.abs(RANDOM.nextDouble());
    }

    /**
     * @param max верхняя граница возвращаемого значения.
     * @param accuracy точность возвращаемого значения.
     * @return случайный положительный Double, <code>0 <= x < max</code>.
     */
    public static Double getRandomDouble(final double max,
                                         final int accuracy)
    {
        final Double randomValue = Math.abs(RANDOM.nextDouble(max));
        return new BigDecimal(randomValue).setScale(accuracy, RoundingMode.UP).doubleValue();
    }

    /**
     * @return случайный положительный Long.
     */
    public static Long getRandomLong() {
        return Math.abs(RANDOM.nextLong());
    }

    /**
     * @return случайный положительный Long.
     */
    public static Long getRandomLongOrNull() {
        return RandomPrimitives.getFromLambdaOrNull(RandomPrimitives::getRandomLong);
    }

    /**
     * @param max максимальное возвращаемое значение.
     * @return случайное число, <code>0 <= x < max</code>.
     */
    public static Long getRandomLongTo(final Long max) {
        return Math.abs(RANDOM.nextLong(max));
    }


    /**
     * @param existingInts коллекция с уже существующими числами.
     * @return случайный положительное целое число <code>0 <= x < {@link Integer#MAX_VALUE}</code>,
     * отсутствующее в заданной коллекции. Возвращаемое значение также автоматически добавляется в existingInts.
     */
    public static Long getNewRandomLong(final Collection<Long> existingInts) {
        return RandomPrimitives.getNewRandomLong(existingInts, Long.MAX_VALUE);
    }

    /**
     * @param existingLongs коллекция с уже существующими числами.
     * @param max верхняя граница возвращаемого значения.
     * @return случайный положительное целое число <code>0 <= x < max</code>, отсутствующее в заданной коллекции.
     * Возвращаемое значение также автоматически добавляется в existingLongs.
     */
    public static Long getNewRandomLong(final Collection<Long> existingLongs, final Long max) {
        if (max == existingLongs.size()) {
            throw new RuntimeException("Использованы все возможные значения в заданном диапазоне.");
        }

        Long newLong;
        do {
            newLong = RandomPrimitives.getRandomAbsLongTo(max);
        } while (existingLongs.contains(newLong));
        existingLongs.add(newLong);

        return newLong;
    }

    /**
     * @param min минимальное возвращаемое значение.
     * @return случайное число, <code>min <= x < {@link Long#MAX_VALUE}</code>.
     */
    public static long getRandomLongFrom(final Long min) {
        return Math.abs(RANDOM.nextLong(min, Long.MAX_VALUE));
    }

    /**
     * @return случайная строка.
     */
    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param length длина желаемой строки.
     * @return случайная строка.
     */
    public static String getRandomString(final int length) {
        RandomStringGenerator randomStringGenerator =
            new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
        final String randomString = randomStringGenerator.generate(length);
        return randomString;
    }

    /**
     * @param maxLength максимальная длина желаемой строки.
     * @return случайная строка со случайной длиной x: 1 <= x <= maxLength.
     */
    public static String getRandomStringOfRandomLength(final int maxLength) {
        final int minLength = 1;
        final Integer length = RandomPrimitives.getRandomAbsInt(minLength, maxLength + 1);
        return RandomPrimitives.getRandomString(length);
    }

    /**
     * @param segmentsAmount кол-во строковых сегментов.
     * @param eachSegmentMaxLength максимальная длина строкового сегмента, должна быть > 1
     *                             (минимальная длина сегмента - 1).
     * @param separator разделитель строковых сегментов.
     * @return строка из заданного кол-ва строк (сегментов) с заданной максимальной длиной,
     * которые объединены через {@link StringUtils#join(Iterable, char)}
     * с использованием заданного разделителя.
     */
    public static String getRandomStringSegments(final int segmentsAmount,
                                                 final int eachSegmentMaxLength,
                                                 final String separator)
    {
        final List<String> segments = Stream.generate(() ->
            getRandomStringOfRandomLength(eachSegmentMaxLength)
        )
            .limit(segmentsAmount)
            .collect(Collectors.toList());
        return StringUtils.join(segments, separator);
    }

    /**
     * @return случайный управляющий символ, который трактуется в качестве пустой строки
     * или случайный пробельный символ.
     */
    public static String getRandomBlank() {
        return RandomPrimitives.getRandomOf(
            RandomPrimitives.getRandomWhitespace(),
            "\b",
            ""
        );
    }

    /**
     * @return строка из случайных пробельных символов и управляющих символов, трактующихся в качестве пустых строк.
     */
    public static String getRandomBlanks() {
        throw new RuntimeException("not impl"); // fixme
        /*final List<String> whitespaces = CollectionsCreator.getList(RandomPrimitives::getRandomBlank);
        return StringUtils.join(whitespaces, "");*/
    }

    /**
     * @return один случайный символ пробела, такой, для которого метод {@link Character#isWhitespace} вернет true.
     */
    public static String getRandomWhitespace() {
        return RandomPrimitives.getRandomOf(
            new String(new byte[] {Character.SPACE_SEPARATOR}),
            new String(new byte[] {Character.LINE_SEPARATOR}),
            new String(new byte[] {Character.PARAGRAPH_SEPARATOR}),
            "\t",
            "\n",
            "\u000B",
            "\f",
            "\r",
            "\u001C",
            "\u001D",
            "\u001E",
            "\u001F"
        );
    }

    /**
     * @param amount количество пробельных символов.
     * @return случайные пробельные символы, такие, для которорых метод {@link Character#isWhitespace} вернет true.
     */
    public static String getRandomWhitespaces(final int amount) {
        throw new RuntimeException("not impl"); // fixme
        /*final List<String> whitespaces = CollectionsCreator.getList(amount, RandomPrimitives::getRandomWhitespace);
        return StringUtils.join(whitespaces, "");*/
    }

    /**
     * @return случайные пробельные символы, такие, для которорых метод {@link Character#isWhitespace} вернет true.
     */
    public static String getRandomWhitespaces() {
        throw new RuntimeException("not impl"); // fixme
        /*final List<String> whitespaces = CollectionsCreator.getList(RandomPrimitives::getRandomWhitespace);
        return StringUtils.join(whitespaces, "");*/
    }


    /**
     * @return случайный Boolean.
     */
    public static Boolean getRandomBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * @return случайный Boolean или null.
     */
    public static Boolean getRandomBooleanOrNull() {
        return RandomPrimitives.getFromLambdaOrNull(
            RandomPrimitives::getRandomBoolean
        );
    }

    /**
     * @param enumClass класс enum-а.
     * @param <T> тип enum-а.
     * @return случайная enum-константа заданного типа.
     */
    public static <T extends Enum<T>> T getRandomEnum(@NonNull final Class<T> enumClass) {
        int i = RandomPrimitives.getRandomAbsIntTo(enumClass.getEnumConstants().length);
        return enumClass.getEnumConstants()[i];
    }

    /**
     * @param enums множество enum-констант.
     * @param <T> тип enum-а.
     * @return случайная enum-константа из заданного набора.
     * @throws IllegalArgumentException если enums пуст.
     */
    public static <T extends Enum<T>> T getRandomEnum(@NonNull final T... enums)
        throws IllegalArgumentException
    {
        RandomPrimitives.validateVarArgNotEmpty(enums);
        final T randomEnum = RandomPrimitives.getRandomEnum(new HashSet<>(Arrays.asList(enums)));
        return randomEnum;
    }

    /**
     * @param enums коллекция enum-констант.
     * @param <T> тип enum-а.
     * @return случайная enum-константа из заданного набора.
     */
    public static <T extends Enum<T>> T getRandomEnum(@NonNull final Collection<T> enums) {
        RandomPrimitives.validateVarArgNotEmpty(enums);
        int i = RandomPrimitives.getRandomAbsIntTo(enums.size());
        return enums.stream().skip(i).findFirst().get();
    }

    /**
     * @param enums enum-константы.
     * @param <T> тип enum-а.
     * @return случайная enum-константа из заданного набора.
     * @throws IllegalArgumentException если enums пуст.
     */
    public static <T extends Enum<T>> T getRandomEnum(@NonNull final Set<T> enums)
        throws IllegalArgumentException
    {
        int i = RandomPrimitives.getRandomAbsIntTo(enums.size());
        return new ArrayList<>(enums).get(i);
    }

    /**
     * @param enumValue значение enum константы.
     * @param clazz класс enum-а.
     * @param <EnumType> тип enum констант.
     * @return значение enumValue или случайное значение из класса enum-а, если enumValue не задан.
     */
    public static <EnumType extends Enum<EnumType>> EnumType getEnumOrElseRandomEnum(
        @NonNull final Optional<EnumType> enumValue,
        @NonNull final Class<EnumType> clazz)
    {
        return enumValue.orElse(RandomPrimitives.getRandomEnum(clazz));
    }

    /**
     * @param excludedEnumsArray множество enum констант, которые метод возвращать не будет.
     * @param <T> тип enum-а.
     * @return случайная enum-константа заданного типа, кроме заданных.
     * @throws IllegalArgumentException если excludedEnumsArray пуст или если кол-во элментов в нем
     * совпадает с количеством enum-констант.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getRandomEnumExceptFor(@NonNull final T... excludedEnumsArray)
        throws IllegalArgumentException
    {
        RandomPrimitives.validateVarArgNotEmpty(excludedEnumsArray);
        final T randomEnum = RandomPrimitives.getRandomEnumExceptFor(new HashSet<>(Arrays.asList(excludedEnumsArray)));
        return randomEnum;
    }

    /**
     * @param excludedEnumsCollection enum константы, которые метод возвращать не будет.
     * @param <T> тип enum-а.
     * @return случайная enum-константа заданного типа, кроме заданных.
     * @throws IllegalArgumentException если excludedEnumsArray пуст или если кол-во элментов в нем
     * совпадает с количеством enum-констант.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getRandomEnumExceptFor(@NonNull final Set<T> excludedEnumsCollection)
        throws IllegalArgumentException
    {
        if (excludedEnumsCollection.isEmpty()) {
            throw new IllegalArgumentException("enum collection is empty");
        }
        final T enumObject = excludedEnumsCollection.stream().findFirst().get();
        final Class<T> enumClass = (Class<T>) enumObject.getClass();

        return RandomPrimitives.getRandomEnumExceptFor(enumClass, excludedEnumsCollection);
    }

    /**
     * @param enumClass класс enum-а.
     * @param excludedEnumsCollection enum константы, которые метод возвращать не будет.
     * @param <T> тип enum-а.
     * @return случайная enum-константа заданного типа, кроме заданных.
     * Если коллекция excludedEnumsCollection пуста, метод ведет себя аналогично
     * {@link RandomPrimitives#getRandomEnum(Class)}.
     * @throws IllegalArgumentException если кол-во элментов в нем
     * совпадает с количеством enum-констант.
     */
    public static <T extends Enum<T>> T getRandomEnumExceptFor(
        @NonNull final Class<T> enumClass,
        @NonNull final Set<T> excludedEnumsCollection)
        throws IllegalArgumentException
    {
        final T[] allEnums = enumClass.getEnumConstants();
        if (allEnums.length == excludedEnumsCollection.size()) {
            throw new IllegalArgumentException("You exclude all enums");
        }

        final ArrayList<T> includedEnums = Lists.newArrayList(allEnums);
        includedEnums.removeAll(excludedEnumsCollection);

        return RandomPrimitives.getRandomOf(includedEnums);
    }

    /**
     * Проверят, что varargs-параметры не пусты.
     * @param params массив каких либо объектов.
     * @throws IllegalArgumentException при провале валидации.
     */
    private static void validateVarArgNotEmpty(final Object[] params) throws IllegalArgumentException {
        if (params.length == 0) {
            RandomPrimitives.throwNotEmptyException();
        }
    }

    /**
     * Проверят, что коллекция не пуста.
     * @param params коллекция каких либо объектов.
     * @throws IllegalArgumentException при провале валидации.
     */
    private static void validateVarArgNotEmpty(final Collection params) throws IllegalArgumentException {
        if (params.isEmpty()) {
            RandomPrimitives.throwNotEmptyException();
        }
    }

    /**
     * Бросает исключение о передаче пустого списка параметров.
     */
    private static void throwNotEmptyException() {
        throw new IllegalArgumentException("you must provide not empty list of parameters");
    }

    /**
     * @param supplier лямбда, предоставляющая значение.
     * @param <ObjectType> тип возращаемого значения.
     * @return значение из лямбды или null (случайный выбор из этих двух вариантов).
     */
    public static <ObjectType> ObjectType getFromLambdaOrNull(@NonNull final Supplier<ObjectType> supplier) {
        return RandomPrimitives.getRandomOf(
            supplier,
            () -> null
        );
    }

    /**
     * @param leftSupplier лямбда, предоставляющая значение для левой компоненты пары.
     * @param rightSupplier лямбда, предоставляющая значение для правой компоненты пары.
     * @param <LeftObjectType> тип значения левой компоненты пары.
     * @param <RightObjectType> тип значения правой компоненты пары.
     * @return формирует пару из двух null значений, либо из значений, полученных от заданных лямбд.
     * Гарантируется, что если для одной из компонент пары значение получено от соответствующей лямбды,
     * то и оставшееся значение будет получено из оставшейся лямбды.
     */
    public static <LeftObjectType, RightObjectType>
    Pair<LeftObjectType, RightObjectType> getPairFromLambdasOrPairOfNulls(
        @NonNull final Supplier<LeftObjectType> leftSupplier,
        @NonNull final Supplier<RightObjectType> rightSupplier)
    {
        final LeftObjectType left = RandomPrimitives.getRandomOf(
            leftSupplier,
            () -> null
        );
        final RightObjectType right = (null != left) ? rightSupplier.get() : null;
        return Pair.of(left, right);
    }


}
