/*
A macro to save all particles as images!
Author: M.Austenfeld
See: https://forum.image.sc/t/saving-each-roi-as-individual-images/3227/8
*/
setBatchMode(true);
input = "C:\\particles\\";
output = "C:\\partout\\";
list = getFileList(input);

for (i = 0; i < list.length; i++){
        //print(input + list[i]);
        open(input + list[i]);
        mainTitle=getTitle();
        dirCropOutput=output+File.separator+mainTitle;
        File.makeDirectory(dirCropOutput);     
        run("Duplicate...", "title=particles");
        run("8-bit");
        selectWindow("particles");
        setAutoThreshold("Default");
        setOption("BlackBackground", false);
        run("Analyze Particles...", "size=0-Infinity display exclude clear add");
        selectWindow("Results");
        saveAs("Results", dirCropOutput+File.separator+"Results.xls");
        run("Close"); 
        selectWindow(mainTitle);

        for (u=0; u<roiManager("count"); ++u) {
            run("Duplicate...", "title=crop");
            roiManager("Select", u);
            run("Crop");
            saveAs("Tiff", dirCropOutput+File.separator+"The_Particle_"+(u+1)+".tif");
            close();
             //Next round!
             selectWindow(mainTitle);
        }
        close();
        selectWindow("particles");
        close();
        //print("finished");    
}
