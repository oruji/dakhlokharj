package org.oruji.dakhlokharj;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TransactionDao {
	protected EntityManager getEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("dakhlokharj");
		EntityManager ecm = emf.createEntityManager();
		return ecm;
	}

	public TransactionModel transCreate(TransactionModel trans) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		if (!em.contains(trans)) {
			em.persist(trans);
			em.flush();
		}
		em.getTransaction().commit();

		return trans;
	}

	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead() {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findAll").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, String description) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeDesc")
				.setParameter("transType", typeSearch).setParameter("transDesc", "%" + description + "%").getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByType")
				.setParameter("transType", typeSearch).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(String description) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByDesc")
				.setParameter("transDesc", "%" + description + "%").getResultList();
	}

	public TransactionModel transUpdate(TransactionModel trans) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		if (!em.contains(trans)) {
			em.merge(trans);
			em.flush();
		}
		em.getTransaction().commit();
		return trans;
	}

	public TransactionModel transDelete(TransactionModel trans) {
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		if (!em.contains(trans)) {
			em.remove(em.find(TransactionModel.class, trans.getId()));
			em.flush();
		}
		em.getTransaction().commit();
		return trans;
	}
}
