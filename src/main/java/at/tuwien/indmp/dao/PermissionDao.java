package at.tuwien.indmp.dao;

import at.tuwien.indmp.model.Permission;

import org.springframework.stereotype.Repository;

@Repository
public class PermissionDao extends AbstractDao<Permission> {

    public PermissionDao() {
        super(Permission.class);
    }
}
