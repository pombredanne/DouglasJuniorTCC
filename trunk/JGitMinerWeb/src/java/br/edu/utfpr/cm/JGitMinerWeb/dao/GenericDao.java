package br.edu.utfpr.cm.JGitMinerWeb.dao;

import br.edu.utfpr.cm.JGitMinerWeb.pojo.miner.InterfaceEntity;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class GenericDao implements Serializable {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;
    private int transCount;

    private EntityManager getEntityManager() {
        return em;
    }

    public void insert(InterfaceEntity entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    public void edit(InterfaceEntity entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    public void remove(InterfaceEntity entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public <T> T findByID(Long id, Class classe) {
        verifyClearCache();
        return (T) getEntityManager().find(classe, id);
    }

    public <T> T findByID(String strId, Class classe) {
        try {
            Long lId = Long.valueOf(strId);
            Object obj = findByID(lId, classe);
            return (T) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public <T> T findByID(String strId, String strClasse) {
        try {
            Class cClass = Class.forName(strClasse);
            Object obj = findByID(strId, cClass);
            return (T) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List selectAll(Class classe) {
        verifyClearCache();
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(classe));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List selectBy(int[] intervalo, Class classe) {
        verifyClearCache();
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(classe));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(intervalo[1] - intervalo[0]);
        q.setFirstResult(intervalo[0]);
        return q.getResultList();
    }

    public int count(Class classe) {
        verifyClearCache();
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root rt = cq.from(classe);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List executeNamedQueryComParametros(String namedQuery, String[] parametros, Object[] objetos) {
        verifyClearCache();
        Query query = getEntityManager().createNamedQuery(namedQuery);
        if (parametros.length != objetos.length) {
            System.err.println("A quantidade de parametros difere da quantidade de atributos.");
            return null;
        }
        for (int i = 0; i < parametros.length; i++) {
            String atributo = parametros[i];
            Object parametro = objetos[i];
            query.setParameter(atributo, parametro);
        }
        List list = query.getResultList();
        return list;
    }

    public List executeNamedQuery(String namedQuery) {
        verifyClearCache();
        List list = getEntityManager().createNamedQuery(namedQuery).getResultList();
        return list;
    }

    public List selectWithParams(String select, String[] params, Object[] objects) {
        verifyClearCache();
        Query query = em.createQuery(select);
        if (params.length != objects.length) {
            throw new IndexOutOfBoundsException("The lenght of params array is not equals lenght of objects array.");
        }
        for (int i = 0; i < params.length; i++) {
            String atributo = params[i];
            Object parametro = objects[i];
            query.setParameter(atributo, parametro);
        }
        return query.getResultList();
    }

    public void clearCache() {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            em.clear();
            System.gc();
            System.out.println("######### LIMPOU CACHE #########");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void verifyClearCache() {
        if (transCount > 100) {
            clearCache();
            transCount = 0;
        } else {
            transCount++;
        }
    }
}
