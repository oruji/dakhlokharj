package org.oruji.dakhlokharj;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "TransactionModel.findAll", query = "SELECT t from TransactionModel t") })
public class TransactionModel implements Serializable {
	private static final long serialVersionUID = -4819996652263837857L;
	@Id
	@SequenceGenerator(name = "transactionmodel_id_seq", sequenceName = "transactionmodel_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionmodel_id_seq")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date transDate;
	private BigDecimal transCur;
	private String transTo;
	private String payNo;
	private String transNo;
	private Integer transType;
	private String transDesc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public BigDecimal getTransCur() {
		return transCur;
	}

	public void setTransCur(BigDecimal transCur) {
		this.transCur = transCur;
	}

	public String getTransTo() {
		return transTo;
	}

	public void setTransTo(String transTo) {
		this.transTo = transTo;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public String getTransDesc() {
		return transDesc;
	}

	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}

	@Override
	public String toString() {
		return transCur.toString();
	}
}
