package org.oruji.dakhlokharj;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class TransactionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private TransactionModel transaction;
	private List<TransactionModel> transList = null;
	private boolean editable;
	private String date;

	public void save() {
		new TransactionDao().transCreate(getTransaction());
		transList = null;
	}

	public String deleteAction(TransactionModel bean) {

		transList = null;
		return null;
	}

	public String editAction(TransactionModel bean) {
		return null;
	}

	public String saveEdit(TransactionModel bean) {
		return null;
	}

	public void readAction() {
		setTransList(new TransactionDao().transRead());
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public TransactionModel getTransaction() {
		if (transaction == null)
			transaction = new TransactionModel();
		return transaction;
	}

	public void setTransaction(TransactionModel transaction) {
		this.transaction = transaction;
	}

	public List<TransactionModel> getTransList() {
		if (transList == null)
			readAction();
		return transList;
	}

	public void setTransList(List<TransactionModel> transList) {
		this.transList = transList;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}