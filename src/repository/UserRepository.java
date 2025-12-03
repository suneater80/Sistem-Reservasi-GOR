package repository;

import model.User;

// Implementasi Generic Programming dan JCF (Java Collections Framework)
public class UserRepository extends GenericRepository<User, String> {
    
    @Override
    protected String getId(User entity) {
        return entity.getId();
    }
}