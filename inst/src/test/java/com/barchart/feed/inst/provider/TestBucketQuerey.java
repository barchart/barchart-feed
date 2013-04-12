package com.barchart.feed.inst.provider;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestBucketQuerey {

	private static final String URL = "https://s3.amazonaws.com/instrument-def/";
	
	public static void main(final String[] args) throws Exception {
		
		final URL url = new URL(URL);
		
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Element element = db.parse(URL).getDocumentElement();
		NodeList nodeList = element.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++) {
			
			if(nodeList.item(i).getNodeName().equals("Contents")) {
				Element ct = (Element) nodeList.item(i);
				NodeList contentNodes = ct.getChildNodes();
				
				for(int n = 0; n < contentNodes.getLength(); n++) {
					
					System.out.println(contentNodes.item(n).getNodeName() + " " +
							contentNodes.item(n).getTextContent());
					
				}
				
			}
		}
		
	}
	
}
