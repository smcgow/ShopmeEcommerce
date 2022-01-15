package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.shopme.common.entity.User;

public abstract class AbstractExporter {

	public void setResponseHeader(String contentType, String extension, HttpServletResponse response) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp = dateFormat.format(new Date());
		String fileName = "users_" + timeStamp + extension;
		
		response.setContentType(contentType);
		String headerName = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerName, headerValue);
		
	}
	
	public abstract void export(List<User> users, HttpServletResponse response) throws IOException;
}
