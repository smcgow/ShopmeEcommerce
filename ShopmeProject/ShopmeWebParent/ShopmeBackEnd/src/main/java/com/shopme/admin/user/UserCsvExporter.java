package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

public class UserCsvExporter {
	
	public void export(List<User> users, HttpServletResponse response) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp = dateFormat.format(new Date());
		String fileName = "users_" + timeStamp + ".csv";
		
		response.setContentType("text/csv");
		String headerName = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerName, headerValue);
		
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
