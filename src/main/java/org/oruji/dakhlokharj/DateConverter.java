package org.oruji.dakhlokharj;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.joda.time.DateTime;
import org.oruji.java.util.Jalali;

@FacesConverter("org.oruji.dakhlokharj.DateConverter")
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 == "" || arg2.isEmpty() || arg2 == null)
			return "";
		return Jalali.toGregorian(arg2).toDate();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return Jalali.toJalali(new DateTime(arg2));
	}
}
