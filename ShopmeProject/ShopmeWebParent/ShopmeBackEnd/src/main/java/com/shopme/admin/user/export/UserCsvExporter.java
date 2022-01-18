package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.user.AbstractExporter;
import com.shopme.common.entity.User;

public class UserCsvExporter extends AbstractExporter {
	
	public void export(List<User> users, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader("text/csv", ".csv", response);
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] header = {"User Id","Email","First Name","Last Name","Roles","Enabled"};
		String[] fieldMappings = {"id","email","firstName","lastName","roles","enabled"};
		csvBeanWriter.writeHeader(header);
		users.forEach(user -> {
			try {
				csvBeanWriter.write(user, fieldMappings);
			} catch (IOException e) {
				throw new RuntimeException("Problem mapping csv row.",e);
			}
		});
		
		csvBeanWriter.close();
	}

}
