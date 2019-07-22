package de.inverso.jooqexample.model;

import javax.persistence.*;

/**
 * @author fabian
 * on 18.05.19.
 */
@Entity
@Table(name = "BROKER")
public class Broker {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String brokerId;

    public Broker(){

    }

    public Broker(String firstName, String lastName, String brokerId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.brokerId = brokerId;
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

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    @Override
    public String toString() {
        return "Broker{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", brokerId='" + brokerId + '\'' +
                '}';
    }
}
