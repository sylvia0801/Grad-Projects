package GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class gradeTableModel extends AbstractTableModel{
	private String[] columnNames;
	private List<String> studentDto = new ArrayList<>();
	public gradeTableModel(List<String> columnNamesList) {
		Object[] temp = columnNamesList.toArray();
		this.columnNames =  Arrays.copyOf(temp,temp.length,String[].class);
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	//deep copy
	public void addRows(List<String> input) {
		for(String i : input) {
			this.studentDto.add(i);
		}
		fireTableRowsInserted(studentDto.size(), studentDto.size());
	}
}
