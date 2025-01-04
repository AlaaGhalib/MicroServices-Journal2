package com.example.journalsystem.db;
import com.example.journalsystem.bo.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    // Custom query methods can be added here
}
