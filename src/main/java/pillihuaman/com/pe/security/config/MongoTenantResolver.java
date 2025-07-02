package pillihuaman.com.pe.security.config;


import org.springframework.stereotype.Component;

import pillihuaman.com.pe.lib.domain.Tenant;
import pillihuaman.com.pe.lib.domain.TenantResolver;
import pillihuaman.com.pe.security.repository.TenantRepository;

@Component
public class MongoTenantResolver implements TenantResolver {

    private final TenantRepository tenantRepository;

    public MongoTenantResolver(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant resolveByHost(String host) {
        return tenantRepository.findByDomain(host)
                .map(entity -> Tenant.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .domain(entity.getDomain())
                        .active(entity.isActive())
                        .build()
                ).orElse(null);
    }
}
