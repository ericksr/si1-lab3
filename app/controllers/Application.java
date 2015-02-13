package controllers;

import models.*;
import models.dao.GenericDAO;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;

public class Application extends Controller {
    static final GenericDAO dao = new GenericDAO();

    @Transactional
    public static Result index() {
        return redirect(routes.Application.series());
    }

    @Transactional
    public static Result series() {
        List<Series> seriesList = dao.findByAttributeName("Series", "watched", "false");
        List<Series> watchedSeriesList = dao.findByAttributeName("Series", "watched", "true");
        return ok(index.render(seriesList, watchedSeriesList));
    }

    @Transactional
    public static Result watchSeries(long id) {
        Series series = dao.findByEntityId(Series.class, id);
        series.setWatched();
        dao.merge(series);
        return redirect(routes.Application.series());
    }

    @Transactional
    public static Result watchEpisode(long id) {
        Episode episode = dao.findByEntityId(Episode.class, id);
        episode.setWatched();
        dao.merge(episode);
        return redirect(routes.Application.series());
    }

    @Transactional
    public static Result recommend(Long id) {
        DynamicForm form = Form.form().bindFromRequest();
        Series serie = dao.findByEntityId(Series.class, id);

        int opcao = Integer.parseInt(form.get("opcao"));

        if (opcao == 1){
            serie.setSistemaDeRecomendacao(new SistemaDeRecomendacao1());
        }else if (opcao == 2){
            serie.setSistemaDeRecomendacao(new SistemaDeRecomendacao2());
        }else{
            serie.setSistemaDeRecomendacao(new SistemaDeRecomendacao3());
        }

        dao.merge(serie);
        dao.flush();

        return redirect(routes.Application.series());
    }
}
