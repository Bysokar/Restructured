/*
 * This file is part of Restructured, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.blockartistry.mod.Restructured.component;

import java.util.List;
import java.util.Random;

import org.blockartistry.mod.Restructured.ModLog;
import org.blockartistry.mod.Restructured.assets.Assets;
import org.blockartistry.mod.Restructured.assets.SchematicProperties;
import org.blockartistry.mod.Restructured.util.Dimensions;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class SchematicStructureCreationHandler implements VillagerRegistry.IVillageCreationHandler {

	static {
		// MapGenStructureIO.registerStructure(SchematicStructure.class,
		// "reSS");
		MapGenStructureIO.registerStructureComponent(SchematicStructure.class, "reSS");
	}

	public SchematicStructureCreationHandler() {
		VillagerRegistry.instance().registerVillageCreationHandler(this);
	}

	@Override
	public PieceWeight getVillagePieceWeight(final Random random, final int i) {
		return new SchematicPieceWeight(Assets.getTableForVillageGen());
	}

	@Override
	public Class<?> getComponentClass() {
		return SchematicStructure.class;
	}

	@Override
	public Village buildComponent(final PieceWeight villagePiece, final Start startPiece,
			final List<StructureComponent> pieces, final Random random, final int x, final int y, final int z,
			final EnumFacing facing, final int type) {

		// This shouldn't happen, but just in case...
		if (!(villagePiece instanceof SchematicPieceWeight))
			return null;

		// Get our next structure
		final SchematicPieceWeight pw = (SchematicPieceWeight) villagePiece;
		final SchematicProperties props = pw.getNextStructure();

		// If we don't get properties we may have exceeded
		// the spawn limit.
		if (props == null)
			return null;

		// Bound it out
		final Dimensions size = props.schematic.getDimensions();
		final StructureBoundingBox _boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0,
				size.width, size.height, size.length, facing);

		// Check to see if the region is OK, and if so return back
		// a SchematicStructure surrogate for the schematic.
		if (canVillageGoDeeper(_boundingBox)) {
			if (StructureComponent.findIntersecting(pieces, _boundingBox) == null) {
				ModLog.debug("Village structure [%s] @(%s); mode %d", props.name, _boundingBox, facing);
				final SchematicStructure struct = new SchematicStructure(startPiece, type, random, _boundingBox,
						facing);
				struct.setProperties(props);
				return struct;
			}
		}
		return null;
	}

	private boolean canVillageGoDeeper(final StructureBoundingBox _boundingBox) {
		return _boundingBox != null && _boundingBox.minY > 10;
	}
}
