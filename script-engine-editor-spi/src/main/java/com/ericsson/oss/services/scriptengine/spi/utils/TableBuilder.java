package com.ericsson.oss.services.scriptengine.spi.utils;


import com.ericsson.oss.services.scriptengine.spi.dtos.HeaderRowDto;
import com.ericsson.oss.services.scriptengine.spi.dtos.RowCell;
import com.ericsson.oss.services.scriptengine.spi.dtos.RowDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TableBuilder{
    private final String title;

    List<RowCell> headerCells = new ArrayList<>();
    Map<Integer,List<RowCell>> mapOfRows = new TreeMap<>();
    Map<Integer, MutableInt> widthForColumn = new TreeMap<>();
    Map<Integer, RowCell> emptyCellForColumn = new TreeMap<>();

    //map to hold for each column the rows in which there is a value

    public TableBuilder() {
        this(null);
    }

    public int getNumberOfRows(){
        return mapOfRows.size();
    }

    public int getNumberOfColumns(){
        return widthForColumn.size();
    }

    /*
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     */
    public int getColumnIndexForHeader(final String header){
        for(int i =0; i < headerCells.size(); ++i){
            if(headerCells.get(i).getValue().equals(header)){
                return i;
            }
        }
       return -1;
    }

    public TableBuilder(final String title) {
        this.title = title;
    }

    public TableBuilder withHeader(final int columnIndex, final String headerCellValue){
        final int width = headerCellValue == null ? 0 : headerCellValue.length();
        updateColumnWidth(columnIndex, width);
        if(headerCells.size() == columnIndex){
            headerCells.add(new RowCell(headerCellValue,widthForColumn.get(columnIndex)));
        }else{
            headerCells.set(columnIndex,new RowCell(headerCellValue,widthForColumn.get(columnIndex)));
        }
        return this;
    }

    /**
     * Note that the column must be added in order e.g. 1 can not be added before 0,  22 can not be added before 23
     *
     *
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     */
    public TableBuilder withCell(final int rowIndex, final int columnIndex,final  String cellValue){
        final int cellWidth = cellValue == null ? 0 : cellValue.length();
        updateColumnWidth(columnIndex, cellWidth);

        List<RowCell> row =  mapOfRows.get(rowIndex);
        if(row == null){
            row = newRowWithExistingCells();
            mapOfRows.put(rowIndex,row);
        }
        row.set(columnIndex,new RowCell(cellValue,widthForColumn.get(columnIndex)));

        return this;
    }

    public List<RowDto> build() {
        final List<RowDto> rows = new ArrayList<>(mapOfRows.size()+1);
        if( headerCells.size() >0 || title != null){
            final HeaderRowDto headerRowDto = new HeaderRowDto(headerCells,title);
            rows.add(headerRowDto);
        }
        for(Integer rowIndex: mapOfRows.keySet()){
            rows.add(new RowDto(mapOfRows.get(rowIndex)));
        }
        return rows;
    }

    private TableBuilder addNewColumn(final int columnIndex, final int length) {
        final MutableInt refrenceIntegerForColumnCells = new MutableInt(length);
        widthForColumn.put(columnIndex, refrenceIntegerForColumnCells);
        final RowCell emptyCellForColumn = new RowCell(null, refrenceIntegerForColumnCells);
        this.emptyCellForColumn.put(columnIndex, emptyCellForColumn);

        for(List<RowCell> row : mapOfRows.values()){
            row.add(columnIndex,emptyCellForColumn);
        }

        return this;
    }

    private void updateColumnWidth(final int columnIndex, final int length) {
        final MutableInt width = widthForColumn.get(columnIndex);
        if(width == null){
            addNewColumn( columnIndex, length);
        }else if(width.intValue() < length){
            width.setValue(length);
        }
    }

    private List<RowCell> newRowWithExistingCells() {
        final List<RowCell> row = new ArrayList<>(emptyCellForColumn.size());
        for(Integer colIndex : emptyCellForColumn.keySet()){
            row.add(emptyCellForColumn.get(colIndex));
        }
        return row;
    }
}
