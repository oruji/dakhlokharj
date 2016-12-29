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
	private Integer exactDate = 1;
	private Date monthly;
	private Date daily;

	private BigDecimal totalAyande;
	private BigDecimal totalMellat;
	private BigDecimal totalRefah;

	private HashMap<String, Integer> transactionType;
	private HashMap<String, Integer> bankType;

	public String saveAction() {
		Integer tType = new Integer(getTransaction().getTransType());
		if (tType == 1 || tType == 3 || tType == 8 || tType == 9 || tType == 12 || tType == 13 || tType == 18
				|| tType == 19 || tType == 20 || tType == 22) {

			if (getTransaction().getTransCur().compareTo(BigDecimal.ZERO) > 0)
				getTransaction().setTransCur(getTransaction().getTransCur().negate());

		} else
			getTransaction().setTransCur(getTransaction().getTransCur().abs());

		new TransactionDao().transCreate(getTransaction());

		refreshData();

		return "index.xhtml?faces-redirect=true";
	}

	public String changeAction() {
		toDate = null;
		fromDate = null;
		monthly = null;
		daily = null;

		if (exactDate == 3)
			exactDate = 1;

		else
			exactDate++;

		return "index.xhtml?faces-redirect=true";
	}

	public String deleteAction(TransactionModel bean) {
		new TransactionDao().transDelete(bean);

		refreshData();

		return "index.xhtml?faces-redirect=true";
	}

	public String editAction(TransactionModel bean) {
		bean.setEditable(true);

		return "index.xhtml?faces-redirect=true";
	}

	public String copyAction(TransactionModel bean) {
		// TransactionModel tm = new TransactionModel();
		bean.setId(null);
		setTransaction(bean);

		return "index.xhtml?faces-redirect=true";
	}

	public String saveEditAction(TransactionModel bean) {
		new TransactionDao().transUpdate(bean);
		bean.setEditable(false);

		refreshData();

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

		if (monthly != null) {
			DatePlus dp = new DatePlus(monthly);
			fromDate = dp.getFirstDay();
			toDate = dp.getLastDay();

		} else if (daily != null) {
			DatePlus dp = new DatePlus(daily);
			fromDate = dp.getFirstHour();
			toDate = dp.getLastHour();

		} else if (fromDate == null || toDate == null) {
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
					if (transList.get(i).getTransCur().longValue() > 0 && transList.get(i).getTransType() != 11)
						newList.add(transList.get(i));
				break;
			case 2:
				for (int i = 0; i < transList.size(); i++)
					if (transList.get(i).getTransCur().longValue() < 0 && transList.get(i).getTransType() != 20)
						newList.add(transList.get(i));
				break;
			}
			transList = newList;
		}

		if (monthly != null || daily != null) {
			fromDate = null;
			toDate = null;
		}

		return "index.xhtml?faces-redirect=true";
	}

	public String searchAction() {
		refreshData();

		return "index.xhtml?faces-redirect=true";
	}

	public String exportAction() {
		String content = format();
		FileIO.writeFile(FILENAME, content);

		refreshData();

		return "index.xhtml?faces-redirect=true";
	}

	public String importAction() {
		if (getTransList().size() > 1)
			return "index.xhtml?faces-redirect=true";

		TransactionDao td = new TransactionDao();

		for (TransactionModel model : unFormat(FileIO.readFile(FILENAME))) {
			td.transCreate(model);
		}

		refreshData();

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
				if (model.getTransCur().longValue() > 0 && model.getTransType() != 11)
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
				if (model.getTransCur().longValue() < 0 && model.getTransType() != 20)
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
			transactionType.put("خدمات", 1);
			transactionType.put("وام", 2);
			transactionType.put("بدهی", 3);
			transactionType.put("طلب", 4);
			transactionType.put("قرض", 5);
			transactionType.put("هدیه گرفتن", 6);
			transactionType.put("دستی", 8);
			transactionType.put("خرید", 9);
			transactionType.put("فروش", 10);
			transactionType.put("واریز", 11);
			transactionType.put("هدیه دادن", 12);
			transactionType.put("بیمه", 13);
			transactionType.put("سود", 14);
			transactionType.put("حقوق", 15);
			transactionType.put("یارانه", 16);
			transactionType.put("قسط", 18);
			transactionType.put("قبض", 19);
			transactionType.put("برداشت", 20);
			transactionType.put("کسری", 21);
			transactionType.put("افزونگی", 22);
			transactionType.put("دیگر", 99);
		}

		return transactionType;
	}

	public void setTransactionType(HashMap<String, Integer> transactionType) {
		this.transactionType = transactionType;
	}

	public HashMap<String, Integer> getBankType() {
		if (bankType == null) {
			bankType = new HashMap<String, Integer>();
			bankType.put("", 0);
			bankType.put("آینده", 1);
			bankType.put("ملت", 2);
			bankType.put("رفاه", 3);
		}

		return bankType;
	}

	public void setBankType(HashMap<String, Integer> bankType) {
		this.bankType = bankType;
	}

	public String getTodayDate() {
		return new DatePlus().getPersian(DATE_FORMAT.YMmDd);
	}

	public Integer getExactDate() {
		return exactDate;
	}

	public void setExactDate(Integer exactDate) {
		this.exactDate = exactDate;
	}

	public Date getMonthly() {
		return monthly;
	}

	public void setMonthly(Date monthly) {
		this.monthly = monthly;
	}

	public Date getDaily() {
		return daily;
	}

	public void setDaily(Date daily) {
		this.daily = daily;
	}

	public BigDecimal getTotalAyande() {
		if (totalAyande == null)
			totalAyande = new TransactionDao().getTotalAcc(1);

		return totalAyande;
	}

	public void setTotalAyande(BigDecimal totalAyande) {
		this.totalAyande = totalAyande;
	}

	public BigDecimal getTotalMellat() {
		if (totalMellat == null)
			totalMellat = new TransactionDao().getTotalAcc(2);

		return totalMellat;
	}

	public void setTotalMellat(BigDecimal totalMellat) {
		this.totalMellat = totalMellat;
	}

	public BigDecimal getTotalRefah() {
		if (totalRefah == null)
			totalRefah = new TransactionDao().getTotalAcc(3);

		return totalRefah;
	}

	public void setTotalRefah(BigDecimal totalRefah) {
		this.totalRefah = totalRefah;
	}

	private void refreshData() {
		transList = null;
		transaction = null;
		totalMoney = null;
		totalDakhl = null;
		totalKharj = null;
		totalAyande = null;
		totalMellat = null;
		totalRefah = null;
	}
}