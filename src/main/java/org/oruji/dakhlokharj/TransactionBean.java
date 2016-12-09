package org.oruji.dakhlokharj;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class TransactionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private TransactionModel transaction;
	private List<TransactionModel> transList = null;
	private Integer typeSearch = 0;
	private String description = "";
	private Date fromDate = null;
	private Date toDate = null;

	public String save() {
		new TransactionDao().transCreate(getTransaction());
		transList = null;
		transaction = null;
		return null;
	}

	public String deleteAction(TransactionModel bean) {
		new TransactionDao().transDelete(bean);
		transList = null;
		transaction = null;
		return null;
	}

	public String editAction(TransactionModel bean) {
		bean.setEditable(true);
		return null;
	}

	public String saveEdit(TransactionModel bean) {
		new TransactionDao().transUpdate(bean);
		bean.setEditable(false);
		transList = null;
		transaction = null;

		return null;
	}

	public String cancel(TransactionModel bean) {
		bean.setEditable(false);
		transList = null;
		transaction = null;
		return null;
	}

	public void readAction() {
		if (description.isEmpty() || description == null)
			description = "";
		
		if(fromDate == null || toDate == null) {
			fromDate = null;
			toDate = null;
		}
			
			
		if (typeSearch == 0 && description == "" && fromDate == null)
			setTransList(new TransactionDao().transRead());
		else if (typeSearch != 0 && description != "" && fromDate != null)
			setTransList(new TransactionDao().transRead(typeSearch, description, fromDate, toDate));
		else if (typeSearch != 0 && description == "" && fromDate == null)
			setTransList(new TransactionDao().transRead(typeSearch));
		else if (typeSearch != 0 && description != "" && fromDate == null)
			setTransList(new TransactionDao().transRead(typeSearch, description));
		else if (typeSearch == 0 && description != "" && fromDate == null)
			setTransList(new TransactionDao().transRead(description));
		else if (typeSearch == 0 && description == "" && fromDate != null)
			setTransList(new TransactionDao().transRead(fromDate, toDate));
		else if (typeSearch != 0 && description == "" && fromDate != null)
			setTransList(new TransactionDao().transRead(typeSearch, fromDate, toDate));
		else if (typeSearch == 0 && description != "" && fromDate != null)
			setTransList(new TransactionDao().transRead(description, fromDate, toDate));
		
		
	}

	public String searchAction() {
		transList = null;
		return null;
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

	public Integer getTypeSearch() {
		return typeSearch;
	}

	public void setTypeSearch(Integer typeSearch) {
		this.typeSearch = typeSearch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}