package de.inverso.jooqexample.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author fabian
 * on 17.05.19.
 */
@Entity
@NamedQueries( {@NamedQuery(name= "persons", query = "SELECT p from Person p") } )
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private  String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "person_id")
    private List<Bankverbindung> bankverbindungen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Bankverbindung> getBankverbindungen() {
        return bankverbindungen;
    }

    public void setBankverbindungen(List<Bankverbindung> bankverbindungen) {
        this.bankverbindungen = bankverbindungen;
    }

    public Person(){

    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
