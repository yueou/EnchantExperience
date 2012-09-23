package com.yueou.EnchantExperience;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.skill.Skill;

public class EnchantExperience extends JavaPlugin{

    private Logger log = Logger.getLogger("Minecraft");
    private File file = new File("plugins" + File.separatorChar + "EnchantExperience" + File.separatorChar + "save.yml");
    private YamlConfiguration savefile;
    
    private EnchantExperienceHash enchantermap;
    private EnchantExperienceListener listener;
    private EnchantExperienceCommand commander;
    private Heroes heroes;
    private Skill enchantskill;
    private Skill repairskill;
    
    private int maxexp;
    private int maxlv;
    
    public void onEnableConf(){
    	
    	enchantskill = heroes.getSkillManager().getSkill(getConfig().getString("options.EnchantSkillName"));
    	repairskill = heroes.getSkillManager().getSkill(getConfig().getString("options.RepairSkillName"));
    	
    	if(enchantskill==null||repairskill==null){
    		log.info("Î´ÕÒµ½ÒÀÀµ¸½Ä§¼¼ÄÜ,²å¼þÆô¶¯Ê§°Ü");
    		onDisable();
    		return;
    	}
    }
    
    @Override
    public void onEnable(){
    	
    	log.info("EnchantExperience v" + this.getDescription().getVersion() + " loading!");
    	
    	getConfig().options().copyDefaults(true);
    	saveConfig();

     	enchantermap = new EnchantExperienceHash(this);
     	
    	heroes = (Heroes)getServer().getPluginManager().getPlugin("Heroes");
    	if(heroes==null){
    		log.info("Heroes²å¼þÎ´ÕÒµ½,²å¼þÆô¶¯Ê§°Ü");
    		onDisable();
    		return;
    	}
    	listener = new EnchantExperienceListener(this);
    	commander = new EnchantExperienceCommand(this);
    	
    	getCommand("enchantlv").setExecutor(commander);
    	
    	maxexp = getConfig().getInt("options.maxEnchantExp");
    	maxlv = getConfig().getInt("options.maxEnchantLevel");
    	
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(listener, this);
        
        try 
 	   	{
        	file.createNewFile();
 	   	} 
 	   	catch (IOException e) 
 	   	{
 	        e.printStackTrace();
 	   	}
    	
        savefile = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(file);
        
        loadEnchanters();
        
    	log.info("EnchantExperience v" + getDescription().getVersion() + " by yueou Enabled!");
    }
    
    @Override
    public void onDisable(){
    	save();
    	enchantermap.clear();
    	log.info("EnchantExperience v" + getDescription().getVersion() + " by yueou disabled!");
    }
    
    public void onReload(){
    	
    }
    
    public Skill getEnchantSkill(){
    	
    	return enchantskill;
    }
    
    public Skill getRepairSkill(){
    	
    	return repairskill;
    }
    
    public int getMaxExp(){
    	
    	return maxexp;
    }   
    
    public int getMaxLv(){
    	
    	return maxlv;
    }
    
    public void loadEnchanters(){
    	Player []player = getServer().getOnlinePlayers();
    	int len = player.length;
    	String playername = null;
    	
    	for(int i=0;i<len;i++){
    		playername = player[i].getName();
    		int exp = savefile.getInt(playername,0);
    		if(exp==0)
    			savefile.set(playername, 0);
    		loadEnchanter(player[i],exp);
    	}
    }
    
    
    public void loadEnchanter(Player player,int exp){

    	enchantermap.addEnchanter(player, exp);
    }
    
    public void newEnchanter(Player player){
    	
    	enchantermap.addEnchanter(player, 0);
    }
    
    public void removeEnchanter(Player player){
    	
    	enchantermap.removeEnchanter(player);
    }
    
    public EnchantExperienceHash getMap(){
    	
    	return enchantermap;
    }
    
    public void save(){
		try {
			savefile.save("plugins" + File.separatorChar + "EnchantExperience" + File.separatorChar + "save.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Enchanter getEnchanter(Player player){
    	
    	return enchantermap.getEnchanter(player);
    }
    
    public YamlConfiguration getSaveFile(){
    	return savefile;
    }
   
}
