package GUI;

import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import service.ClassesService;
import service.TaskService;
import service.impl.ClassesServiceImpl;
import service.impl.TaskServiceImpl;
import entity.Classes;
import entity.SubTask;
import entity.Task;
import entity.dto.TaskDto;

public class ClassPanel extends JFrame {
	private  TaskService ts = new TaskServiceImpl();
	JScrollPane scrollPane;
	JTable classTable;
	JFrame classCreationPopup;
	JTextField classNameTxt;
	JTextField classSemesterTxt;

	String className = "";
	String semester = "";

	public ClassPanel() {
		initComponents();
		populateTable();

	}

	public List<Classes> ListClass() {
		ClassesService classesService = new ClassesServiceImpl();
		List<Classes> list = classesService.getClassesList();
		return list;
	}

	public void populateTable() {
		DefaultTableModel model = (DefaultTableModel) classTable.getModel();
		model.setRowCount(0);
		List<Classes> list = ListClass();
		Object rowData[] = new Object[3];
		for (int i = 0; i < list.size(); i++) {
			rowData[0] = list.get(i).getClassId();
			rowData[1] = list.get(i).getSemester();
			rowData[2] = list.get(i).getClassName();
			model.addRow(rowData);
		}

	}

	private void initComponents() {
		scrollPane = new JScrollPane();
		classTable = new JTable() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(6, 1));
		JPanel scrollView = new JPanel();

		JButton removeClassButton = new JButton("Remove Class");
		removeClassButton.addActionListener(new RemoveClassListener());

		JButton addClassButton = new JButton("Add Class");
		addClassButton.addActionListener(new AddClassListener());

		JButton classSettingsButton = new JButton("Class Settings");
		classSettingsButton.addActionListener(new ClassSettingsListener());

		JButton switchGradeButton = new JButton("Grade View");
		switchGradeButton.addActionListener(new GradeViewActionListener());

		JButton switchStudentButton = new JButton("Student View");
		switchStudentButton.addActionListener(new StudentViewActionListener());

		JButton loadExistedButton = new JButton("Load Existed Criterias");
		loadExistedButton.addActionListener(new GradingCriteraLoadListener());

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		buttonsPanel.add(removeClassButton);
		buttonsPanel.add(addClassButton);
		buttonsPanel.add(classSettingsButton);
		buttonsPanel.add(switchGradeButton);
		buttonsPanel.add(switchStudentButton);
		buttonsPanel.add(loadExistedButton);
		buttonsPanel.setVisible(true);

		classTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Id", "Semester", "Class Name" }));
		scrollPane.setViewportView(classTable);

		getContentPane().setLayout(new BorderLayout());

