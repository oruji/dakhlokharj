package com.mkyong.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class HelloBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String FILENAME = "C:\\Users\\oruji\\Desktop\\me\\dakhlokharj\\1395";

	private String date;
	private String time;
	private String price;
	private String transNo;
	private String dest;
	private String description;
	private List<HelloBean> transList;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
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

	public List<HelloBean> getTransList() {
		if (transList == null) {
			transList = new ArrayList<HelloBean>();

			for (String row : readFile().split(";")) {
				String[] columns = row.split(",");
				HelloBean myBean = new HelloBean();
				myBean.setDate(columns[0]);
				myBean.setTime(columns[1]);
				myBean.setPrice(columns[2]);
				myBean.setTransNo(columns[3]);
				myBean.setDest(columns[4]);
				myBean.setDescription(columns[5]);
				transList.add(myBean);
			}
		}
		return transList;
	}

	public void setTransList(List<HelloBean> transList) {
		this.transList = transList;
	}

	public void save() {
		writeCsv();
		transList = null;
	}

	public void writeCsv() {

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			StringBuilder myStr = new StringBuilder();
			myStr.append(readFile());
			myStr.append(toString());

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(myStr.toString());

			System.out.println("Done");

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

	@Override
	public String toString() {
		return date + "," + time + "," + price + "," + transNo + "," + dest + "," + description + ";";
	}
}