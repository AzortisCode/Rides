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

package com.azortis.rides.tracked.editor;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EditorTool {

    private final Material material;
    private final int slotId;
    private final String displayName;
    private final List<String> lore;

    public EditorTool(Material material, int slotId, String displayName, List<String> lore) {
        this.material = material;
        this.slotId = slotId;
        this.displayName = displayName;
        this.lore = lore;
    }

    public ItemStack getItemStack(){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        List<String> colouredLore = new ArrayList<>();
        for (String loreLine : lore){
            colouredLore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        }
        itemMeta.setLore(colouredLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
