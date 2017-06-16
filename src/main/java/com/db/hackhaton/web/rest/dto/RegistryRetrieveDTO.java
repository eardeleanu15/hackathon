package com.db.hackhaton.web.rest.dto;

import com.db.hackhaton.domain.Registry;

/**
 * Created by echo on 6/15/17.
 */
public class RegistryRetrieveDTO extends Registry{

    private int numberOfCases;

    public int getNumberOfCases() {
        return numberOfCases;
    }

    public void setNumberOfCases(int numberOfCases) {
        this.numberOfCases = numberOfCases;
    }
}
