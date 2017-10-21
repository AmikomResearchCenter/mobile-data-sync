package controls;

import java.util.List;

import models.Posting;

/**
 * Created by ariflaksito on 10/19/17.
 */

public interface IPosting {

    void add(Posting p);

    void update(int pubid, Posting p);

    void delete(int pubid);

    List<Posting> get();

}
