package sk.luce.data.repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sk.luce.data.UserAccount;

@RepositoryRestResource(exported = false)
public interface AccountRepo extends CrudRepository<UserAccount, Long> {

    UserAccount findByName(String name);

}
