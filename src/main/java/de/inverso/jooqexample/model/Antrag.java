package de.inverso.jooqexample.model;

import javax.persistence.*;

/**
 * @author fabian
 * on 18.05.19.
 */
@Entity
public class Antrag {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    private Person person;

    @Column(nullable = false)
    private String antragsnummer;

    @Column(nullable = false)
    private String vermittlernummer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAntragsnummer() {
        return antragsnummer;
    }

    public void setAntragsnummer(String antragsnummer) {
        this.antragsnummer = antragsnummer;
    }

    public String getVermittlernummer() {
        return vermittlernummer;
    }

    public void setVermittlernummer(String vermittlernummer) {
        this.vermittlernummer = vermittlernummer;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Antrag(Person person, String antragsnummer, String vermittlernummer) {
        this.person = person;
        this.antragsnummer = antragsnummer;
        this.vermittlernummer = vermittlernummer;
    }

    public Antrag() {

    }
}
