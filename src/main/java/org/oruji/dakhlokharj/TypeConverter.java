package org.oruji.dakhlokharj;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("org.oruji.dakhlokharj.TypeConverter")
public class TypeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return arg2;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		TransactionBean bean = new TransactionBean();
		Map<String, Integer> myMap = bean.getTransactionType();
		Set<Entry<String, Integer>> s = myMap.entrySet();
		Iterator<Entry<String, Integer>> it = s.iterator();

		while (it.hasNext()) {
			Map.Entry<String, Integer> m = (Entry<String, Integer>) it.next();

			if (m.getValue() == (Integer) arg2)
				return m.getKey();
		}

		return "";
	}
}