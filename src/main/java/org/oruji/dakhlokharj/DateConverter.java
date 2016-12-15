package org.oruji.dakhlokharj;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.oruji.java.util.DatePlus;

@FacesConverter("org.oruji.dakhlokharj.DateConverter")
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 == "" || arg2.isEmpty() || arg2 == null)
			return "";

		return new DatePlus(arg2).getDate();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return new DatePlus((Date) arg2).getPersian();
	}
}
