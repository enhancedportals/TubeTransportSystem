package tubeTransportSystem.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import tubeTransportSystem.network.ProxyCommon;

public class Utilities {
    public static void entityAccelerate(Entity entity, ForgeDirection direction) {
        if (direction == ForgeDirection.DOWN)
            entity.addVelocity(0, -0.1, 0);
        else if (direction == ForgeDirection.UP)
            entity.addVelocity(0, 0.1, 0);
        else if (direction == ForgeDirection.NORTH)
            entity.addVelocity(0, 0, -0.1);
        else if (direction == ForgeDirection.SOUTH)
            entity.addVelocity(0, 0, 0.1);
        else if (direction == ForgeDirection.EAST)
            entity.addVelocity(0.1, 0, 0);
        else if (direction == ForgeDirection.WEST)
            entity.addVelocity(-0.1, 0, 0);
    }

    public static void entityLimitSpeed(Entity entity) {
        entity.motionX = MathHelper.clamp_double(entity.motionX, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
        entity.motionY = MathHelper.clamp_double(entity.motionY, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
        entity.motionZ = MathHelper.clamp_double(entity.motionZ, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
    }

    public static void entityResetFall(Entity entity) {
        entity.fallDistance = 0f;
    }

    public static ForgeDirection entityGetDirection(EntityLivingBase entityLiving) {
        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facing == 0)
            return ForgeDirection.NORTH;
        else if (facing == 1)
            return ForgeDirection.EAST;
        else if (facing == 2)
            return ForgeDirection.SOUTH;
        else if (facing == 3)
            return ForgeDirection.WEST;

        return null;
    }

    static double AXIS_MIN_MIN = 0, AXIS_MIN_MAX = 0.1, AXIS_MAX_MIN = 0.9, AXIS_MAX_MAX = 1, AXIS_FLOOR_MIN = -0.01, AXIS_FLOOR_MAX = 0;

    public static AxisAlignedBB getCollisionBoxPart(int x, int y, int z, ForgeDirection direction) {
        if (direction == ForgeDirection.EAST)
            return AxisAlignedBB.getBoundingBox(x + AXIS_MAX_MIN, y, z, x + AXIS_MAX_MAX, y + 1, z + 1);
        else if (direction == ForgeDirection.WEST)
            return AxisAlignedBB.getBoundingBox(x + AXIS_MIN_MIN, y, z, x + AXIS_MIN_MAX, y + 1, z + 1);
        else if (direction == ForgeDirection.SOUTH)
            return AxisAlignedBB.getBoundingBox(x, y, z + AXIS_MAX_MIN, x + 1, y + 1, z + AXIS_MAX_MAX);
        else if (direction == ForgeDirection.NORTH)
            return AxisAlignedBB.getBoundingBox(x, y, z + AXIS_MIN_MIN, x + 1, y + 1, z + AXIS_MIN_MAX);
        else if (direction == ForgeDirection.UP)
            return AxisAlignedBB.getBoundingBox(x, y + AXIS_MAX_MIN, z, x + 1, y + AXIS_MAX_MAX, z + 1);
        else if (direction == ForgeDirection.DOWN)
            return AxisAlignedBB.getBoundingBox(x, y + AXIS_MIN_MIN, z, x + 1, y + AXIS_MIN_MAX, z + 1);

        return null;
    }

    public static AxisAlignedBB getCollisionBoxPartFloor(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y + AXIS_FLOOR_MIN, z, x + 1, y + AXIS_FLOOR_MAX, z + 1);
    }
    
    public static ChunkCoordinates getBlock(int x, int y, int z, int s) {
        if (s == 0)
            y++;
        else if (s == 1)
            y--;
        else if (s == 2)
            z++;
        else if (s == 3)
            z--;
        else if (s == 4)
            x++;
        else if (s == 5)
            x--;
        
        return new ChunkCoordinates(x, y, z);
    }
}
