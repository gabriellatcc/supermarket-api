package br.com.supermarket_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.supermarket_api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
