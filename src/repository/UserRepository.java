package repository;

import model.User;

public class UserRepository extends GenericRepository<User, String> {
    
    @Override
    protected String getId(User entity) {
        return entity.getId();
    }
}