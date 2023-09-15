package org.example.services;

import org.example.models.Mood;
import org.example.models.Person;
import org.example.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository repository;


    @Autowired
    public PeopleService(PeopleRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public void save(Person person){
        person.setCreatedAt(new Date());
        person.setMood(Mood.CALM);
        repository.save(person);
    }

    public List<Person> findAll(){
        return repository.findAll();
    }

    public Person findOne(int id){
        Optional<Person> person = repository.findById(id);
        return person.orElse(null);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        repository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }

    public void test(){
        System.out.println("Testing here with debug. Inside Hibernate transaction");
    }
}
