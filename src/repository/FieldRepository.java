package repository;

import model.Field;

public class FieldRepository extends GenericRepository<Field, String> {
    
    @Override
    protected String getId(Field entity) {
        return entity.getId();
    }
}