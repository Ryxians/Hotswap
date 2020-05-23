package homebrew.hotswap;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;

public class PlayerInteract implements Listener {

    //Transfer config
    FileConfiguration config = Hotswap.getInstance().config;
    boolean helmet = config.getBoolean("swap.helmet");
    boolean chestplate = config.getBoolean("swap.chestplate");
    boolean leggings = config.getBoolean("swap.leggings");
    boolean boots = config.getBoolean("swap.boots");
    boolean elytra = config.getBoolean("swap.elytra");
    boolean swapped;
    Cancellable event;

    @EventHandler
    public void playerInteract(PlayerInteractEvent evt) {
        event = evt;
        Player player = evt.getPlayer();
        //When a player right clicks it right clicks for both hands, we only want the main hand
        switch (evt.getHand()) {
            //For the Main Hand swap everything.
            case HAND:
                //For Right Click we want to perform the swap, otherwise we don't want this plugin to do anything.
                switch (evt.getAction()) {
                    case RIGHT_CLICK_AIR:
                    case RIGHT_CLICK_BLOCK:
                        //Convert the item in hand to string array
                        String strv[] = player.getInventory().getItemInMainHand().getType().toString().split("_");

                        //Get the Player's Inventory
                        PlayerInventory pi = player.getInventory();
                        swapped = false;

                        //Do the correct swap based on what is being held
                        switch (strv[strv.length-1]){
                            case "ELYTRA":
                                if (!player.hasPermission("hotswap.elytra")) {
                                    break;
                                }
                                if (!elytra) {
                                    break;
                                }
                            case "CHESTPLATE":
                                if (chestplate && player.hasPermission("hotswap.chest")) {
                                    pi.setChestplate(swap(pi, pi.getItemInMainHand(), pi.getChestplate()));
                                }
                                break;
                            case "HELMET":
                                if (helmet && player.hasPermission("hotswap.helmet")) {
                                    pi.setHelmet(swap(pi, pi.getItemInMainHand(), pi.getHelmet()));
                                }
                                break;
                            case "LEGGINGS":
                                if (leggings && player.hasPermission("hotswap.leggings")) {
                                    pi.setLeggings(swap(pi, pi.getItemInMainHand(), pi.getLeggings()));
                                }
                                break;
                            case "BOOTS":
                                if (boots && player.hasPermission("hotswap.boots")) {
                                    pi.setBoots(swap(pi, pi.getItemInMainHand(), pi.getBoots()));
                                }
                                break;
                            default:
                                break;
                        }
                        if (swapped) {
                            player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 2, 0);
                        }
                        break;
                    default:
                        break;
                }
                break;
            //For any non-main hand interact do nothing
            default:
                break;
        }
    }

    public ItemStack swap(PlayerInventory pi, ItemStack hand, ItemStack armor) {
        swapped = true;
        event.setCancelled(true);
        pi.setItemInMainHand(armor);
        return hand;
    }
}
