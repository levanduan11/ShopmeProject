package com.shopme.admin.brand.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Brand;

public class BrandCsvExporter extends AbstractExporter {
	
	public void export(HttpServletResponse response,List<Brand>brands) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv");
		
		ICsvBeanWriter writer=new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String header[]= {"ID","Name","Categories"};
		String mapping[]= {"id","name","categories"};
		writer.writeHeader(header);
		for (Brand b  : brands) {
			writer.write(b, mapping);
		}
		writer.close();
	}

}
