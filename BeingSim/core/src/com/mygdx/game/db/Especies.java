package com.mygdx.game.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Especies")
public class Especies
{
	@DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String nombre;

    @DatabaseField(foreign = true, columnName = "id_dieta", foreignAutoRefresh = true)
    private Dieta dieta;

    @DatabaseField(foreign = true, columnName = "id_habitat", foreignAutoRefresh = true)
    private Habitat habitat;

    // Constructor vacío requerido por ORMlite
    public Especies() {}

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

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Dieta getDieta() 
	{
		return dieta;
	}

	public void setDieta(Dieta dieta) 
	{
		this.dieta = dieta;
	}

	public Habitat getHabitat()
	{
		return habitat;
	}

	public void setHabitat(Habitat habitat) 
	{
		this.habitat = habitat;
	}   
}