package com.ericsson.oss.services.scriptengine.spi.utils;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.oss.services.scriptengine.spi.dtos.HeaderRowDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.RowCell;
import com.ericsson.oss.services.scriptengine.spi.dtos.RowDto;

public class TableBuilderTest {

    public static final String TABLE_NAME = "table name";
    TableBuilder tableBuilder;
    public String[] HEADER_ROW = null;
    public String[] ROW_VALUE_1 = null;
    public String[] ROW_VALUE_2 = null;
    public String[] ROW_VALUE_3 = null;
    public String[][] MULTI_ROW_VALUES = null;

    @Before
    public void setUp(){
        tableBuilder = new TableBuilder(TABLE_NAME);

        HEADER_ROW = new String[]{"H 1st", "H 2nd val", "H third value"};
        ROW_VALUE_1 = new String[]{"1 1st", "1 2nd val", "1 third value"};
        ROW_VALUE_2 = new String[]{"2 1st", "2 2nd val", "2 third value"};
        ROW_VALUE_3 = new String[]{"3 1st", "3 2nd val", "3 third value"};
        MULTI_ROW_VALUES = new String[][]{ROW_VALUE_1, ROW_VALUE_2, ROW_VALUE_3};
    }

    @Test
    public void canCreateTableWithName(){
        assertEquals(new HeaderRowDto(new ArrayList<RowCell>(), TABLE_NAME), tableBuilder.build().get(0));
    }

    @Test
    public void canCreateTableWithNoName(){
        tableBuilder = new TableBuilder();
        tableBuilder.withHeader(0, "some value");
        assertNull(((HeaderRowDto) tableBuilder.build().get(0)).getTitle());
        assertEquals(1,tableBuilder.build().size());
    }

    @Test
    public void canCreateTableWithAddedHeader(){
        final String firstVal = "1st";
        List<RowCell> rowCells = new ArrayList<>();
        rowCells.add(new RowCell(firstVal,new MutableInt(firstVal.length())));
        HeaderRowDto headerRowDto = new HeaderRowDto( rowCells, TABLE_NAME);

        tableBuilder.withHeader(0,firstVal);
        assertTrue(headerRowDto.equals(tableBuilder.build().get(0)));
        assertEquals(headerRowDto, tableBuilder.build().get(0));
    }

    @Test
    public void canCreateTableWithAddedHeaders(){
        HeaderRowDto headerRowDto =  getHeaderRowDtoForArrayValues(ROW_VALUE_1);

        tableBuilder.withHeader(0,ROW_VALUE_1[0]);
        tableBuilder.withHeader(1,ROW_VALUE_1[1]);
        tableBuilder.withHeader(2,ROW_VALUE_1[2]);

        assertEquals(headerRowDto, tableBuilder.build().get(0));

    }

    @Test
    public void canCreateTableWithNullValuesForSomeHeaderCells(){

        final String value[] = {"1st", "2nd val",null,"third value",null};
        HeaderRowDto headerRowDto = getHeaderRowDtoForArrayValues(value);

        tableBuilder.withHeader(0,value[0]);
        tableBuilder.withHeader(1,value[1]);
        tableBuilder.withHeader(2,value[2]);
        tableBuilder.withHeader(3,value[3]);
        tableBuilder.withHeader(4,value[4]);

        assertEquals(headerRowDto, tableBuilder.build().get(0));
    }

    @Test
    public void canCreateTableWithRowsAndValidHeader(){
        tableBuilder.withCell(0,0,ROW_VALUE_1[0]);
        tableBuilder.withCell(0,1,ROW_VALUE_1[1]);
        tableBuilder.withCell(0,2,ROW_VALUE_1[2]);

        List<RowCell> rowCells = new ArrayList<>();
        final HeaderRowDto headerRowDto = new HeaderRowDto( rowCells, TABLE_NAME);
        assertEquals(headerRowDto, tableBuilder.build().get(0));

    }

    @Test
    public void canCreateTableHeadLessTable(){
        tableBuilder = new TableBuilder();
        tableBuilder.withCell(0,0,ROW_VALUE_1[0]);
        tableBuilder.withCell(0,1,ROW_VALUE_1[1]);
        tableBuilder.withCell(0,2,ROW_VALUE_1[2]);

        List<RowDto> singleRow = tableBuilder.build();
        assertEquals(1, singleRow.size());
        assertFalse("Row should not be a HeaderRowDto ", singleRow instanceof HeaderRowDto);
    }

