package models;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by Erick on 05/02/2015.
 */
@Entity
public class SistemaDeRecomendacao1 extends SistemaDeRecomendacao {

    @Override
    public String recomendarEpisodio(List<Episode> episodes) {

        String toString = episodes.get(0).getName() + " (" + episodes.get(0).getNumber() + ")";

        for (int i = episodes.size() - 1; i >= 0; i--) {
            if (episodes.get(i).isWatched()) {
                if ((i + 1) == episodes.size())
                    toString = "O último episódio já foi assistido";
                else
                    toString = episodes.get(i + 1).getName() + " (" + episodes.get(i + 1).getNumber() + ")";
                break;
            }
        }
        return toString;
    }
}
