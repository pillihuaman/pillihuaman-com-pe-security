package pillihuaman.com.pe.security.repository;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.security.Help.AuditEntity;
import pillihuaman.com.pe.security.Help.Constants;
import pillihuaman.com.pe.security.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.util.MyJsonWebToken;

import java.util.*;


@Repository
public class UserDaoImplement extends AzureAbstractMongoRepositoryImpl<User> implements UserRepository {

    @Autowired
    private TokenRepository tokenRepository;


    UserDaoImplement() {
        DS_WRITE = Constants.DW;
        // DS_READ = Constants.DR;
        COLLECTION = "";
        COLLECTION = Constants.COLLECTION_USER;
    }

    @Override
    public Optional<User> findByEmail(String mail) {
        Optional<UserDetails> op = null;
        MongoCollection<User> collection = getCollection(Constants.COLLECTION_USER, User.class);
        Document query = new Document().append("email", mail);
        User user = collection.find(query, User.class).limit(1).first();
        if (user != null) {
            user.setPassword(user.getPasswordP());
            user.setTokens(tokenRepository.findAllValidTokenByUser(user.getId()));
            if (user != null) {
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } else {

            return Optional.empty();
        }
    }

    @Override
    public int getLastIdUser() {
        int id = 0;
        try {
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase(DS_WRITE);
            MongoCollection<Document> collection = database.getCollection(Constants.COLLECTION_USER);
            Document sort = new Document().append("_id", -1);
            Document lis =
                    collection.find().sort(sort).first();
            if (Objects.nonNull(lis)) {
                id = (int) lis.get("idUser");
            }
            mongoClient.close();
        } catch (MongoException e) {
            id = 0;
        }
        return id;
    }

    @Override
    public List<User> findUserByMail(String mail) {
        try {
            Document query = new Document()
                    .append("mail", mail);
            Document sort = new Document()
                    .append("_id", -1);
            Optional<User> f = findAllByQuery(query).stream().findFirst();
            List<User> l = findAll();
        } catch (MongoException e) {
            throw e;
        }
        return null;
    }


    @Override
    public List<User> findUserName(String username) {
        MongoCollection<User> collection = getCollection(Constants.COLLECTION_USER, User.class);
        Document query = new Document().append("email", username);
        List<User> lisUser = collection.find(query, User.class).limit(1).into(new ArrayList<User>());

        return lisUser;
    }

    @Override
    public Class<User> provideEntityClass() {
        // TODO Auto-generated method stub
        return User.class;
    }

    @Override
    public User saveUser(User request, MyJsonWebToken jwt) {
        User us = null;
        try {

            Document docAud = new Document();
            AuditEntity aud = new AuditEntity();
            aud.setDateRegister(new Date());
            aud.setMail(request.getEmail());
            request.setAuditEntity(aud);
            us = save(request);
        } catch (Exception e) {
            // Handle the exception (e.g., log it or throw a custom exception)
            e.printStackTrace();
        }
        return us;
    }

    @Override
    public List<User> findLastUser() {
        MongoCollection<User> collection = getCollection(Constants.COLLECTION_USER, User.class);
        Document query = new Document();
        Document sort = new Document().append("id_user", -1);
        List<User> lisProduct = collection.find(query, User.class).sort(sort).limit(1).into(new ArrayList<User>());
        return lisProduct;
    }

    @Override
    public List<User> findUserById(ObjectId id) {
        MongoCollection<User> collection = getCollection(Constants.COLLECTION_USER, User.class);
        Document query = new Document().append("_id", id);
        List<User> lisUser = collection.find(query, User.class).limit(1).into(new ArrayList<User>());
        return lisUser;
    }


    public UserDetails convertUserToUserDetailsService(User user) {
        // Create a UserDetails object using Spring Security's User class
        UserDetails userDetails = User.builder()
                .userName(user.getUsername())
                .password(user.getPasswordP())
                .email(user.getEmail())
                .build();

        return userDetails;
    }

    @Override
    public List<User> listByStatus(boolean status) {
        MongoCollection<User> collection = getCollection(Constants.COLLECTION_USER, User.class);
        Document query = new Document().append("enabled", status);
        return collection.find(query, User.class).into(new ArrayList<User>());
    }
}
