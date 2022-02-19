package com.shopme.admin.category.export;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
	

	public void setResponHeader(HttpServletResponse response,String contentType,String extension) {
		
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timstamp=format.format(new Date());
		String fileName="category_"+timstamp+extension;
		
		response.setContentType(contentType);
		
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
	}
}
