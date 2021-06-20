package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class MathTools {

    public static boolean intersects(Vector2[] lineseg1, Vector2[] lineseg2) {
        int dir1 = directionOfPointToLine(lineseg1, lineseg2[0]);
        int dir2 = directionOfPointToLine(lineseg1, lineseg2[1]);
        int dir3 = directionOfPointToLine(lineseg2, lineseg1[0]);
        int dir4 = directionOfPointToLine(lineseg2, lineseg1[1]);

        if(dir1 != dir2 && dir3 != dir4){
            return true;
        }else if(pointOnLineSeg(lineseg1, lineseg2[0])){
            return true;
        }else if(pointOnLineSeg(lineseg1, lineseg2[1])){
            return true;
        }else if(pointOnLineSeg(lineseg2, lineseg1[0])){
            return true;
        }else if(pointOnLineSeg(lineseg2, lineseg1[1])){
            return true;
        }
        return false;
    }

    public static boolean pointOnLineSeg(Vector2[] lineSeg, Vector2 point) {
        if(directionOfPointToLine(lineSeg, point) == 0 &&
                point.x <= Math.max(lineSeg[0].x, lineSeg[1].x) &&
                point.x >= Math.min(lineSeg[0].x, lineSeg[1].x) &&
                point.y <= Math.max(lineSeg[0].y, lineSeg[1].y) &&
                point.y >= Math.min(lineSeg[0].y, lineSeg[1].y)){
            return true;
        }
        return false;
    }

    public static int directionOfPointToLine(Vector2[] lineSeg, Vector2 point) {
        float val = (lineSeg[1].x - lineSeg[0].x)*(point.y - lineSeg[0].y) -
                (lineSeg[1].y - lineSeg[0].y)*(point.x - lineSeg[0].x);
        if(val == 0){
            return 0; //Co-linear
        }else if(val < 0){
            return 2; // Anti-clockwise
        }
        return 1; //Clockwise

    }

    public static boolean isInTriangle(Vector2 vertex1, Vector2 vertex2, Vector2 vertex3, Vector2 point) {
        Vector2 v1 = new Vector2(vertex1.x - point.x,
                vertex1.y - point.y);
        Vector2 v2 = new Vector2(vertex2.x - point.x,
                vertex2.y - point.y);
        Vector2 v3 = new Vector2(vertex3.x - point.x,
                vertex3.y - point.y);
        float angle1 = getAcuteAngle((float) Math.toDegrees(Math.abs(vectorToAngle(v1) - vectorToAngle(v2))));
        float angle2 = getAcuteAngle((float) Math.toDegrees(Math.abs(vectorToAngle(v1) - vectorToAngle(v3))));
        float angle3 = getAcuteAngle(((float) Math.toDegrees(Math.abs(vectorToAngle(v2) - vectorToAngle(v3)))));
        // Note in theory the below must equal to 0,
        // to allow for rounding errors, you can adjust tolerance by changing the 1
        if(Math.abs(angle1+angle2+angle3-360) < 1){
            return true;
        }
        return false;
    }

    public static float getAcuteAngle(float angle) {
        if(angle > 180){
            return 360 - angle;
        }
        return angle;
    }

    public static float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    public static Vector2[] perpendicularDiametricalPoints(Vector2 origin, Vector2 target, float radius){
        Vector2 distanceVector = subtractVector2D(target, origin);

        Vector2 v1 = scalarMultiplication(radius, unitVector2D(getPerpendicularVectors2D(distanceVector)[0]));
        Vector2 v2 = scalarMultiplication(radius, unitVector2D(getPerpendicularVectors2D(distanceVector)[1]));
        return new Vector2[]{addVector2D(target, v1), addVector2D(target, v2)};
    }

    public static Vector2 scalarMultiplication(float radius, Vector2 vector) {
        return new Vector2(radius*vector.x, radius*vector.y);
    }

    public static Vector2 unitVector2D(Vector2 vector) {
        return new Vector2(vector.x /vector.len(), vector.y /vector.len());
    }

    public static Vector2[] getPerpendicularVectors2D(Vector2 vector) {
        return new Vector2[]{new Vector2(vector.y, -vector.x), new Vector2(-vector.y, vector.x)};
    }

    public static Vector2 subtractVector2D(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2 addVector2D(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public static boolean pointInRect(float[] rect, Vector2 vector2) {
        if(vector2.x >= rect[0] && vector2.x <= (rect[0] + rect[2]) &&
                vector2.y >= rect[1] && vector2.y <= (rect[1] + rect[3])){
            return true;
        }
        return false;
    }
}
