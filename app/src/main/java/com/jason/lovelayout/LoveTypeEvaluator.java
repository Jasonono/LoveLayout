package com.jason.lovelayout;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class LoveTypeEvaluator implements TypeEvaluator<PointF> {

    private PointF controlPoint1;
    private PointF controlPoint2;

    public LoveTypeEvaluator(PointF p1, PointF p2) {
        this.controlPoint1 = p1;
        this.controlPoint2 = p2;
    }

    @Override
    public PointF evaluate(float fraction, PointF pointF0, PointF pointF3) {
        //三阶贝赛尔曲线
        PointF pointF = new PointF();

        pointF.x = pointF0.x * (1 - fraction) * (1 - fraction) * (1 - fraction) +
                3 * controlPoint1.x * fraction * (1 - fraction) * (1 - fraction) +
                3 * controlPoint2.x * fraction * fraction * (1 - fraction)
                + pointF3.x * fraction * fraction * fraction;

        pointF.y = pointF0.y * (1 - fraction) * (1 - fraction) * (1 - fraction) +
                3 * controlPoint1.y * fraction * (1 - fraction) * (1 - fraction) +
                3 * controlPoint2.y * fraction * fraction * (1 - fraction) +
                pointF3.y * fraction * fraction * fraction;

//        PointF pointF = new PointF();
//        pointF.x = pointF0.x * (1 - t) * (1 - t) * (1 - t) + 3 * pointF1.x * t * (1 - t) * (1 - t) +
//                3 * pointF2.x * t * t * (1 - t)
//                + pointF3.x * t * t * t;
//
//        pointF.y = pointF0.y  * (1 - t) * (1 - t) * (1 - t) + 3 * pointF1.y  * t * (1 - t) * (1 - t) +
//                3 * pointF2.y  * t * t * (1 - t)
//                + pointF3.y  * t * t * t;

        return pointF;
    }
}
