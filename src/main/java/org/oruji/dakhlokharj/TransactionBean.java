package org.oruji.dakhlokharj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.joda.time.DateTime;
import org.oruji.java.util.Jalali;

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
	private BigDecimal totalDakhl;
	private BigDecimal totalKharj;

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

		if (typeSearch == 0 && description == "" && fromDate == null && accSearch == 0)
			setTransList(new TransactionDao().transRead());
		else if (typeSearch != 0 && description == "" && fromDate == null && accSearch == 0)
			setTransList(new TransactionDao().transRead(typeSearch));
		else if (typeSearch == 0 && description != "" && fromDate == null && accSearch == 0)
			setTransList(new TransactionDao().transRead(description));
		// date
		else if (typeSearch == 0 && description == "" && fromDate != null && accSearch == 0)
			setTransList(new TransactionDao().transRead(fromDate, toDate));
		// acc
		else if (typeSearch == 0 && description == "" && fromDate == null && accSearch != 0)
			setTransList(new TransactionDao().transReadAcc(accSearch));
		// typedesc
		else if (typeSearch != 0 && description != "" && fromDate == null && accSearch == 0)
			setTransList(new TransactionDao().transRead(typeSearch, description));
		// typeDate
		else if (typeSearch != 0 && description == "" && fromDate != null && accSearch == 0)
			setTransList(new TransactionDao().transRead(typeSearch, fromDate, toDate));
		// typeAcc
		else if (typeSearch != 0 && description == "" && fromDate == null && accSearch != 0)
			setTransList(new TransactionDao().transRead(typeSearch, accSearch));
		// descDate
		else if (typeSearch == 0 && description != "" && fromDate != null && accSearch == 0)
			setTransList(new TransactionDao().transRead(description, fromDate, toDate));
		// descAcc
		else if (typeSearch == 0 && description != "" && fromDate == null && accSearch != 0)
			setTransList(new TransactionDao().transReadAcc(description, accSearch));
		// dateAcc
		else if (typeSearch == 0 && description == "" && fromDate != null && accSearch != 0)
			setTransList(new TransactionDao().transReadAcc(fromDate, toDate, accSearch));
		// typeDescAcc
		else if (typeSearch != 0 && description != "" && fromDate == null && accSearch != 0)
			setTransList(new TransactionDao().transRead(typeSearch, description, accSearch));
		// typeAccDate
		else if (typeSearch != 0 && description == "" && fromDate != null && accSearch != 0)
			setTransList(new TransactionDao().transRead(typeSearch, accSearch, fromDate, toDate));
		// descAccDate
		else if (typeSearch == 0 && description != "" && fromDate != null && accSearch != 0)
			setTransList(new TransactionDao().transReadAcc(description, accSearch, fromDate, toDate));
		// typeDescDate
		else if (typeSearch != 0 && description != "" && fromDate != null && accSearch == 0)
			setTransList(new TransactionDao().transRead(typeSearch, description, fromDate, toDate));
		// typeDescDateAcc
		else if (typeSearch != 0 && description != "" && fromDate != null && accSearch != 0)
			setTransList(new TransactionDao().transRead(typeSearch, description, fromDate, toDate, accSearch));
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
		File file = new File(FILENAME);
		String content = format();

		try (FileOutputStream fop = new FileOutputStream(file)) {

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}

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

		for (TransactionModel model : unFormat(readFile())) {
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

	private String readFile() {

		StringBuilder myStr = new StringBuilder();

		try {
			File fileDir = new File(FILENAME);

			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));

			String str;
			while ((str = in.readLine()) != null) {
				myStr.append(str + "\n");
			}

			in.close();

		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());

		} catch (IOException e) {
			System.out.println(e.getMessage());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return myStr.toString();
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

	public void setTotalDakhl(BigDecimal totalDakhl) {
		this.totalDakhl = totalDakhl;
	}

	public void setTotalKharj(BigDecimal totalKharj) {
		this.totalKharj = totalKharj;
	}
}