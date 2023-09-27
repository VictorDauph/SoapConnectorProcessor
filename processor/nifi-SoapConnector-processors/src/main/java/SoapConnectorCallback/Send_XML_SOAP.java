package SoapConnectorCallback;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.io.StreamCallback;

import CustomUtils.StringUtils;
import models.XmlResponse;

public class Send_XML_SOAP implements StreamCallback {
	
	public String url;
	String xmlDeclaration;
	String xmlBody;
	ComponentLog logger;
	

	public Send_XML_SOAP(String url, String xmlDeclaration, String xmlBody,ComponentLog logger) {
		this.url = url;
		this.xmlDeclaration = xmlDeclaration;
		this.xmlBody = xmlBody;
		this.logger=logger;
	}

	@Override
	public void process(InputStream in, OutputStream out) throws IOException {
		
		logger.debug("Process Soap Connector Processor");
		Writer streamWriter = new OutputStreamWriter(out,"UTF-8");
		streamWriter.write(request().response);
		streamWriter.close();
	}
	
	public XmlResponse request() {
	
		try {
			 
			 URL obj = new URL(this.url);
			 HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 con.setRequestMethod("POST");
			 
			 //header
			 con.setRequestProperty("Content-Type","text/xml");
			 
			 //xml body			 
			 String xml = this.xmlDeclaration+ this.xmlBody;
			 
			 con.setDoOutput(true);
			 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			 wr.writeBytes(xml);
			 wr.flush();
			 wr.close();
			 String responseStatus = con.getResponseMessage();
			 System.out.println(responseStatus);
			 BufferedReader in = new BufferedReader(new InputStreamReader(
			 con.getInputStream()));
			 String inputLine;
			 StringBuffer response = new StringBuffer();
			 while ((inputLine = in.readLine()) != null) {
				 response.append(inputLine);
			 }
			 in.close();
			 
			 //method output
			 //System.out.println("response:" + response.toString());
			 return new XmlResponse(response.toString());
			
			 
		 } catch (Exception e) {
			 System.out.println(e);
			 return null;
		 }
	}

}
