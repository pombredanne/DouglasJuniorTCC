/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.net;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.edge.AbstractEdge;
import br.edu.utfpr.cm.JGitMinerWeb.pojo.EntityRepository;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author douglas
 */
//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractNet implements Runnable, Serializable {

    protected GenericDao dao;
    protected EntityRepository repository;
    protected Date begin;
    protected Date end;
    protected List<AbstractEdge> net;

    public AbstractNet(EntityRepository repository, Date begin, Date end, GenericDao dao) {
        this.dao = dao;
        this.repository = repository;
        this.begin = begin;
        this.end = end;
    }

    public EntityRepository getRepository() {
        return repository;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public List<AbstractEdge> getNet() {
        return net;
    }

    @Override
    public abstract void run();
}
