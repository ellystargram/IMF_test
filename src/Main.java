import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import static database.DB.execute;
import static database.DB.getRows;

public class Main extends JFrame{
    int currentTry = 0;
    final int maxTry = 3;

    JLabel label1 = new JLabel("0");
    JLabel label2 = new JLabel("0");
    JLabel label3 = new JLabel("0");
    JPanel panel = new JPanel(null);
    JLabel tryDisplayer = new JLabel("남은 시도 횟수: "+(maxTry-currentTry));
    JButton[][] buttons = new JButton[4][3];
    JButton tryCodeButton = new JButton("시도");
    JButton informationButton = new JButton("i");
    int labelFocus = 0;
    String code;
    ArrayList<Object> problemCode = new ArrayList<>();
    ArrayList<String> problems = new ArrayList<>();
    String gameCode;
    JLabel teamCode;
    MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            JLabel candidate = (JLabel) e.getSource();
            if(candidate.equals(label1)){
                labelFocus = 0;
            }else if(candidate.equals(label2)){
                labelFocus = 1;
            }else if(candidate.equals(label3)){
                labelFocus = 2;
            }
            focusPainter();
        }
    };
    KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if(e.getKeyCode()==KeyEvent.VK_LEFT){
                labelFocus--;
                if(labelFocus<0){
                    labelFocus = 2;
                }
            }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                labelFocus++;
                if(labelFocus>2){
                    labelFocus = 0;
                }
            }else if(e.getKeyCode()==KeyEvent.VK_ENTER && maxTry>currentTry){
                String inputCode = label1.getText()+label2.getText()+label3.getText();
                if(inputCode.equals(code)) {
                    tryCodeButton.setBackground(Color.green);
                    panel.setBackground(Color.green);
                    JOptionPane.showMessageDialog(null, "성공");
                }
                else{
                    tryCodeButton.setBackground(Color.red);
                    panel.setBackground(Color.red);
                    Thread bounceAnime = new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            int defaultXLocation = tryCodeButton.getX();
                            for(int i=100;i>=0;i--){
                                try {
                                    Thread.sleep(8);
                                } catch (InterruptedException interruptedException) {
                                    break;
                                }
                                double x = defaultXLocation + Math.sin(i*Math.PI/10)*i/2;
                                tryCodeButton.setLocation((int)x, tryCodeButton.getY());
                                panel.repaint();
                            }
                            tryCodeButton.setLocation(defaultXLocation, tryCodeButton.getY());
                            panel.setBackground(null);
                        }
                    };
                    bounceAnime.start();

                }
            } else if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' && maxTry>currentTry){
                switch(labelFocus){
                    case 0:
                        label1.setText(e.getKeyChar()+"");
                        break;
                    case 1:
                        label2.setText(e.getKeyChar()+"");
                        break;
                    case 2:
                        label3.setText(e.getKeyChar()+"");
                        break;
                }
            } else if(e.getKeyChar() == 'i' || e.getKeyChar() == 'I'){
                informationButton.doClick();
            }
            focusPainter();
        }
    };

    JPanel buttonArea = new JPanel(new GridLayout(4,3,0,0));
    public static void main(String[] args) {
        new Main();
    }
    public Main(){
        super();
        setTitle("테스트");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setResizable(false);
        setUndecorated(true);

        var problemsRecords = getRows("select * from problems");

        int countProblems = getRows("select * from problems").size();

        Random rand = new Random();


        for (int i = 0; i < 3; i++) {
            int randomIndex = rand.nextInt(countProblems);
            if (problemCode.contains(problemsRecords.get(randomIndex).get(0))) {
                i--;
                continue;
            }
            problemCode.add(problemsRecords.get(randomIndex).get(0));
            problems.add(problemsRecords.get(randomIndex).get(1).toString());
        }

        execute("insert into problem_visibility () values ()");
        var problemVisibilityRecords = getRows("select * from problem_visibility");
        var thisProblemVisibilityRecord = problemVisibilityRecords.getLast();
        gameCode = thisProblemVisibilityRecord.getFirst().toString();
        code = problemsRecords.get((Integer) problemCode.get(0)-1).get(2).toString() + problemsRecords.get((Integer) problemCode.get(1)-1).get(2).toString() + problemsRecords.get((Integer) problemCode.get(2)-1).get(2).toString();

        panel.add(label1);
        panel.add(label2);
        panel.add(label3);

        panel.add(tryCodeButton);
        tryCodeButton.setBounds(150, 500, 300, 50);
        tryCodeButton.setFont(new Font("", Font.BOLD, 30));
        tryCodeButton.setOpaque(true);
        tryCodeButton.setBackground(Color.green);
        tryCodeButton.setForeground(Color.black);
        tryCodeButton.addKeyListener(keyAdapter);

        label1.setBackground(Color.white);
        label1.setOpaque(true);
        label1.setBounds(0, 200, 200, 200);
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setFont(new Font("", Font.BOLD, 100));

        label2.setBackground(Color.white);
        label2.setOpaque(true);
        label2.setBounds(200, 200, 200, 200);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setFont(new Font("", Font.BOLD, 100));

        label3.setBackground(Color.white);
        label3.setOpaque(true);
        label3.setBounds(400, 200, 200, 200);
        label3.setHorizontalAlignment(JLabel.CENTER);
        label3.setFont(new Font("", Font.BOLD, 100));

        focusPainter();

        buttonArea.setBounds(600, 0, 400, 600);
        buttonArea.setBackground(Color.BLACK);
        panel.add(buttonArea);

        add(panel);
        for(int i=0; i<4; i++){
            for(int j=0; j<3; j++){
                buttons[i][j] = new JButton();
                buttons[i][j].addKeyListener(keyAdapter);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setOpaque(true);
                buttonArea.add(buttons[i][j]);
                buttons[i][j].setText(i*3+j+1+"");
                if(i==3){
                    buttons[i][j].setText("0");
                }
                buttons[i][j].setFont(new Font("", Font.BOLD, 50));
                buttons[i][j].addActionListener(e -> {
                    String text = ((JButton)e.getSource()).getText();
                    if(text.equals("←")){
                        labelFocus--;
                        if(labelFocus<0){
                            labelFocus = 2;
                        }
                        focusPainter();
                    }else if(text.equals("→")){
                        labelFocus++;
                        if(labelFocus>2){
                            labelFocus = 0;
                        }
                        focusPainter();
                    }
                    else{
                        switch(labelFocus){
                            case 0:
                                label1.setText(text);
                                break;
                            case 1:
                                label2.setText(text);
                                break;
                            case 2:
                                label3.setText(text);
                                break;
                        }
                    }
                });
            }
        }
        buttons[3][0].setText("←");
        buttons[3][2].setText("→");

        label1.addMouseListener(mouseAdapter);
        label2.addMouseListener(mouseAdapter);
        label3.addMouseListener(mouseAdapter);

        panel.add(tryDisplayer);
        tryDisplayer.setBounds(0, 50, 600, 50);

        tryCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCode = label1.getText()+label2.getText()+label3.getText();
                if(inputCode.equals(code)) {
                    tryCodeButton.setBackground(Color.green);
                    panel.setBackground(Color.green);
                    for(int i=0; i<12;i++){
                        buttons[i/3][i%3].setEnabled(false);
                    }
                    tryCodeButton.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "성공");
                }
                else{
                    currentTry++;
                    tryDisplayer.setText("남은 시도 횟수: "+(maxTry-currentTry));

                    tryCodeButton.setBackground(Color.red);
                    panel.setBackground(Color.red);
                    Thread bounceAnime = new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            int defaultXLocation = tryCodeButton.getX();
                            for(int i=100;i>=0;i--){
                                try {
                                    Thread.sleep(8);
                                } catch (InterruptedException interruptedException) {
                                    break;
                                }
                                double x = defaultXLocation + Math.sin(i*Math.PI/10)*i/2;
                                tryCodeButton.setLocation((int)x, tryCodeButton.getY());
                                panel.repaint();
                            }
                            tryCodeButton.setLocation(defaultXLocation, tryCodeButton.getY());
                            panel.setBackground(null);
                        }
                    };
                    bounceAnime.start();
                    if(currentTry>=maxTry){
                        for(int i=0; i<12;i++){
                            buttons[i/3][i%3].setEnabled(false);
                        }
                        tryCodeButton.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "실패");
                    }
                }
            }
        });

        panel.add(informationButton);
        informationButton.setBounds(0, 0, 50, 50);
        informationButton.setFont(new Font("", Font.BOLD, 30));
        informationButton.setOpaque(true);
        informationButton.setBackground(Color.white);
        informationButton.setForeground(Color.black);
        informationButton.addKeyListener(keyAdapter);
        informationButton.addActionListener(e -> {
            var visabilityRecord = getRows("select * from problem_visibility where team_no = ?", gameCode).getFirst();
            if(visabilityRecord.get(labelFocus+1).toString().equals("true")){
                JOptionPane.showMessageDialog(null, problems.get(labelFocus));
            }
            else{
                JOptionPane.showMessageDialog(null, "해당 문제는 아직 공개되지 않았습니다.");
            }
        });

        teamCode = new JLabel(gameCode);
        teamCode.setBounds(0, 0, 600, 50);
        teamCode.setFont(new Font("", Font.BOLD, 30));
        teamCode.setHorizontalAlignment(JLabel.CENTER);
        teamCode.setOpaque(true);
        teamCode.setBackground(Color.white);
        teamCode.setForeground(Color.black);
        panel.add(teamCode);

        setVisible(true);
    }
    private void focusPainter(){
        switch(labelFocus){
            case 0:
                label1.setBackground(Color.white);
                label2.setBackground(Color.lightGray);
                label3.setBackground(Color.lightGray);
                break;
            case 1:
                label1.setBackground(Color.lightGray);
                label2.setBackground(Color.white);
                label3.setBackground(Color.lightGray);
                break;
            case 2:
                label1.setBackground(Color.lightGray);
                label2.setBackground(Color.lightGray);
                label3.setBackground(Color.white);
                break;
        }
    }
}