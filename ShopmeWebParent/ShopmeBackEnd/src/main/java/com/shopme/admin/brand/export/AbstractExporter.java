package com.shopme.admin.brand.export;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
	
	public void setResponseHeader(HttpServletResponse response,String type,String extension) {
		
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String date=format.format(new Date());
		String fileName="Brand_"+date+extension;
		response.setContentType(type);
		
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
		
	}
	

}
