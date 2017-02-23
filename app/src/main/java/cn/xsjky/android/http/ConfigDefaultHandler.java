package cn.xsjky.android.http;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * 配置文件xml解析器
 * @author Jerry
 *
 */
public class ConfigDefaultHandler extends DefaultHandler {
	private StringBuffer values = new StringBuffer();
	
	public ConfigDefaultHandler(){
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String chars = new String(ch, start, length).trim();
		values.append(chars);
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println("endElement:" + qName);
	}

	@Override
	public void startDocument() throws SAXException {
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		values.setLength(0);
		System.out.println(qName);
	}

}
