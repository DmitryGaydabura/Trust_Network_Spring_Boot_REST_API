package com.example.trustnetwork.repository;

import com.example.trustnetwork.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Person, String> {
    Person findPersonById(String id);

}
