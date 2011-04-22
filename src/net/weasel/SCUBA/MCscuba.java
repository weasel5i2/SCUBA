package net.weasel.SCUBA;

import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MCscuba extends JavaPlugin {

	public final BasicPlayerListener listener = new BasicPlayerListener();
	public static Logger log;
	public static Server server;
	public static Plugin instance;
	public static PluginDescriptionFile desc;
	
	public void onEnable(){
		instance = this;
		server = this.getServer();
		desc = this.getDescription();
		log = Logger.getLogger("Minecraft");
	
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, listener, Priority.Monitor, this);
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	
	public void onDisable(){
		
	}
}