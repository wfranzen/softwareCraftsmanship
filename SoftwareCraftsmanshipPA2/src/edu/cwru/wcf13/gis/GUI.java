package edu.cwru.wcf13.gis;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GUI extends JFrame {

    private Container pane;
    private JButton [][] buttonMapGrid;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuQuit;
    private JMenuItem menuCreateNewMap;
    private JMenuItem menuRectRegion;
    private JLabel mapInformation;
    private BiDimensionalMap currentMap;
    private BiDimensionalMap.Updater mapUpdater;
    private RectilinearRegion rectRegion;

    GUI() {
        super();
        pane = getContentPane();
        pane.setLayout(new GridLayout(3, 3)); // Set the layout width/height by size of map created
        setTitle("BiDimensionalMap GUI");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        initializeMap();
        initializeMenuBar();
        initializeInformationArea();
        mapInformation.setText("Options>Create New Map to get started...");

    }

    private void initializeMap() {
        JButton coordinate = new JButton();
        pane.add(coordinate);
    }
    private void initializeMenuBar() {

        menuCreateNewMap = new JMenuItem("Create New Map");
        menuCreateNewMap.addActionListener( action -> createNewMap() );

        menuQuit = new JMenuItem("Quit");
        menuQuit.addActionListener( action -> System.exit(0) );

        menuRectRegion = new JMenuItem("Rectangle Map");
        menuRectRegion.addActionListener( action -> rectMap() );

        menu = new JMenu("Options");
        menu.add(menuQuit);
        menu.add(menuCreateNewMap);
        menu.add(menuRectRegion);

        menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void initializeInformationArea() {

        mapInformation = new JLabel();
        pane.add(mapInformation, -1);
    }

    private Rectangle createNewMap() {

        Rectangle rect = askForRectangle();

        currentMap = new BiDimensionalMap(
                rect.rectBounds(rect.left(), rect.right()),
                rect.rectBounds(rect.bottom(), rect.top())
        );
        mapUpdater = currentMap.getUpdater();

        pane.removeAll();
        buttonMapGrid = new JButton[rect.right().intValue() + 1][rect.top().intValue() + 1];
        pane.setLayout(new GridLayout(rect.top().intValue() - rect.bottom().intValue() + 1, rect.right().intValue() - rect.left().intValue() + 1));

        for( int yCoord = rect.top().intValue()-1; yCoord >= rect.bottom().intValue(); yCoord --) {
            for( int xCoord = rect.left().intValue(); xCoord < rect.right().intValue(); xCoord ++) {

                JButton coordinate = new JButton("(" + xCoord + "," + yCoord + ")");
                buttonMapGrid[xCoord][yCoord] = coordinate;
                BigDecimal finalXCoord = new BigDecimal(xCoord);
                BigDecimal finalYCoord = new BigDecimal(yCoord);
                buttonMapGrid[xCoord][yCoord].addActionListener(action -> mapUpdater(finalXCoord, finalYCoord));
                pane.add(coordinate);
            }
        }
        initializeInformationArea();
        setVisible(true);
        return rect;
    }

    private void mapUpdater(BigDecimal xCoord, BigDecimal yCoord) {

        Coordinate currentCoord = new Coordinate(xCoord, yCoord);

        Object[] possibleValues = { "Check Values", "Set", "Add", "Add Everywhere", "Remove" };
        Object selectedValue = JOptionPane.showInputDialog(this,
                "Choose function:", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        switch ((String) selectedValue) {

            case "Check Values": {

                JOptionPane.showInternalMessageDialog(pane, currentMap.get(currentCoord),
                        "Coordinate Values", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            case "Set": {

                Object marker = JOptionPane.showInputDialog(pane, "Set the only marker to:", null).trim();

                mapUpdater.setCoordinate(currentCoord);
                mapUpdater.setSingleValue(marker);
                mapUpdater.set();
                break;
            }
            case "Add": {

                Object marker = JOptionPane.showInputDialog(pane, "Add the marker:", null).trim();

                mapUpdater.setCoordinate(currentCoord);
                mapUpdater.setSingleValue(marker);
                mapUpdater.add();
                break;
            }
            case "Add Everywhere": {

                Object marker = JOptionPane.showInputDialog(pane, "Add the marker to all:", null).trim();
                currentMap.addEverywhere(marker);
                break;
            }
            case "Remove": {

                mapUpdater.setCoordinate(currentCoord);
                mapUpdater.setValues(new HashSet());
                mapUpdater.set();
                break;
            }

        }
    }

    private void rectMap() {

        Set<Rectangle> rectCollection = new HashSet<>();
            rectCollection.add(createNewMap());

        while( true ) {
            int test = JOptionPane.showConfirmDialog(null,
                    "Add another rectangle to the map?", "Create Rectangle Map", JOptionPane.YES_NO_OPTION);

            if( test == JOptionPane.YES_OPTION ) {
                rectCollection.add(askForRectangle());
            } else break;
        }

        rectRegion = RectilinearRegion.of(rectCollection);
        BiDimensionalMap<Rectangle> rectRegionMap = rectRegion.getRectMap();

        Function<Rectangle, BigDecimal> left = Rectangle::left;
        Function<Rectangle, BigDecimal> right = Rectangle::right;
        Function<Rectangle, BigDecimal> bottom = Rectangle::bottom;
        Function<Rectangle, BigDecimal> top = Rectangle::top;
        Set<BigDecimal> yRegionSet = rectRegion.coordSet(bottom, top).stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
        Set<BigDecimal> xRegionSet = rectRegion.coordSet(left, right).stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));

        int largestXElement = Collections.max(xRegionSet).intValue();
        int largestYElement = Collections.max(yRegionSet).intValue();
        int smallestXElement = Collections.min(xRegionSet).intValue();
        int smallestYElement = Collections.min(yRegionSet).intValue();

        System.out.println(largestXElement);
        System.out.println(largestYElement);
        System.out.println(smallestXElement);
        System.out.println(smallestYElement);

        pane.removeAll();
        buttonMapGrid = new JButton[ largestXElement + 1][ largestYElement + 1];
        pane.setLayout(new GridLayout(largestYElement - smallestYElement + 2, largestXElement - smallestXElement + 1));

        for( BigDecimal yCoord : yRegionSet ){
            for( BigDecimal xCoord : xRegionSet ) {

                if( rectRegionMap.get(xCoord, yCoord).isEmpty() ) {
                    JButton coordinate = new JButton();
                    buttonMapGrid[xCoord.intValue()][yCoord.intValue()] = coordinate;
                    pane.add(coordinate);
                }
                else {
                    JButton coordinate = new JButton("(" + xCoord + "," + yCoord + ")");
                    buttonMapGrid[xCoord.intValue()][yCoord.intValue()] = coordinate;
                    buttonMapGrid[xCoord.intValue()][yCoord.intValue()].addActionListener(action -> mapUpdater(xCoord, yCoord));
                    pane.add(coordinate);
                }
            }
        }

        initializeInformationArea();
        setVisible(true);
    }

    private Rectangle askForRectangle() {
        JOptionPane.showInternalMessageDialog(pane, "Please enter the x-bounds and y-bounds of your rectangle next",
                "Create Rectangle", JOptionPane.INFORMATION_MESSAGE);

        int lowerX = Integer.parseInt(JOptionPane.showInputDialog(this, "Left bound X value:", null));
        int upperX = Integer.parseInt(JOptionPane.showInputDialog(this, "Right bound X value:", null));
        int lowerY = Integer.parseInt(JOptionPane.showInputDialog(this, "Left bound Y value:", null));
        int upperY = Integer.parseInt(JOptionPane.showInputDialog(this, "Right bound Y value:", null));

        return new Rectangle(
                new Coordinate(new BigDecimal(lowerX), new BigDecimal(lowerY)).validate(),
                new Coordinate(new BigDecimal(upperX), new BigDecimal(upperY)).validate()
        ).validate();
    }

    public static void main(String [] args) {

        SwingUtilities.invokeLater(GUI::new);
    }
}
