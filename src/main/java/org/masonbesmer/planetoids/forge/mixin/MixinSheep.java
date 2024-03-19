package org.masonbesmer.planetoids.forge.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(net.minecraft.world.entity.animal.Sheep.class)
public abstract class MixinSheep {

    @Shadow
    protected abstract DyeColor getOffspringColor(Animal pFather, Animal pMother);

    @Inject(at = @At(value = "HEAD"), method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Sheep;", cancellable = true)
    private void getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent, @Nullable CallbackInfoReturnable<Sheep> cir) {
        assert cir != null;
        cir.cancel();

        Animal p = (Animal)((Object)this);
        Animal pMate = (Animal)pOtherParent;

        AgeableMob ageablemob = EntityType.AXOLOTL.create(pLevel);

        if (ageablemob == null) {
            cir.setReturnValue(null);
            return;
        }
        ServerPlayer serverplayer = p.getLoveCause();
        if (serverplayer == null && pMate.getLoveCause() != null) {
            serverplayer = pMate.getLoveCause();
        }

        if (serverplayer != null) {
            serverplayer.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, p, pMate, ageablemob);
        }

        p.setAge(6000);
        pMate.setAge(6000);
        p.resetLove();
        pMate.resetLove();
        ageablemob.moveTo(p.getX(), p.getY(), p.getZ(), 0.0F, 0.0F);
        pLevel.addFreshEntityWithPassengers(ageablemob);
        pLevel.broadcastEntityEvent(p, (byte)18);
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            pLevel.addFreshEntity(new ExperienceOrb(pLevel, p.getX(), p.getY(), p.getZ(), p.getRandom().nextInt(7) + 1));
        }
        cir.setReturnValue(null);
    }
}
