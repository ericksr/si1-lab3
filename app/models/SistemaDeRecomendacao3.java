package models;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by Erick on 05/02/2015.
 */
@Entity
public class SistemaDeRecomendacao3 extends SistemaDeRecomendacao {

    @Override
    public String recomendarEpisodio(List<Episode> episodes) {
        String toString = episodes.get(0).getName() + " (" + episodes.get(0).getNumber() + ")";

        //implementar

        return toString;
    }
}
