package me.lilac.teagarden.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import me.lilac.teagarden.TeaGarden;
import me.lilac.teagarden.container.TeaPotContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TeaPotScreen extends ContainerScreen<TeaPotContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(TeaGarden.ID, "textures/gui/tea_pot.png");

    public TeaPotScreen(TeaPotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        int relX = x - (this.width - this.xSize) / 2;
        int relY = y - (this.height - this.ySize) / 2;

        if (x >= guiLeft + 31 && x <= guiLeft + 40 && y >= guiTop + 19 && y <= guiTop + 65) {
            this.renderTooltip(matrixStack, new TranslationTextComponent("tooltip.teagarden.waterLevel", this.container.getCurrentWater(), 18),
                    relX, relY);
        }
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);

        int progress = this.container.getProgress();
        this.blit(matrixStack, relX + 120, relY + 35, 176, 0, progress + 1, 16);

        int waterLevel = this.container.getWaterLevel();
        this.blit(matrixStack, relX + 31, relY + 2 + 64 - waterLevel, 176, 64 - waterLevel, 10, waterLevel + 1);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
