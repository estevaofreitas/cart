package br.com.mateus.cart.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import io.quarkus.qute.TemplateExtension;

@TemplateExtension
public class ReportExtensions {

	static String currency(BigDecimal val) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.'); 
		DecimalFormat df = new DecimalFormat("#,###,###,##0.00", otherSymbols);
		
		return df.format(val);
	}
	
	static String weight(BigDecimal val) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.'); 
		DecimalFormat df = new DecimalFormat("#,###,###,##0.000", otherSymbols);
		
		return df.format(val);
	}
}