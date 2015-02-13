package models;

import models.dao.GenericDAO;
import org.junit.Test;
import test.AbstractTest;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class SeriesTest extends AbstractTest {
    GenericDAO dao = new GenericDAO();
    List<Series> seriesList;

    @Test
    public void mustStartWithNoSeries() throws Exception {
        seriesList = dao.findAllByClass(Series.class);
        assertThat(seriesList).isEmpty();
    }

    @Test
    public void mustAddSeriesToDB() throws Exception {
        Series series = new Series("Vikings");
        Season season = new Season(1, series);
        Episode episode = new Episode(1, "Primeiro epi", season);
        season.addEpisode(episode);
        series.addSeason(season);
        dao.persist(series);

        seriesList = dao.findAllByClass(Series.class);
        assertThat(seriesList.size()).isEqualTo(1);
        assertThat(seriesList.get(0)).isEqualTo(series);
    }

    @Test
    public void mustSetSeriesAsWatched() throws Exception {
        Series series = new Series("Vikings");
        Season season = new Season(1, series);
        Episode episode = new Episode(1, "Primeiro epi", season);
        season.addEpisode(episode);
        series.addSeason(season);
        dao.persist(series);

        series = (Series) dao.findByAttributeName("Series", "name", "Vikings").get(0);
        series.setWatched();
        dao.merge(series);

        seriesList = dao.findByAttributeName("Series", "watched", "false");
        assertThat(seriesList).isEmpty();
        seriesList = dao.findByAttributeName("Series", "watched", "true");
        assertThat(seriesList.size()).isEqualTo(1);
    }
}