package at.tuwien.indmp.service;

import at.tuwien.indmp.dao.PermissionDao;
import at.tuwien.indmp.model.Permission;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 
     * Create a new permission
     * 
     * @param permission
     */
    @Transactional
    public void create(Permission permission) {
        Objects.requireNonNull(permission);
        permissionDao.persist(permission);
    }
}
