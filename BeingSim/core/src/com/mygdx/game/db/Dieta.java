package com.mygdx.game.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Dieta")

public class Dieta 
{
	@DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String nombre;

    // Constructor vacío requerido por ORMlite
    public Dieta() {}

    // Getters y setters    
	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getNombre() 
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
}