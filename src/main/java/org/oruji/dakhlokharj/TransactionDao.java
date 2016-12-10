package org.oruji.dakhlokharj;

import java.util.Date;
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

	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Date fromDate, Date toDate) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByDate")
				.setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<TransactionModel> transReadAcc(Integer accSearch) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByAcc")
				.setParameter("transAcc", accSearch).getResultList();
	}

	// typeDesc
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, String description) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeDesc")
				.setParameter("transType", typeSearch).setParameter("transDesc", "%" + description + "%")
				.getResultList();
	}

	// typeDate
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, Date fromDate, Date toDate) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeDate")
				.setParameter("transType", typeSearch).setParameter("fromDate", fromDate).setParameter("toDate", toDate)
				.getResultList();
	}

	// typeAcc
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, Integer accSearch) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeAcc")
				.setParameter("transType", typeSearch).setParameter("transAcc", accSearch).getResultList();
	}

	// descDate
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(String description, Date fromDate, Date toDate) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByDescDate")
				.setParameter("transDesc", "%" + description + "%").setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate).getResultList();
	}

	// descAcc
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transReadAcc(String description, Integer accSearch) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByDescAcc")
				.setParameter("transAcc", accSearch).setParameter("transDesc", "%" + description + "%").getResultList();
	}

	// dateAcc
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transReadAcc(Date fromDate, Date toDate, Integer accSearch) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByDateAcc")
				.setParameter("transAcc", accSearch).setParameter("fromDate", fromDate).setParameter("toDate", toDate)
				.getResultList();
	}

	// typeDescAcc
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, String description, Integer accSearch) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeDescAcc")
				.setParameter("transType", typeSearch).setParameter("transDesc", "%" + description + "%")
				.setParameter("transAcc", accSearch).getResultList();
	}

	// typeAccDate
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, Integer accSearch, Date fromDate, Date toDate) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeAccDate")
				.setParameter("transType", typeSearch).setParameter("fromDate", fromDate).setParameter("toDate", toDate)
				.setParameter("transAcc", accSearch).getResultList();
	}

	// descAccDate
	@SuppressWarnings("unchecked")
	public List<TransactionModel> transReadAcc(String description, Integer accSearch, Date fromDate, Date toDate) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByDescAccDate")
				.setParameter("transAcc", accSearch).setParameter("transDesc", "%" + description + "%")
				.setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, String description, Date fromDate, Date toDate) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeDescDate")
				.setParameter("transType", typeSearch).setParameter("transDesc", "%" + description + "%")
				.setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<TransactionModel> transRead(Integer typeSearch, String description, Date fromDate, Date toDate,
			Integer accSearch) {
		EntityManager em = getEntityManager();
		return (List<TransactionModel>) em.createNamedQuery("TransactionModel.findByTypeDescDateAcc")
				.setParameter("transType", typeSearch).setParameter("transDesc", "%" + description + "%")
				.setParameter("fromDate", fromDate).setParameter("toDate", toDate).setParameter("transAcc", accSearch)
				.getResultList();
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
