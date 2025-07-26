package pillihuaman.com.pe.security.repository;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.util.MyJsonWebToken;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseMongoRepository<User> {

    List<User> findUserByMail(String mail);

    List<User> findUserName(String mail);

    User saveUser(User request, MyJsonWebToken jwt);

    List<User> findLastUser();

    List<User> findUserById(ObjectId id);

    Optional<User> findByEmail(String mail);

    int getLastIdUser();

    List<User> listByStatus(boolean status);

    Optional<User> findByMobilPhone(String mobilPhone);

    Optional<User> findByEmailOrMobilPhone(String email, String mobilPhone);

}
