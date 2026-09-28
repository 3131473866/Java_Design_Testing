package com.example.diaop.services;

import com.example.diaop.interfaces.Connection;
import com.example.diaop.interfaces.FileSystem;
import com.example.diaop.interfaces.Persistence;

import javax.inject.Inject;

public class PersistenceOracle implements Persistence {
    private final FileSystem fileSystem;
    private final Connection connection;

    @Inject
    public PersistenceOracle(FileSystem fileSystem, Connection connection) {
        this.fileSystem = fileSystem;
        this.connection = connection;
    }

    @Override
    public String getType() {
        return "Oracle Persistence";
    }

    @Override
    public boolean run() {
        System.out.println("running " + this.getType());
        fileSystem.run();
        connection.run();
        return true;
    }
}
