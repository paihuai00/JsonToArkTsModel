package com.arkts.jsontoarktsmodel;

import com.arkts.jsontoarktsmodel.utils.LogUtils;
import com.arkts.jsontoarktsmodel.widgets.ArkTsMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JsonToArk extends JDialog {
    private static final String TAG = "JsonToArk";

    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JTextArea jsonText;
    private JTextArea arkTsText;
    private JScrollPane arkTsPane;
    private JScrollPane jsonTsPane;

    public JsonToArk() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        setTitle("Json to ArkTs Model");
        setMinimumSize(new Dimension(800, 460));



        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String json = jsonText.getText();

        LogUtils.log(TAG, json);

        //核心代码
        ArkTsMaker arkTsMaker = new ArkTsMaker(json);
        String arkStr = arkTsMaker.makeArkTsModel();

        arkTsText.setText(arkStr);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        JsonToArk dialog = new JsonToArk();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
