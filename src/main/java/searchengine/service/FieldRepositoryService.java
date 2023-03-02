package searchengine.service;

import searchengine.models.Field;

import java.util.List;

public interface FieldRepositoryService {
    Field getFieldByName(String fieldName);
    void save(Field field);
    List<Field> getAllField();


}
