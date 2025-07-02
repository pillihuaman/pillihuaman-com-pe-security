package pillihuaman.com.pe.security.repository;



import pillihuaman.com.pe.lib.domain.Tenant;

import java.util.Optional;

public interface TenantRepository {
    Class<Tenant> provideEntityClass();
    Optional<Tenant> findByDomain(String domain);
}