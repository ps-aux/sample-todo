package sk.luce.data.repo;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import sk.luce.data.Todo;

/**
 * Repository which returns and allows operation only on the
 * objects related to the current user (taken from security context).
 */
@RepositoryRestResource(path = "todo", collectionResourceRel = "todos")
public interface TodoRepo extends CrudRepository<Todo, Long> {

    @Override
    @Query("SELECT t FROM Todo t WHERE t.userAccount.name = ?#{authentication.name}")
    Iterable<Todo> findAll();

    /**
     * This prevents also delete, update operations - DELETE, POST, PATCH  will
     * stop here as they use this method.
     * TODO is this part of the official Spring API ? Can we really on this ?
     * <p>
     * Also returns 403 on repeated deletion - ok, as user has no right to delete
     * resouce with an id he no longer owns
     */
    @Override
    @PostAuthorize("returnObject?.userAccount?.name == authentication?.name")
    Todo findOne(Long aLong);
}
