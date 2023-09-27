package models;

public class XmlResponse {
		public XmlResponse(String xmlString) {
			if(xmlString !=null) {
				this.response =xmlString;
			}else {
				this.response = "null";
			}
			
		}
	@Override
		public String toString() {
			return "XmlResponse [response=" + response + "]";
		}
	public String response;
}
