package org.oruji.dakhlokharj;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.joda.time.DateTime;
import org.oruji.java.util.Jalali;

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
		return Jalali.toJalali(new DateTime(arg2));
	}
}
