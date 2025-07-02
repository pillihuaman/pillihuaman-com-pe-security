package pillihuaman.com.pe.security.repository;


import pillihuaman.com.pe.security.entity.role.Roles;

import java.util.List;

public interface RoleRepository {
    Class<Roles> provideEntityClass();

    List<Roles> findByName(String name);
    Roles save(Roles role);
    long countAll();
}