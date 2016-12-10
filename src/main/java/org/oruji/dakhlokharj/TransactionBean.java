package org.oruji.dakhlokharj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.joda.time.DateTime;

@ManagedBean
@SessionScoped
public class TransactionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String FILENAME = "C:\\Users\\oruji\\Desktop\\me\\dakhlokharj\\1395";

	private TransactionModel transaction;
	private List<TransactionModel> transList = null;
	private Integer typeSearch = 0;
	private Integer accSearch = 0;
	private String description = "";
	private Date fromDate = null;
	private Date toDate = null;
	private BigDecimal totalMoney;

	public String saveAction() {
		new TransactionDao().transCreate(getTransaction());

		transList = null;
		transaction = null;
		totalMoney = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String deleteAction(TransactionModel bean) {
		new TransactionDao().transDelete(bean);

		transList = null;
		transaction = null;
		totalMoney = null;

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

		return "index.xhtml?faces-redirect=true";
	}

	public String searchAction() {
		transList = null;
		totalMoney = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String exportAction() {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(format());

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		transList = null;
		totalMoney = null;

		return "index.xhtml?faces-redirect=true";
	}

	public String importAction() {
		if (getTransList().size() > 1)
			return "index.xhtml?faces-redirect=true";

		TransactionDao td = new TransactionDao();

		for (TransactionModel model : unFormat(readFile())) {
			td.transCreate(model);
		}

		transList = null;
		totalMoney = null;

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
			long mySum = 0;
			for (TransactionModel model : getTransList())
				mySum += model.getTransCur().longValue();
			totalMoney = new BigDecimal(mySum);
		}
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	private String readFile() {

		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;
			StringBuilder myStr = new StringBuilder();

			br = new BufferedReader(new FileReader(FILENAME));

			while ((sCurrentLine = br.readLine()) != null) {
				myStr.append(sCurrentLine + "\n");
			}

			return myStr.toString();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return "";
	}

	public List<TransactionModel> unFormat(String myStr) {
		List<TransactionModel> beanList = new ArrayList<TransactionModel>();
		try {

			String[] row = readFile().split("\n");

			for (int i = 0; i < row.length; i++) {
				TransactionModel model = new TransactionModel();
				String[] columns = row[i].split(";");
				model.setTransAcc(Integer.parseInt(columns[0]));
				model.setTransDate(Jalali.toGregorian(columns[1]).toDate());
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

		for (TransactionModel model : getTransList()) {
			myStr.append(model.getTransAcc());
			myStr.append(";");
			myStr.append(Jalali.toJalali(new DateTime(model.getTransDate())));
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
}