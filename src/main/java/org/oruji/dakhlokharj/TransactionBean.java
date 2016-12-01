package org.oruji.dakhlokharj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class TransactionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String FILENAME = "C:\\Users\\oruji\\Desktop\\me\\dakhlokharj\\1395";

	private int id;
	private String date;
	private BigDecimal price;
	private String transNo;
	private String dest;
	private String description;
	private List<TransactionBean> transList = null;
	private boolean editable;

	public void save() {
		getTransList().add(this);
		writeCsv();
		transList = null;
	}

	public String deleteAction(TransactionBean bean) {
		getTransList().remove(bean.getId());
		writeCsv();
		transList = null;
		return null;
	}

	public String editAction(TransactionBean bean) {
		bean.setEditable(true);
		return null;
	}
	
	public String saveEdit(TransactionBean bean) {
		writeCsv();
		bean.setEditable(false);
		return null;
	}

	public List<TransactionBean> readAction() {
		List<TransactionBean> beanList = new ArrayList<TransactionBean>();
		try {

			String[] row = readFile().split(";");

			for (int i = 0; i < row.length; i++) {
				TransactionBean myBean = new TransactionBean();
				myBean.setId(i);
				String[] columns = row[i].split(",");
				myBean.setDate(columns[0]);
				myBean.setPrice(new BigDecimal(columns[1]));
				myBean.setTransNo(columns[2]);
				myBean.setDest(columns[3]);
				myBean.setDescription(columns[4]);
				beanList.add(myBean);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return beanList;
	}

	public void writeCsv() {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			StringBuilder myStr = new StringBuilder();

			for (TransactionBean bean : getTransList()) {
				myStr.append(bean.toString2());
			}

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(myStr.toString());

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
				myStr.append(sCurrentLine);
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getPrice() {
		if (price == null)
			price = new BigDecimal(0);
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TransactionBean> getTransList() {
		if (transList == null) {
			transList = readAction();
		}
		return transList;
	}

	public void setTransList(List<TransactionBean> transList) {
		this.transList = transList;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public String toString() {
		return id + "," + date + "," + price + "," + transNo + "," + dest + "," + description + ";";
	}

	public String toString2() {
		return date + "," + price + "," + transNo + "," + dest + "," + description + ";";
	}

}