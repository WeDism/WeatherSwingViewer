package com.weather_viewer.gui.settings;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.neovisionaries.i18n.CountryCode;
import com.weather_viewer.functional_layer.application.IContext;
import com.weather_viewer.functional_layer.exceptions.EmptyCityException;
import com.weather_viewer.functional_layer.exceptions.EmptyCountryException;
import com.weather_viewer.functional_layer.services.delayed_task.IWorkerService;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.gui.general.General;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Settings extends JDialog implements SettingsFormDelegate {
    private final static Logger LOGGER = Logger.getLogger(Settings.class.getName());
    private final IContext context;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox cityIsFindCheckBox;
    private JTextField cityTextField;
    private JLabel searchLabel;
    private JComboBox<String> comboBoxCountry;
    private JLabel selectCountryLabel;
    private JButton searchButton;
    private JPanel loadingPanel;
    private JLabel loadingLabel;
    private boolean isAnimate = false;

    public Settings(IContext context) {
        this.context = context;
        this.buttonOK.setEnabled(false);
        this.addListeners();
        this.comboBoxCountry.setModel(
                new DefaultComboBoxModel<>(Stream.of(CountryCode.values())
                        .map(map -> map.toLocale().getCountry())
                        .collect(Collectors.toList())
                        .toArray(new String[CountryCode.values().length])));

        this.initJDialog();
    }

    private void initJDialog() {
        this.setTitle("Choose location");
        this.setIconImage(new ImageIcon(General.class.getResource("/images/PartlyCloudy.png")).getImage());
        this.setContentPane(this.contentPane);
        this.setModal(true);
        this.getRootPane().setDefaultButton(this.buttonOK);
        this.setResizable(false);
        this.setVisible(false);
        this.pack();
    }

    private void addListeners() {
        this.buttonOK.addActionListener(e -> this.onOK());

        this.buttonCancel.addActionListener(e -> this.onCancel());

        this.searchButton.addActionListener(e -> this.readDataFromForm());

        // call onCancel() when cross is clicked
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        this.contentPane.registerKeyboardAction(e -> this.onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.cityTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    super.keyPressed(e);
                    readDataFromForm();
                }
            }
        });
    }

    private void readDataFromForm() {
        try {
            ((IWorkerService) this.context.get(IWorkerService.class)).onSearch(
                    new Country(String.valueOf(this.comboBoxCountry.getSelectedItem()).toLowerCase()),
                    new City(this.cityTextField.getText().trim().toLowerCase()));
        } catch (EmptyCountryException | EmptyCityException ex) {
            JOptionPane.showMessageDialog((this), ex.getMessage());
        }
    }

    public void resetUI() {
        this.setVisible(true);
        this.cityIsFindCheckBox.setSelected(false);
        this.buttonOK.setEnabled(false);
    }

    @Override
    public void onFindLocation(boolean find) {
        this.cityIsFindCheckBox.setSelected(find);
        this.buttonOK.setEnabled(find);
        this.cityTextField.setEnabled(true);
        this.isAnimate = !isAnimate;
        this.loadingLabel.setVisible(isAnimate);
    }

    @Override
    public void onOK() {
        if (this.cityIsFindCheckBox.isSelected()) {
            IWorkerService instance = ((IWorkerService) this.context.get(IWorkerService.class));
            if (instance != null) {
                instance.onChangeLocationData();
                this.setVisible(false);
            } else LOGGER.log(Level.SEVERE, "IWorkerService is null");
        }
    }

    private void onCancel() {
        ((IWorkerService) this.context.get(IWorkerService.class)).resetExecutor();
        this.setVisible(false);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchLabel = new JLabel();
        searchLabel.setText("Search city");
        panel3.add(searchLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cityTextField = new JTextField();
        panel3.add(cityTextField, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        selectCountryLabel = new JLabel();
        selectCountryLabel.setName("");
        selectCountryLabel.setText("Select country");
        panel3.add(selectCountryLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxCountry = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        comboBoxCountry.setModel(defaultComboBoxModel1);
        comboBoxCountry.setName(" ");
        panel3.add(comboBoxCountry, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel3.add(spacer4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cityIsFindCheckBox = new JCheckBox();
        cityIsFindCheckBox.setEnabled(false);
        cityIsFindCheckBox.setSelected(false);
        cityIsFindCheckBox.setText("City is find");
        panel3.add(cityIsFindCheckBox, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        panel3.add(searchButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadingPanel = new JPanel();
        loadingPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        loadingPanel.setFocusable(false);
        loadingPanel.setVisible(true);
        panel3.add(loadingPanel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(64, 64), null, null, 1, false));
        loadingLabel = new JLabel();
        loadingLabel.setDoubleBuffered(true);
        loadingLabel.setFocusable(false);
        loadingLabel.setIcon(new ImageIcon(getClass().getResource("/gifs/search64.gif")));
        loadingLabel.setText("");
        loadingLabel.setVisible(false);
        loadingPanel.add(loadingLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
