package pillihuaman.com.pe.security.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.security.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.security.entity.role.Roles;
import pillihuaman.com.pe.security.Help.Constants;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleDaoImplement extends AzureAbstractMongoRepositoryImpl<Roles> implements RoleRepository {

    public RoleDaoImplement() {
        DS_WRITE = Constants.DW;
        COLLECTION = Constants.COLLECTION_ROLE;
    }

    @Override
    public Class<Roles> provideEntityClass() {
        return Roles.class;
    }

    @Override
    public List<Roles> findByName(String name) {
        MongoCollection<Roles> collection = getCollection(COLLECTION, Roles.class);
        Document query = new Document("name", name);
        return collection.find(query, Roles.class).into(new ArrayList<>());
    }

    @Override
    public long countAll() {
        MongoCollection<Roles> collection = getCollection(COLLECTION, Roles.class);
        return collection.countDocuments();
    }
    @Override
    public Roles save(Roles role) {
        return super.save(role); // Usa el método de tu clase base
    }
}
