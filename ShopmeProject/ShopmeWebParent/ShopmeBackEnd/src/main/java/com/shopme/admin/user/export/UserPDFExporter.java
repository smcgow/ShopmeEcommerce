package com.shopme.admin.user.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.user.AbstractExporter;
import com.shopme.common.entity.User;

public class UserPDFExporter extends AbstractExporter {

	@Override
	public void export(List<User> users, HttpServletResponse response) throws IOException {
		
		this.setResponseHeader("application/pdf", ".pdf", response);

		Document document = new Document(PageSize.A4);
		PdfWriter pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(16);
		font.setColor(Color.BLUE);
		Paragraph paragraph = new Paragraph("List of Users", font );
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100F);
		table.setSpacingBefore(10);
		table.setWidths(new float[]{1.5F,3.5F,3.0F,3.0F,3.0F,1.5F});
		
		writeTableHeader(table);
		writeTableRows(users,table);
		
		document.open();
		document.add(paragraph);
		document.add(table);
		
		document.close();
		pdfWriter.close();
	}

	private void writeTableRows(List<User> users, PdfPTable table) {
		
		users.forEach(user -> {
			
			PdfPCell cell = new PdfPCell();
			cell.setPadding(5);
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			
			cell.setPhrase(new Phrase(user.getId().toString(), font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(user.getEmail(), font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(user.getFirstName(), font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(user.getLastName(), font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(user.getRoles().toString(), font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(user.isEnabled() ? "Yes" : "No", font));
			table.addCell(cell);
		});
	}

	private void writeTableHeader(PdfPTable table) {
		
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("User Id", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("First Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Last Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Roles", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
		
		
	}

}
