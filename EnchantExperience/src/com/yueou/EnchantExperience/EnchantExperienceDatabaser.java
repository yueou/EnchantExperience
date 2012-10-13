package com.yueou.EnchantExperience;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

public class EnchantExperienceDatabaser {

    private static Connection conn = null;     
    private static String host;     
    private static String port;    
    private static String db;     
    private static String dbuser;   
    private static String dbpw;
    private static String prefix;
    private static EnchantExperience plugin;
    
    public EnchantExperienceDatabaser(String phost,String database,String puser,String ppw,String pport,String pprefix,EnchantExperience pplugin){

    	host = phost;
    	dbuser = puser;
    	db = database;
    	dbpw = ppw;
    	port =pport;
    	prefix = pprefix;
    	
    	
    	plugin = pplugin;
    	 	
        try{
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.setLoginTimeout(3);
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?" + "user=" + 
                           dbuser+ "&" + "password=" + dbpw);
        }
	    catch(ClassNotFoundException e)
	    {
	            System.out.println("[MineInvnetory]: 没有找到数据库");
	            plugin.onDisable();
	    }
	    catch(SQLException e)
	    {
	            System.out.println("[MineInventory]: 连接数据库发生错误,请检查你的配置文件");
	            plugin.onDisable();
	    }
	    createTable();
	    //hold connection to db open through a repeating sql task
	    plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {public void run(){checkConnection();holdConnection();}}, 60, 72000);
	
    }
    public void createTable(){
    	
        if(conn != null)
        {
                checkConnection();
                try{
                        
                       	Statement query = conn.createStatement();
                        String sql = "Show Tables like '" + prefix + "Enchanter';";
                        ResultSet result = query.executeQuery(sql);
                        if(result.next())
                        {
                                return;
                        }
                        else {
                                String sqlcre = "Create Table " + prefix + "Enchanter (ID Integer Primary Key NOT NULL AUTO_INCREMENT, " +
                                                "Username Varchar (20) NOT NULL, " + 
                                                "Experience Integer(8) DEFAULT 0);";
                                query.executeUpdate(sqlcre);
                        }
                        
               }catch(SQLException e){
                        e.printStackTrace();
                }
        }
    	
    }

    public int saveQuery(Enchanter enchanter){
    	String sql = "Update prefix_Enchanter SET Experience = %Experience% WHERE Username = '%name%';";
        checkConnection();
        Statement query;
        int result = 0;
        sql = sql.replace("prefix_", prefix);
        sql = sql.replace("%Experience%", enchanter.getExp()+"");
        sql = sql.replace("%name%", enchanter.getName());
        try{
                query = conn.createStatement();
                result = query.executeUpdate(sql);
                
       }
        catch(SQLException e){
                e.printStackTrace();
        }
        return result;
    }
    
    public int InsertQuery(Player player){
    	String sql = "INSERT INTO prefix_Enchanter (Username) VALUES('"+player.getName()+"');";
        checkConnection();
        Statement query;
        int result = 0;
        sql = sql.replace("prefix_", prefix);
        try{
                query = conn.createStatement();
                result = query.executeUpdate(sql);
                
       }
        catch(SQLException e){
                e.printStackTrace();
        }
        return result;
    }
    
    public ResultSet LoadQuery(Player player){
    	
		String sql = "SELECT * FROM prefix_Enchanter Where Username = '%name%';";
        checkConnection();
        Statement query;
        ResultSet result = null;
        sql = sql.replace("prefix_", prefix);
        sql = sql.replace("%name%", player.getName());
        try{
                query = conn.createStatement();
                result = query.executeQuery(sql);
                }
        catch(SQLException e)
        {
                e.printStackTrace();
        }
        return result;
    }
    
    public ResultSet LoadAllQuery()
    {
    		String sql = "SELECT * FROM prefix_Enchanter;";
            checkConnection();
            Statement query;
            ResultSet result = null;
            sql = sql.replace("prefix_", prefix);
            try{
                    query = conn.createStatement();
                    result = query.executeQuery(sql);
                    }
            catch(SQLException e)
            {
                    e.printStackTrace();
            }
            return result;
    }
    private void checkConnection()
    {
            
           try {
                    if(conn.isClosed())
                    {
                            conn = null;
                            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?" + "user=" + 
                                           dbuser+ "&" + "password=" + dbpw);
                            
                   }
            } catch (SQLException e) {
                    e.printStackTrace();
            }
    }
    
    private void holdConnection()
    {
            String sql = "Select * from " + prefix + "Enchanter;";
            Statement query;
            try{
                    query = conn.createStatement();
                    query.executeQuery(sql);
            }
            catch(SQLException e)
            {
                    e.printStackTrace();
            }
    }
    
}
