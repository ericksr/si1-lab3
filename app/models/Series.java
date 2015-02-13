package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavo on 05/12/14.
 */
@Entity(name = "Series")
public class Series {
    @Id
    @GeneratedValue
    @Column
    private long id;
    @Column
    private String name;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn
    private List<Season> seasons;
    @Column
    private boolean watched;
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="sistema_de_recomendacao")
    private SistemaDeRecomendacao sistemaDeRecomendacao;

    public Series() {
        seasons = new ArrayList<>();
        watched = false;
        sistemaDeRecomendacao = new SistemaDeRecomendacao1();
    }

    public Series(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void addSeason(Season season) {
        seasons.add(season);
    }

    public long getId() {
        return id;
    }

    public void setWatched() {
        watched = true;
    }

    public SistemaDeRecomendacao getSistemaDeRecomendacao() {
        return sistemaDeRecomendacao;
    }

    public void setSistemaDeRecomendacao(SistemaDeRecomendacao sistemaDeRecomendacao) {
        this.sistemaDeRecomendacao = sistemaDeRecomendacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Series series = (Series) o;

        if (watched != series.watched) return false;
        if (!name.equals(series.name)) return false;
        if (!seasons.equals(series.seasons)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + seasons.hashCode();
        result = 31 * result + (watched ? 1 : 0);
        return result;
    }
}
