package com.mygdx.game.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Especies_Comportamientos")
public class EspeciesComportamientos
{
    @DatabaseField(foreign = true, columnName = "id_especie", foreignAutoRefresh = true)
    private Especies especie;

    @DatabaseField(foreign = true, columnName = "id_comportamiento", foreignAutoRefresh = true)
    private Comportamientos comportamiento;

    // Constructor vacío requerido por ORMlite
    public EspeciesComportamientos() {}

    // Getters y setters
    public Especies getEspecie() 
	{
		return especie;
	}

	public void setEspecie(Especies especie) 
	{
		this.especie = especie;
	}

	public Comportamientos getComportamiento()
	{
		return comportamiento;
	}

	public void setComportamiento(Comportamientos comportamiento) 
	{
		this.comportamiento = comportamiento;
	}
}