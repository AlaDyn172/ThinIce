package ro.andreielvis.thinice;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	private static final Logger log = Logger.getLogger("Minecraft");
	
	public int Water_Blocks = 0;
	
	int Task = 0;
	
	HashMap<String, Integer> waterTimes = new HashMap<String, Integer>();
	
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		
		loadConfig();
		
		runTask();
	}
	
	public void onDisable() {
		log.info(String.format("[%s] Plugin disabled", getDescription().getName()));
		
		getServer().getScheduler().cancelTask(Task);
	}
	
	public void runTask() {
		
		Task = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {

				for(Player p : getServer().getOnlinePlayers()) {
					
					if(p.isOnGround()) {
						Location pLoc = p.getLocation();
						pLoc.setY(pLoc.getY() - 1);
												
						if(pLoc.getBlock().getType() == Material.ICE) {
							
							if(waterTimes.get(p.getName()) == null) waterTimes.put(p.getName(), 0);
							
							for(int i = 0; i < Water_Blocks; i++) {
								
								int checkWater = i+1;
								
								Location checkLoc = p.getLocation();
								checkLoc.setY(pLoc.getY() - 1 - checkWater);
								
								if(checkLoc.getBlock().getType() == Material.WATER) {
									waterTimes.put(p.getName(), waterTimes.get(p.getName()) + 1);
								}
							
								if(waterTimes.get(p.getName()) == Water_Blocks) {
									pLoc.getBlock().setType(Material.WATER);
									pLoc.setX(pLoc.getX() - 1);
									if(pLoc.getBlock().getType() == Material.ICE) pLoc.getBlock().setType(Material.WATER);
									pLoc.setX(pLoc.getX() + 2);
									if(pLoc.getBlock().getType() == Material.ICE) pLoc.getBlock().setType(Material.WATER);
									pLoc.setX(pLoc.getX() - 1);
									pLoc.setZ(pLoc.getZ() - 1);
									if(pLoc.getBlock().getType() == Material.ICE) pLoc.getBlock().setType(Material.WATER);
									pLoc.setZ(pLoc.getZ() + 2);
									if(pLoc.getBlock().getType() == Material.ICE) pLoc.getBlock().setType(Material.WATER);
									waterTimes.put(p.getName(), 0);
									
									p.playSound(pLoc, Sound.BLOCK_GLASS_BREAK, 3.0F, 0.5F);
								}
							}
						} else {
							
							if(waterTimes.get(p.getName()) != null && waterTimes.get(p.getName()) > 0) waterTimes.put(p.getName(), 0);
							
						}
					}
				}
				
			}
			
		}, 0, 20 * 1);
		
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		Water_Blocks = getConfig().getInt("Water_Blocks");
	}
	
}
