package net.ldauvilaire.sample.jms.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.ldauvilaire.sample.jms.domain.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    public List<Person> findByFirstName(String firstName);
    public List<Person> findByLastName(String lastName);
    public List<Person> findByFirstNameAndLastName(String firstName, String lastName);
}
