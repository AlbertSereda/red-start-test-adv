package org.redstart.testclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestJson {
    private String name = "Acehil";

    private int[] arrayInt = new int[]{4, 65, 223, 6, 234, 234, 234, 2345, 34};

    private List<Integer> listInt = new ArrayList<>(Arrays.asList(4, 65, 223, 6, 234, 234, 234, 2345, 34));

    private String[] arrayStr = new String[]{"fgh", "fh", "dfg", "dfg", "fgh", "sf", "dfg", "dfg", "dg"};

    private List<String> listStr = new ArrayList<>(Arrays.asList(arrayStr));

    private TestCell[] cells = new TestCell[]{new TestCell(), new TestCell(), new TestCell(), new TestCell(), new TestCell(),};

    private List<TestCell> cellsList = new ArrayList<>(Arrays.asList(cells));



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getArrayInt() {
        return arrayInt;
    }

    public void setArrayInt(int[] arrayInt) {
        this.arrayInt = arrayInt;
    }

    public String[] getArrayStr() {
        return arrayStr;
    }

    public void setArrayStr(String[] arrayStr) {
        this.arrayStr = arrayStr;
    }

    public List<String> getListStr() {
        return listStr;
    }

    public void setListStr(List<String> listStr) {
        this.listStr = listStr;
    }

    public TestCell[] getCells() {
        return cells;
    }

    public void setCells(TestCell[] cells) {
        this.cells = cells;
    }

    public List<TestCell> getCellsList() {
        return cellsList;
    }

    public void setCellsList(List<TestCell> cellsList) {
        this.cellsList = cellsList;
    }

    public List<Integer> getListInt() {
        return listInt;
    }

    public void setListInt(List<Integer> listInt) {
        this.listInt = listInt;
    }


}
