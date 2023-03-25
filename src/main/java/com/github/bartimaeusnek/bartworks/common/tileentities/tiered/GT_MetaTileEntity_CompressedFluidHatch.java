/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.widget.FluidDisplaySlotWidget;

public class GT_MetaTileEntity_CompressedFluidHatch extends GT_MetaTileEntity_Hatch_Input {

    private final FluidStack allowedFluid;

    public GT_MetaTileEntity_CompressedFluidHatch(int aID, String aName, String aNameRegional,
            FluidStack aAllowedFluid) {
        super(aID, aName, aNameRegional, 0);
        this.mDescriptionArray[1] = "Capacity: 2,000,000,000L";
        allowedFluid = aAllowedFluid;
    }

    public GT_MetaTileEntity_CompressedFluidHatch(String aName, int aTier, String[] aDescription,
            FluidStack aAllowedFluid, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        allowedFluid = aAllowedFluid;
    }

    @Override
    public int getCapacity() {
        return 2000000000;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return GT_Utility.areFluidsEqual(aFluid, allowedFluid);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_CompressedFluidHatch(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.allowedFluid,
                this.mTextures);
    }

    @Override
    protected FluidDisplaySlotWidget createDrainableFluidSlot() {
        if (Materials.LiquidAir.getFluid(1).equals(allowedFluid)) {
            return super.createDrainableFluidSlot().setEmptyCanFillFilter(f -> f == Materials.LiquidAir.mFluid);
        } else if (Materials.Helium.getGas(1).equals(allowedFluid)) {
            return super.createDrainableFluidSlot().setEmptyCanFillFilter(f -> f == Materials.Helium.mGas);
        } else if (Materials.Hydrogen.getGas(1).equals(allowedFluid)) {
            return super.createDrainableFluidSlot().setEmptyCanFillFilter(f -> f == Materials.Hydrogen.mGas);
        }
        return super.createDrainableFluidSlot().setEmptyCanFillFilter(null);
    }
}
