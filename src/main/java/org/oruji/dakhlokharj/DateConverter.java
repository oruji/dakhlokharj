package org.oruji.dakhlokharj;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("org.oruji.dakhlokharj.DateConverter")
public class DateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return Jalali.toGregorian(arg2).toDate();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		System.out.println(arg2);
		return null;
	}

}
