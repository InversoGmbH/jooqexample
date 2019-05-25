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
public class Bankverbindung {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private String bic;

    @Column(name = "PERSON_ID")
    private Long personId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPersonId() {
        return personId;
    }

    public Bankverbindung(){};

    public Bankverbindung(String iban, String bic) {
        this.iban = iban;
        this.bic = bic;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Bankverbindung{");
        sb.append("id=").append(id);
        sb.append(", iban='").append(iban).append('\'');
        sb.append(", bic='").append(bic).append('\'');
        sb.append(", personId=").append(personId);
        sb.append('}');
        return sb.toString();
    }
}
