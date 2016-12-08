package org.oruji.dakhlokharj;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class TransactionDao {
	@PersistenceContext(unitName = "dakhlokharj")
	private EntityManager em;

	protected EntityManager getEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("dakhlokharj");
		EntityManager ecm = emf.createEntityManager();
		return ecm;
	}

	public TransactionModel transCreate(TransactionModel trans) {
		EntityManager em = getEntityManager();

		// begin transaction
		em.getTransaction().begin();
		if (!em.contains(trans)) {
			// persist object - add to entity manager
			em.persist(trans);
			// flush em - save to DB
			em.flush();
		}
		// commit transaction at all
		em.getTransaction().commit();

		return trans;
	}

	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead() {
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findAll").getResultList();
	}

	public TransactionModel transUpdate(TransactionModel trans) {
		return em.merge(trans);
	}

	public TransactionModel transDelete(TransactionModel trans) {
		em.remove(em.find(TransactionModel.class, trans.getId()));
		return trans;
	}
}
