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

package com.azortis.rides.tracked;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Train {

    // Serializable values
    private String mainCartFile;
    private Map<Integer, String> cartFiles;
    private Map<Integer, Double> cartOffsets;

    // Runtime values
    private transient Cart mainCart;
    private transient List<Cart> carts;

    public Train(){}

    private Train(String mainCartFile, Map<Integer, String> cartFiles, Map<Integer, Double> cartOffsets) {
        this.mainCartFile = mainCartFile;
        this.cartFiles = cartFiles;
        this.cartOffsets = cartOffsets;
    }

    public Train createCopy(){
        return new Train(mainCartFile, new HashMap<>(cartFiles), new HashMap<>(cartOffsets));
    }

}
