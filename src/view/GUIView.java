package view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import guicontroller.IMazeGUIController;
import maze.Direction;

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

  private boolean isControlPressed;

  private JLabel playerTurnLabel;

  private JLabel gameStatusLabel;

  private JLabel validDirectionsLabel;

  private Container commandContainer;

  private Container inputContainer;

  private JPanel mazePanel;

  private JPanel mazeCommandPanel;

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

  private JTextField infoField;

  private int rows;

  private int columns;

  private int externalWalls;

  private int internalWalls;

  private int playerCount;

  private int batPercent;

  private int pitPercent;

  private int arrowCount;

  public GUIView(String ctx, IMazeGUIController mazeController) {
    super(ctx);
    if (mazeController == null) {
      throw new IllegalArgumentException("Controller can't be null in view");
    }

    this.mazeController = mazeController;
    this.isControlPressed = false;
    setSize(800, 600);
    setLocation(20, 20);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //this.setLayout(null);
    this.setLayout(new BorderLayout());
    this.createInputFrame();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setPreferredSize(screenSize);
    initMazeContainer();
    this.addKeyListeners();
    this.pack();
    this.setVisible(true);
    this.rows = 0;
    this.columns = 0;
  }

  private JLabel getImageLabel(Image bufImage) {
    JLabel label = null;
    try {
      label = new JLabel();
      label.setBounds(0, 0, 64, 64);
      //Image bufImage =  ImageIO.read(getClass().getResource(imagePath));
      bufImage = bufImage.getScaledInstance(label.getWidth(), label.getHeight(),
              Image.SCALE_AREA_AVERAGING);
      label.setIcon(new ImageIcon(bufImage));
      //label.setBorder(new EmptyBorder(0, 0, 0,0));
    } catch (Exception exception) {
      this.showErrorMessage("Cannot display maze: " + exception.getMessage());
    }
    return label;
  }

  private void initMazeContainer() {
    mazeCommandPanel = new JPanel();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.7;
    gbc.weighty = 0.7;
    gbc.fill = GridBagConstraints.BOTH;
    GridBagLayout gbLayout = new GridBagLayout();

    mazeCommandPanel.setLayout(gbLayout);
    mazeCommandPanel.setBounds(0, 0, 800, 600);

    mazeContainer = new Container();
    mazePanel = new JPanel();
    scrollPane = new JScrollPane();
    mazeContainer.setLayout(null);
    scrollPane.setPreferredSize(new Dimension(400, 400));
    mazeCommandPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
    mazePanel.add(mazeContainer);
    scrollPane = new JScrollPane(mazePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    mazeCommandPanel.add(scrollPane, gbc);
    this.add(mazeCommandPanel);
    this.mazeCommandPanel.setVisible(false);
    commandContainer = new Container();
    gameStatusLabel = getCustomJLabel(32, Color.RED);
    playerTurnLabel = getCustomJLabel(24, Color.BLACK);
    validDirectionsLabel = getCustomJLabel(30, Color.BLACK);
    commandContainer.add(gameStatusLabel);
    commandContainer.add(validDirectionsLabel);
    commandContainer.add(playerTurnLabel);
    commandContainer.setVisible(true);
    commandContainer.setBounds(0, 0, 200, 500);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 0.15;
    gbc.weighty = 0.15;
    mazeCommandPanel.add(commandContainer, gbc);
    infoField = new JTextField();
    infoField.setText("   Press w, a, s or d key to move players. Press ctrl and w, a,"
            + " s or d together to shoot. Press q to restart the game! Player 1 is marked as"
            + " black circle.");
    infoField.setEditable(false);
    infoField.setFont(new Font(null, Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    mazeCommandPanel.add(infoField, gbc);
    commandContainer.setLayout(new BoxLayout(commandContainer, BoxLayout.Y_AXIS));
  }

  private int getArrowPower() {
    String arrowPower = JOptionPane.showInputDialog("Enter the power of arrow greater than 0: ");
    try {
      return Integer.parseInt(arrowPower);
    } catch (Exception exception) {
      showErrorMessage("Enter an integer: " + exception.getMessage());
    }
    return 0;
  }

  private JLabel getCustomJLabel(int size, Color color) {
    JLabel label = new JLabel();
    Font f = new Font(null, Font.PLAIN, size);
    label.setFont(f);
    label.setForeground(color);
    return label;
  }

  private void addKeyListeners() {
    this.requestFocus();
    this.setFocusable(true);
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public synchronized void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          isControlPressed = true;
        }
      }

      @Override
      public synchronized void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
          showInputScreen();
        }
        if (!mazeContainer.isEnabled() || !mazeCommandPanel.isVisible()) {
          return;
        }
        Direction direction = null;
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          isControlPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
          direction = Direction.NORTH;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
          direction = Direction.SOUTH;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
          direction = Direction.WEST;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
          direction = Direction.EAST;
        }
        if (direction != null) {
          if (!isControlPressed) {
            mazeController.movePlayerInDirection(direction);
          } else {
            isControlPressed = false;
            int power = getArrowPower();
            mazeController.shootPlayerInDirection(direction, power);
          };
        }
      }
    });
  }

  @Override
  public void endGameWithMessage(String message) {
    gameStatusLabel.setText(message.toUpperCase());
    validDirectionsLabel.setVisible(false);
    this.mazeContainer.setEnabled(false);
  }

  public void sendCellImagesToView(Image[][] images) {
    mazeContainer.removeAll();
    int rows = images.length;
    int cols = images[0].length;
    //this.scrollPane.setBounds(20, 20, 550, 450);
    GridLayout layout = new GridLayout(rows, cols);
    mazeContainer.setLayout(layout);
    for (int ii = 0; ii < rows; ii++) {
      for (int jj = 0; jj < cols; jj++) {
        JLabel label = getImageLabel(images[ii][jj]);
        mazeContainer.add(label);
      }
    }
  }

  private JTextArea getTextArea(int fontSize, Color color) {
    JTextArea textArea = new JTextArea(2, 2);
    JScrollPane scrollPane = new JScrollPane( textArea );
    scrollPane.setBounds(0, 0, 20, 10);
    Font font = new Font(null, Font.PLAIN, fontSize);
    textArea.setFont(font);
    textArea.setWrapStyleWord(true);
    //textArea.setLayout(new FlowLayout());
    //textArea.setVisible(false);
    textArea.setLineWrap(true);
    textArea.setOpaque(false);
    textArea.setEditable(false);
    textArea.setFocusable(false);
    textArea.setForeground(color);
    textArea.setFont(UIManager.getFont("Label.font"));
    textArea.setBorder(UIManager.getBorder("Label.border"));
    commandContainer.add(scrollPane);
    //textArea.setBounds(0, 0, 50, 30);
    return textArea;
  }

  public void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  public void showGameStatusMessage(String message) {
    this.gameStatusLabel.setVisible(true);
    this.gameStatusLabel.setText(message);
  }

  public void showValidDirectionsMessage(String message) {
    this.validDirectionsLabel.setVisible(true);
    this.validDirectionsLabel.setText(message);
  }

  public void showPlayerTurnMessage(String message) {
    this.playerTurnLabel.setVisible(true);
    this.playerTurnLabel.setText(message);
  }

  public void showInputScreen() {
    this.inputContainer.setVisible(true);
    this.mazeCommandPanel.setVisible(false);
    this.mazeContainer.setEnabled(false);
  }

  public void hideInputScreen() {
    this.inputContainer.setVisible(false);
  }

  public void showMaze() {
    this.mazeContainer.setEnabled(true);
    this.inputContainer.setVisible(false);
    this.mazeCommandPanel.setVisible(true);
    this.scrollPane.setVisible(true);
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
    JCheckBox checkBox = new JCheckBox("Repeat previous maze");
    checkBox.setBorderPaintedFlat(true);
    checkBox.setBounds(400, 470, 200, 100);
    createMazeButton.addActionListener(e -> createMaze(checkBox.isSelected()));
    createMazeButton.setLayout(null);
    createMazeButton.setBounds(100, 500, 200, 30);
    createMazeButton.setAlignmentX(CENTER_ALIGNMENT);
    addToInputContainer(checkBox);
    addToInputContainer(createMazeButton);
    inputContainer.setLayout(new BorderLayout());
    inputContainer.setBounds(0, 0, 600, 600);
    this.add(inputContainer);
  }

  private void createMaze(boolean usePast) {
    if (!usePast || (rows == 0 || columns == 0)) {
      rows = rowModel.getNumber().intValue();
      columns = colModel.getNumber().intValue();
      internalWalls = internalWallsModel.getNumber().intValue();
      externalWalls = externalWallsModel.getNumber().intValue();
      playerCount = playerCountModel.getNumber().intValue();
      batPercent = batPercentModel.getNumber().intValue();
      pitPercent = pitPercentModel.getNumber().intValue();
      arrowCount = arrowCountModel.getNumber().intValue();
    }
    mazeController.createMaze(rows, columns, internalWalls, externalWalls, playerCount,
            pitPercent, batPercent, arrowCount, usePast);
  }


  private void addToInputContainer(JComponent component) {
    if ((component instanceof JLabel)) {
      component.setBorder(new EmptyBorder(0, 50, 0, 0));
    }
    inputContainer.add(component);
  }


}
