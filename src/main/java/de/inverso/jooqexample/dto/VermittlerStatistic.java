package de.inverso.jooqexample.dto;

import java.math.BigInteger;

/**
 * @author fabian
 * on 19.05.19.
 */
public class VermittlerStatistic {

    private String vermittlernummer;

    private BigInteger anzahl;

    public String getVermittlernummer() {
        return vermittlernummer;
    }

    public void setVermittlernummer(String vermittlernummer) {
        this.vermittlernummer = vermittlernummer;
    }

    public BigInteger getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(BigInteger anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VermittlerStatistic{");
        sb.append("vermittlernummer='").append(vermittlernummer).append('\'');
        sb.append(", anzahl=").append(anzahl);
        sb.append('}');
        return sb.toString();
    }
}