    @Test
    public void canCreateTableWithAddedRowCells(){
        RowDto rowDto = getRowDtoForArrayValues(ROW_VALUE_1);

        tableBuilder.withCell(0,0,ROW_VALUE_1[0]);
        tableBuilder.withCell(0,1,ROW_VALUE_1[1]);
        tableBuilder.withCell(0,2,ROW_VALUE_1[2]);

        assertEquals(rowDto, tableBuilder.build().get(1));

    }

    @Test
    public void canCreateTableWithAddedMultipleRowCells(){
        RowDto rowDto1 = getRowDtoForArrayValues(ROW_VALUE_1);
        RowDto rowDto2 = getRowDtoForArrayValues(ROW_VALUE_2);
        RowDto rowDto3 = getRowDtoForArrayValues(ROW_VALUE_3);

        addRowsAndColsToTableBuilderFor2dArray(MULTI_ROW_VALUES);

        final List<RowDto> rows = tableBuilder.build();
        assertEquals(rowDto1, rows.get(1));
        assertEquals(rowDto2, rows.get(2));
        assertEquals(rowDto3, rows.get(3));
    }

    @Test
    public void canCreateTableWithAddedMultipleRowCellsByColumn(){
        RowDto rowDto1 = getRowDtoForArrayValues(ROW_VALUE_1);
        RowDto rowDto2 = getRowDtoForArrayValues(ROW_VALUE_2);
        RowDto rowDto3 = getRowDtoForArrayValues(ROW_VALUE_3);

        //all rows must have same number of cols
        for(int col = 0; col< MULTI_ROW_VALUES[0].length ; ++col){
            for(int row=0; row< MULTI_ROW_VALUES.length; ++row){
                tableBuilder.withCell(row,col, MULTI_ROW_VALUES[row][col]);
            }
        }

        List<RowDto> rows = tableBuilder.build();
        assertEquals(rowDto1, rows.get(1));
        assertEquals(rowDto2, rows.get(2));
        assertEquals(rowDto3, rows.get(3));
    }


    @Test
    public void canCreateTableWithNullRowCells(){
        String[] rowVal1 = new String[]{"1 1st", "1 2nd val",null, "1 third value",null};
        String[] rowVal2  = new String[]{"2 1st", null, null, "2 2nd val", "2 third value"};
        String[] rowVal3  = new String[]{null,"3 1st", "3 2nd val", "3 third value",null};
        String[][] allRows = {rowVal1,rowVal2,rowVal3};

        RowDto rowDto1 = getRowDtoForArrayValues(rowVal1);
        RowDto rowDto2 = getRowDtoForArrayValues(rowVal2);
        RowDto rowDto3 = getRowDtoForArrayValues(rowVal3);

        addRowsAndColsToTableBuilderFor2dArray(allRows);

        List<RowDto> rows = tableBuilder.build();
        checkValueOfWidths(rows.subList(1,rows.size()-1));
    }

    @Test
    public  void canCreateTableWhereRowsHaveDiffNumberOfCols(){
        String[] rowVal1 = new String[]{"1 1st", "1 2nd val",null, "1 third value",null,null};
        String[] rowVal2  = new String[]{"2 1st", null, "2 2nd val", "2 third value"};
        String[] rowVal3  = new String[]{null,"3 1st", "3 2nd val", null,null,null,"3 third value"};
        String[][] allVals = {rowVal1,rowVal2,rowVal3};

        RowDto rowDto1 = getRowDtoForArrayValues(rowVal1);
        List<RowCell> row1Elements = new ArrayList(rowDto1.getElements());
        row1Elements.add(new RowCell(null, 13));
        rowDto1 = new RowDto(row1Elements);
        RowDto rowDto2 = getRowDtoForArrayValues(rowVal2);
        List<RowCell> row2Elements = new ArrayList(rowDto2.getElements());
        row2Elements.add(new RowCell(null, 0));
        row2Elements.add(new RowCell(null, 0));
        row2Elements.add(new RowCell(null, 13));
        rowDto2 = new RowDto(row2Elements);
        RowDto rowDto3 = getRowDtoForArrayValues(rowVal3);

        addRowsAndColsToTableBuilderFor2dArray(allVals);

        List<RowDto> rows = tableBuilder.build();
        checkValueOfWidths(rows.subList(1,rows.size()-1));

    }

