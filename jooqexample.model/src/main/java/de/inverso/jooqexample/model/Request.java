package de.inverso.jooqexample.model;

import javax.persistence.*;
import java.time.LocalDate;

import java.util.Set;

/**
 * @author fabian
 * on 18.05.19.
 */
@Entity
@Table(name = "REQUEST")
@NamedQuery(name = "requestWithProducts", query = "SELECT r from Request r join r.products")
public class Request {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    private Person person;

    @Column(nullable = false)
    private String requestNumber;

    @Column(nullable = false)
    private String brokerId;

    @Column(nullable = false)
    private LocalDate creationDate;

    @ManyToMany
    private Set<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Request(Person person, String requestNumber, String brokerId, LocalDate creationDate) {
        this.person = person;
        this.requestNumber = requestNumber;
        this.brokerId = brokerId;
        this.creationDate = creationDate;
    }

    public Request() {

    }
}
