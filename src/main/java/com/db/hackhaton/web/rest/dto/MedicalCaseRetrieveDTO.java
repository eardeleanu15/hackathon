package com.db.hackhaton.web.rest.dto;

import com.db.hackhaton.domain.MedicalCase;
import com.db.hackhaton.domain.Registry;
import com.db.hackhaton.domain.User;

/**
 * Created by echo on 6/16/17.
 */
public class MedicalCaseRetrieveDTO extends MedicalCase {
    private Registry registry;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
