package models;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by Erick on 05/02/2015.
 */
@Entity
public class SistemaDeRecomendacao2 extends SistemaDeRecomendacao {

    @Override
    public String recomendarEpisodio(List<Episode> episodes) {

        String toString = episodes.get(0).getName() + " (" + episodes.get(0).getNumber() + ")";

        for (int i = 0; i <= episodes.size() - 1; i++) {
            if (!episodes.get(i).isWatched()) {
                toString = episodes.get(i).getName() + " (" + episodes.get(i).getNumber() + ")";
                break;
            }
        }
        return toString;
    }
}