package edu.uga.cs.project4;

import java.io.Serializable;

/**
 * The Country class represents a country with a unique ID, its name, and the continent it belongs to.
 */
public class Country implements Serializable {
    // Unique identifier for the country
    private long id;

    // Name of the country
    private String country;

    // Continent location for country
    private String continent;

    /**
     * Constructor to initialize a Country object
     *
     * @param country
     * @param continent
     */
    public Country(String country, String continent) {
        this.country = country;
        this.continent = continent;
    } // Country

    /**
     * Gets the ID of the country
     */
    public long getId() {
        return id;
    } // getId

    /**
     * Sets the ID of the country.
     *
     */
    public void setId(long id) {
        this.id = id;
    } // setId

    /**
     * Gets the name of the country.
     */
    public String getCountry() {
        return country;
    } // getCountry

    /**
     * Sets the name of the country
     */
    public void setCountry(String country) {
        this.country = country;
    } // setCountry

    /**
     * Gets the continent of the country.

     */
    public String getContinent() {
        return continent;
    } // getContinent

    /**
     * Sets the continent of the country.
     */
    public void setContinent(String continent) {
        this.continent = continent;
    } //setContinent

    /**
     * Returns a string representation of the Country object.
     *
     */
    @Override
    public String toString() {
        return "QuizScore{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", continent=" + continent +
                '}';
    } // toString
} // Country

