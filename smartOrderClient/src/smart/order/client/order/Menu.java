package smart.order.client.order;

public abstract class Menu
{	
	protected int id;
	protected String name = null;
	protected double price;
	
	public int getId()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public double getPrice()
	{
		return this.price;
	}
}
