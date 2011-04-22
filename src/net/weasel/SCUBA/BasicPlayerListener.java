package net.weasel.SCUBA;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BasicPlayerListener extends PlayerListener
{
	private static ArrayList<String> players = new ArrayList<String>(50);
	
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		if (!players.contains(event.getPlayer().getName())) {
			players.add(event.getPlayer().getName());
			activateUBA(event.getPlayer().getName());
		}
    }
	
	public static void activateUBA(final String name) 
	{
		if (MCscuba.server.getPlayer(name) == null) {
			players.remove(name);
			return;
		}
		final int currentAir = MCscuba.server.getPlayer(name).getRemainingAir();
		final boolean wearingDivingHelmet = MCscuba.server.getPlayer(name).getInventory().getHelmet().getType().getId() == 310;

		if (wearingDivingHelmet) {
			MCscuba.server.getPlayer(name).setMaximumAir(3000);
		}
		else {
			MCscuba.server.getPlayer(name).setMaximumAir(300);
		}
		new Thread() {
			public void run() {
				try {
					sleep(150);
					Player player = MCscuba.server.getPlayer(name);
					if (currentAir != player.getRemainingAir()) {
						onRemainingAirChange(player, currentAir);
					}
					if (wearingDivingHelmet && player.getInventory().getHelmet().getType().getId() == 310) {
						player.setMaximumAir(300);
					}
					else if (!wearingDivingHelmet && player.getInventory().getHelmet().getType().getId() == 310) {
						player.setMaximumAir(3000);
					}
					activateUBA(name);
				}
				catch (InterruptedException e) {
					activateUBA(name);
				}
				catch (NullPointerException e) {
					players.remove(name);
				}
			}
		}.start();
	}

	public static void onRemainingAirChange(Player player, int old) 
	{
		if (player.getInventory().getHelmet().getType().getId() == 310 ) 
		{
			int oldPercent = (old + 299) / 300;
			int remaining = (player.getRemainingAir() + 299) / 300;
			boolean ignore = old == 300 && player.getRemainingAir() == 3000 || old == 3000 && player.getRemainingAir() == 300;
			if (oldPercent != remaining && !ignore) {
				
				String message = "[";
				for (int i = 0; i < 10; i++) {
					if (i < remaining){
						message += ChatColor.BLUE.toString() + "|";
					}
					else {
						message += ChatColor.RED.toString() + "|";
					}
				}
				message += ChatColor.WHITE.toString() + "]";
				message += String.format(" %d%c Air Remaining.", remaining * 10, '%');
				player.sendMessage(message);
			}
		}
	}
}
