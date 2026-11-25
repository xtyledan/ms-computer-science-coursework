package com.snhu.mscs.cs600.salesguru;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class ExternalUserDetailsService implements UserDetailsService {

    private final String jdbcUrl;
    private final String dbUsername;
    private final String dbPassword;

    public ExternalUserDetailsService(
            @Value("${salesguru.auth.url}") String jdbcUrl,
            @Value("${salesguru.auth.username}") String dbUsername,
            @Value("${salesguru.auth.password}") String dbPassword) {

        this.jdbcUrl = jdbcUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException(
                "Invalid username or password.");
        }

        String sql =
            "SELECT username, password_hash, enabled " +
            "FROM auth_user WHERE username = ?";

        try (
            Connection connection = DriverManager.getConnection(
                jdbcUrl,
                dbUsername,
                dbPassword
            );

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);
            statement.setQueryTimeout(5);

            try (ResultSet result = statement.executeQuery()) {

                if (!result.next()) {
                    throw new UsernameNotFoundException(
                        "Invalid username or password.");
                }

                return User
                    .withUsername(result.getString("username"))
                    .password(result.getString("password_hash"))
                    .roles("USER")
                    .disabled(!result.getBoolean("enabled"))
                    .build();
            }

        } catch (SQLException ex) {

            throw new AuthenticationServiceException(
                "External authentication database is unavailable.",
                ex
            );
        }
    }
}