package main;

import java.util.ArrayList;

public class Form 
{
	public ArrayList<String> behaviors = new ArrayList<>();
	public int count = 0;
	public String diet = "carnivore";
	
	public void addBehavior(String behavior)
	{
		if(count < 3 && !behaviors.contains(behavior))
		{
			behaviors.add(behavior);
			count++;
			System.out.println("Contador = " + count + " y se ha añadido " + behavior);
		}
		else if(count > 0 && behaviors.contains(behavior))
		{
			behaviors.remove(behavior);
			count--;
			System.out.println("Contador = " + count + " y se ha eliminado " + behavior);
		}		
	}
	
	public void addDiet(String diet)
	{
		if(this.diet.equals("carnivore") && diet.equals("carnivore"))
		{
			this.diet = "herbivore";
			System.out.println("Cambiada la Dieta a " + this.diet);
		}
		else if(this.diet.equals("carnivore") && diet.equals("herbivore"))
		{
			this.diet = "herbivore";
			System.out.println("Cambiada la Dieta a " + this.diet);
		}
		else if(this.diet.equals("herbivore") && diet.equals("carnivore"))
		{
			this.diet = "carnivore";
			System.out.println("Cambiada la Dieta a " + this.diet);
		}
		else if(this.diet.equals("herbivore") && diet.equals("herbivore"))
		{
			this.diet = "carnivore";
			System.out.println("Cambiada la Dieta a " + this.diet);
		}
		
	}
	
	@Override
	public String toString() 
	{
		String resultado;
		resultado = "Contador " + count + " - Behaviors: |";
		
		for(String beh : behaviors)
		{
			resultado += beh + "|";
		}
		
		resultado += " Diet: " + diet;
		return resultado;
	}
}
