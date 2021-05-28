# Please enter your Jython code here!
# See JavaScript https://forum.image.sc/t/solved-save-summary-in-headlessmode/26606/3
from ij.plugin.filter import ParticleAnalyzer;
from ij.measure import ResultsTable;
from ij import IJ;

img1 = IJ.openImage("http://wsr.imagej.net/images/blobs.gif");
IJ.setAutoThreshold(img1, "Default");
IJ.run("Set Measurements...", "area mean redirect=None decimal=3");
rt =  ResultsTable();
ParticleAnalyzer.setResultsTable(rt);
#ParticleAnalyzer.setSummaryTable(rt);
IJ.run(img1, "Analyze Particles...", "clear");
rt.save("/Applications/Bio7.app/Contents/MacOS/workspace/Jyth/summary.csv");
