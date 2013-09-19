package xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import storage.JAXBHelper;

@XmlRootElement(name = "order")
public class OrderXml{

	final public static String ORDERS_PATH = "xml-orders/";

	@XmlAttribute(name = "orderNumber")
	public String orderNumber;

	@XmlElement(name = "item")
	public List<ItemXml> items = new ArrayList<ItemXml>();

	public boolean saveOrder()
	{
		return JAXBHelper.writeToXmlFile(this, ORDERS_PATH + orderNumber);
	}

	public static OrderXml loadOrder(String orderNumber)
	{
		return (OrderXml)JAXBHelper.readFromXmlFile(ORDERS_PATH + orderNumber,
				OrderXml.class);
	}
}
