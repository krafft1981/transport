package com.rental.transport;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;

/**
 * Базовый класс JUnit тестов.
 */
@ContextConfiguration(
        classes = TestApplication.class,
        loader = AnnotationConfigWebContextLoader.class
)
@SpringBootTest
public abstract class BaseTest {

    /**
     * Инициализация mock-полей тестовых классов, аннотированных через @Mock.
     */
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Проверяет, что все заглушки, получаемые методом {@link BaseTest#getAllMocks()} были проверены в ходе теста
     * на вызов в тестируемом методе с помощью {@link Mockito#verify(Object)} и ему подобных методов.
     * Если данный метод вызывать в тестах после всех проверок (или переопределить его с аннотаций {@link After}),
     * это привидет к тому, что тесты будут падать до тех пор, пока для всех зависимстей тестируемого класса
     * не будет полностью проверено их поведение в тестируемом сценарии.
     * Обращайте внимание на корректность набора заглушек, построенного методом {@link BaseTest#getAllMocks()},
     * данный метод можно переопределить для исправления списка проверяемых заглушек в конкретных случаях,
     * если требуется.
     */
    protected void verifyNoMoreInteractionsForAllMocks() {
        final List<Object> allMocks = this.getAllMocks();
        this.verifyNoMoreInteractionsForMocks(allMocks);
    }

    /**
     * Провереят, что с заданными заглушками больше не было никаких взаимодействий кроме тех,
     * которые были проверены с помощью {@link Mockito#verify(Object)} до вызова данного метода.
     * @param mocks заглушки.
     */
    protected void verifyNoMoreInteractionsForMocks(@NonNull final List<Object> mocks) {
        Mockito.verifyNoMoreInteractions(
            mocks.toArray()
        );
    }

    /**
     * @return список всех заглушек теста. Если не переопределен, считает заглушками все поля теста,
     * которые были переданы в самый длинный (по списку параметров) конструктор тестируемого класса.
     * Можно переопределить для добавления заглушек, которые подставляются в тестируемый класс другими
     * путями. Для построения корректного списка, у теста должна быть проставлена аннотация {@link TestForClass}.
     */
    protected List<Object> getAllMocks() {
        final TestForClass annotation = this.getClass().getAnnotation(TestForClass.class);
        if (null == annotation) {
            return new ArrayList<>();
        }

        final Optional<Constructor<?>> constructorOpt = this.getTestedClassConstructorWithMaxParamsAmount(annotation);
        final List<Object> allMocks = this.getMocksThatPassToConstructor(constructorOpt);
        return allMocks;
    }

    /**
     * @param annotation объект аннотации {@link TestForClass} из тестового класса.
     * @return конструктор тестируемого класса с самым длинным списком аргументов.
     */
    private Optional<Constructor<?>> getTestedClassConstructorWithMaxParamsAmount(
        @NonNull final TestForClass annotation)
    {
        final Optional<Constructor<?>> constructor = Arrays.stream(annotation.value().getConstructors())
            .max(Comparator.comparingInt(Constructor::getParameterCount));
        return constructor;
    }

    /**
     * @param constructorOpt конструктор тестируемого класса.
     * @return список полей тест-класса, которые были переданы в качестве зависимостей в тестируемый класс
     * через заданный конструктор.
     */
    private List<Object> getMocksThatPassToConstructor(@NonNull final Optional<Constructor<?>> constructorOpt) {
        if (!constructorOpt.isPresent()) {
            return new ArrayList<>();
        }

        final Constructor<?> constructor = constructorOpt.get();
        final List<Field> fieldsInTest = Arrays.asList(this.getClass().getDeclaredFields());

        final List mocks = fieldsInTest.stream()
            .filter(field -> this.isFieldPassToConstructor(constructor, field))
            .map(this::getObjectFromField)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return mocks;
    }

    /**
     * @param field рефлексивное представление поля класса.
     * @return объект заданного поля.
     */
    private Optional<Object> getObjectFromField(@NonNull final Field field) {
        field.setAccessible(true);
        try {
            return Optional.ofNullable(
                field.get(this)
            );
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * @param constructor конструктор тестируемого класса.
     * @param field поле тестового класса.
     * @return передается ли заданное поле тестового класса в конструктор тестируемого класса.
     */
    private boolean isFieldPassToConstructor(@NonNull final Constructor<?> constructor,
                                             @NonNull final Field field)
    {
        final List<Parameter> testedClassConstructorParameters = Arrays.asList(constructor.getParameters());
        final boolean isPass = testedClassConstructorParameters.stream()
            .anyMatch(
                parameter -> field.getType().equals(parameter.getType())
            );
        return isPass;
    }
}
