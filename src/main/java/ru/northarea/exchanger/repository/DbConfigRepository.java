package ru.northarea.exchanger.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.northarea.exchanger.model.DbConfig;

import java.util.Optional;

@Repository
public interface DbConfigRepository extends CrudRepository<DbConfig, Long> {
    Optional<DbConfig> findByName(String name);
}
