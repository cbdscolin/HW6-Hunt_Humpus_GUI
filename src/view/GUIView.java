package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import guicontroller.IMazeGUIController;

/**
 * Class that handles functionalities related to view component in mvc design of hunt the wumpus
 * game. The class receives the number of rows, columns, percentage of bats, pits, the number of
 * players playing the game and the number of arrows a player can shoot. The view has interfaces
 * to receive this input and provides functionality to move and shoot a player in the dark maze.
 */
public class GUIView extends JFrame implements IView {

  private static final int INPUT_MAX_PADDING = 50;

  private static final int SPINNER_HEIGHT = 30;

  private static final int SPINNER_WIDTH = 60;

  private static final int LABEL_HEIGHT = 30;

  private static final int LABEL_WIDTH = 350;

  private static final int LABEL_X_POS = 0;

  private static final int SPINNER_X_POS = 500;

  private JTextArea errorMessage;

  private Container commandContainer;

  private Container inputContainer;

  private Container mazeContainer;

  private JScrollPane scrollPane;

  private SpinnerNumberModel rowModel;

  private SpinnerNumberModel colModel;

  private SpinnerNumberModel internalWallsModel;

  private SpinnerNumberModel externalWallsModel;

  private SpinnerNumberModel playerCountModel;

  private SpinnerNumberModel pitPercentModel;

  private SpinnerNumberModel batPercentModel;

  private SpinnerNumberModel arrowCountModel;

  private final IMazeGUIController mazeController;

  public GUIView(String ctx, IMazeGUIController mazeController) {
    super(ctx);
    if (mazeController == null) {
      throw new IllegalArgumentException("Controller can't be null in view");
    }

    this.mazeController = mazeController;
    errorMessage = this.createTextArea();
    this.add(errorMessage);

    setSize(800, 600);
    setLocation(50, 50);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(null);
    //this.setLayout(new GridLayout());
    this.createInputFrame();

    Dimension dim = new Dimension(800, 600);
    this.setPreferredSize(dim);
    initMazeContainer();
    this.pack();
    this.setVisible(true);
  }

  private JLabel getImageLabel() {
    JLabel label = null;
    try {
      label = new JLabel();
      BufferedImage bufImage =  ImageIO.read(getClass().getResource("/resources/htw/player.png"));
      label.setIcon(new ImageIcon(bufImage));
      label.setBounds(0, 0, 64, 64);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
      exception.printStackTrace();
    }
    return label;
  }

  private void initMazeContainer() {

    mazeContainer = new Container();
    JScrollPane scrollPane = new JScrollPane();
    mazeContainer.setBounds(0, 0, 600, 600);
    mazeContainer.setLayout(new GridLayout(4, 4));
    for (int ii = 0; ii < 4; ii++) {
      for (int jj = 0; jj < 4; jj++) {
        //mazeContainer.add(new JLabel("ey"));
        JLabel label = getImageLabel();
        mazeContainer.add(label);
      }
    }
    this.add(mazeContainer);
    mazeContainer.setVisible(false);
  }

  private JTextArea createTextArea() {
    JTextArea textArea = new JTextArea(2, 30);
    textArea.setFont(textArea.getFont().deriveFont(19f));
    textArea.setWrapStyleWord(true);
    textArea.setVisible(false);
    textArea.setLineWrap(true);
    textArea.setOpaque(false);
    textArea.setEditable(false);
    textArea.setFocusable(false);
    textArea.setForeground(Color.RED);
    textArea.setFont(UIManager.getFont("Label.font"));
    textArea.setBorder(UIManager.getBorder("Label.border"));
    textArea.setBounds(60, 30, 600, 30);
    return textArea;
  }

  public void showErrorMessage(String message) {
    errorMessage.setText(message);
    this.errorMessage.setVisible(true);
  }

  public void hideErrorMessage() {
    errorMessage.setText("");
    this.errorMessage.setVisible(false);
  }



  public void showInputScreen() {
    this.inputContainer.setVisible(true);
  }

  public void hideInputScreen() {
    this.inputContainer.setVisible(false);
  }

  public void displayMaze() {
    this.inputContainer.setVisible(false);
    this.mazeContainer.setVisible(true);
    pack();
  }

  private void createInputFrame() {

    int startPos = 100;

    inputContainer = new Container();

    JLabel rowLabel = new JLabel("Number of rows in maze: ");
    rowModel = new SpinnerNumberModel(5, 2, mazeController.getMaximumRows(),
            1);
    rowLabel.setBounds(LABEL_X_POS, startPos, LABEL_WIDTH, LABEL_HEIGHT);
    JSpinner rowScrollBar = new JSpinner(rowModel);
    rowScrollBar.setBounds(SPINNER_X_POS, startPos, SPINNER_WIDTH, SPINNER_HEIGHT);

    JLabel colLabel = new JLabel("Number of columns in maze: ");
    colModel = new SpinnerNumberModel(5, 2,
            mazeController.getMaximumColumns(), 1);
    colLabel.setBounds(LABEL_X_POS, startPos + INPUT_MAX_PADDING, LABEL_WIDTH, LABEL_HEIGHT);
    JSpinner colScrollBar = new JSpinner(colModel);
    colScrollBar.setBounds(SPINNER_X_POS, startPos + INPUT_MAX_PADDING, SPINNER_WIDTH,
            SPINNER_HEIGHT);

    JLabel internalWallLabel = new JLabel("Number of internal walls to be removed: ");
    internalWallLabel.setBounds(LABEL_X_POS, startPos + 2 * INPUT_MAX_PADDING, LABEL_WIDTH, SPINNER_HEIGHT);
    internalWallsModel = new SpinnerNumberModel(0, 0,
            Integer.MAX_VALUE, 1);
    JSpinner internalWallScrollBar = new JSpinner(internalWallsModel);
    internalWallScrollBar.setBounds(SPINNER_X_POS, startPos + 2 * INPUT_MAX_PADDING, SPINNER_WIDTH, SPINNER_HEIGHT);

    JLabel externalWallLabel = new JLabel("Number of external walls to be removed: ");
    externalWallLabel.setBounds(LABEL_X_POS, startPos + 3 * INPUT_MAX_PADDING, LABEL_WIDTH, LABEL_HEIGHT);
    externalWallsModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE,
            1);
    JSpinner externalWallScrollBar = new JSpinner(externalWallsModel);
    externalWallScrollBar.setBounds(SPINNER_X_POS, startPos + 3 * INPUT_MAX_PADDING, SPINNER_WIDTH, SPINNER_HEIGHT);

