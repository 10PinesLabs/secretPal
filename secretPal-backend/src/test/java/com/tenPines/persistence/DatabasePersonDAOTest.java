package com.tenPines.persistence;

import com.tenPines.builder.PersonBuilder;
import com.tenPines.model.Person;
import com.tenPines.model.SecretPalEvent;
import org.hibernate.cfg.Environment;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DatabasePersonDAOTest {

    private AbstractRepository<Person> personDao;
    private AbstractRepository<SecretPalEvent> secretPalEventDao;


    @Before
    public void setUp() {
        HibernateUtils.addConfiguration(Environment.URL, "jdbc:mysql://localhost/calendardbtest");
        HibernateUtils.addConfiguration(Environment.HBM2DDL_AUTO, "create-drop");

        this.personDao = new DatabasePersonDao( HibernateUtils.createSessionFactory() );
        this.secretPalEventDao = new DatabaseSecretPalEventDao();
    }

    @Test
    public void When_I_Have_Zero_Persons_Persisted_When_I_Retrieve_Then_The_List_Is_Empty() {
        List<Person> result = this.personDao.retrieveAll();
        assertThat(result, hasSize(0));
    }

    @Test
    public void When_I_Save_A_New_Person_Then_I_Have_One_More_Person_Persisted() {
        Person aPerson = new PersonBuilder().build();
        personDao.save(aPerson);

        List<Person> result = this.personDao.retrieveAll();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(aPerson));
    }

    @Test
    public void When_I_Delete_A_Person_It_Should_Be_No_More() {
        Person aPerson = new PersonBuilder().build();
        personDao.save(aPerson);

        personDao.delete(aPerson);
        List<Person> result = this.personDao.retrieveAll();

        assertThat(result, not(hasItem(aPerson)));
    }
}