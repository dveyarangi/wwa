package eir.world.controllers;


public class ControllerFactory
{
	public static IController createController(final String type)
	{
		String className = "eir.world.controllers." + type;
		Class<?> ctrlClass;
		try {
			ctrlClass = Class.forName( className );
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Faction controller of type " + className + " not found.");
		}

		IController ctrl;
		try
		{
			ctrl = (IController) ctrlClass.newInstance();
		}
		catch( InstantiationException e ) {
			throw new IllegalArgumentException("Faction controller of type " + className + " not found.");
		}
		catch( IllegalAccessException e ) {
			throw new IllegalArgumentException("Faction controller of type " + className + " not found.");
		}

		return ctrl;
	}
}
