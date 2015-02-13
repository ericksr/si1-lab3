package models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Erick on 05/02/2015.
 */
@Entity
@DiscriminatorColumn(name="REF_TYPE")
public abstract class SistemaDeRecomendacao {
    @Id
    @GeneratedValue
    private long id;

    public abstract String recomendarEpisodio(List<Episode> episodes);
}
