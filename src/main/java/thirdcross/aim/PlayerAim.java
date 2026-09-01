package thirdcross.aim;

import dev.ryanhcode.sable.companion.SableCompanion;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PlayerAim {

    private static final double MINIMUM_RAY_DISTANCE = 100.0D;

    private PlayerAim() {
        // Utility class; do not instantiate.
    }

    public static HitResult getAimResult(float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || minecraft.level == null) {
            return null;
        }

        Vec3 start = SableCompanion.INSTANCE.getEyePositionInterpolated(
                player,
                partialTicks
        );
        Vec3 lookDirection = player.getViewVector(partialTicks);
        double rayDistance = Math.max(
                MINIMUM_RAY_DISTANCE,
                Math.max(
                        player.blockInteractionRange(),
                        player.entityInteractionRange()
                )
        );
        Vec3 rayEnd = start.add(lookDirection.scale(rayDistance));

        BlockHitResult blockHit = minecraft.level.clip(
                new ClipContext(
                        start,
                        rayEnd,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        player
                )
        );

        AABB searchBox = player.getBoundingBox()
                .expandTowards(lookDirection.scale(rayDistance))
                .inflate(1.0D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                player,
                start,
                rayEnd,
                searchBox,
                entity -> !entity.isSpectator() && entity.isPickable(),
                rayDistance
        );

        if (entityHit != null) {
            double entityDistance = start.distanceToSqr(entityHit.getLocation());

            if (blockHit.getType() == HitResult.Type.MISS
                    || entityDistance < start.distanceToSqr(blockHit.getLocation())) {
                return entityHit;
            }
        }

        if (blockHit.getType() != HitResult.Type.MISS) {
            Position hitPosition = blockHit.getLocation();

            if (SableCompanion.INSTANCE.getContaining(
                    minecraft.level,
                    hitPosition
            ) != null) {
                Vec3 projectedHit = SableCompanion.INSTANCE.projectOutOfSubLevel(
                        minecraft.level,
                        hitPosition
                );

                return new BlockHitResult(
                        projectedHit,
                        blockHit.getDirection(),
                        blockHit.getBlockPos(),
                        blockHit.isInside()
                );
            }

            return blockHit;
        }

        return BlockHitResult.miss(
                rayEnd,
                Direction.getNearest(lookDirection),
                BlockPos.containing(rayEnd)
        );
    }
}
