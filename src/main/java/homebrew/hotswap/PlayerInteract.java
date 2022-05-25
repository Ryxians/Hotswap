package homebrew.hotswap;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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
    boolean container = config.getBoolean("container-swap");
    World world = null;
    boolean swapped;
    Cancellable event;

    // Constructors
    public PlayerInteract() {

    }

    public PlayerInteract(World world) {
        this.world = world;
    }


    @EventHandler
    public void playerInteractCondition(PlayerInteractEvent evt) {
        if (world == null || evt.getPlayer().getWorld().equals(world)) {
            playerInteract(evt);
        }
    }

    public void playerInteract(PlayerInteractEvent evt) {
        event = evt;
        Player player = evt.getPlayer();
        EquipmentSlot hand = evt.getHand();
        boolean doSwap = true;


        if (hand == null) return;

        // Check if they used a container
        if (evt.getClickedBlock() != null && container)
            if (evt.getClickedBlock().getState() instanceof Container)
                return;

        // Check if the event is for the main hand
        if (evt.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        // On right click, swap
        if (evt.useItemInHand().equals(Event.Result.DEFAULT) && doSwap) {
            if (evt.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                toSwap(player);
            } else if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                // When right clicking a block, only swap if allowed
                if (evt.useInteractedBlock().equals(Event.Result.ALLOW))
                    toSwap(player);
            }
        }

    }



    // Gets the item in the player's hand and swaps it to the correct location
    public void toSwap(Player player) {
        //Convert the item in hand to string array
        String[] strv = player.getInventory().getItemInMainHand().getType().toString().split("_");

        //Get the Player's Inventory
        PlayerInventory pi = player.getInventory();
        swapped = false;

        //Do the correct swap based on what is being held
        switch (strv[strv.length - 1]) {
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
    }

    public ItemStack swap(PlayerInventory pi, ItemStack hand, ItemStack armor) {
        swapped = true;
        event.setCancelled(true);
        pi.setItemInMainHand(armor);
        return hand;
    }
}
