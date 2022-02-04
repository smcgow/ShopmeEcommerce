package com.shopme.admin.category.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;

public class CategoryCsvExporter extends AbstractExporter<Category>{

	@Override
	public void export(List<Category> objects, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader("text/csv", ".csv","categories", response);
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] header = {"Id","Name"};
		String[] fieldMappings = {"id","name"};
		csvBeanWriter.writeHeader(header);
		objects.forEach(category -> {
			try {
				category.setName(category.getName().replace("--", "  "));
				csvBeanWriter.write(category, fieldMappings);
			} catch (IOException e) {
				throw new RuntimeException("Problem mapping csv row.",e);
			}
		});
		
		csvBeanWriter.close();
		
	}

	

}
