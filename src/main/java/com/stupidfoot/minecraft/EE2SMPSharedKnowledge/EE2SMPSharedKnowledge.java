package com.stupidfoot.minecraft.EE2SMPSharedKnowledge;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ee.EEBase;
import ee.TransTabletData;

public final class EE2SMPSharedKnowledge extends JavaPlugin implements Listener {

	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(
				pdfFile.getName() + " version " + pdfFile.getVersion()
						+ " is enabled!");

	}

	public boolean isDebugging(final Player player) {
		if (debugees.containsKey(player)) {
			return debugees.get(player);
		} else {
			return false;
		}
	}

	public void setDebugging(final Player player, final boolean value) {
		debugees.put(player, value);
	}

	public void syncEE2KnownItems() {
		HashSet<Integer[]> knownItems = new HashSet<Integer[]>();

		// this.plugin.getLogger().info("datas: " +
		// TransTabletData.datas.toString());
		ListIterator ttdIterator = TransTabletData.datas.listIterator();
		while (ttdIterator.hasNext()) {
			TransTabletData ttd = (TransTabletData) ttdIterator.next();
			HashMap uuu = ttd.getKnowledge();

			for (int var3 = 0; var3 < ttd.getKnowledge().size(); var3++) {
				if (ttd.getKnowledge().get(Integer.valueOf(var3)) != null) {
					knownItems.add((Integer[]) ttd.getKnowledge().get(
							Integer.valueOf(var3)));
				}
			}
		}

		ttdIterator = TransTabletData.datas.listIterator();
		while (ttdIterator.hasNext()) {
			TransTabletData ttd = (TransTabletData) ttdIterator.next();
			for (Integer[] kk : knownItems) {
				ttd.pushKnowledge(kk[0], kk[1]);
			}
		}
	}

	@EventHandler
	public void normalLogin(PlayerLoginEvent event) {
		this.syncEE2KnownItems();
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		this.syncEE2KnownItems();
	}

}
