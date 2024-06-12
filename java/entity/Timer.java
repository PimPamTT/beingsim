package entity;

public class Timer 
{
	int counter, max;
	boolean trigger;

	public Timer(int max) 
	{
		super();
		counter = 0;
		this.max = max;
		trigger = false;
	}
	
	public void update()
	{
		if(counter < max)
		{
			counter++;
		}
		else
		{
			counter = 0;
			trigger = true;
		}
	}
	
	public boolean isTriggered()
	{
		if(trigger)
		{
			trigger = false;
			return true; 
		}
		else
		{
			return false;
		}
	}
}