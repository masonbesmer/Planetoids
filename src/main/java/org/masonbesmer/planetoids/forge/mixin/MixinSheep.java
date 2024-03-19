package org.masonbesmer.planetoids.forge.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.entity.animal.Sheep.class)
public abstract class MixinSheep {

    @Shadow
    protected abstract DyeColor getOffspringColor(Animal pFather, Animal pMother);
    @Inject(at = @At(value = "HEAD"), method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Sheep;", cancellable = true)
    private void getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent, CallbackInfoReturnable<Sheep> cir) {
        cir.cancel();

        System.out.println("hello from sheep method!!!!");

        Sheep sheep = EntityType.SHEEP.create(pLevel);
        Animal p = (Animal)((Object)this);
        if (sheep != null) {
            //sheep.setColor(this.getOffspringColor(p, (Sheep)pOtherParent));
            sheep.setColor(DyeColor.PINK);
        }
        cir.setReturnValue(sheep);
    }
}
