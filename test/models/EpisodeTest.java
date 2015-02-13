package models;

import models.dao.GenericDAO;
import org.junit.Test;
import test.AbstractTest;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class EpisodeTest extends AbstractTest {
    GenericDAO dao = new GenericDAO();
    List<Episode> episodes;

    @Test
    public void mustStartWithNoEpisode() throws Exception {
        episodes = dao.findAllByClass(Episode.class);
        assertThat(episodes).isEmpty();
    }

    @Test
    public void mustAddEpisodeToDB() throws Exception {
        Series series = new Series("Vikings");
        Season season = new Season(1, series);
        Episode episode = new Episode(1, "Primeiro epi", season);
        season.addEpisode(episode);
        series.addSeason(season);
        dao.persist(series);

        episodes = dao.findAllByClass(Episode.class);
        assertThat(episodes.size()).isEqualTo(1);
        assertThat(episodes.get(0)).isEqualTo(episode);
    }

    @Test
    public void mustSetEpisodeAsWatched() throws Exception {
        Series series = new Series("Vikings");
        Season season = new Season(1, series);
        Episode episode = new Episode(1, "Primeiro epi", season);
        season.addEpisode(episode);
        series.addSeason(season);
        dao.persist(series);

        episode = (Episode) dao.findByAttributeName("Episode", "number", "1").get(0);
        episode.setWatched();
        dao.merge(episode);

        episodes = dao.findByAttributeName("Episode", "watched", "false");
        assertThat(episodes).isEmpty();
        episodes = dao.findByAttributeName("Episode", "watched", "true");
        assertThat(episodes.size()).isEqualTo(1);
    }
}