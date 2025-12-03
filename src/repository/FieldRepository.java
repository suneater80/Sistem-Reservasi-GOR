package repository;

import model.Field;

// Implementasi Generic Programming dan JCF (Java Collections Framework)
public class FieldRepository extends GenericRepository<Field, String> {
    
    @Override
    protected String getId(Field entity) {
        return entity.getId();
    }
}