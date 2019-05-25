package de.inverso.jooqexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author fabian
 * on 18.05.19.
 */
@Entity
public class Vermittler {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String vermittlernummer;

    public Vermittler(){

    }

    public Vermittler(String firstName, String lastName, String vermittlernummer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.vermittlernummer = vermittlernummer;
    }

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

    public String getVermittlernummer() {
        return vermittlernummer;
    }

    public void setVermittlernummer(String vermittlernummer) {
        this.vermittlernummer = vermittlernummer;
    }
}
