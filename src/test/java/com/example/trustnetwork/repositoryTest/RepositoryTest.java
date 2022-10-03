package com.example.trustnetwork.repositoryTest;

import com.example.trustnetwork.domain.Person;
import com.example.trustnetwork.repository.Repository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTest {
    @Autowired
    private Repository repository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void savePersonTest() {
        Person person = Person.builder().id("Mark").topics("Test").build();
        repository.save(person);
        Assertions.assertThat(person.getCode()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void findPersonByIdTest() {
        Person person = repository.findPersonById("Mark");
        Assertions.assertThat(person.getId()).isEqualTo("Mark");
    }


}
