package ru.northarea.exchanger.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.northarea.exchanger.model.DbConfig;

@Repository
public interface DbConfigRepository extends CrudRepository<DbConfig, Long> {
}
