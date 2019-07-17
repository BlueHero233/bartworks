/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.client.renderer.RendererGlasBlock;
import com.github.bartimaeusnek.bartworks.client.renderer.RendererSwitchingColorFluid;
import com.github.bartimaeusnek.bartworks.common.blocks.BioFluidBlock;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BWTileEntityDimIDBridge;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.Pair;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.objects.GT_Fluid;
import gregtech.api.util.GT_Utility;
import ic2.core.item.ItemFluidCell;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.awt.*;

public class FluidLoader implements Runnable {

    public static IIcon autogenIIcon;
    public static Fluid ff;
    public static int renderID;
    public static Block bioFluidBlock;
    public static Fluid[] BioLabFluidMaterials;
    public static ItemStack[] BioLabFluidCells;

    //OilProcessing chain
    public static Fluid fulvicAcid,heatedfulvicAcid,Kerogen;




    @Override
    public void run() {
        FluidLoader.renderID = RenderingRegistry.getNextAvailableRenderId();
        short[] rgb = new short[3];
        Arrays.fill(rgb, (short) 255);
        FluidLoader.ff = new GT_Fluid("BWfakeFluid", "molten.autogenerated", rgb);
        FluidLoader.fulvicAcid = FluidLoader.createAndRegisterFluid("Fulvic Acid", new Color(20, 20, 20));
        FluidLoader.heatedfulvicAcid = FluidLoader.createAndRegisterFluid("Heated Fulvic Acid", new Color(40, 20, 20),720);
        FluidLoader.Kerogen = FluidLoader.createAndRegisterFluid("Kerogen", new Color(85, 85, 85));
        FluidLoader.BioLabFluidMaterials = new Fluid[]{
                new GT_Fluid("FluorecentdDNA", "molten.autogenerated", new short[]{125, 50, 170, 0}),
                new GT_Fluid("EnzymesSollution", "molten.autogenerated", new short[]{240, 200, 125, 0}),
                new GT_Fluid("Penicillin", "molten.autogenerated", new short[]{255, 255, 255, 0}),
                new GT_Fluid("Polymerase", "molten.autogenerated", new short[]{110, 180, 110, 0}),
        };

        FluidLoader.BioLabFluidCells = new ItemStack[FluidLoader.BioLabFluidMaterials.length];
        for (int i = 0; i < FluidLoader.BioLabFluidMaterials.length; i++) {
            FluidRegistry.registerFluid(FluidLoader.BioLabFluidMaterials[i]);
            FluidLoader.BioLabFluidCells[i] = ItemFluidCell.getUniversalFluidCell(new FluidStack(FluidLoader.BioLabFluidMaterials[i], 1000));
        }

//        BioCulture.BIO_CULTURE_ARRAY_LIST.get(0).setFluid(new GT_Fluid("_NULL", "molten.autogenerated", BW_Util.splitColorToRBGArray(BioCulture.BIO_CULTURE_ARRAY_LIST.get(0).getColorRGB())));
        FluidStack dnaFluid = Loader.isModLoaded("gendustry") ? FluidRegistry.getFluidStack("liquiddna", 100) : Materials.Biomass.getFluid(100L);
        for (BioCulture B : BioCulture.BIO_CULTURE_ARRAY_LIST) {
            if (B.isBreedable()) {
                B.setFluid(new GT_Fluid(B.getName().replaceAll(" ", "").toLowerCase() + "fluid", "molten.autogenerated", new short[]{(short) B.getColor().getRed(), (short) B.getColor().getBlue(), (short) B.getColor().getGreen()}));
                FluidRegistry.registerFluid(B.getFluid());
                GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(10),GT_Values.NI,new FluidStack(B.getFluid(),1000),dnaFluid,GT_Values.NI,GT_Values.NI,GT_Values.NI,GT_Values.NI,GT_Values.NI,GT_Values.NI,null,500,120);
            }
        }

        FluidLoader.bioFluidBlock = new BioFluidBlock();
        GameRegistry.registerBlock(FluidLoader.bioFluidBlock, "coloredFluidBlock");
        GameRegistry.registerTileEntity(BWTileEntityDimIDBridge.class, "bwTEDimIDBridge");
        if (FMLCommonHandler.instance().getSide().isClient()) {
            RenderingRegistry.registerBlockHandler(RendererSwitchingColorFluid.instance);
            RenderingRegistry.registerBlockHandler(RendererGlasBlock.instance);
        }
    }

    public static Fluid createAndRegisterFluid(String Name,Color color){
        Fluid f = new GT_Fluid(Name,"molten.autogenerated",new short[]{(short) color.getRed(),(short) color.getGreen(),(short) color.getBlue(), (short) color.getAlpha()});
        FluidRegistry.registerFluid(f);
        return f;
    }
    public static Fluid createAndRegisterFluid(String Name, Color color, int temperature){
        Fluid f = new GT_Fluid(Name,"molten.autogenerated",new short[]{(short) color.getRed(),(short) color.getGreen(),(short) color.getBlue(), (short) color.getAlpha()});
        f.setTemperature(temperature);
        FluidRegistry.registerFluid(f);
        return f;
    }
}
