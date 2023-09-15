package org.example.DAO;

import org.example.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    private SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("Select t from Person t", Person.class)
                .getResultList();
    }

    @Transactional
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();

        return session.get(Person.class, id);
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?", new Object[]{email},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();

        session.persist(person);
    }

    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();

        Person person = session.get(Person.class, id);

        person.setName(updatedPerson.getName());
        person.setAge(updatedPerson.getAge());
        person.setEmail(updatedPerson.getEmail());
    }

    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();

        session.remove(session.get(Person.class, id));
    }


//    ////////////////////
//     Тестируем производительность
//    ////////////////////

    public void testMultiUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for (Person person : people) {
            jdbcTemplate.update("INSERT INTO Person VALUES(?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(),
                    person.getEmail());
        }

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES(?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, people.get(i).getId());
                        ps.setString(2, people.get(i).getName());
                        ps.setInt(3, people.get(i).getAge());
                        ps.setString(4, people.get(i).getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 1000; i++)
            people.add(new Person("Name" + i, 30, "test" + i + "@mail.ru", "some address"));

        return people;
    }
}