package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import service.GradeService;
import service.StudentService;
import service.impl.GradeServiceImpl;
import service.impl.StudentServiceImpl;
import entity.Student;
import entity.dto.GradeDto;

public class StudentPanel extends JFrame implements ActionListener, DocumentListener {

	JScrollPane scrollPane;
	JTable studentTable;
	JFrame studentPopup;

	JTextField firstNameTxt;
	JTextField lastNameTxt;
	JTextField BUIDTxt;
	String firstName = "Test";
	String lastName = "Fall 2019";
	String BUID = "";
	JButton switchClassButton, switchGradeButton;
	JTextField searchField;

	StudentService ss = new StudentServiceImpl();
	GradeService gs = new GradeServiceImpl();
	private int classID;

	public StudentPanel(int classID) {
		this.classID = classID;
		initComponents();
		populateTable(ListStudents(classID));
	}

	public List<Student> ListStudents(int classID) {
		StudentService ss = new StudentServiceImpl();
		List<Student> list = ss.getStudentList(classID);
		return list;
	}

	public void populateTable(List<Student> list) {
		DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
		model.setRowCount(0);
		Object rowData[] = new Object[4];
		for (int i = 0; i < list.size(); i++) {
			rowData[0] = list.get(i).getStuId();
			rowData[1] = list.get(i).getFirstName();
			rowData[2] = list.get(i).getLastName();
			rowData[3] = list.get(i).getBuId();
			model.addRow(rowData);
		}
	}

	private void initComponents() {

		scrollPane = new JScrollPane();
		studentTable = new JTable() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JPanel searchPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel scrollPanel = new JPanel();

		JButton removeStudentButton = new JButton("Remove Student");
		removeStudentButton.addActionListener(new RemoveStudentListener());

		JButton addStudentButton = new JButton("Add Student");
		addStudentButton.addActionListener(new AddStudentListener());

		switchGradeButton = new JButton("Show Grades");
		switchGradeButton.addActionListener(this);

		switchClassButton = new JButton("Load Student From CSV");
		switchClassButton.addActionListener(this);

		buttonsPanel.add(removeStudentButton);
		buttonsPanel.add(addStudentButton);
		buttonsPanel.add(switchClassButton);
		buttonsPanel.add(switchGradeButton);

		studentTable.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "Id", "First Name", "Last Name", "BU ID" }));
		scrollPane.setViewportView(studentTable);
		getContentPane().setLayout(new BorderLayout());
		GroupLayout layout = new GroupLayout(scrollPanel);
		scrollPanel.setLayout(layout);
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
		searchField = new JTextField();
		searchField.setHorizontalAlignment(SwingConstants.CENTER);
		searchField.setText("Enter Name/ID");
		searchField.setBounds(6, 6, 180, 20);
		searchField.setColumns(10);
		searchField.getDocument().addDocumentListener(this);
		searchPanel.add(searchField, BorderLayout.WEST);
		searchField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				searchField.setText("");
			}
		});
//		JButton searchBtn = new JButton("Search");
//		searchBtn.setBounds(179, 6, 117, 29);
//		searchPanel.add(searchBtn, BorderLayout.EAST);
		add(searchPanel, BorderLayout.NORTH);
		add(scrollPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
		pack();
	}

	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StudentPanel(2).setVisible(true);
			}
		});
	}

	private class RemoveStudentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int row = studentTable.getSelectedRow();
				int modelRow = studentTable.convertRowIndexToModel(row);
				DefaultTableModel model = (DefaultTableModel) studentTable.getModel();

				String BUID = model.getValueAt(modelRow, 0).toString();
				String firstName = model.getValueAt(modelRow, 1).toString();
				String lastName = model.getValueAt(modelRow, 2).toString();

				Student rem = new Student(Integer.parseInt(BUID), 2, firstName, lastName, 1, " ");

				StudentService ss = new StudentServiceImpl();
				ss.deleteStudent(rem);
				model.removeRow(modelRow);

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, "Please select a student!");
			}
		}
	}

	private class AddStudentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			studentPopup = new JFrame();
			JPanel studentCreation = new JPanel();

			JButton addStudent = new JButton("Add Student");
			addStudent.addActionListener(new CreateStudentActionListener());

			firstNameTxt = new JTextField("Enter First Name");
			lastNameTxt = new JTextField("Enter Last Name");
			BUIDTxt = new JTextField("Enter BUID");

			firstNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					firstNameTxt.setText("");
				}
			});

			lastNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					lastNameTxt.setText("");
				}
			});

			BUIDTxt.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					BUIDTxt.setText("");
				}
			});

			studentCreation.add(firstNameTxt);
			studentCreation.add(lastNameTxt);
			studentCreation.add(BUIDTxt);
			studentCreation.add(addStudent);
			studentCreation.setVisible(true);

			studentPopup.add(studentCreation);
			studentPopup.setVisible(true);
			studentPopup.pack();

		}

		private class CreateStudentActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				firstName = firstNameTxt.getText();
				lastName = lastNameTxt.getText();
				BUID = BUIDTxt.getText();

				firstNameTxt.setText("Enter First Name");
				lastNameTxt.setText("Enter Last Name");
				BUIDTxt.setText("Enter BUID");

				firstNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						firstNameTxt.setText("");
					}
				});

				lastNameTxt.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						lastNameTxt.setText("");
					}
				});

				BUIDTxt.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						BUIDTxt.setText("");
					}
				});
				DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
				if (!firstName.isEmpty() && !lastName.isEmpty() && !BUID.isEmpty()) {
					Student add = new Student(0, classID, firstName, lastName, 1, BUID);

					ss.addStudent(add);
					populateTable(ListStudents(classID));
				}
				studentPopup.setVisible(false);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == switchClassButton) {
			JFileChooser jf = new JFileChooser();

			FileFilter ff = new FileFilter() {

				@Override
				public String getDescription() {
					return "*.csv";
				}

				@Override
				public boolean accept(File f) {
					String name = f.getName();
					return name.toLowerCase().endsWith(".csv");
				}
			};
			jf.addChoosableFileFilter(ff);
			jf.setFileFilter(ff);
			jf.showOpenDialog(this);// 显示打开的文件对话框
			File f = jf.getSelectedFile();// 使用文件类获取选择器选择的文件
			String s = f.getAbsolutePath();// 返回路径名
			if (ss.LoadStudentFromCsv(s, classID)) {
				JOptionPane.showMessageDialog(null, "Load Successfull");
				populateTable(ListStudents(classID));
			} else {
				JOptionPane.showMessageDialog(null, "Failed");
			}
		} else if (e.getSource() == switchGradeButton) {
			try {
				int row = studentTable.getSelectedRow();
				int modelRow = studentTable.convertRowIndexToModel(row);
				DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
				int stuId = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
				List<GradeDto> grades = gs.getGrades(stuId);
				if(grades.size()>0) {
					new ShowGradesOfStudentFrame(ss.getStudent(stuId));
				}else {
					JOptionPane.showMessageDialog(null, "Student does not have grades.");
				}
				
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog(null, "Please select a student.");
			}
		}

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		populateTable(ss.searchStudent(searchField.getText() , classID));

	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		populateTable(ss.searchStudent(searchField.getText(),classID));

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		populateTable(ss.searchStudent(searchField.getText(),classID));

	}
}