    JLabel playerCountLabel = new JLabel("Number of players in the game: ");
    playerCountLabel.setBounds(LABEL_X_POS, startPos + 4 * INPUT_MAX_PADDING, LABEL_WIDTH, LABEL_HEIGHT);
    playerCountModel = new SpinnerNumberModel(2, 1, 2, 1);
    JSpinner playerCountScrollBar = new JSpinner(playerCountModel);
    playerCountScrollBar.setBounds(SPINNER_X_POS, startPos + 4 * INPUT_MAX_PADDING, SPINNER_WIDTH, SPINNER_HEIGHT);

    JLabel batPercentLabel = new JLabel("Percentage of cells with Bats: ");
    batPercentLabel.setBounds(LABEL_X_POS, startPos + 5 * INPUT_MAX_PADDING, LABEL_WIDTH, LABEL_HEIGHT);
    batPercentModel = new SpinnerNumberModel(0, 0, 100, 1);
    JSpinner batPercentScrollBar = new JSpinner(batPercentModel);
    batPercentScrollBar.setBounds(SPINNER_X_POS, startPos + 5 * INPUT_MAX_PADDING, SPINNER_WIDTH, SPINNER_HEIGHT);


    JLabel pitPercentLabel = new JLabel("Percentage of cells with Pits: ");
    pitPercentLabel.setBounds(LABEL_X_POS, startPos + 6 * INPUT_MAX_PADDING, LABEL_WIDTH, LABEL_HEIGHT);
    pitPercentModel = new SpinnerNumberModel(0, 0, 100, 1);
    JSpinner pitPercentScrollBar = new JSpinner(pitPercentModel);
    pitPercentScrollBar.setBounds(SPINNER_X_POS, startPos + 6 * INPUT_MAX_PADDING, SPINNER_WIDTH, SPINNER_HEIGHT);

    JLabel arrowCountLabel = new JLabel("Number of arrows available per player: ");
    arrowCountLabel.setBounds(LABEL_X_POS, startPos + 7 * INPUT_MAX_PADDING, LABEL_WIDTH, LABEL_HEIGHT);
    arrowCountModel = new SpinnerNumberModel(2, 2, Integer.MAX_VALUE, 1);
    JSpinner arrowCountScrollBar = new JSpinner(arrowCountModel);
    arrowCountScrollBar.setBounds(SPINNER_X_POS, startPos + 7 * INPUT_MAX_PADDING, SPINNER_WIDTH, SPINNER_HEIGHT);

    addToInputContainer(rowLabel);
    addToInputContainer(rowScrollBar);
    addToInputContainer(colLabel);
    addToInputContainer(colScrollBar);
    addToInputContainer(internalWallLabel);
    addToInputContainer(internalWallScrollBar);
    addToInputContainer(externalWallLabel);
    addToInputContainer(externalWallScrollBar);
    addToInputContainer(playerCountLabel);
    addToInputContainer(playerCountScrollBar);
    addToInputContainer(batPercentLabel);
    addToInputContainer(batPercentScrollBar);
    addToInputContainer(pitPercentLabel);
    addToInputContainer(pitPercentScrollBar);
    addToInputContainer(arrowCountLabel);
    addToInputContainer(arrowCountScrollBar);

    JButton createMazeButton = new JButton("Create Maze");
    createMazeButton.addActionListener(e -> {
      mazeController.createMaze(rowModel.getNumber().intValue(), colModel.getNumber().intValue(),
              internalWallsModel.getNumber().intValue(), externalWallsModel.getNumber().intValue(),
              playerCountModel.getNumber().intValue(), pitPercentModel.getNumber().intValue(),
              batPercentModel.getNumber().intValue(), arrowCountModel.getNumber().intValue());
    });
    createMazeButton.setLayout(null);
    createMazeButton.setBounds(100, 500, 200, 30);
    createMazeButton.setAlignmentX(CENTER_ALIGNMENT);
    addToInputContainer(createMazeButton);
    inputContainer.setLayout(new BorderLayout());
    inputContainer.setBounds(0, 0, 600, 600);
    this.add(inputContainer);
  }

  private void addToInputContainer(JComponent component) {
    if ((component instanceof JLabel)) {
      component.setBorder(new EmptyBorder(0, 50, 0, 0));
    }
    inputContainer.add(component);
  }


}
