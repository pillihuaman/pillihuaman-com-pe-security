package pillihuaman.com.pe.security.repository;


import pillihuaman.com.pe.security.entity.token.Token;


import java.util.List;
import java.util.Optional;
public interface TokenRepository extends BaseMongoRepository<Token> {

    List<Token> findAllValidTokenByUser(Object id);

    Optional<Token> findByToken(String token);
}
