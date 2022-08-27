package org.example.repositories;

import org.example.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query(value = "SELECT * FROM messages ORDER BY id DESC LIMIT :count", nativeQuery = true)
    List<Message> findLastMessages(int count);
}
