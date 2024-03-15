package org.masonbesmer.planetoids.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.masonbesmer.planetoids.Planetoids;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Planetoids.MODID);

    public static final RegistryObject<Item> ZIRCON = ITEMS.register("zircon", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLACK_OPAL = ITEMS.register("black_opal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_BLACK_OPAL = ITEMS.register("raw_black_opal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_ZIRCON = ITEMS.register("raw_zircon", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
