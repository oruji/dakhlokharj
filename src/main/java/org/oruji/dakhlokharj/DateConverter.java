package org.oruji.dakhlokharj;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.oruji.java.util.DatePlus;
import org.oruji.java.util.DatePlus.DATE_FORMAT;

@FacesConverter("org.oruji.dakhlokharj.DateConverter")
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 == "" || arg2.isEmpty() || arg2 == null)
			return "";
		DatePlus dp = new DatePlus(arg2);

		return dp.getDate();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		DatePlus dp = new DatePlus((Date) arg2);
		return dp.getPersian(DATE_FORMAT.YMDHM);
	}
}
