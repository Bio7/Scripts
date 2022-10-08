/*
A macro example to get the pixel coordinates and values of all ROI's in the ROI
Manager!
*/
n = roiManager("count");
for (index = 0; index < n; index++) {
    print("ROI Nr.:" + index + 1);
    roiManager("select", index);
    print("Label: " + Roi.getName);
    print("Coordinates & Pixel Values: ");
    Roi.getContainedPoints(x, y);
    for (i = 0; i < x.length; i++) {
        print(x[i] + " " + y[i] + ": " + getPixel(x[i], y[i]));
    }
}