package org.masonbesmer.planetoids.forge.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    private Optional<?> currentlyHovered;

    @Shadow
    private boolean setFirstMousePos;

    @Shadow
    private int firstMouseX;

    @Shadow
    private int firstMouseY;

    @Inject(at = @At(value = "HEAD"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V")
    private void onRender(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo info) {
        info.cancel();

        if (!this.checkToClose()) {
            pPoseStack.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.setShaderTexture(0, GAMEMODE_SWITCHER_LOCATION);
            int i = this.width / 2 - 62;
            int j = this.height / 2 - 31 - 27;
            blit(pPoseStack, i, j, 0.0F, 0.0F, 125, 75, 128, 128);
            pPoseStack.popPose();
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            this.currentlyHovered.ifPresent((p_274676_) -> {
                drawCenteredString(pPoseStack, this.font, p_274676_.getName(), this.width / 2, this.height / 2 - 31 - 20, -1);
            });
            drawCenteredString(pPoseStack, this.font, SELECT_KEY, this.width / 2, this.height / 2 + 5, 16777215);
            if (!this.setFirstMousePos) {
                this.firstMouseX = pMouseX;
                this.firstMouseY = pMouseY;
                this.setFirstMousePos = true;
            }

            boolean flag = this.firstMouseX == pMouseX && this.firstMouseY == pMouseY;

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
