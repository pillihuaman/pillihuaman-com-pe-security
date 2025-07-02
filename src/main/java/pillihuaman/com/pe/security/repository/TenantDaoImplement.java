package pillihuaman.com.pe.security.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.security.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.security.Help.Constants;

import java.util.Optional;

@Repository
public class TenantDaoImplement extends AzureAbstractMongoRepositoryImpl<Tenant> implements TenantRepository {

    public TenantDaoImplement() {
        DS_WRITE = Constants.DW;
        COLLECTION = "tenant"; // Usa el nombre que tengas en MongoDB
    }

    @Override
    public Class<Tenant> provideEntityClass() {
        return Tenant.class;
    }

    @Override
    public Optional<Tenant> findByDomain(String domain) {
        MongoCollection<Tenant> collection = getCollection(COLLECTION, Tenant.class);
        Document query = new Document("domain", domain);
        Tenant result = collection.find(query).first();
        return Optional.ofNullable(result);
    }
}
