package edu.uga.cs.project4;

public class Country {
    private long id;
    private String country;
    private String continent;

    public Country(String country, String continent) {
        this.country = country;
        this.continent = continent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "QuizScore{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", continent=" + continent +
                '}';
    }
}
