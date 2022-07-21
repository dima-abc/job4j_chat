package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.4. Spring
 * 3.4.8. Rest
 * 2. Создания чата на Rest API. [#9143]
 * IService интерфейс бизнес логики
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.07.2022
 */
public interface IService<T, D> {
    T save(T type);

    Optional<T> findById(int id);

    void delete(T type);

    Iterable<T> findAll();

    /**
     * Метод преобразует DTO в domain
     *
     * @param dto DTO model
     * @return DOMAIN model
     */
    T dtoToDomain(D dto);

    /**
     * Метод преобразует DOMAIN model в DTO
     *
     * @param domain Model
     * @return DTO model
     */
    D domainToDTO(T domain);

    /**
     * Обновление модели через reflect Method
     *
     * @param currentDTO DTO
     * @param bodyDTO    RequestModel
     * @return modelTDO
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException exception
     */
    default D pathUpdate(D currentDTO, D bodyDTO) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = currentDTO.getClass().getDeclaredMethods();
        var namePerMethod = genNamePerMethod(methods);
        for (String name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(
                        name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Impossible invoke set method from object : "
                                    + currentDTO
                                    + ", Check set and get pairs.");
                }
                var newValue = getMethod.invoke(bodyDTO);
                if (newValue != null) {
                    setMethod.invoke(currentDTO, newValue);
                }
            }
        }
        return currentDTO;
    }

    /**
     * Метод создает карту MethodName, Method.
     *
     * @param methods Method[]
     * @return Map.
     */
    private Map<String, Method> genNamePerMethod(Method[] methods) {
        var result = new HashMap<String, Method>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                result.put(name, method);
            }
        }
        return result;
    }
}
