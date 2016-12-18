package org.oruji.dakhlokharj;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.oruji.java.util.DatePlus;
import org.oruji.java.util.DatePlus.DATE_FORMAT;
import org.oruji.java.util.FileIO;

@ManagedBean
@SessionScoped
public class TransactionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String FILENAME = "C:\\Users\\oruji\\Desktop\\me\\dakhlokharj\\1395";

	private TransactionModel transaction;
	private List<TransactionModel> transList = null;
	private Integer typeSearch = 0;
	private Integer accSearch = 0;
	private Integer transTypeSearch = 0;
	private String description = "";
	private Date fromDate = null;
	private Date toDate = null;
	private BigDecimal totalMoney;
	private BigDecimal totalDakhl;
	private BigDecimal totalKharj;

	private HashMap<String, Integer> transactionType;

	public String saveAction() {
		new TransactionDao().transCreate(getTransaction());

		transList = null;
		transaction = null;
		totalMoney = null;
		totalDakhl = null;
		totalKharj = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String deleteAction(TransactionModel bean) {
		new TransactionDao().transDelete(bean);

		transList = null;
		transaction = null;
		totalMoney = null;
		totalDakhl = null;
		totalKharj = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String editAction(TransactionModel bean) {
		bean.setEditable(true);

		return "index.xhtml?faces-redirect=true";
	}

	public String saveEditAction(TransactionModel bean) {
		new TransactionDao().transUpdate(bean);
		bean.setEditable(false);

		transList = null;
		transaction = null;
		totalMoney = null;
		totalDakhl = null;
		totalKharj = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String cancelAction(TransactionModel bean) {
		bean.setEditable(false);
		transList = null;
		transaction = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String readAction() {
		if (description.isEmpty() || description == null)
			description = "";

		if (fromDate == null || toDate == null) {
			fromDate = null;
			toDate = null;
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("transAcc", accSearch);
		params.put("transType", typeSearch);
		params.put("transDesc", description);

		List<Date> dateList = new ArrayList<Date>();
		dateList.add(fromDate);
		dateList.add(toDate);

		params.put("transDate", dateList);

		setTransList(new TransactionDao().transRead(params));

		// transType
		if (transTypeSearch != 0) {
			List<TransactionModel> newList = new ArrayList<TransactionModel>();

			switch (transTypeSearch) {
			case 1:
				for (int i = 0; i < transList.size(); i++)
					if (transList.get(i).getTransCur().longValue() > 0)
						newList.add(transList.get(i));
				break;
			case 2:
				for (int i = 0; i < transList.size(); i++)
					if (transList.get(i).getTransCur().longValue() < 0)
						newList.add(transList.get(i));
				break;
			}
			transList = newList;
		}

		return "index.xhtml?faces-redirect=true";
	}

	public String searchAction() {
		transList = null;
		totalMoney = null;
		totalDakhl = null;
		totalKharj = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String exportAction() {
		String content = format();
		FileIO.writeFile(FILENAME, content);

		transList = null;
		totalMoney = null;
		totalDakhl = null;
		totalKharj = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String importAction() {
		if (getTransList().size() > 1)
			return "index.xhtml?faces-redirect=true";

		TransactionDao td = new TransactionDao();

		for (TransactionModel model : unFormat(FileIO.readFile(FILENAME))) {
			td.transCreate(model);
		}

		transList = null;
		totalMoney = null;
		totalDakhl = null;
		totalKharj = null;

		return "index.xhtml?faces-redirect=true";
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

	public BigDecimal getTotalMoney() {
		if (totalMoney == null) {
			totalMoney = getTotalDakhl().add(getTotalKharj());
		}
		return totalMoney;
	}

	public BigDecimal getTotalDakhl() {
		if (totalDakhl == null) {
			long mySum = 0;
			for (TransactionModel model : getTransList()) {
				if (model.getTransCur().longValue() > 0)
					mySum += model.getTransCur().longValue();
			}
			totalDakhl = new BigDecimal(mySum);
		}
		return totalDakhl;
	}

	public BigDecimal getTotalKharj() {
		if (totalKharj == null) {
			long mySum = 0;
			for (TransactionModel model : getTransList()) {
				if (model.getTransCur().longValue() < 0)
					mySum += model.getTransCur().longValue();
			}
			totalKharj = new BigDecimal(mySum);
		}
		return totalKharj;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<TransactionModel> unFormat(String myStr) {
		List<TransactionModel> beanList = new ArrayList<TransactionModel>();
		try {

			String[] row = FileIO.readFile(FILENAME).split("\n");

			for (int i = 0; i < row.length; i++) {
				TransactionModel model = new TransactionModel();
				String[] columns = row[i].split(";");
				model.setTransAcc(Integer.parseInt(columns[0]));
				DatePlus dp = new DatePlus(columns[1]);
				model.setTransDate(dp.getDate());
				model.setTransCur(new BigDecimal(columns[2]));
				model.setTransType(Integer.parseInt(columns[3]));
				model.setTransDesc(columns[4].replaceAll("\\{col\\}", ";"));
				model.setPayNo(columns[5].replaceAll("\\{col\\}", ";"));
				model.setTransNo(columns[6].replaceAll("\\{col\\}", ";"));
				model.setTransTo(columns[7].replaceAll("\\{col\\}", ";"));

				beanList.add(model);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return beanList;
	}

	public String format() {
		StringBuilder myStr = new StringBuilder();

		for (TransactionModel model : new TransactionDao().transRead()) {
			myStr.append(model.getTransAcc());
			myStr.append(";");
			myStr.append(new DatePlus(model.getTransDate()).getPersian());
			myStr.append(";");
			myStr.append(model.getTransCur());
			myStr.append(";");
			myStr.append(model.getTransType());
			myStr.append(";");
			myStr.append(model.getTransDesc().replaceAll(";", "{col}"));
			myStr.append(";");
			myStr.append(model.getPayNo().replaceAll(";", "{col}"));
			myStr.append(";");
			myStr.append(model.getTransNo().replaceAll(";", "{col}"));
			myStr.append(";");
			myStr.append(model.getTransTo().replaceAll(";", "{col}"));
			myStr.append("\n");
		}

		return myStr.toString();
	}

	public Integer getAccSearch() {
		return accSearch;
	}

	public void setAccSearch(Integer accSearch) {
		this.accSearch = accSearch;
	}

	public Integer getTransTypeSearch() {
		return transTypeSearch;
	}

	public void setTransTypeSearch(Integer transTypeSearch) {
		this.transTypeSearch = transTypeSearch;
	}

	public void setTotalDakhl(BigDecimal totalDakhl) {
		this.totalDakhl = totalDakhl;
	}

	public void setTotalKharj(BigDecimal totalKharj) {
		this.totalKharj = totalKharj;
	}

	public HashMap<String, Integer> getTransactionType() {
		if (transactionType == null) {
			transactionType = new HashMap<String, Integer>();
			transactionType.put("", 0);			
			transactionType.put("قبض موبایل امین", 1);
			transactionType.put("قبض موبایل فاطمه", 2);
			transactionType.put("قبض برق", 3);
			transactionType.put("قبض گاز", 4);
			transactionType.put("قبض تلفن", 5);
			transactionType.put("قبض آب", 6);
			transactionType.put("شارژ ساختمان", 7);
			transactionType.put("دستی", 8);
			transactionType.put("خرید", 9);
			transactionType.put("غذا", 10);
			transactionType.put("میوه", 11);
			transactionType.put("هدیه", 12);
			transactionType.put("بیمه", 13);
			transactionType.put("سود", 14);
			transactionType.put("حقوق", 15);
			transactionType.put("یارانه", 16);
			transactionType.put("دکتر", 17);
			transactionType.put("دیگر", 99);
		}
		return transactionType;
	}

	public void setTransactionType(HashMap<String, Integer> transactionType) {
		this.transactionType = transactionType;
	}

	public String getTodayDate() {
		DatePlus dp = new DatePlus();
		return dp.getPersian(DATE_FORMAT.YMmDd);
	}
}