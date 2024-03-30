import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static database.DB.execute;
import static database.DB.getRows;

public class Manager extends JFrame {
    public static void main(String[] args) {
        new Manager();
    }
    JLabel userNoLabel = new JLabel("User No: ");
    int minimum;
    JTextField userNo = new JTextField();
    JButton userNoPlus = new JButton("+");
    JButton userNoMinus = new JButton("-");
    JPanel userControlPanel = new JPanel(null);
    JPanel controlButtonPanel = new JPanel(null);

    JButton problem1true = new JButton("보여주기");
    JButton problem1false = new JButton("숨기기");
    JButton problem2true = new JButton("보여주기");
    JButton problem2false = new JButton("숨기기");
    JButton problem3true = new JButton("보여주기");
    JButton problem3false = new JButton("숨기기");

    JLabel problem1Label = new JLabel("Problem 1");
    JLabel problem2Label = new JLabel("Problem 2");
    JLabel problem3Label = new JLabel("Problem 3");
    JTextField consoleSQL = new JTextField();
    JButton consoleExecute = new JButton("Execute");
    JPanel consolePanel = new JPanel(null);
    public Manager(){
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Manager");

        this.setUndecorated(true);
        this.setResizable(false);

        this.add(userControlPanel,"North");

        userControlPanel.add(userNoLabel);
        userNoLabel.setBounds(0, 0, 200, 60);
        userNoLabel.setFont(new Font("", Font.PLAIN, 20));

        userControlPanel.add(userNo);
        userControlPanel.setPreferredSize(new Dimension(1000,60));
        userNo.setBounds(200, 0, 600, 60);
        userNo.setText("1");
        userNo.setFont(new Font("", Font.PLAIN, 20));
        userNo.setEditable(true);
        userNo.addKeyListener(labelKey);

        userControlPanel.add(userNoPlus);
        userNoPlus.setBounds(800, 0, 100, 60);
        userNoPlus.setFont(new Font("", Font.PLAIN, 20));
        userNoPlus.addActionListener(e -> {
            int userNoInt = Integer.parseInt(userNo.getText());
            userNoInt++;
            var records = getRows("select * from problem_visibility");
            if(userNoInt > records.size()){
                userNoInt = records.size();
            }

            userNo.setText(String.valueOf(userNoInt));
            updateOptionHighlight();
        });

        userControlPanel.add(userNoMinus);
        userNoMinus.setBounds(900, 0, 100, 60);
        userNoMinus.setFont(new Font("", Font.PLAIN, 20));
        userNoMinus.addActionListener(e -> {
            int userNoInt = Integer.parseInt(userNo.getText());
            userNoInt--;
            if(userNoInt < 1){
                userNoInt = 1;
            }
            userNo.setText(String.valueOf(userNoInt));
            updateOptionHighlight();
        });

        controlButtonPanel.add(problem1true);
        problem1true.setBounds(100, 0, 200, 200);
        problem1true.setFont(new Font("", Font.PLAIN, 20));
        problem1true.addActionListener(e -> {
            execute("update problem_visibility set first_problem_unlocked = true where team_no = ?", userNo.getText());
            updateOptionHighlight();
        });
        problem1true.setOpaque(true);

        controlButtonPanel.add(problem1false);
        problem1false.setBounds(100, 300, 200, 200);
        problem1false.setFont(new Font("", Font.PLAIN, 20));
        problem1false.addActionListener(e -> {
            execute("update problem_visibility set first_problem_unlocked = false where team_no = ?", userNo.getText());
            updateOptionHighlight();
        });
        problem1false.setOpaque(true);

        controlButtonPanel.add(problem2true);
        problem2true.setBounds(400, 0, 200, 200);
        problem2true.setFont(new Font("", Font.PLAIN, 20));
        problem2true.addActionListener(e -> {
            execute("update problem_visibility set second_problem_unlocked = true where team_no = ?", userNo.getText());
            updateOptionHighlight();
        });
        problem2true.setOpaque(true);

        controlButtonPanel.add(problem2false);
        problem2false.setBounds(400, 300, 200, 200);
        problem2false.setFont(new Font("", Font.PLAIN, 20));
        problem2false.addActionListener(e -> {
            execute("update problem_visibility set second_problem_unlocked = false where team_no = ?", userNo.getText());
            updateOptionHighlight();
        });
        problem2false.setOpaque(true);

        controlButtonPanel.add(problem3true);
        problem3true.setBounds(700, 0, 200, 200);
        problem3true.setFont(new Font("", Font.PLAIN, 20));
        problem3true.addActionListener(e -> {
            execute("update problem_visibility set last_problem_unlocked = true where team_no = ?", userNo.getText());
            updateOptionHighlight();
        });
        problem3true.setOpaque(true);

        controlButtonPanel.add(problem3false);
        problem3false.setBounds(700, 300, 200, 200);
        problem3false.setFont(new Font("", Font.PLAIN, 20));
        problem3false.addActionListener(e -> {
            execute("update problem_visibility set last_problem_unlocked = false where team_no = ?", userNo.getText());
            updateOptionHighlight();
        });
        problem3false.setOpaque(true);

        controlButtonPanel.add(problem1Label);
        problem1Label.setBounds(100, 200, 200, 100);
        problem1Label.setFont(new Font("", Font.PLAIN, 20));

        controlButtonPanel.add(problem2Label);
        problem2Label.setBounds(400, 200, 200, 100);
        problem2Label.setFont(new Font("", Font.PLAIN, 20));

        controlButtonPanel.add(problem3Label);
        problem3Label.setBounds(700, 200, 200, 100);
        problem3Label.setFont(new Font("", Font.PLAIN, 20));

        this.add(controlButtonPanel,"Center");

        var records = getRows("select * from problem_visibility");
        if(records.isEmpty()){
            JOptionPane.showMessageDialog(null, "팀이 없습니다.");
            System.exit(0);
        }
        minimum = Integer.parseInt(records.getFirst().getFirst().toString());
        userNo.setText(String.valueOf(minimum));
        updateOptionHighlight();


        consolePanel.add(consoleSQL);
        consolePanel.setPreferredSize(new Dimension(1000,60));
        consoleSQL.setBounds(0, 0, 800, 60);
        consoleSQL.setFont(new Font("", Font.PLAIN, 20));
        consoleSQL.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    consoleExecute.doClick();
                }
            }
        });
        consolePanel.add(consoleExecute);
        consoleExecute.setBounds(800, 0, 200, 60);
        consoleExecute.setFont(new Font("", Font.PLAIN, 20));
        consoleExecute.addActionListener(e -> {
            execute(consoleSQL.getText());
            updateOptionHighlight();
        });
        this.add(consolePanel, "South");

        this.setVisible(true);
    }

    KeyAdapter labelKey = new KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            JTextField label = (JTextField) e.getSource();
            if(label.getText().isEmpty()){
                return;
            }
            int labelValue;
            try{
                labelValue = Integer.parseInt(label.getText());
            }
            catch (Exception exception){
                label.setText(String.valueOf(minimum));
                return;
            }
            var records = getRows("select * from problem_visibility");
            if(labelValue> records.size()){
                label.setText(String.valueOf(records.size()));
            }
            if(labelValue<minimum){
                label.setText(String.valueOf(minimum));
            }
            updateOptionHighlight();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            JTextField label = (JTextField) e.getSource();
            if(label.getText().isEmpty()){
                return;
            }
            int labelValue = minimum;
            try{
                labelValue = Integer.parseInt(label.getText());
            }catch (Exception exception){
                label.setText(String.valueOf(minimum));
            }
            var records = getRows("select * from problem_visibility");
            if(labelValue> records.size()){
                label.setText(String.valueOf(records.size()));
            }
            updateOptionHighlight();
        }
    };
    private void updateOptionHighlight(){
        var record = getRows("select * from problem_visibility where team_no = ?", userNo.getText()).getFirst();
        if(record.get(1).equals(true)){
            problem1true.setBackground(Color.GREEN);
            problem1false.setBackground(Color.WHITE);
        }else{
            problem1true.setBackground(Color.WHITE);
            problem1false.setBackground(Color.RED);
        }

        if(record.get(2).equals(true)){
            problem2true.setBackground(Color.GREEN);
            problem2false.setBackground(Color.WHITE);
        }else{
            problem2true.setBackground(Color.WHITE);
            problem2false.setBackground(Color.RED);
        }

        if(record.get(3).equals(true)){
            problem3true.setBackground(Color.GREEN);
            problem3false.setBackground(Color.WHITE);
        }else{
            problem3true.setBackground(Color.WHITE);
            problem3false.setBackground(Color.RED);
        }
    }
}
