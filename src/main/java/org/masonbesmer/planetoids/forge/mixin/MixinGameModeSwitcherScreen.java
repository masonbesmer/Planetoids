package org.masonbesmer.planetoids.forge.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static net.minecraft.client.gui.GuiComponent.blit;
import static net.minecraft.client.gui.GuiComponent.drawCenteredString;

@Mixin(GameModeSwitcherScreen.class)
public abstract class MixinGameModeSwitcherScreen extends Screen {
    @Unique
    private static final ResourceLocation GAMEMODE_SWITCHER_LOCATION = new ResourceLocation("textures/gui/container/gamemode_switcher.png");

    protected MixinGameModeSwitcherScreen(Component pTitle) {
        super(pTitle);
    }

    @Shadow
    protected abstract boolean checkToClose();
    @Shadow
    //private Object currentlyHovered;
    //@Shadow
    private Optional<GameModeIcon> currentlyHovered;

    @Shadow
    private boolean setFirstMousePos;

    @Shadow
    private int firstMouseX;

    @Shadow
    private int firstMouseY;

    @Shadow
    private static GameModeSwitcherScreen $$enum$GameModeIcon;

    @Inject(at = @At(value = "HEAD"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", cancellable = true)
    private void onRender(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo info) {
        info.cancel();

        GameModeSwitcherScreen s = (GameModeSwitcherScreen)((Object)this);

        System.out.println("hello from mixin!!!");

        if (!this.checkToClose()) {
            pPoseStack.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.setShaderTexture(0, GAMEMODE_SWITCHER_LOCATION);
            int i = s.width / 2 - 62;
            int j = s.height / 2 - 31 - 27;
            blit(pPoseStack, i, j, 0.0F, 0.0F, 125, 75, 128, 128);
            pPoseStack.popPose();
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            this.currentlyHovered.ifPresent((fws) -> {
                drawCenteredString(pPoseStack, s.font, fws::getName(), s.width / 2, s.height / 2 - 31 - 20, -1);
            });
            drawCenteredString(pPoseStack, s.font, SELECT_KEY, s.width / 2, s.height / 2 + 5, 16777215);
            if (!this.setFirstMousePos) {
                firstMouseX = pMouseX;
                firstMouseY = pMouseY;
                setFirstMousePos = true;
            }

            boolean flag = firstMouseX == pMouseX && firstMouseY == pMouseY;

            for(GameModeSwitcherScreen.GameModeSlot gamemodeswitcherscreen$gamemodeslot : this.slots) {
                gamemodeswitcherscreen$gamemodeslot.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
                this.currentlyHovered.ifPresent((p_97569_) -> {
                    gamemodeswitcherscreen$gamemodeslot.setSelected(p_97569_ == gamemodeswitcherscreen$gamemodeslot.icon);
                });
                if (!flag && gamemodeswitcherscreen$gamemodeslot.isHoveredOrFocused()) {
                    this.currentlyHovered = Optional.of(gamemodeswitcherscreen$gamemodeslot.icon);
                }
            }

        }
    }
}
