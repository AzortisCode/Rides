/*
 * Create custom rides for your Minecraft server.
 *     Copyright (C) 2020  Azortis
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.azortis.rides.tracked.editor.cart;

import com.azortis.rides.tracked.Seat;
import com.azortis.rides.tracked.editor.EditorTool;
import com.azortis.rides.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CartListener implements Listener {

    private final CartEditor cartEditor;
    private final Player editor;
    private final ItemStack[] inventoryContents;

    private int incrementAmount = 1;
    private Seat selectedSeat;

    CartListener(CartEditor cartEditor, Player editor) {
        this.cartEditor = cartEditor;
        this.editor = editor;

        this.inventoryContents = editor.getInventory().getContents();
        editor.getInventory().clear();

        List<EditorTool> editorTools = Arrays.asList(
                new EditorTool(Material.COMPASS, 36, "&dSelect Seat",
                        Collections.singletonList("&eRight Click to iterate seats.")),
                new EditorTool(Material.EMERALD, 37, "&aCreate&8/&4Delete &dSeat",
                        Arrays.asList("&eLeft click to &a&lCreate &ea seat.",
                                "&eRight click to &4&lDelete &ethe current seat.")),
                new EditorTool(Material.REDSTONE_LAMP, 38, "&dCopy & Select Seat",
                        Collections.singletonList("&eRight click to copy and select the seat.")),
                new EditorTool(Material.HOPPER, 39, "&dCopy Symmetrical & Select Seat",
                        Collections.singletonList("&eRight click to create a symmetrical copy of the seat and select it.")),
                new EditorTool(Material.DIAMOND_SWORD, 40, "&aIncrease&8/&cDecrease &6&lX",
                        Arrays.asList("&eLeft click to &a&lIncrease &6&lX&e.",
                                "&eRight click to &c&lDecrease &6&lX&e.")),
                new EditorTool(Material.GOLDEN_SWORD, 41, "&aIncrease&8/&cDecrease &6&lY",
                        Arrays.asList("&eLeft click to &a&lIncrease &6&lY&e.",
                                "&eRight click to &c&lDecrease &6&lY&e.")),
                new EditorTool(Material.IRON_SWORD, 42, "&aIncrease&8/&cDecrease &6&lZ",
                        Arrays.asList("&eLeft click to &a&lIncrease &6&lZ&e.",
                                "&eRight click to &c&lDecrease &6&lZ&e.")),
                new EditorTool(Material.NAME_TAG, 43, "&aIncrease&8/&cDecrease &d&lIncrements",
                        Arrays.asList("&eLeft click to &a&lIncrease &d&lIncrements&e.",
                                "&eRight click to &c&lDecrease &d&lIncrements&e.")),
                new EditorTool(Material.BARRIER, 44, "&cClose & Save Cart",
                        Collections.singletonList("&eRight click to close and save the cart."))
        );

        for (EditorTool editorTool : editorTools) {
            editor.getInventory().setItem(editorTool.getSlotId(), editorTool.getItemStack());
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() == editor) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
                Material tool = event.getMaterial();
                if (tool == Material.COMPASS && event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (selectedSeat == null) {
                        Seat seat = cartEditor.getCart().getSeats().get(1);
                        if (seat == null) {
                            editor.sendMessage(ChatColor.RED + "Please create a seat before selecting one!");
                        } else {
                            selectedSeat = seat;
                            cartEditor.setSelectedSeat(1);
                            cartEditor.getVisualizer().setSelectedSeat(selectedSeat);
                            editor.sendMessage(ChatColor.GREEN + "Selected seat 1!");
                        }
                    } else {
                        int seatToSelect = cartEditor.getSelectedSeat() + 1;
                        if (seatToSelect > cartEditor.getCart().getSeats().size()) seatToSelect = 1;
                        selectedSeat = cartEditor.getCart().getSeats().get(seatToSelect);
                        cartEditor.setSelectedSeat(seatToSelect);
                        cartEditor.getVisualizer().setSelectedSeat(selectedSeat);
                        editor.sendMessage(ChatColor.GREEN + "Selected seat " + seatToSelect + "!");
                    }
                } else if (tool == Material.EMERALD) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                        if (selectedSeat != null) {
                            cartEditor.getCart().getSeats().remove(selectedSeat);
                            if (!cartEditor.getCart().getSeats().isEmpty()) {
                                selectedSeat = cartEditor.getCart().getSeats().get(1);
                                cartEditor.setSelectedSeat(1);
                                cartEditor.getVisualizer().setSelectedSeat(selectedSeat);
                            } else {
                                cartEditor.setSelectedSeat(0);
                                cartEditor.getVisualizer().setSelectedSeat(null);
                                selectedSeat = null;
                            }
                            editor.sendMessage(ChatColor.RED + "Successfully deleted seat!");
                        } else {
                            editor.sendMessage(ChatColor.RED + "No seat is selected to be deleted!");
                        }
                    } else {
                        selectedSeat = cartEditor.createSeat();
                        cartEditor.setSelectedSeat(cartEditor.getCart().getSeats().size());
                        cartEditor.getVisualizer().setSelectedSeat(selectedSeat);
                        editor.sendMessage(ChatColor.GREEN + "Created and selected seat!");
                    }
                } else if (tool == Material.REDSTONE_LAMP && event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (selectedSeat != null) {
                        Seat copySeat = cartEditor.createSeat();
                        copySeat.setYawOffset(selectedSeat.getYawOffset());
                        applySelectedSettings(copySeat);
                    } else {
                        editor.sendMessage(ChatColor.RED + "You must select a seat first!");
                    }
                } else if (tool == Material.HOPPER && event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (selectedSeat != null) {
                        Seat copySeat = cartEditor.createSeat();
                        copySeat.setYawOffset(-selectedSeat.getYawOffset());
                        applySelectedSettings(copySeat);
                    } else {
                        editor.sendMessage(ChatColor.RED + "You must select a seat first!");
                    }
                } else if (tool == Material.DIAMOND_SWORD) {
                    if (selectedSeat != null) {
                        moveSelectedSeat(1, event.getAction() != Action.LEFT_CLICK_AIR);
                        editor.sendMessage(ChatColor.GREEN + "Moved seat along X-axis, now x=" +
                                selectedSeat.getStand().getBukkitStand().getLocation().getX());
                    } else {
                        editor.sendMessage(ChatColor.RED + "You must select a seat first!");
                    }
                } else if (tool == Material.GOLDEN_SWORD) {
                    if (selectedSeat != null) {
                        moveSelectedSeat(2, event.getAction() != Action.LEFT_CLICK_AIR);
                        editor.sendMessage(ChatColor.GREEN + "Moved seat along Y-axis, now y=" +
                                selectedSeat.getStand().getBukkitStand().getLocation().getY());
                    } else {
                        editor.sendMessage(ChatColor.RED + "You must select a seat first!");
                    }
                } else if (tool == Material.IRON_SWORD) {
                    if (selectedSeat != null) {
                        moveSelectedSeat(3, event.getAction() != Action.LEFT_CLICK_AIR);
                        editor.sendMessage(ChatColor.GREEN + "Moved seat along Z-axis, now z=" +
                                selectedSeat.getStand().getBukkitStand().getLocation().getZ());
                    } else {
                        editor.sendMessage(ChatColor.RED + "You must select a seat first!");
                    }
                } else if (tool == Material.NAME_TAG) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                        incrementAmount++;
                        editor.sendMessage(ChatColor.GREEN + "Now incrementing by: " + (1 / incrementAmount));
                    } else {
                        if (incrementAmount != 1) {
                            incrementAmount--;
                            editor.sendMessage(ChatColor.GREEN + "Now incrementing by: " + (1 / incrementAmount));
                        } else {
                            editor.sendMessage(ChatColor.RED + "Increments cannot be longer than 1!");
                        }
                    }
                } else if (tool == Material.BARRIER && event.getAction() == Action.RIGHT_CLICK_AIR) {
                    editor.getInventory().clear();
                    editor.getInventory().setContents(inventoryContents);
                    editor.sendMessage(ChatColor.GREEN + "Saving cart...");
                    cartEditor.save();
                    return;
                }
            }
            event.setCancelled(true);
        }
    }

    private void moveSelectedSeat(int axis, boolean subtract) {
        Location location = selectedSeat.getStand().getBukkitStand().getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        if (axis == 1) {
            if (subtract) {
                x -= (double) 1 / incrementAmount;
            } else {
                x += (double) 1 / incrementAmount;
            }
        } else if (axis == 2) {
            if (subtract) {
                y -= (double) 1 / incrementAmount;
            } else {
                y += (double) 1 / incrementAmount;
            }
        } else if (axis == 3) {
            if (subtract) {
                z -= (double) 1 / incrementAmount;
            } else {
                z += (double) 1 / incrementAmount;
            }
        }
        Vector originPoint = cartEditor.getLocation().toVector();
        Vector nextPoint = new Vector(x, y, z);
        EulerAngle direction = MathUtils.getDirection(originPoint, nextPoint);
        selectedSeat.setYawOffset(direction.getY());
        selectedSeat.setPitchOffset(direction.getX());
        Vector yawVector = new Vector(x, 0, z);
        selectedSeat.setYawDistance(yawVector.length());
        Vector pitchVector = new Vector(0, y, z);
        selectedSeat.setPitchDistance(pitchVector.length());
        cartEditor.getVisualizer().setChanged(true);
    }

    private void applySelectedSettings(Seat copySeat) {
        copySeat.setYawDistance(selectedSeat.getYawDistance());
        copySeat.setPitchOffset(selectedSeat.getPitchOffset());
        copySeat.setPitchDistance(selectedSeat.getPitchDistance());
        cartEditor.setSelectedSeat(cartEditor.getCart().getSeats().size());
        cartEditor.getVisualizer().setSelectedSeat(copySeat);
        editor.sendMessage(ChatColor.GREEN + "Copied and selected seat!");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer() == editor) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() == editor) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() == editor) {
            event.setCancelled(true);
        }
    }

}
