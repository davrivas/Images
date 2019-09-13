package model.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.entities.Image;

@Stateless
public class ImageFacade extends AbstractFacade<Image> {

    @PersistenceContext(unitName = "ImagesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImageFacade() {
        super(Image.class);
    }

}