    @Test
    public void assertThatSizeOfRowIsUsedToSetColumnSize(){
        String longVal = "23 character long value";
        String[] rowWithLongStringValue = {null,longVal};
        String[][] allVals = {ROW_VALUE_1,ROW_VALUE_2,ROW_VALUE_3,rowWithLongStringValue};

        addRowsAndColsToTableBuilderFor2dArray(allVals);


        List<RowDto> rows = tableBuilder.build();
        assertEquals(longVal.length(), rows.get(1).getElements().get(1).getWidth());
        assertEquals(longVal.length(), rows.get(2).getElements().get(1).getWidth());
        assertEquals(longVal.length(), rows.get(3).getElements().get(1).getWidth());
        assertEquals(longVal.length(), rows.get(4).getElements().get(1).getWidth());

    }

    @Test
    public void assertThatSizeOfColumnHeaderIsUsedToSetColumnSize(){
        tableBuilder.withHeader(0,HEADER_ROW[0]);
        tableBuilder.withHeader(1,HEADER_ROW[1]);
        tableBuilder.withHeader(2,HEADER_ROW[2]);

        addRowsAndColsToTableBuilderFor2dArray(MULTI_ROW_VALUES);

        List<RowDto> rows = tableBuilder.build();
        checkValueOfWidths(rows);

        String longVal = "23 character long value";
        tableBuilder.withCell(0,2,longVal);

        rows = tableBuilder.build();
        assertEquals(longVal.length(), rows.get(0).getElements().get(2).getWidth());
        checkValueOfWidths(rows);
    }

    @Test
    public void canGetNumberOfRowsWhenNoCellsAdded(){
        assertEquals(0, tableBuilder.getNumberOfRows());
    }

    @Test
    public void canGetNumberOfColsWhenNoCellsAdded(){
        assertEquals(0, tableBuilder.getNumberOfColumns());
    }

    @Test
    public void canGetNumberOfRowsAfterCellsAdded(){
        addRowsAndColsToTableBuilderFor2dArray(MULTI_ROW_VALUES);
        assertEquals(3, tableBuilder.getNumberOfRows());
    }

    @Test
    public void canGetNumberOfColsAfterCellsAdded(){
        String[][] multiVal =  {ROW_VALUE_1};
        addRowsAndColsToTableBuilderFor2dArray(multiVal);
        assertEquals(3, tableBuilder.getNumberOfColumns());
    }

    @Test
    public void canGetIndexOfHeaderWithNoHeaderValue(){
        assertEquals(-1,tableBuilder.getColumnIndexForHeader(HEADER_ROW[1]));
    }

    @Test
    public void canGetIndexOfHeader(){
        for(int i =0; i < HEADER_ROW.length; i++ ){
            tableBuilder.withHeader(i, HEADER_ROW[i]);
        }
        assertEquals(1,tableBuilder.getColumnIndexForHeader(HEADER_ROW[1]));
    }

    private void addRowsAndColsToTableBuilderFor2dArray(String[][] allVals) {
        for(int row = 0; row<allVals.length ; ++row){
            for(int col=0; col<allVals[row].length; ++col){
                tableBuilder.withCell(row,col,allVals[row][col]);
            }
        }
    }


    private void checkValueOfWidths(List<RowDto> rows) {
        for(int col=0; col<rows.get(0).getElements().size();++col){
            int colWidth = rows.get(0).getElements().get(col).getWidth();
            for(RowDto row :rows){
                assertEquals(colWidth, row.getElements().get(col).getWidth());
            }
        }
    }

    private RowDto getRowDtoForArrayValues(String[] value){
        List<RowCell> rowCells = getRowCellsForArray(value);
        return new RowDto( rowCells);

    }

    private HeaderRowDto getHeaderRowDtoForArrayValues(String[] value) {
        List<RowCell> rowCells = getRowCellsForArray(value);
        return new HeaderRowDto( rowCells, TABLE_NAME);
    }

    private List<RowCell> getRowCellsForArray(String[] value) {
        List<RowCell> rowCells = new ArrayList<>();
        int width = 0;
        for(int i =0; i< value.length; ++i){
            width = value[i] == null ? 0 : value[i].length();
            rowCells.add(new RowCell(value[i],new MutableInt(width)));
        }
        return rowCells;
    }

    @Test
    public void canReplaceHeaders(){
        tableBuilder.withHeader(0, "1st");
        tableBuilder.withHeader(0, "1ST");
        List<RowDto> build = tableBuilder.build();
        assertEquals("1ST", build.get(0).getElements().get(0).getValue());
    }
}
