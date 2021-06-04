/*
Export script for R code to *.svg!
*/
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.widgets.Display;
import com.eco.bio7.util.Util;
import com.eco.bio7.reditors.REditor;
import org.eclipse.core.runtime.IProgressMonitor;
import com.eco.bio7.rbridge.ExecuteRScript;

Display display = Util.getDisplay();
display.asyncExec(new Runnable() {
	public void run() {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof REditor) {
			String path = Bio7Dialog.saveFile("*.svg");
			String pathRData = path.replace(".svg", ".RData").replace("\\", "/");
			new ExecuteRScript((rserveCon, monitor) -> {
				rserveCon.eval("save(file=\"" + pathRData + "\")");
			monitor.worked(1);
			}, 1);
			/*if (RServe.isAlive()) {
				RServeUtil.evalR("save(file=\"" + pathRData + "\")", null);
			}
			else{
				System.out.println("Rserve not running. R workspace not saved!");
			}*/
			IDocumentProvider dp = editor.getDocumentProvider();
			IDocument doc = dp.getDocument(editor.getEditorInput());
			String rCode = doc.get();
			/*XML replacements!*/
			rCode = rCode.replace("&", "&#38;");
			rCode = rCode.replace("<", "&#60;");
			rCode = rCode.replace(">", "&#62;");
			rCode = rCode.replace("\"", "&#34;");
			rCode = rCode.replace("'", "&#39;");		
			String svgTemplate = svgString(pathRData, rCode);
			//System.out.println(svgTemplate);
			if (path != null) {
				File fil = new File(path);
				FileWriter fileWriter = null;
				try {
					fileWriter = new FileWriter(fil);
					BufferedWriter buffWriter = new BufferedWriter(fileWriter);
					buffWriter.write(svgTemplate, 0, svgTemplate.length());
					buffWriter.close();
				} catch (IOException ex) {

				} finally {
					try {
						fileWriter.close();
					} catch (IOException ex) {

					}
				}
			}
		} else {
			Bio7Dialog.message("Please open a R file!");
		}

	}

});

public String svgString(pathRData, rCode){
String svgTemplate = """
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<svg
   width="210mm"
   height="297mm"
   viewBox="0 0 210 297"
   version="1.1"
   id="svg5"
   inkscape:version="1.1-alpha (17bc9184, 2021-03-28)"
   sodipodi:docname="Zeichnung2.svg"
   xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape"
   xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd"
   xmlns="http://www.w3.org/2000/svg"
   xmlns:svg="http://www.w3.org/2000/svg">
  <sodipodi:namedview
     id="namedview7"
     pagecolor="#ffffff"
     bordercolor="#999999"
     borderopacity="1"
     objecttolerance="10.0"
     gridtolerance="10.0"
     guidetolerance="10.0"
     inkscape:pageshadow="0"
     inkscape:pageopacity="0"
     inkscape:pagecheckerboard="0"
     inkscape:document-units="mm"
     showgrid="false"
     inkscape:zoom="0.50244108"
     inkscape:cx="396.06634"
     inkscape:cy="562.25498"
     inkscape:window-width="1259"
     inkscape:window-height="685"
     inkscape:window-x="0"
     inkscape:window-y="25"
     inkscape:window-maximized="0"
     inkscape:current-layer="layer1" />
  <defs
     id="defs2" />
  <g
     inkscape:label="Ebene 1"
     inkscape:groupmode="layer"
     id="layer1">
    <rect
       style="fill:#f9f9f9;stroke-width:0.264583"
       id="rect31"
       width="191.68085"
       height="80.042557"
       x="7.8989358"
       y="34.228725">
      <desc
         id="desc33">load(file='"""+pathRData+"')\n"+rCode+"""</desc>
    </rect>
  </g>
</svg>
""";
return 	svgTemplate;
}

