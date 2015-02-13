package models;

import models.dao.GenericDAO;
import org.junit.Test;
import test.AbstractTest;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class SeasonTest extends AbstractTest {
    GenericDAO dao = new GenericDAO();
    List<Season> seasons;

    @Test
    public void mustStartWithNoSeason() throws Exception {
        seasons = dao.findAllByClass(Season.class);
        assertThat(seasons).isEmpty();
    }

    @Test
    public void mustAddEpisodeToDB() throws Exception {
        Series series = new Series("Vikings");
        Season season = new Season(1, series);
        Episode episode = new Episode(1, "Primeiro epi", season);
        season.addEpisode(episode);
        series.addSeason(season);
        dao.persist(series);

        seasons = dao.findAllByClass(Season.class);
        assertThat(seasons.size()).isEqualTo(1);
        assertThat(seasons.get(0)).isEqualTo(season);
    }
}