package CustomUtils;

public class StringUtils  {
	
	public static void main(String[] args) {
	String testString1="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
			  "<soap:Body>"+
			   "<NumberToWords xmlns=\"http://www.dataaccess.com/webservicesserver/\">"+
			     "<ubiNum>"+500+"</ubiNum>"+
			   "</NumberToWords>"+
			  "</soap:Body>"+
			"</soap:Envelope>";
	
	String testString2="https:\\\\www.dataaccess.com\\webservicesserver\\NumberConversion.wso";
		
	System.out.println(echapCaracteresSpeciaux(testString1));	
	System.out.println(echapCaracteresSpeciaux(testString2));	
	}
	
	public static String echapCaracteresSpeciaux(String stringToEchap) {
		
		String newString = "";
		String regexp ="(?=\\\\)|(?=\")";
		//String regexp ="(?=\")";
		
		System.out.println(stringToEchap);
		
		String[] stringArray= stringToEchap.split(regexp);
		
		
		for(int i=0;i<stringArray.length;i++) {
			System.out.println(i);
			String strring=stringArray[i];
			if(i>=1) {
				strring = "\\"+strring;
			}
			System.out.println(strring);
			newString = newString+strring;
		}	
		return newString;
	}
}
