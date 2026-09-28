package com.example.mocking;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAdminTest {
    UserAdmin userAdmin;

    @Mocked
    DBConnection dbConnection;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        userAdmin = new UserAdmin(dbConnection);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        userAdmin = null;
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void createUser_success() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("alice"); result = false;
            dbConnection.addUser("alice", "pass123"); times = 1;
        }};
        assertTrue(userAdmin.createUser("alice", "pass123"));
    }

    @Test
    void createUser_userAlreadyExists() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("alice"); result = true;
        }};
        assertFalse(userAdmin.createUser("alice", "pass123"));
        assertTrue(outContent.toString().contains("username already exists"));
        new Verifications() {{
            dbConnection.addUser(anyString, anyString); times = 0;
        }};
    }

    @Test
    void createUser_sqlExceptionOnUserExists() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("alice"); result = new SQLException("DB error");
        }};
        assertFalse(userAdmin.createUser("alice", "pass123"));
        assertTrue(outContent.toString().contains("DBConnection problem at createUser"));
    }

    @Test
    void createUser_sqlExceptionOnAddUser() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("alice"); result = false;
            dbConnection.addUser("alice", "pass123"); result = new SQLException("DB error");
        }};
        assertFalse(userAdmin.createUser("alice", "pass123"));
        assertTrue(outContent.toString().contains("DBConnection problem at createUser"));
    }

    @Test
    void removeUser_success() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("bob"); result = true;
            dbConnection.isAdmin("bob"); result = false;
            dbConnection.deleteUser("bob"); times = 1;
        }};
        assertTrue(userAdmin.removeUser("bob"));
    }

    @Test
    void removeUser_userDoesNotExist() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("bob"); result = false;
        }};
        assertFalse(userAdmin.removeUser("bob"));
        assertTrue(outContent.toString().contains("does NOT exist"));
        new Verifications() {{
            dbConnection.isAdmin(anyString); times = 0;
            dbConnection.deleteUser(anyString); times = 0;
        }};
    }

    @Test
    void removeUser_userIsAdmin() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("admin"); result = true;
            dbConnection.isAdmin("admin"); result = true;
        }};
        assertFalse(userAdmin.removeUser("admin"));
        assertTrue(outContent.toString().contains("cannot remove an administrator"));
        new Verifications() {{
            dbConnection.deleteUser(anyString); times = 0;
        }};
    }

    @Test
    void removeUser_sqlExceptionOnUserExists() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("bob"); result = new SQLException("DB error");
        }};
        assertFalse(userAdmin.removeUser("bob"));
        assertTrue(outContent.toString().contains("DBConnection problem at removeUser"));
        new Verifications() {{
            dbConnection.isAdmin(anyString); times = 0;
            dbConnection.deleteUser(anyString); times = 0;
        }};
    }

    @Test
    void removeUser_sqlExceptionOnDeleteUser() throws SQLException {
        new Expectations() {{
            dbConnection.userExists("bob"); result = true;
            dbConnection.isAdmin("bob"); result = false;
            dbConnection.deleteUser("bob"); result = new SQLException("DB error");
        }};
        assertFalse(userAdmin.removeUser("bob"));
        assertTrue(outContent.toString().contains("DBConnection problem at removeUser"));
    }

    @Test
    void runUserReport_emptyDatabase() throws SQLException {
        new Expectations() {{
            dbConnection.getUsers(); result = new ArrayList<>();
        }};
        userAdmin.runUserReport();
        assertTrue(outContent.toString().contains("No users in database"));
    }

    @Test
    void runUserReport_oneUser() throws SQLException {
        List<User> users = new ArrayList<>();
        users.add(new User("alice", "pass"));
        new Expectations() {{
            dbConnection.getUsers(); result = users;
        }};
        userAdmin.runUserReport();
        String output = outContent.toString();
        assertTrue(output.contains("Listing all usernames"));
        assertTrue(output.contains("alice"));
    }

    @Test
    void runUserReport_smallList() throws SQLException {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 5; i++) users.add(new User("user" + i, "pass"));
        new Expectations() {{
            dbConnection.getUsers(); result = users;
        }};
        userAdmin.runUserReport();
        String output = outContent.toString();
        assertTrue(output.contains("Listing all usernames"));
        for (int i = 1; i <= 5; i++) assertTrue(output.contains("user" + i));
    }

    @Test
    void runUserReport_exactlyTenUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 10; i++) users.add(new User("user" + i, "pass"));
        new Expectations() {{
            dbConnection.getUsers(); result = users;
        }};
        userAdmin.runUserReport();
        String output = outContent.toString();
        assertTrue(output.contains("Listing all usernames"));
        assertTrue(output.contains("user10"));
    }

    @Test
    void runUserReport_elevenUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 11; i++) users.add(new User("user" + i, "pass"));
        new Expectations() {{
            dbConnection.getUsers(); result = users;
        }};
        userAdmin.runUserReport();
        String output = outContent.toString();
        assertTrue(output.contains("Total number of users: 11"));
        assertTrue(output.contains("6 more..."));
    }

    @Test
    void runUserReport_largeList() throws SQLException {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 15; i++) users.add(new User("user" + i, "pass"));
        new Expectations() {{
            dbConnection.getUsers(); result = users;
        }};
        userAdmin.runUserReport();
        String output = outContent.toString();
        assertTrue(output.contains("Total number of users: 15"));
        assertTrue(output.contains("user1"));
        assertTrue(output.contains("user5"));
        assertTrue(output.contains("10 more..."));
    }

    @Test
    void runUserReport_sqlException() throws SQLException {
        new Expectations() {{
            dbConnection.getUsers(); result = new SQLException("DB error");
        }};
        assertDoesNotThrow(() -> userAdmin.runUserReport());
        assertTrue(outContent.toString().contains("DBConnection problem at runUserReport"));
    }
}