package items;

import java.util.Hashtable;
import java.util.List;

import enums.ProductionSteps;

public class Item{

	private String itemName;

	private int quantity;

	private List<ProductionSteps> productionSteps;

	private Hashtable<String, String> attributes;

	public String getItemName()
	{
		return itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public List<ProductionSteps> getProductionSteps()
	{
		return productionSteps;
	}

	public void setProductionSteps(List<ProductionSteps> productionSteps)
	{
		this.productionSteps = productionSteps;
	}

	public Hashtable<String, String> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(Hashtable<String, String> attributes)
	{
		this.attributes = attributes;
	}

}
