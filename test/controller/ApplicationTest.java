package controller;

import models.Episode;
import models.Series;
import models.dao.GenericDAO;

import org.junit.Before;
import org.junit.Test;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.Helpers;
import scala.Option;
import test.AbstractTest;

import java.util.List;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

/**
 * Functional test to test the application controller
 *
 * Created by gustavo on 07/12/14.
 */
public class ApplicationTest extends AbstractTest {

    public final GenericDAO dao = new GenericDAO();
    private Result result;

    @Override
    @Before
    public void setUp() {
        //Using Global.java to the tests
        FakeApplication app = Helpers.fakeApplication();
        Helpers.start(app);
        Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
        em = jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(em);
        em.getTransaction().begin();
    }

    @Test
    public void mustConnectToIndex() throws Exception {
        result = callAction(controllers.routes.ref.Application.index(), fakeRequest());
        assertThat(status(result)).isEqualTo(Http.Status.SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/series");
    }

    @Test
    public void mustSeeInitialUserInterface() throws Exception {
        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());
        assertThat(status(result)).isEqualTo(Http.Status.OK);
        assertThat(charset(result)).isEqualTo("utf-8");
        assertThat(contentAsString(result)).contains("Séries App");
        assertThat(contentAsString(result)).contains("Lista de Séries");
        assertThat(contentAsString(result)).contains("Séries Assistindo");
        assertThat(contentAsString(result)).contains("Séries App é um sistema para você cadastrar o que você assistiu os episódios das suas séries");
    }

    @Test
    public void mustSetSeriesAsWatched() throws Exception {

        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());

        assertThat(contentAsString(result)).contains("Two and a Half Men");
        int before = contentAsString(result).indexOf("Two and a Half Men");

        Series series = (Series) dao.findByAttributeName("Series", "name", "Two and a Half Men").get(0);

        result = callAction(controllers.routes.ref.Application.watchSeries(series.getId()), fakeRequest());
        assertThat(status(result)).isEqualTo(Http.Status.SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/series");
        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());

        assertThat(contentAsString(result)).contains("Two and a Half Men");
        int after = contentAsString(result).indexOf("Two and a Half Men");

        assertThat(after).isNotEqualTo(-1);
        assertThat(before).isNotEqualTo(-1);
        assertThat(after).isGreaterThan(before);
    }

    @Test
    public void mustSeeUnwatchedSeasonTag() throws Exception {

        Series series = (Series) dao.findByAttributeName("Series", "name", "Two and a Half Men").get(0);
        result = callAction(controllers.routes.ref.Application.watchSeries(series.getId()), fakeRequest());

        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());
        assertThat(contentAsString(result)).contains("Two and a Half Men");
        assertThat(contentAsString(result)).contains("Não Assitida");
    }

    @Test
    public void mustSetEpisodeAsWatched() throws Exception {
        Series series = (Series) dao.findByAttributeName("Series", "name", "Two and a Half Men").get(0);
        Episode episode = series.getSeasons().get(0).getEpisodes().get(0);
        result = callAction(controllers.routes.ref.Application.watchSeries(series.getId()), fakeRequest());

        result = callAction(controllers.routes.ref.Application.watchEpisode(episode.getId()), fakeRequest());
        assertThat(status(result)).isEqualTo(Http.Status.SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/series");
        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());
        assertThat(contentAsString(result)).contains("<a class=\"list-group-item list-group-item-success\">");
    }

    @Test
    public void mustSeeIncompleteSeasonTag() throws Exception {
        Series series = (Series) dao.findByAttributeName("Series", "name", "Two and a Half Men").get(0);
        Episode episode = series.getSeasons().get(0).getEpisodes().get(0);
        result = callAction(controllers.routes.ref.Application.watchSeries(series.getId()), fakeRequest());
        result = callAction(controllers.routes.ref.Application.watchEpisode(episode.getId()), fakeRequest());
        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());
        assertThat(contentAsString(result)).contains("Incompleta");
    }

    @Test
    public void mustSeeNextUnwatchedEpisode() throws Exception {
        Series series = (Series) dao.findByAttributeName("Series", "name", "Two and a Half Men").get(0);
        Episode episode = series.getSeasons().get(0).getEpisodes().get(0);
        result = callAction(controllers.routes.ref.Application.watchSeries(series.getId()), fakeRequest());
        result = callAction(controllers.routes.ref.Application.watchEpisode(episode.getId()), fakeRequest());
        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());
        assertThat(contentAsString(result)).contains("<a class=\"list-group-item list-group-item-warning\">");
        assertThat(contentAsString(result)).contains("Próximo Episódio: 2");
    }

    @Test
    public void mustSeeCompleteSeasonTag() throws Exception {
        Series series = (Series) dao.findByAttributeName("Series", "name", "Two and a Half Men").get(0);
        List<Episode> episodes = series.getSeasons().get(0).getEpisodes();
        result = callAction(controllers.routes.ref.Application.watchSeries(series.getId()), fakeRequest());
        for(Episode episode : episodes) {
            result = callAction(controllers.routes.ref.Application.watchEpisode(episode.getId()), fakeRequest());
        }
        result = callAction(controllers.routes.ref.Application.series(), fakeRequest());
        assertThat(contentAsString(result)).contains("Completa");
    }
}
