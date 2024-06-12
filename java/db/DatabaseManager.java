package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager
{
    private Connection connection;
    
    public DatabaseManager(String url) throws SQLException
    {
        this.connection = DriverManager.getConnection(url);
    }

    public void createTables() throws SQLException 
    {
        try (Statement statement = connection.createStatement())
        {
            String createDietaTable = "CREATE TABLE IF NOT EXISTS Diet (\n"
                    + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "    name VARCHAR(25) NOT NULL\n"
                    + ");";
            statement.execute(createDietaTable);
            
            String createComportamientosTable = "CREATE TABLE IF NOT EXISTS Behaviors (\n"
                    + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "    name VARCHAR(25) NOT NULL\n"
                    + ");";
            statement.execute(createComportamientosTable);

            String createEspeciesTable = "CREATE TABLE IF NOT EXISTS Species (\n"
                    + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "    name VARCHAR(25) NOT NULL,\n"
                    + "    id_diet INTEGER NOT NULL,\n"
                    + "    FOREIGN KEY (id_diet) REFERENCES Diet(id)\n"
                    + ");";
            statement.execute(createEspeciesTable);

            String createEspeciesComportamientosTable = "CREATE TABLE IF NOT EXISTS Species_Behaviors (\n"
                    + "    id_species INTEGER NOT NULL,\n"
                    + "    id_behaviors INTEGER NOT NULL,\n"
                    + "    PRIMARY KEY (id_species, id_behaviors),\n"
                    + "    FOREIGN KEY (id_species) REFERENCES Species(id),\n"
                    + "    FOREIGN KEY (id_behaviors) REFERENCES Behaviors(id)\n"
                    + ");";
            statement.execute(createEspeciesComportamientosTable);
        }
    }

    public void insert(String table, String[] columns, Object[] values) throws SQLException 
    {
        String sql = "INSERT INTO " + table + " (" + String.join(", ", columns) + ") VALUES (";
        
        for (int i = 0; i < columns.length; i++)
        {
            sql += (i == columns.length - 1) ? "?" : "?, ";
        }
        
        sql += ")";
        
        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            for (int i = 0; i < values.length; i++) 
            {
                statement.setObject(i + 1, values[i]);
            }
            
            statement.executeUpdate();
            System.out.println("Registro insertado correctamente en la tabla " + table + ".");
        }
    }

    public void insertSpecies(String name, int idDiet)
    {
        String table = "Species";
        String[] columns = {"name", "id_diet"};
        Object[] values = {name, idDiet};

        try 
        {
            insert(table, columns, values);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void insertSpeciesBehaviors(int idSpecies, int idBehaviors)
    {
        String table = "Species_Behaviors";
        String[] columns = {"id_species", "id_behaviors"};
        Object[] values = {idSpecies, idBehaviors};

        try 
        {
            insert(table, columns, values);
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void select(String sql) throws SQLException 
    {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) 
        {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            
            while (resultSet.next()) 
            {
                for (int i = 1; i <= columnsNumber; i++) 
                {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                
                System.out.println("");
            }
        }
    }

    public void update(String sql) throws SQLException 
    {
        try (Statement statement = connection.createStatement())
        {
            int rowsUpdated = statement.executeUpdate(sql);
            
            if (rowsUpdated > 0)
            {
                System.out.println("Registro actualizado correctamente.");
            }
            else
            {
                System.out.println("No se encontró ninguna fila para actualizar.");
            }
        }
    }

    public void delete(String sql) throws SQLException 
    {
        try (Statement statement = connection.createStatement())
        {
            int rowsDeleted = statement.executeUpdate(sql);
            
            if (rowsDeleted > 0) 
            {
                System.out.println("Registro eliminado correctamente.");
            } 
            else
            {
                System.out.println("No se encontró ninguna fila para eliminar.");
            }
        }
    }
    
    public Integer getBehaviorByName(String name) throws SQLException 
    {
        String sql = "SELECT id FROM Behaviors WHERE name = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setString(1, name);
            
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                if (resultSet.next())
                {
                    return resultSet.getInt("id");
                }
            }
        }
        
        return null;
    }
    
    public Integer getDietByName(String name) throws SQLException
    {
        String sql = "SELECT id FROM Diet WHERE name = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, name);
            
            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next()) 
                {
                    return resultSet.getInt("id");
                }
            }
        }
        
        return null;
    }
    
    public String[] getBehaviorsBySpeciesId(int id)
    {
        String query = "SELECT b.name " +
                       "FROM Species_Behaviors sb " +
                       "JOIN Behaviors b ON sb.id_behaviors = b.id " +
                       "WHERE sb.id_species = ?";

        List<String> behaviorsList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) 
        {
        	statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                behaviorsList.add(resultSet.getString("name"));
            }
        } 
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return behaviorsList.toArray(new String[0]);
    }
    
    public String getDietById(int id) throws SQLException 
    {
        String sql = "SELECT name FROM Diet WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                if (resultSet.next()) 
                {
                    return resultSet.getString("name");
                }
            }
        }
        
        return null;
    }
    
    public List<String> selectColumn(String tableName, String columnName) throws SQLException
    {
        List<String> columnValues = new ArrayList<>();
        String sql = "SELECT " + columnName + " FROM " + tableName;
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) 
        {
            while (resultSet.next()) 
            {
                String columnValue = resultSet.getString(columnName);
                columnValues.add(columnValue);
            }
        }
        
        return columnValues;
    }

    public void closeConnection() throws SQLException 
    {
        if (connection != null) 
        {
            connection.close();
        }
    }
}