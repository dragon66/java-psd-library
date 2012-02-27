package psdtool;

import psd.model.Layer;
import psd.model.Psd;
import psd.parser.layer.LayerParser;
import psd.parser.layer.LayersSectionHandler;
import psd.parser.layer.additional.LayerTypeToolHandler;
import psd.parser.layer.additional.LayerTypeToolParser;
import psd.parser.layer.additional.Matrix;
import psd.parser.object.PsdDescriptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class MainFrame {

	private JFrame frame;
	private TreeLayerModel<Layer> treeLayerModel = new TreeLayerModel<Layer>();
	private PsdView psdView;

	public MainFrame() {
		frame = new JFrame("Psd Tool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTree tree = new JTree(treeLayerModel);
		tree.setBorder(null);
		tree.setPreferredSize(new Dimension(300, 400));

		psdView = new PsdView();

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(new JScrollPane(tree));
		split.setRightComponent(new JScrollPane(psdView));
		frame.getContentPane().add(split);
		frame.setJMenuBar(buildMenu());

		frame.pack();

	}

	public JFrame getFrame() {
		return frame;
	}

	private JMenuBar buildMenu() {
		JMenuBar bar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new OpenFileAction()).setAccelerator(
				KeyStroke.getKeyStroke("meta O"));
		bar.add(fileMenu);

		return bar;
	}

	private class OpenFileAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public OpenFileAction() {
			super("Open file");
		}

		@Override
        public void actionPerformed(ActionEvent e) {
            FileDialog fileDialog = new FileDialog(frame, "Open psd file", FileDialog.LOAD);
            // fileDialog.setDirectory("~/Downloads");
            fileDialog.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".psd");
                }
            });

            fileDialog.setVisible(true);
            if (fileDialog.getFile() != null) {
                File directory = new File(fileDialog.getDirectory());
                File psdFile = new File(directory, fileDialog.getFile());
                try {
                    Psd psd = new Psd(psdFile) {
                    	@Override
                    	protected Layer createLayer(LayerParser parser) {
                    		parser.putAdditionalInformationParser(LayerTypeToolParser.TAG, new LayerTypeToolParser(new LayerTypeToolHandler() {
								
								@Override
								public void typeToolTransformParsed(Matrix transform) {
									System.out.println("transform: " + transform);
									
								}
								
								@Override
								public void typeToolDescriptorParsed(int version, PsdDescriptor descriptor) {
									System.out.println("version: " + version + ", " + descriptor);
								}
							}));
                    		return super.createLayer(parser);
                    	}
                    };
                    treeLayerModel.setPsd(psd);
                    psdView.setPsd(psd);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
	}

}
