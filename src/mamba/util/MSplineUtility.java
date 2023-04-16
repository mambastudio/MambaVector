/*
 * Internet Systems Consortium license
 *
 * Copyright (c) 2017, Colin Meinke
 *
 * Permission to use, copy, modify, and/or distribute this software for any 
 * purpose with or without fee is hereby granted, provided that the above 
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, 
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM 
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR 
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR 
 * PERFORMANCE OF THIS SOFTWARE.
 *
 */
package mamba.util;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import mamba.base.engine.shape.attributes.MArcData;
import mamba.base.engine.shape.attributes.bezier.MCubicBezier;
import mamba.base.engine.shape.attributes.bezier.MQuadraticBezier;

/**
 *
 * @author user
 * https://github.com/colinmeinke/svg-arc-to-cubic-bezier
 * 
 */
public class MSplineUtility {
    private static final double TAU = Math.PI * 2;
    
    
    private static Point2D mapToEllipse(
            Point2D xy, 
            double rx, double ry, 
            double cosphi, double sinphi, 
            double centerx, double centery)
    {
        double x = xy.getX() * rx;
        double y = xy.getY() * ry;

        double xp = cosphi * x - sinphi * y;
        double yp = sinphi * x + cosphi * y;
          
        return new Point2D(xp + centerx, yp + centery);
    }
    
    private static Point2D[] approxUnitArc(double ang1, double ang2)
    {
        // If 90 degree circular arc, use a constant
        // as derived from http://spencermortensen.com/articles/bezier-circle
        
        double a = ang2 == 1.5707963267948966
                ? 0.551915024494
                : ang2 == -1.5707963267948966
                ? -0.551915024494
                : 4 / 3 * Math.tan(ang2 / 4);
        
        double x1 = Math.cos(ang1);
        double y1 = Math.sin(ang1);
        double x2 = Math.cos(ang1 + ang2);
        double y2 = Math.sin(ang1 + ang2);
        
        return new Point2D[]{
                new Point2D(x1 - y1 * a, y1 + x1 * a),                
                new Point2D(x2 + y2 * a, y2 - x2 * a),
                new Point2D(x2, y2)};        
        
    }
    
    private static double vectorAngle(double ux, double uy, double vx, double vy)
    {
        double sign = (ux * vy - uy * vx < 0) ? -1 : 1;
        double dot = ux * vx + uy * vy;
        
        if (dot > 1) {
            dot = 1;
        }

        if (dot < -1) {
            dot = -1;
        }

        return sign * Math.acos(dot);
    }
    
    private static ArcCenter getArcCenter(
            double px,
            double py,
            double cx,
            double cy,
            double rx,
            double ry,
            double largeArcFlag,
            double sweepFlag,
            double sinphi,
            double cosphi,
            double pxp,
            double pyp
    )
    {
        double rxsq = Math.pow(rx, 2);
        double rysq = Math.pow(ry, 2);
        double pxpsq = Math.pow(pxp, 2);
        double pypsq = Math.pow(pyp, 2);

        double radicant = (rxsq * rysq) - (rxsq * pypsq) - (rysq * pxpsq);

        if (radicant < 0) {
          radicant = 0;
        }
        
        radicant /= (rxsq * pypsq) + (rysq * pxpsq);
        radicant = Math.sqrt(radicant) * (largeArcFlag == sweepFlag ? -1 : 1);
        
        double centerxp = radicant * rx / ry * pyp;
        double centeryp = radicant * -ry / rx * pxp;

        double centerx = cosphi * centerxp - sinphi * centeryp + (px + cx) / 2;
        double centery = sinphi * centerxp + cosphi * centeryp + (py + cy) / 2;
        
        double vx1 = (pxp - centerxp) / rx;
        double vy1 = (pyp - centeryp) / ry;
        double vx2 = (-pxp - centerxp) / rx;
        double vy2 = (-pyp - centeryp) / ry;
        
        double ang1 = vectorAngle(1, 0, vx1, vy1);
        double ang2 = vectorAngle(vx1, vy1, vx2, vy2);

        if (sweepFlag == 0 && ang2 > 0) {
            ang2 -= TAU;
        }

        if (sweepFlag == 1 && ang2 < 0) {
            ang2 += TAU;
        }

        return new ArcCenter(centerx, centery, ang1, ang2);
    }
    
    public static ArrayList<MCubicBezier> convertArcToCubic(
            Point2D previousPoint,
            MArcData arcData
    )
    {
        return MSplineUtility.convertArcToCubic(
                previousPoint,
                arcData.currentPoint,
                arcData.radius,
                arcData.xAxisRotation,
                arcData.largeArcFlag,
                arcData.sweepFlag
        );
    }
    
