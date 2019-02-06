package com.songoda.epicbuckets.utils;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * ChatUtil created by: SoFocused
 * Date Created: oktober 02 2018
 * Time created: 22:54
 */
public class ChatUtil {

    public static String colorPrefix(String msg) {
        return ChatColor.translateAlternateColorCodes('&', getPrefix() + msg);
    }

    public static String colorString(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);

    }

    public static List<String> colorList(List<String> list) {

        List<String> newList = new ArrayList<>();

        list.forEach(string -> newList.add(colorString(string)));

        return newList;

    }

    public static List<String> colorList(List<String> list, Material material, int price) {

        List<String> newList = new ArrayList<>();

        list.forEach(string -> newList.add(colorString(string.replace("{material}", properMaterialName(material).toLowerCase()).replace("{price}", String.valueOf(price)))));

        return newList;

    }

    public static String stripColor(String input) {
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', input));
    }

    public static String getPrefix() {
        return EpicBuckets.getInstance().getLocale().getMessage("general.nametag.prefix").equals("none") ? "" : EpicBuckets.getInstance().getLocale().getMessage("general.nametag.prefix") + " ";
    }

    public static void debugMSG(Player player, Object... args) {

        StringJoiner stringBuilder = new StringJoiner("§8:");

        for (int i = 0; i < args.length; i++)
            stringBuilder.add("§a" + args[i].toString());


        player.sendMessage(stringBuilder.toString());

    }

    public static String getCoordinatesFromLocation(Location l) {
        return "X: " + l.getBlockX() + " Y: " + l.getBlockY() + " Z: " + l.getBlockZ();
    }

    public static String properMaterialName(Material material) {

        String materialName;

        if (material.name().split("_").length > 1) {

            StringJoiner stringJoiner = new StringJoiner(" ");

            for (String str : material.name().split("_"))
                stringJoiner.add(str);

            materialName = stringJoiner.toString();
        } else
            materialName = material.name();

        return materialName;

    }


}
