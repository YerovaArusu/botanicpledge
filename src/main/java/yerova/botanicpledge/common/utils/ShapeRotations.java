// Utils/ShapeRotations.java
package yerova.botanicpledge.common.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class ShapeRotations {


    /** Rotiere eine Shape von der Default-Ausrichtung (UP) in die gewünschte Richtung. */
    public static VoxelShape rotateFromUp(VoxelShape shape, Direction to) {
        if (to == Direction.UP) return shape;

        VoxelShape out = Shapes.empty();
        for (AABB box : shape.toAabbs()) {
            AABB rotated = switch (to) {
                case DOWN  -> rotateX(box, 180);
                case NORTH -> rotateX(box, 270); // -90: +Y -> -Z
                case SOUTH -> rotateX(box,  90); // +90: +Y -> +Z
                case EAST  -> rotateZ(box, 270); // -90: +Y -> +X  (EAST)
                case WEST  -> rotateZ(box,  90); // +90: +Y -> -X  (WEST)
                default    -> box;
            };
            out = Shapes.or(out, Shapes.create(rotated));
        }
        return out;
    }

    /** Rotation um X-Achse um 0/90/180/270 Grad um das Blockzentrum (0.5,0.5,0.5). */
    private static AABB rotateX(AABB box, int degrees) {
        switch (normalize(degrees)) {
            case 90:  return rotateBox(box, Axis.X,  90);
            case 180: return rotateBox(box, Axis.X, 180);
            case 270: return rotateBox(box, Axis.X, 270);
            default:  return box;
        }
    }

    /** Rotation um Z-Achse um 0/90/180/270 Grad um das Blockzentrum. */
    private static AABB rotateZ(AABB box, int degrees) {
        switch (normalize(degrees)) {
            case 90:  return rotateBox(box, Axis.Z,  90);
            case 180: return rotateBox(box, Axis.Z, 180);
            case 270: return rotateBox(box, Axis.Z, 270);
            default:  return box;
        }
    }

    private enum Axis { X, Z }

    private static int normalize(int deg) {
        int d = ((deg % 360) + 360) % 360;
        if (d % 90 != 0) throw new IllegalArgumentException("Only 90° steps supported");
        return d;
        }

    /** Transformiert alle 8 Ecken und bildet daraus die neue AABB. */
    private static AABB rotateBox(AABB in, Axis axis, int degrees) {
        double minX = 1, minY = 1, minZ = 1;
        double maxX = 0, maxY = 0, maxZ = 0;

        // alle 8 Ecken
        for (double x : new double[]{in.minX, in.maxX}) {
            for (double y : new double[]{in.minY, in.maxY}) {
                for (double z : new double[]{in.minZ, in.maxZ}) {
                    double[] p = rotatePoint(x, y, z, axis, degrees);
                    minX = Math.min(minX, p[0]); minY = Math.min(minY, p[1]); minZ = Math.min(minZ, p[2]);
                    maxX = Math.max(maxX, p[0]); maxY = Math.max(maxY, p[1]); maxZ = Math.max(maxZ, p[2]);
                }
            }
        }
        // leichte Rundungsfehler abfangen
        minX = clamp01(minX); minY = clamp01(minY); minZ = clamp01(minZ);
        maxX = clamp01(maxX); maxY = clamp01(maxY); maxZ = clamp01(maxZ);

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /** rotiert einen Punkt um das Zentrum (0.5/0.5/0.5). */
    private static double[] rotatePoint(double x, double y, double z, Axis axis, int degrees) {
        // zum Zentrum verschieben
        double cx = x - 0.5, cy = y - 0.5, cz = z - 0.5;
        double rx = cx, ry = cy, rz = cz;

        switch (axis) {
            case X -> {
                // X: (x, y, z) -> (x, -z, y) für +90°
                switch (normalize(degrees)) {
                    case 90  -> { ry = -cz; rz =  cy; }
                    case 180 -> { ry = -cy; rz = -cz; }
                    case 270 -> { ry =  cz; rz = -cy; }
                }
            }
            case Z -> {
                // Z: (x, y, z) -> (-y, x, z) für +90°
                switch (normalize(degrees)) {
                    case 90  -> { rx = -cy; ry =  cx; }
                    case 180 -> { rx = -cx; ry = -cy; }
                    case 270 -> { rx =  cy; ry = -cx; }
                }
            }
        }

        // zurück verschieben
        return new double[]{ rx + 0.5, ry + 0.5, rz + 0.5 };
    }

    private static double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}