    public static ArrayList<MCubicBezier> convertArcToCubic(
            Point2D previousPoint,
            Point2D currentPoint,
            Point2D radius,
            double xAxisRotation,       //rotate in degrees clockwise
            double largeArcFlag,        
            double sweepFlag
    )
    {
        return convertArcToCubic(
                previousPoint.getX(),
                previousPoint.getY(),
                currentPoint.getX(),
                currentPoint.getY(),
                radius.getX(),
                radius.getY(),
                xAxisRotation,       //rotate in degrees clockwise
                largeArcFlag,        
                sweepFlag
        );
    }
    
    public static ArrayList<MCubicBezier> convertArcToCubic(
            double px,                  //previous point x
            double py,                  //previous point y
            double cx,                  //current point x
            double cy,                  //current point y
            double rx,                  //radius at x
            double ry,                  //radius at y
            double xAxisRotation,       //rotate in degrees clockwise
            double largeArcFlag,        
            double sweepFlag
    )
    {
        ArrayList<Point2D[]> curves = new ArrayList();
        if (rx == 0 || ry == 0) {
            return null;
        }
        
        double sinphi = Math.sin(xAxisRotation * TAU / 360);
        double cosphi = Math.cos(xAxisRotation * TAU / 360);

        double pxp = cosphi * (px - cx) / 2 + sinphi * (py - cy) / 2;
        double pyp = -sinphi * (px - cx) / 2 + cosphi * (py - cy) / 2;

        if (pxp == 0 && pyp == 0) {
          return null;
        }

        rx = Math.abs(rx);
        ry = Math.abs(ry);

        double lambda =
            Math.pow(pxp, 2) / Math.pow(rx, 2) +
            Math.pow(pyp, 2) / Math.pow(ry, 2);

        if (lambda > 1) {
            rx *= Math.sqrt(lambda);
            ry *= Math.sqrt(lambda);
        }
        
        ArcCenter arcCenter = getArcCenter(
            px,
            py,
            cx,
            cy,
            rx,
            ry,
            largeArcFlag,
            sweepFlag,
            sinphi,
            cosphi,
            pxp,
            pyp
        );
        
        // If 'ang2' == 90.0000000001, then `ratio` will evaluate to
        // 1.0000000001. This causes `segments` to be greater than one, which is an
        // unecessary split, and adds extra points to the bezier curve. To alleviate
        // this issue, we round to 1.0 when the ratio is close to 1.0.
        double ratio = Math.abs(arcCenter.ang2) / (TAU / 4);
        if (Math.abs(1.0 - ratio) < 0.0000001) {
            ratio = 1.0;
        }
        
        double segments = Math.max(Math.ceil(ratio), 1);
        
        arcCenter.ang2 /= segments;

        for (double i = 0; i < segments; i++) {
            curves.add(approxUnitArc(arcCenter.ang1, arcCenter.ang2));
            arcCenter.ang1 += arcCenter.ang2;
        }
        
        ArrayList<MCubicBezier> cubics = new ArrayList((int)segments);
        
        for(Point2D[] curve : curves)
        {
            cubics.add(new MCubicBezier(                   
                    mapToEllipse(curve[2], rx, ry, cosphi, sinphi, arcCenter.centerX, arcCenter.centerY), //p2                    
                    mapToEllipse(curve[0], rx, ry, cosphi, sinphi, arcCenter.centerX, arcCenter.centerY), //c1
                    mapToEllipse(curve[1], rx, ry, cosphi, sinphi, arcCenter.centerX, arcCenter.centerY)  //c2                  
            ));            
        }        
        return cubics;
    }
    
    // https://fontforge.org/docs/techref/bezier.html#converting-truetype-to-postscript
    // slight error but not noticeable
    public static MCubicBezier convertQuadraticToCubic(Point2D previousPoint, MQuadraticBezier q)
    {
        Point2D qP0 = previousPoint;
        Point2D qP1 = q.getControl();
        Point2D qP2 = q.getPoint();
        
        Point2D cP3 = qP2; //current point
        
        Point2D cP1 = qP0.add(qP1.subtract(qP0).multiply(2.d/3.d)); //control 1
        Point2D cP2 = qP2.add(qP1.subtract(qP2).multiply(2.d/3.d)); //control 2
        
        return new MCubicBezier(cP3, cP1, cP2);
    }
    
    private static class ArcCenter
    {
        double centerX, centerY;
        double ang1, ang2;
        
        public ArcCenter(double centerX, double centerY, double ang1, double ang2)
        {
            this.centerX = centerX;
            this.centerY = centerY;
            this.ang1 = ang1;
            this.ang2 = ang2;
        }
    }
    
    public static Point2D getMirrorControlPoint(Point2D point, Point2D control)
    {
        //The Continuity of Splines (YouTube - Time: 20:00) by Freya Holmér
        return point.multiply(2).subtract(control);
    }
}
