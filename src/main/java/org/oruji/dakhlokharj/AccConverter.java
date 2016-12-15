package org.oruji.dakhlokharj;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.oruji.java.util.DatePlus;
import org.oruji.java.util.DatePlus.DATE_FORMAT;

@FacesConverter("org.oruji.dakhlokharj.AccConverter")
public class AccConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return arg2;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		switch ((Integer) arg2) {
		case 1:
			return "آینده";
		case 2:
			return "ملت";
		default:
			break;
		}
		return new DatePlus().getPersian(DATE_FORMAT.YMDHM);
	}
}
