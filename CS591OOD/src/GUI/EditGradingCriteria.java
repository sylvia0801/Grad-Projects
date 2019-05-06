package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import entity.SubTask;
import entity.Task;
import entity.dto.TaskDto;
import service.TaskService;
import service.impl.TaskServiceImpl;

public class EditGradingCriteria extends JFrame {
	public EditGradingCriteria(int classId) {
		this.classId = classId;
	}

	private int classId;
	JTree tree;
	DefaultTreeModel model;
	JPanel treePanel;

	TaskService ts = new TaskServiceImpl();
	Map<String, Integer> taskNameIdMap = new HashMap<>();
	Map<String, Integer> subTaskNameIdMap = new HashMap<>();
	Map<String, String> newOldNameMap = new HashMap<>();
	
	
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Grading Criteria");
	DefaultMutableTreeNode assignment = new DefaultMutableTreeNode("Assignments/50%");
	DefaultMutableTreeNode exam = new DefaultMutableTreeNode("Exams/50%");

	TreePath movePath;

	JButton addParentButton = new JButton("Add Category");
	JButton addChildButton = new JButton("Add Sub-Category");
	JButton deleteButton = new JButton("Delete");
	JButton editButton = new JButton("Edit");
	JButton saveButton = new JButton("Save");

	public void init() {
	
		treePanel = new LoadJTreePanel(classId);
		this.tree = ((LoadJTreePanel) treePanel).getTree();
		this.getContentPane().add(treePanel, BorderLayout.EAST);
		
		this.setTitle("Grading Criteria");
		tree.setRootVisible(false);
		model = (DefaultTreeModel) tree.getModel();
		DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
		tree.setEditable(true);
		TreePath tp = new TreePath(dtm.getPathToRoot(assignment));
		tree.setSelectionPath(tp);
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				if (tp != null) {
					movePath = tp;
				}
			}

			public void mouseReleased(MouseEvent e) {
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());

