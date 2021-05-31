/*
A Bio7 API method to select selected ROI Manager names in the R shell (if transferred to the R workspace)!
*/
import com.eco.bio7.rbridge.views.RShellView;
import org.eclipse.swt.widgets.Display;
import com.eco.bio7.image.Util;
import com.eco.bio7.rbridge.RServe;
import com.eco.bio7.batch.Bio7Dialog;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;


String[] firstChar = [".", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" ];

Display display = Util.getDisplay();
    display.syncExec(new Runnable() {
			public void run() {
				RoiManager mInstance = RoiManager.getInstance();
				if (mInstance == null) {
					/* If ROI Manager isn't active! */
					Bio7Dialog.message("Please open and add selections to the ROI Manager!");
					return;
				}
				Roi[] r = mInstance.getSelectedRoisAsArray();

				if (r.length < 1) {
					Bio7Dialog.message("NO ROI's available in ROI Manager!");
					return;
				}
				/*Convert ROI Manager names for the R-Shell!*/
				String[]name=new String[r.length]
				for (int i = 0; i < r.length; i++) {
					String temp=r[i].getName();
					name[i] = temp.replace(",", ".").replace("-", "_");
					for (int j = 0; j < firstChar.length; j++) {

						if (name[i].startsWith(firstChar[j])) {
							name[i] = name[i].replaceFirst(firstChar[j], "X" + firstChar[j]);
						}
					}
				}
				RShellView shell = RShellView.getInstance();
				if (shell != null) {
					shell.getListShell().setSelection(name);
				}
		}
});