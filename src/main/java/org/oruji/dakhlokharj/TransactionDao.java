package org.oruji.dakhlokharj;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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
	public List<TransactionModel> transRead(Map<String, Object> params) {
		EntityManager em = getEntityManager();

		StringBuilder myStr = new StringBuilder("SELECT t FROM TransactionModel t");

		Set<Entry<String, Object>> s = params.entrySet();
		Iterator<Entry<String, Object>> it = s.iterator();

		myStr.append(" WHERE");

		int paramNo = 0;
		while (it.hasNext()) {
			Map.Entry<String, Object> m = (Entry<String, Object>) it.next();

			if (m.getValue() instanceof String) {
				if ((String) m.getValue() == "")
					continue;

				myStr.append(" t.");
				myStr.append(m.getKey() + " LIKE " + ":" + m.getKey());

			} else if (m.getValue() instanceof Integer) {
				if ((Integer) m.getValue() == 0)
					continue;

				myStr.append(" t.");
				myStr.append(m.getKey() + "=" + ":" + m.getKey());

			} else if (m.getValue() instanceof List) {
				List<Date> dateList = new ArrayList<Date>();
				dateList = (List<Date>) m.getValue();

				if (dateList.get(0) == null)
					continue;

				myStr.append(" t.");
				myStr.append(m.getKey() + " BETWEEN" + " :fromDate AND :toDate");
			}

			myStr.append(" AND");
			paramNo++;
		}

		if (paramNo == 0)
			myStr.replace(myStr.length() - 5, myStr.length(), "");

		else
			myStr.replace(myStr.length() - 3, myStr.length(), "");

		myStr.append(" ORDER BY t.transDate DESC");

		Query q = em.createQuery(myStr.toString());

		s = params.entrySet();
		it = s.iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> m = (Map.Entry<String, Object>) it.next();

			if (m.getValue() instanceof String) {
				if ((String) m.getValue() == "")
					continue;

				q.setParameter((String) m.getKey(), "%" + (String) m.getValue() + "%");

			} else if (m.getValue() instanceof Integer) {
				if ((Integer) m.getValue() == 0)
					continue;

				q.setParameter((String) m.getKey(), (Integer) m.getValue());

			} else if (m.getValue() instanceof List) {
				List<Date> dateList = new ArrayList<Date>();
				dateList = (List<Date>) m.getValue();

				if (dateList.get(0) == null)
					continue;

				q.setParameter("fromDate", dateList.get(0));
				q.setParameter("toDate", dateList.get(1));
			}
		}

		return (List<TransactionModel>) q.getResultList();
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
