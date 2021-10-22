import javax.swing.*;
import java.awt.event.*;

public class MvpControllerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tx_base_package;
    private JTextField tx_activity;
    private JTextField tx_layout;
    private OnOwnerInputListener onOwnerInputListener = null;

    public MvpControllerDialog(String pack,OnOwnerInputListener onOwnerInputListener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.onOwnerInputListener = onOwnerInputListener;
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        tx_base_package.setText(pack);
        tx_activity.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                tx_layout.setText("activity_"+HumpUtil.camelToUnderline(tx_activity.getText()));
            }
        });
    }

    private void onOK() {
        // add your code here
        String pack = tx_base_package.getText();
        String activity = tx_activity.getText();
        String xmlName = tx_layout.getText();
        System.out.println("create text " + activity + " --> " + xmlName);

        if (!activity.isEmpty() && activity != null && !xmlName.isEmpty()){
            if (onOwnerInputListener!=null){
                onOwnerInputListener.submit(activity,xmlName,pack);
            }
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /*public static void main(String[] args) {
        MvpControllerDialog dialog = new MvpControllerDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/


    public interface OnOwnerInputListener{
        void submit(String activity,String act_layout,String pack);
    }
}
