package com.tenPines.persistence;

import com.tenPines.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryPersonDao implements AbstractRepository<Person> {

    protected static List<Person> persons = new ArrayList<>();

    @Override
    public List<Person> retrieveAll() {
        return persons;
    }

    @Override
    public List save(Person... people) {
        Collections.addAll(persons, people);
        return null;
    }

    @Override
    public Person refresh(Person person) {
        return person;
    }

    @Override
    public void delete(Person aPerson) {
        persons.remove(aPerson);
    }

    @Override
    public Person findById(Long id) {
        return persons.get(id.intValue() - 1);
    }


}