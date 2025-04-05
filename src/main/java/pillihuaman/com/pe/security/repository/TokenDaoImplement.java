package pillihuaman.com.pe.security.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.security.Help.Constants;
import pillihuaman.com.pe.security.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.security.entity.token.Token;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TokenDaoImplement extends AzureAbstractMongoRepositoryImpl<Token> implements TokenRepository {
    public TokenDaoImplement() {
        DS_WRITE = Constants.DW;
        // DS_READ = Constants.DR;
        COLLECTION = Constants.COLLECTION_TOKEN;

    }

    @Override
    public Class<Token> provideEntityClass() {
        // TODO Auto-generated method stub
        return Token.class;
    }


    @Override
    public List<Token> findAllValidTokenByUser(Object id) {
        MongoCollection<Token> collection = getCollection(this.COLLECTION, Token.class);
        //Document query = new Document().append("_id", id);
        //Created with NoSQLBooster, the essential IDE for MongoDB - https://nosqlbooster.com
        Document query = new Document()
                .append("user._id", id);
        List<Token> lsiTok = collection.find(query, Token.class).limit(1).into(new ArrayList<Token>());
        return lsiTok;
    }

    @Override
    public Optional<Token> findByToken(String token) {
        MongoCollection<Token> collection = getCollection(this.COLLECTION, Token.class);
        Document query = new Document().append("_id", token);

        Token toke = collection.find(query, Token.class).limit(1).first();
        return Optional.ofNullable(toke);
    }
}