				if (tp != null && movePath != null) {
					if (movePath.isDescendant(tp) && movePath != tp) {
						JOptionPane.showMessageDialog(null, "Cannot Move", "Illegal Transaction",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else if (movePath != tp) {
						System.out.println(tp.getLastPathComponent());
						((DefaultMutableTreeNode) tp.getLastPathComponent())
								.add((DefaultMutableTreeNode) movePath.getLastPathComponent());
						movePath = null;
						tree.updateUI();
					}
				}
			}
		};
		tree.addMouseListener(ml);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1));

		addParentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
				EditCategory cateInfo = new EditCategory();
				cateInfo.setVisible(true);

				JButton save = cateInfo.btnSave;
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String name = cateInfo.getTaskName();
						String percent = cateInfo.getTaskPercent();
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name + "/" + percent + "%");
						parent.add(newNode);
						TreeNode[] nodes = model.getPathToRoot(newNode);
						TreePath path = new TreePath(nodes);
						tree.scrollPathToVisible(path);
						tree.updateUI();
						cateInfo.dispose();
					}
				});
			}
		});
		panel.add(addParentButton);

		addChildButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				EditCategory cateInfo = new EditCategory();
				cateInfo.setVisible(true);
				

				JButton save = cateInfo.btnSave;
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String name = cateInfo.getTaskName();
						String percent = cateInfo.getTaskPercent();
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name + "/" + percent + "%");
						selectedNode.add(newNode);
						TreeNode[] nodes = model.getPathToRoot(newNode);
						TreePath path = new TreePath(nodes);
						tree.scrollPathToVisible(path);
						tree.updateUI();
						cateInfo.dispose();
					}
				});
			}
		});
		panel.add(addChildButton);

		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				try {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (selectedNode != null && selectedNode.getParent() != null) {
						if(taskNameIdMap.containsKey(selectedNode)) {
							ts.deleteTask(new Task(taskNameIdMap.get(selectedNode), 0, null, 0));
						}
						else if (subTaskNameIdMap.containsKey(selectedNode)) {
							ts.deleteSubTask(new SubTask(subTaskNameIdMap.get(selectedNode), null, 0, 0));
						}
						model.removeNodeFromParent(selectedNode);
						
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					JOptionPane.showMessageDialog(null, "Please select a category!");
				}
			}
		});
		panel.add(deleteButton);

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				EditCategory cateInfo = new EditCategory();
				cateInfo.setVisible(true);

				JButton save = cateInfo.btnSave;
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						//newOldNameMap = new HashMap<>();
						
						String name = cateInfo.getTaskName();
						String percent = cateInfo.getTaskPercent();
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
								.getLastSelectedPathComponent();
						String oldString = selectedNode.getUserObject().toString();
						String newString = name + "/" + percent + "%";
						if(((LoadJTreePanel) treePanel).getTaskByString(oldString) != 0) {

								newOldNameMap.put(newString, oldString);
							
						}
					
						
						selectedNode.setUserObject(newString);
						
						
						TreeNode[] nodes = model.getPathToRoot(selectedNode);
						TreePath path = new TreePath(nodes);
						tree.scrollPathToVisible(path);
						tree.updateUI();
						cateInfo.dispose();
					}
				});
			}
		});
		panel.add(editButton);

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
					
				
				// add task to db
				root = (DefaultMutableTreeNode) tree.getModel().getRoot();
				if(!inputValidation(root)) {

				}
				else {
					taskNameIdMap = ((LoadJTreePanel) treePanel).getTaskIdMap(); 
					int numOfTask = root.getChildCount();
					for (int i = 0; i < numOfTask; i++) {
						TreeNode taskNode = root.getChildAt(i);
						String taskString =  ((DefaultMutableTreeNode) taskNode).getUserObject().toString();
						System.out.println("Task String is " + taskString);
						System.out.println(taskNameIdMap);
						if(newOldNameMap.containsKey(taskString)) {
							String oldTaskName = newOldNameMap.get(taskString);
							int taskId = taskNameIdMap.get(oldTaskName);
							String[] arrayOfTask = taskString.split("/");
							String taskName = arrayOfTask[0];
							String taskPerce = arrayOfTask[1].substring(0, arrayOfTask[1].length() - 1);
							Task task = new Task(taskId, classId, taskName, Double.valueOf(taskPerce));
							ts.editTask(task);
						}
						else {
							if( (((LoadJTreePanel) treePanel).getTaskByString(taskString)) == 0){
								String[] arrayOfTask = taskString.split("/");
								String taskName = arrayOfTask[0];
								String taskPerce = arrayOfTask[1].substring(0, arrayOfTask[1].length() - 1);
								//totalTaskPerce.add(Double.valueOf(taskPerce));
								Task task = new Task(0, classId, taskName, Double.valueOf(taskPerce));
								int taskId = ts.addTask(task);
								taskNameIdMap.put(taskString, taskId);
							}

						}
					}
					subTaskNameIdMap = ((LoadJTreePanel) treePanel).getSubTaskIdMap();
					for (int i = 0; i < numOfTask; i++) {
						DefaultMutableTreeNode taskNode = (DefaultMutableTreeNode)root.getChildAt(i);							
						String taskString =  ((DefaultMutableTreeNode) taskNode).getUserObject().toString();
						String[] arrayOfTask = taskString.split("/");					
						String taskPerce = arrayOfTask[1].substring(0, arrayOfTask[1].length() - 1);
						Double target = Double.valueOf(taskPerce);	

						int taskId = taskNameIdMap.get(taskString);
						int numOfSubTask = taskNode.getChildCount();
						for (int j = 0; j < numOfSubTask; j++) {
							DefaultMutableTreeNode subTaskNode = (DefaultMutableTreeNode) taskNode.getChildAt(j);
							Object obj = subTaskNode.getUserObject();
							String subTaskString = obj.toString();
							if(newOldNameMap.containsKey(subTaskString)) {
								String oldName = newOldNameMap.get(subTaskString);
								int subTaskId = subTaskNameIdMap.get(oldName);
								String[] arryOfSubTask = subTaskString.split("/");
								String subTaskName = arryOfSubTask[0];
								String subTaskPerce = arryOfSubTask[1].substring(0, arryOfSubTask[1].length() - 1);
								//totalSubTaskPerce.add(Double.valueOf(subTaskPerce));
								SubTask subTask = new SubTask(subTaskId, subTaskName, taskId, Double.valueOf(subTaskPerce));
								//subTaskToBeEdited.add(subTask);
								ts.editSubTask(subTask);
							}
							else {
								if( (((LoadJTreePanel) treePanel).getSubTaskByString(taskString)) == 0) {
									String[] arryOfSubTask = subTaskString.split("/");
									String subTaskName = arryOfSubTask[0];
									String subTaskPerce = arryOfSubTask[1].substring(0, arryOfSubTask[1].length() - 1);
									//totalSubTaskPerce.add(Double.valueOf(subTaskPerce));
									//System.out.println("The double perce is " + subTaskPerce);
									SubTask subTask = new SubTask(0, subTaskName, taskId, Double.valueOf(subTaskPerce));
									ts.addSubTask(subTask);
								}

								//	subTaskToBeAdded.add(subTask);
							}
						}
					}
				}
				newOldNameMap = new HashMap<>();
				((LoadJTreePanel) treePanel).init();
				//this.tree = ((LoadJTreePanel) treePanel).getTree();
			//	this.getContentPane().add(treePanel, BorderLayout.EAST);
				//gradeView.setTableContent();
			}
		});
		panel.add(saveButton);

		this.getContentPane().add(panel, BorderLayout.WEST);
		this.pack();
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	protected boolean inputValidation(DefaultMutableTreeNode root) {
		int numOfTask = root.getChildCount();
		double totalTaskWeight = 0;
		for(int i = 0; i < numOfTask; i++) {
			TreeNode taskNode = root.getChildAt(i);
			String taskString =  ((DefaultMutableTreeNode) taskNode).getUserObject().toString();
			String[] arrayOfTask = taskString.split("/");
			String taskPerce = arrayOfTask[1].substring(0, arrayOfTask[1].length() - 1);
			double weight = Double.valueOf(taskPerce);
			totalTaskWeight += weight;
		}
		if(totalTaskWeight != 100) {
			JOptionPane.showMessageDialog(null,"total task weight must be equal to 100");
			return false;
		}
		
		//subnode
		for(int i = 0; i < numOfTask; i++) {
			DefaultMutableTreeNode taskNode = (DefaultMutableTreeNode)root.getChildAt(i);
			String taskString =  ((DefaultMutableTreeNode) taskNode).getUserObject().toString();
			String[] arrayOfTask = taskString.split("/");					
			String taskPerce = arrayOfTask[1].substring(0, arrayOfTask[1].length() - 1);
			Double taskWeight = Double.valueOf(taskPerce);
			int numOfSubTask = taskNode.getChildCount();
			double totalSubWeight = 0;
			for (int j = 0; j < numOfSubTask; j++) {
				
				DefaultMutableTreeNode subTaskNode = (DefaultMutableTreeNode) taskNode.getChildAt(j);
				Object obj = subTaskNode.getUserObject();
				String subTaskString = obj.toString();
				String[] arryOfSubTask = subTaskString.split("/");
				String subTaskPerce = arryOfSubTask[1].substring(0, arryOfSubTask[1].length() - 1);
				Double weight = Double.valueOf(subTaskPerce);
				totalSubWeight += weight;
			}
			System.out.println("totalsubweight is " + totalSubWeight);
			if(totalSubWeight != taskWeight) {
				JOptionPane.showMessageDialog(null,"total task weight must be equal to " + taskWeight);
				return false;
			}
		}
		
		return true;
		
	}
	


	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	//public static void main(String[] args) {
	//	new EditGradingCriteria(2).init();
	//}
}