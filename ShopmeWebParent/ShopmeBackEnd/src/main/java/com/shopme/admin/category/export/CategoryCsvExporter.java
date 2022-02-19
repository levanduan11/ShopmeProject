package com.shopme.admin.category.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Category;

public class CategoryCsvExporter extends AbstractExporter {

	public void export(List<Category>list,HttpServletResponse response) throws IOException {
		super.setResponHeader(response, "text/csv", ".csv");
		
		ICsvBeanWriter writer=new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[]header= {"ID","Name","Alias","Enabled"};
		String[]mapping= {"id","name","alias","enabled"};
		writer.writeHeader(header);
		for (Category category : list) {
			writer.write(category, mapping);
		}
		writer.close();
	}
}
