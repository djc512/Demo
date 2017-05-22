package huanxing_print.com.cn.printhome.util.copy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by DjC512 on 2017-4-26.
 */

public class ClipPicFileUtil {
    private static final int MAX_HEIGHT = 500;
    public static Context ctx;

    public static PointF[] getPoints(Mat image) {
        //Mat tempor = image.clone();
        Mat tempor = new Mat();
        image.copyTo(tempor);
        double ratio = tempor.size().height / MAX_HEIGHT;
        int height = Double.valueOf(tempor.size().height / ratio).intValue();
        int width = Double.valueOf(tempor.size().width / ratio).intValue();

        Size size = new Size(width, height);
        Mat resizedImage = new Mat(size, CvType.CV_8UC4);
        Imgproc.resize(tempor, resizedImage, size);
        tempor = resizedImage;

        Mat src = new Mat();
        Mat blurred = src.clone();
        Imgproc.GaussianBlur(tempor, blurred, new Size(11, 11), 0);
        Imgproc.cvtColor(blurred, src, Imgproc.COLOR_BGR2RGB, CvType.CV_8U);

        Mat gray0 = new Mat(src.size(), CvType.CV_8U), gray = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        List<Mat> blurredChannel = new ArrayList<Mat>();
        blurredChannel.add(src);
        List<Mat> gray0Channel = new ArrayList<Mat>();
        gray0Channel.add(gray0);

        MatOfPoint2f approxCurve;

        double maxArea = 0;

        PointF[] tempPoint = null;
        for (int c = 0; c < 3; c++) {
            int ch[] = {c, 0};
            Core.mixChannels(blurredChannel, gray0Channel, new MatOfInt(ch));

            int thresholdLevel = 1;
            for (int t = 0; t < thresholdLevel; t++) {
                if (t == 0) {
                    Imgproc.Canny(gray0, gray, 10, 20, 3, true); // true ?
                    //配置为1时，部分图片解析不了
                    Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 3); // 1
                } else {
                    Imgproc.threshold(gray0, gray, (t + 1) * 255 / thresholdLevel, 255, Imgproc.THRESH_BINARY);
                }

                Imgproc.findContours(gray, contours, new Mat(),
                        Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                for (MatOfPoint contour : contours) {
                    MatOfPoint2f temp = new MatOfPoint2f(contour.toArray());

                    double area = Imgproc.contourArea(contour);
                    approxCurve = new MatOfPoint2f();
                    Imgproc.approxPolyDP(temp, approxCurve,
                            Imgproc.arcLength(temp, true) * 0.02, true);

                    if (approxCurve.total() == 4 && area >= maxArea) {
                        double maxCosine = 0;

                        List<PointF> curves = approxCurve.toList();
                        for (int j = 2; j < 5; j++) {

                            double cosine = Math.abs(angle(curves.get(j % 4),
                                    curves.get(j - 2), curves.get(j - 1)));
                            maxCosine = Math.max(maxCosine, cosine);
                        }

                        if (maxCosine < 0.3) {
                            maxArea = area;
                            tempPoint = sortPoints(approxCurve.toArray());
                        }
                    }
                }
            }
        }
        if (maxArea < 2000) {
            PointF point1 = new PointF(0+50, 0+50);
            PointF point2 = new PointF(width-50, 0+50);
            PointF point3 = new PointF(width-50, height-50);
            PointF point4 = new PointF(0+50, height-50);
            PointF[] finalPoints = {point1, point2, point3, point4};
            return finalPoints;
        }
        tempor.release();
        return tempPoint;
    }
    private static double angle(PointF p1, PointF p2, PointF p0) {
        double dx1 = p1.x - p0.x;
        double dy1 = p1.y - p0.y;
        double dx2 = p2.x - p0.x;
        double dy2 = p2.y - p0.y;
        return (dx1 * dx2 + dy1 * dy2)
                / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
                + 1e-10);
    }


    private static PointF[] sortPoints(PointF[] src) {
        ArrayList<PointF> srcPoints = new ArrayList<>(Arrays.asList(src));
        PointF[] result = {null, null, null, null};

        Comparator<PointF> sumComparator = new Comparator<PointF>() {
            @Override
            public int compare(PointF lhs, PointF rhs) {
                return Double.valueOf(lhs.y + lhs.x).compareTo((double) (rhs.y + rhs.x));
            }
        };
        Comparator<PointF> differenceComparator = new Comparator<PointF>() {
            @Override
            public int compare(PointF lhs, PointF rhs) {
                return Double.valueOf(lhs.y - lhs.x).compareTo((double) (rhs.y - rhs.x));
            }
        };

        result[0] = Collections.min(srcPoints, sumComparator);        // Upper left has the minimal sum
        result[2] = Collections.max(srcPoints, sumComparator);        // Lower right has the maximal sum
        result[1] = Collections.min(srcPoints, differenceComparator); // Upper right has the minimal difference
        result[3] = Collections.max(srcPoints, differenceComparator); // Lower left has the maximal difference

        return result;
    }

    public static Mat perspectiveTransform(Mat src, List<PointF> points) {
        PointF point1 = new PointF(points.get(0).x, points.get(0).y);
        PointF point2 = new PointF(points.get(1).x, points.get(1).y);
        PointF point3 = new PointF(points.get(2).x, points.get(2).y);
        PointF point4 = new PointF(points.get(3).x, points.get(3).y);
        PointF[] pts = {point1, point2, point3, point4};
        return fourPointTransform(src, sortPoints(pts));
    }
    private static Mat fourPointTransform(Mat src, PointF[] pts) {
        //double ratio = src.size().height / (double) MAX_HEIGHT;

        PointF ul = pts[0];
        PointF ur = pts[1];
        PointF lr = pts[2];
        PointF ll = pts[3];

        double widthA = Math.sqrt(Math.pow(lr.x - ll.x, 2) + Math.pow(lr.y - ll.y, 2));
        double widthB = Math.sqrt(Math.pow(ur.x - ul.x, 2) + Math.pow(ur.y - ul.y, 2));
        //double maxWidth = Math.max(widthA, widthB) * ratio;
        double maxWidth = Math.max(widthA, widthB);

        double heightA = Math.sqrt(Math.pow(ur.x - lr.x, 2) + Math.pow(ur.y - lr.y, 2));
        double heightB = Math.sqrt(Math.pow(ul.x - ll.x, 2) + Math.pow(ul.y - ll.y, 2));
        //double maxHeight = Math.max(heightA, heightB) * ratio;
        double maxHeight = Math.max(heightA, heightB);

        Mat resultMat = new Mat(Double.valueOf(maxHeight).intValue(), Double.valueOf(maxWidth).intValue(), CvType.CV_8UC4);

        Mat srcMat = new Mat(4, 1, CvType.CV_32FC2);
        Mat dstMat = new Mat(4, 1, CvType.CV_32FC2);
        //srcMat.put(0, 0, ul.x * ratio, ul.y * ratio, ur.x * ratio, ur.y * ratio, lr.x * ratio, lr.y * ratio, ll.x * ratio, ll.y * ratio);
        srcMat.put(0, 0, ul.x , ul.y , ur.x , ur.y , lr.x , lr.y , ll.x , ll.y );
        dstMat.put(0, 0, 0.0, 0.0, maxWidth, 0.0, maxWidth, maxHeight, 0.0, maxHeight);

        Mat M = Imgproc.getPerspectiveTransform(srcMat, dstMat);
        Imgproc.warpPerspective(src, resultMat, M, resultMat.size());

        srcMat.release();
        dstMat.release();
        M.release();

        return resultMat;
    }

    /**
     * 灰度调节
     * @param src
     * @return
     */
    public static Bitmap applyThresholdGray(Mat src) {
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(src, src, new Size(5, 5), 0);
        Imgproc.adaptiveThreshold(src, src, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);

        Bitmap bm = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bm);
        return bm;
    }

    /**
     * 黑白
     * @param src
     * @return
     */
    public static Bitmap applyThresholdBlack(Mat src) {
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(src, src, new Size(5, 5), 0);

        Bitmap bm = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bm);
        return bm;
    }

    /**
     * 原图
     * @param src
     * @return
     */
    public static Bitmap applyThresholdOriginal(Mat src) {
        Bitmap bm = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bm);
        return bm;
    }
}
