package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

public class UserExcelExporter extends AbstractExporter {
	
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Users");
	}
	
	public void writeHeaderLine() {
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row, cellStyle, "User ID", 0);
		createCell(row, cellStyle, "Email", 1);
		createCell(row, cellStyle, "First Name", 2);
		createCell(row, cellStyle, "Last Name", 3);
		createCell(row, cellStyle, "Roles", 4);
		createCell(row, cellStyle, "Enabled", 5);
	}
	
	private void writeDataLines(List<User> users) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		AtomicInteger rowNum = new AtomicInteger(0);
		users.forEach(user -> {
			XSSFRow row = sheet.createRow(rowNum.incrementAndGet());
			createCell(row, cellStyle, user.getId(), 0);
			createCell(row, cellStyle, user.getEmail(), 1);
			createCell(row, cellStyle, user.getFirstName(), 2);
			createCell(row, cellStyle, user.getLastName(), 3);
			createCell(row, cellStyle, user.getRoles().toString(), 4);
			createCell(row, cellStyle, user.isEnabled(), 5);
			
		});
	}
	
	
	

	private void createCell(XSSFRow row, XSSFCellStyle cellStyle, Object content, Integer columnIndex) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		if(content instanceof Boolean) {
			cell.setCellValue((Boolean)content);
		}else if(content instanceof Integer) {
			cell.setCellValue((Integer)content);
		}else {
			cell.setCellValue(content.toString());
		}
		cell.setCellStyle(cellStyle);
	}

	public void export(List<User> users, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader("application/octet-stream", ".xlsx", response);
		
		this.writeHeaderLine();
		this.writeDataLines(users);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
	}

	
}
