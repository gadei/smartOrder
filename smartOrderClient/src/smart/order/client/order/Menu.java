package smart.order.client.order;

public abstract class Menu
{	
	protected int id;
	protected String name = null;
	protected float price;
	
	public int getId()
	{
		return this.id;
	}
	public String getName()
	{
		return name;
	}
	public float getPrice()
	{
		return price;
	}
}
