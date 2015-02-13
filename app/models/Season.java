package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavo on 05/12/14.
 */
@Entity(name="Season")
public class Season {
    @Id
    @GeneratedValue
    @Column
    private long id;
    @Column
    private int number;
    @ManyToOne(cascade=CascadeType.ALL)
    private Series series;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn
    private List<Episode> episodes;

    public enum Status {
        full,
        incomplete,
        none
    }

    public Season() {
        episodes = new ArrayList<>();
    }

    public Season(int number, Series series) {
        this();
        this.number = number;
        this.series = series;
    }

    public int getNumber() {
        return number;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void addEpisode(Episode episode) {
        episodes.add(episode);
    }

    public long getId() {
        return id;
    }

    public Status getStatus() {
        int cont = 0;
        for (Episode episode : episodes) {
            if (episode.isWatched()) {
                cont++;
            }
        }
        if (cont == 0) {
            return Status.none;
        } else if (cont == episodes.size()) {
            return Status.full;
        } else {
            return Status.incomplete;
        }
    }

    public String nextEpisode(SistemaDeRecomendacao recomendacao){
        return recomendacao.recomendarEpisodio(this.episodes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Season season = (Season) o;

        if (number != season.number) return false;
        if (!episodes.equals(season.episodes)) return false;
        if (!series.equals(season.series)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + series.hashCode();
        result = 31 * result + episodes.hashCode();
        return result;
    }
}
