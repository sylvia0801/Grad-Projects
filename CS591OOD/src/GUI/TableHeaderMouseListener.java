package GUI;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import entity.Grade;
import entity.dto.StudentGradingDto;

public class TableHeaderMouseListener extends MouseAdapter {
    private JTable table;
    private List<StudentGradingDto> rowDataList;
    private List<Integer> subTaskIdList;
    public TableHeaderMouseListener(JTable table, List<StudentGradingDto> rowDataList,List<Integer> subTaskIdList) {
        this.table = table;
        this.rowDataList = rowDataList;
        this.subTaskIdList = subTaskIdList;
    }
     
    public void mouseClicked(MouseEvent event) {
        Point point = event.getPoint();
        int column = table.columnAtPoint(point);
         
    //    JOptionPane.showMessageDialog(table, "Column header #" + column + " is clicked");
        DefaultTableModel currentTableModel = (DefaultTableModel) table.getModel();
        
        //DefaultTableModel newTableModel = new DefaultTableModel();
       
        
        
        int modifiedSubtaskId = subTaskIdList.get(column);
        
        EditGradeView edgraView = new EditGradeView(currentTableModel.getColumnName(column),modifiedSubtaskId,
        		this.rowDataList,this.subTaskIdList);
        
        //table.setColumnSelectionInterval(column,column); 
       
        //set row
       

        
        
        
        // do your real thing here...
    }
}
