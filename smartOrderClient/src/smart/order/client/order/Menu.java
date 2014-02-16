package smart.order.client.order;

public abstract class Menu
{	
	protected int id;
	protected String name = null;
	protected double price;
	protected int quantity = 1;
	
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
	public int getQuantity()
	{
		return this.quantity;
	}
	public void increaseQuantity()
	{
		this.quantity++;
	}
}
