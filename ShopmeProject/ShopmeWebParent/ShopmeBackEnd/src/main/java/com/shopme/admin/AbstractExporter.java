package com.shopme.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


public abstract class AbstractExporter<T> {

	public void setResponseHeader(String contentType, String extension, String filePrefix, HttpServletResponse response) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp = dateFormat.format(new Date());
		String fileName = filePrefix + "_" + timeStamp + extension;
		
		response.setContentType(contentType);
		String headerName = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerName, headerValue);
		
	}
	
	public abstract void export(List<T> objects, HttpServletResponse response) throws IOException;
}
