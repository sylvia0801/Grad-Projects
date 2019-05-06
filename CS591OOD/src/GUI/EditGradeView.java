package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import entity.Grade;
import entity.dto.StudentGradingDto;
import service.GradeService;
import service.impl.GradeServiceImpl;

public class EditGradeView extends JFrame {
	private JTable gradeTable;
	private String columnName;
	private int modifiedSubtaskId;
	private List<StudentGradingDto> rowDataList;
	private List<Integer> subTaskIdList;
	private HashMap<Integer, Integer> rowIndexGradeId = new HashMap<>();
	private GradeService gs = new GradeServiceImpl();

	public EditGradeView(String columnName, int modifiedSubtaskId, List<StudentGradingDto> rowDataList,
			List<Integer> subTaskIdList) {
		this.gradeTable = new JTable() {
			public void setValueAt(Object aValue, int rowIndex, int columnIndex)

			{
				Double num = null;
				try {
					num = Double.parseDouble((String) aValue);
					if (num > 100 || num < -100) {
						aValue = "";
						throw new Exception();
					} else if (num >= -100 && num < 0) {
						num = 100 + num;
						aValue = num.toString();
					}
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(null, "Only Number!");
					return;
				} catch (Exception e) {
					javax.swing.JOptionPane.showMessageDialog(null, "Out of range");
				}
				super.setValueAt(aValue, rowIndex, columnIndex);
			}
		};
		// this.orginalTable = originalGradeTable;
		this.columnName = columnName;
		this.modifiedSubtaskId = modifiedSubtaskId;
		this.rowDataList = rowDataList;
		this.subTaskIdList = subTaskIdList;

		this.setLayout(new BorderLayout());
		Container contentPane = this.getContentPane();

		JPanel tablePanel = createTablePanel();
		contentPane.add(tablePanel, BorderLayout.CENTER);

		this.setTitle("Edit");
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 543, 367);

		this.setVisible(true);

	}

	private JPanel createTablePanel() {

		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		tablePanel.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		gradeTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(gradeTable);

		setTableContent();
		gradeTable.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				// access the values of the model and save them to the file here
				int rowIndex = e.getFirstRow();

				// String buId = (String)gradeTable.getModel().getValueAt(rowIndex, 1);
				int gradeId = rowIndexGradeId.get(rowIndex);

				Object objectGrade = gradeTable.getModel().getValueAt(rowIndex, 2);
				Double newGrade = Double.valueOf(objectGrade.toString());

				updateGrade(gradeId, newGrade);

				System.out.println("Cell edit pos: " + e.getFirstRow());
				System.out.println("Cell editing " + newGrade);
			}
		});
		// if(gradeTable.isEditing()) {
		// System.out.println("Entering stop editting");
		// gradeTable.getCellEditor().stopCellEditing();
		// }

		return tablePanel;
	}

	private void updateGrade(int gradeId, double newGrade) {
		gs.changeGrade(new Grade(gradeId, newGrade, 0, 0));
		gradeView.setTableContent();
	}

	private void setTableContent() {
		DefaultTableModel tableModel = new DefaultTableModel() {
			boolean[] canEdit = new boolean[] { false, false, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};
		tableModel.addColumn("Name");
		tableModel.addColumn("BU Id");
		tableModel.addColumn(this.columnName);
		int i = 0;
		for (StudentGradingDto sgdto : this.rowDataList) {
			List<String> data = new ArrayList<>();
			String name = sgdto.getFirstName() + " " + sgdto.getLastName();
			data.add(name);

			String buId = sgdto.getBuId();
			data.add(buId);

			List<Grade> gradeList = sgdto.getGrades();
			for (Grade g : gradeList) {
				int subTaskId = g.getSubTaskId();
				if (modifiedSubtaskId == subTaskId) {
					rowIndexGradeId.put(i, g.getGradeId());
					data.add(Double.toString(g.getScore()));
				}

			}
			// this.rowDataList.add(data);
			String[] dataString = data.toArray(new String[0]);
			tableModel.addRow(dataString);
			i++;
		}

		gradeTable.setModel(tableModel);
	}

}
