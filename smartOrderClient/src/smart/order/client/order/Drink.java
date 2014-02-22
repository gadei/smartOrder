package smart.order.client.order;

public class Drink extends Menu
{
	public Drink(int id, String name, double price)
	{
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public Drink(Drink drink)
	{
		this(drink.getId(), drink.getName(), drink.getPrice());
	}
}
