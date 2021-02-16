package me.lilac.teagarden.container;

import me.lilac.teagarden.TeaGarden;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TeaGarden.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerRegistry {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, TeaGarden.ID);

    public static final RegistryObject<ContainerType<TeaPotContainer>> TEA_POT = CONTAINERS.register("tea_pot", () -> IForgeContainerType.create(((windowId, inv, data) -> new TeaPotContainer(windowId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player))));
}
