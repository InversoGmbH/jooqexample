package de.inverso.jooqexample.dto;

import java.math.BigInteger;

/**
 * @author fabian
 * on 18.05.19.
 */
public class BankStatisticsDTO {

    private BigInteger id;

    private BigInteger anzahl;

    public BankStatisticsDTO(BigInteger id, BigInteger anzahl) {
        this.id = id;
        this.anzahl = anzahl;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(BigInteger anzahl) {
        this.anzahl = anzahl;
    }

    public BankStatisticsDTO() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BankStatisticsDTO{");
        sb.append("id=").append(id);
        sb.append(", anzahl=").append(anzahl);
        sb.append('}');
        return sb.toString();
    }
}
