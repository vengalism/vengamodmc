/*
 * Copyright (c) 2017
 */

package com.vengalism.vengamodmc.init;

import com.vengalism.vengamodmc.Config;
import com.vengalism.vengamodmc.objects.items.*;
import com.vengalism.vengamodmc.objects.tools.*;
import net.minecraft.item.Item;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;


public class ItemInit {


    //HYDRO
    public static final ItemHydroAirStone item_hydro_air_stone = new ItemHydroAirStone("item_hydro_air_stone", 1000, Config.itemHydroAirStoneUpkeepCost);
    public static final ItemNutrient item_nutrient_a = new ItemNutrient("item_nutrient_a");
    public static final ItemNutrient item_nutrient_b = new ItemNutrient("item_nutrient_b");
    public static final ItemNutrientMixture item_nutrient_mixture = new ItemNutrientMixture("item_nutrient_mixture", new ArrayList<>(Arrays.asList((new ItemNutrient[]{item_nutrient_a, item_nutrient_a, item_nutrient_b, item_nutrient_b}))));

    //ENERGY
    public static final ItemEnergyBatteryPart item_energy_battery_part_1 = new ItemEnergyBatteryPart("item_energy_battery_part_1");
    public static final ItemEnergyBatteryPart item_energy_battery_part_2 = new ItemEnergyBatteryPart("item_energy_battery_part_2");
    public static final ItemEnergyBatteryPart item_energy_circuit = new ItemEnergyBatteryPart("item_energy_circuit");
    public static final ItemEnergyBatteryPart item_energy_battery_cap = new ItemEnergyBatteryPart("item_energy_battery_cap");
    public static final ItemEnergyBatteryPart item_energy_battery_body = new ItemEnergyBatteryPart("item_energy_battery_body");
    public static final ItemEnergyBatteryPart item_energy_battery_base = new ItemEnergyBatteryPart("item_energy_battery_base");
    public static final ItemEnergyBattery item_energy_battery = new ItemEnergyBattery("item_energy_battery", Config.itemEnergyBatteryCapacity, Config.itemEnergyBatteryMaxReceive, Config.itemEnergyBatteryMaxExtract, 0);

    public static final ItemMultiTool item_multi_tool = new ItemMultiTool("item_multi_tool");



    //PRODUCTION


    //TOOLS
    public static final AxeEnergyTool tool_axe_energy_iron = new AxeEnergyTool("tool_axe_energy_iron", Item.ToolMaterial.IRON);
    public static final AxeEnergyTool tool_axe_energy_gold = new AxeEnergyTool("tool_axe_energy_gold", Item.ToolMaterial.GOLD);
    public static final AxeEnergyTool tool_axe_energy_diamond = new AxeEnergyTool("tool_axe_energy_diamond", Item.ToolMaterial.DIAMOND);
    public static final ToolHoeEnergy tool_hoe_energy_iron = new ToolHoeEnergy("tool_hoe_energy_iron", Item.ToolMaterial.IRON);
    public static final ToolHoeEnergy tool_hoe_energy_gold = new ToolHoeEnergy("tool_hoe_energy_gold", Item.ToolMaterial.GOLD);
    public static final ToolHoeEnergy tool_hoe_energy_diamond = new ToolHoeEnergy("tool_hoe_energy_diamond", Item.ToolMaterial.DIAMOND);
    public static final ToolPickaxeEnergy tool_pickaxe_energy_iron = new ToolPickaxeEnergy("tool_pickaxe_energy_iron", Item.ToolMaterial.IRON);
    public static final ToolPickaxeEnergy tool_pickaxe_energy_gold = new ToolPickaxeEnergy("tool_pickaxe_energy_gold", Item.ToolMaterial.GOLD);
    public static final ToolPickaxeEnergy tool_pickaxe_energy_diamond = new ToolPickaxeEnergy("tool_pickaxe_energy_diamond", Item.ToolMaterial.DIAMOND);
    public static final ToolShovelEnergy tool_shovel_energy_iron = new ToolShovelEnergy("tool_shovel_energy_iron", Item.ToolMaterial.IRON);
    public static final ToolShovelEnergy tool_shovel_energy_gold = new ToolShovelEnergy("tool_shovel_energy_gold", Item.ToolMaterial.GOLD);
    public static final ToolShovelEnergy tool_shovel_energy_diamond = new ToolShovelEnergy("tool_shovel_energy_diamond", Item.ToolMaterial.DIAMOND);
    public static final ToolSwordEnergy tool_sword_energy_iron = new ToolSwordEnergy("tool_sword_energy_iron", Item.ToolMaterial.IRON);
    public static final ToolSwordEnergy tool_sword_energy_gold = new ToolSwordEnergy("tool_sword_energy_gold", Item.ToolMaterial.GOLD);
    public static final ToolSwordEnergy tool_sword_energy_diamond = new ToolSwordEnergy("tool_sword_energy_diamond", Item.ToolMaterial.DIAMOND);


    public static void init(){
        //construct items in here, make public outside new way for naming scheme is ingot_copper

    }
}
