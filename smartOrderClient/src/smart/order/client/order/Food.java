package smart.order.client.order;

import java.util.Vector;

public class Food extends Menu
{
	private Vector<String> ingredients = new Vector<String>();
	
	public Food(int id, String name, double price)
	{
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public Vector<String> getIngredients()
	{
		return this.ingredients;
	}
	public void addIngredient(String ingredient)
	{
		this.ingredients.add(ingredient);
	}
	public void removeIngredient(int id)
	{
		this.ingredients.remove(id);
	}
}
