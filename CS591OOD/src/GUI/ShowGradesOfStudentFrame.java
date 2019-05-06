package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.impl.GradeDaoImpl;
import entity.Student;
import entity.dto.GradeDto;
import service.GradeService;
import service.impl.GradeServiceImpl;

import javax.swing.JList;
import javax.swing.JLabel;

public class ShowGradesOfStudentFrame extends JFrame {

	private JPanel contentPane;
	private Student stu;
	private GradeService gs = new GradeServiceImpl();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowGradesOfStudentFrame frame = new ShowGradesOfStudentFrame(
							new Student(1, 2, "Sichi", "Zhang", 0, "U123456"));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ShowGradesOfStudentFrame(Student stu) {
		this.stu = stu;
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JList list = new JList();
		
		Object[] data = null;
		List<GradeDto> grades = gs.getGrades(stu.getStuId());
		if (grades.size() > 0) {
			data = new Object[grades.size() + 1];
			double sum = 0.0;
			for (int i = 0; i < grades.size(); i++) {
				GradeDto gd = grades.get(i);
				data[i] = gd.getTaskName() + "/" + gd.getSubTaskName() + " score:" + gd.getScore() + " weight:"
						+ gd.getWeight()+"%";
				sum = sum + gd.getScore() * gd.getWeight()/100;
			}
			data[grades.size()] = "Overall: " + sum;
		}
		list.setListData(data);
		contentPane.add(list, BorderLayout.CENTER);
		JLabel stuNameLabel = new JLabel(stu.getFirstName() + " " + stu.getLastName());
		contentPane.add(stuNameLabel, BorderLayout.NORTH);
		this.setVisible(true);
	}


}
