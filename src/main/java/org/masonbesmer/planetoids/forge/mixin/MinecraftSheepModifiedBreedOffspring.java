package org.masonbesmer.planetoids.forge.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(net.minecraft.world.entity.animal.Sheep.class)
public abstract class MinecraftSheepModifiedBreedOffspring {
//    @Override
//    public @Nullable Sheep getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
//        Sheep sheep = EntityType.SHEEP.create(pLevel);
//        if (sheep != null) {
//            sheep.setColor(super.getOffspringColor(this, (Sheep)pOtherParent));
//        }
//
//        return sheep;
//    }
}