		GroupLayout layout = new GroupLayout(scrollView);
		scrollView.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout
						.createSequentialGroup().addGap(32, 32, 32).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(61, Short.MAX_VALUE)));

		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout
						.createSequentialGroup().addContainerGap().addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(14, Short.MAX_VALUE)));

		add(scrollView, BorderLayout.EAST);
		add(buttonsPanel, BorderLayout.WEST);

		pack();
	}

	// public static void main(String args[]) {
	//
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// new ClassPanel().setVisible(true);
	// }
	// });
	// }

	public class RemoveClassListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int row = classTable.getSelectedRow();
				int modelRow = classTable.convertRowIndexToModel(row);
				DefaultTableModel model = (DefaultTableModel) classTable.getModel();

				int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
				String sem = model.getValueAt(modelRow, 1).toString();
				String className = model.getValueAt(modelRow, 2).toString();

				Classes rem = new Classes(id, className, sem);

				ClassesService cs = new ClassesServiceImpl();
				cs.deleteClass(rem);

				model.removeRow(modelRow);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, "Please select a class!");
			}
		}
	}

	public class AddClassListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			classCreationPopup = new JFrame();
			JPanel classCreation = new JPanel();

			classNameTxt = new JTextField("Enter Class Name");
			classSemesterTxt = new JTextField("Enter Semester");
			
			classNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					classNameTxt.setText("");
				}
			});
			
			classSemesterTxt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					classSemesterTxt.setText("");
				}
			});

			JButton createClassButton = new JButton("Create Class");
			createClassButton.addActionListener(new CreateClassActionListener());

			classCreation.add(classNameTxt);
			classCreation.add(classSemesterTxt);
			classCreation.add(createClassButton);
			classCreation.setVisible(true);

			classCreationPopup.add(classCreation);
			classCreationPopup.setVisible(true);
			classCreationPopup.pack();

		}

		private class CreateClassActionListener implements ActionListener {

//			public CreateClassActionListener() {
//				this.classId = 0;
//			}
			@Override
			public void actionPerformed(ActionEvent e) {
				className = classNameTxt.getText();
				semester = classSemesterTxt.getText();
				classNameTxt.setText("Enter Class Name");
				classSemesterTxt.setText("Enter Semester");
				
				classNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						classNameTxt.setText("");
					}
				});
				
				classSemesterTxt.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						classSemesterTxt.setText("");
					}
				});
				
				DefaultTableModel model = (DefaultTableModel) classTable.getModel();
				if (!className.isEmpty() && !semester.isEmpty()) {
					Classes add = new Classes(0, className, semester);

					model.addRow(new Object[] { "0", semester, className });

					ClassesService classesService = new ClassesServiceImpl();
					classesService.addClass(add);
					populateTable();

				}
				classCreationPopup.setVisible(false);
			}
		}
	}
	private class GradingCriteraLoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//load existed class setting
			//get selected row
			try {
				int row = classTable.getSelectedRow();
				int modelRow = classTable.convertRowIndexToModel(row);
				DefaultTableModel model = (DefaultTableModel) classTable.getModel();

				int classId = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
			
			
			
			
		//	System.out.println("id number is " + id);
			classCreationPopup = new JFrame();
			JPanel classCreation = new JPanel();

			classNameTxt = new JTextField("Enter Class Name");
			classSemesterTxt = new JTextField("Enter Semester");
			
			classNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					classNameTxt.setText("");
				}
			});
			
			classSemesterTxt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					classSemesterTxt.setText("");
				}
			});

			JButton createClassButton = new JButton("Create Class");
			CreateClassActionListener cl = new CreateClassActionListener(classId);
			createClassButton.addActionListener(cl);

			
			
			classCreation.add(classNameTxt);
			classCreation.add(classSemesterTxt);
			classCreation.add(createClassButton);
			classCreation.setVisible(true);
			

			classCreationPopup.add(classCreation);
			classCreationPopup.setVisible(true);
			classCreationPopup.pack();
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, "Please select a class!");
			}

			//int newClassId = cl.getNewClassId();
			//System.out.println("newClassId is " + newClassId);
			//set new task from taskDto

			
			
			
		}
		private class CreateClassActionListener implements ActionListener {
			int classId;
			public CreateClassActionListener(int classId) {
				this.classId = classId;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				className = classNameTxt.getText();
				semester = classSemesterTxt.getText();
				classNameTxt.setText("Enter Class Name");
				classSemesterTxt.setText("Enter Semester");
				
				classNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						classNameTxt.setText("");
					}
				});
				
				classSemesterTxt.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						classSemesterTxt.setText("");
					}
				});
				DefaultTableModel model = (DefaultTableModel) classTable.getModel();
				if (!className.isEmpty() && !semester.isEmpty()) {
					Classes add = new Classes(0, className, semester);

					model.addRow(new Object[] { "0", semester, className });

					ClassesService classesService = new ClassesServiceImpl();
					int newClassId = classesService.addClass(add);
					System.out.println("In cl class ID is " + newClassId);
					populateTable();
					
					List<TaskDto> taskDtoList = ts.getTaskList(classId);
					for(TaskDto tdo : taskDtoList) {
						Task newTask = new Task(0,newClassId,tdo.getTaskName(),tdo.getWeight());
						System.out.println("new added task is " + newTask);
						int taskId = ts.addTask(newTask);
						List<SubTask> subTaskList = tdo.getSubTaskList();
						for(SubTask st: subTaskList) {
							SubTask newSubTask = new SubTask(0,st.getSubTaskName(),taskId,st.getWeight());
							ts.addSubTask(newSubTask);
							System.out.println("new SubTask is " + newSubTask);
						}
					}

				}
				classCreationPopup.setVisible(false);
			}
		}
	}
	public class ClassSettingsListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				int row = classTable.getSelectedRow();
				int modelRow = classTable.convertRowIndexToModel(row);
				DefaultTableModel model = (DefaultTableModel) classTable.getModel();

				int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());

				// setVisible(false);
				new EditGradingCriteria(id).init();

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, "Please select a class!");
			}
		}
	}

	private class StudentViewActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int row = classTable.getSelectedRow();
				int modelRow = classTable.convertRowIndexToModel(row);
				DefaultTableModel model = (DefaultTableModel) classTable.getModel();

				int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());

				new StudentPanel(id).setVisible(true);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Please select a class!");
			}

		}
	}

	private class GradeViewActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int row = classTable.getSelectedRow();
				int modelRow = classTable.convertRowIndexToModel(row);
				DefaultTableModel model = (DefaultTableModel) classTable.getModel();

				int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());

				new gradeView(id).setVisible(true);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, "Please select a class!");
			}
		}
	}

}
