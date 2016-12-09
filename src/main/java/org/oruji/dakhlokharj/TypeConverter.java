package org.oruji.dakhlokharj;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.joda.time.DateTime;

@FacesConverter("org.oruji.dakhlokharj.TypeConverter")
public class TypeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return arg2;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		switch ((Integer) arg2) {
		case 1:
			return "قبض موبایل امین";
		case 2:
			return "قبض موبایل فاطمه";
		case 3:
			return "قبض برق";
		case 4:
			return "قبض گاز";
		case 5:
			return "قبض تلفن";
		case 6:
			return "قبض آب";
		case 7:
			return "شارژ ساختمان";
		case 8:
			return "دستی";
		case 9:
			return "خرید";
		case 10:
			return "غذا";
		case 11:
			return "میوه";
		case 12:
			return "هدیه";
		case 13:
			return "بیمه";			
		case 99:
			return "دیگر";
		default:
			break;
		}
		return Jalali.toJalali(new DateTime(arg2));
	}
}
