package tools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import entity.Student;
import service.StudentService;
import service.impl.StudentServiceImpl;

import com.csvreader.CsvReader;

public class CsvUtil {
	public static void LoadCsv(String csvFilePath,int classId) throws IOException {
		List<Student> sList = new ArrayList<Student>();
		StudentService ss = new StudentServiceImpl();
			ArrayList<String[]> csvFileList = new ArrayList<String[]>();
			// 定义一个CSV路径
			// String csvFilePath =
			// "/Users/zhang/Downloads/gc_19sprgcascs112_c1_fullgc_2019-04-23-16-14-59.csv";
			
			// 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
			CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
			// 跳过表头 如果需要表头的话，这句可以忽略
			reader.readHeaders();
			// 逐行读入除表头的数据
			while (reader.readRecord()) {
				System.out.println(reader.getRawRecord());
				csvFileList.add(reader.getValues());
			}
			reader.close();
			// 遍历读取的CSV文件
			for (int row = 0; row < csvFileList.size(); row++) {
				Student s = new Student(0, classId, csvFileList.get(row)[1], csvFileList.get(row)[0], 1, csvFileList.get(row)[3]);
				ss.addStudent(s);
			}
	}
/*
	public static void main(String[] args) {
		/*
		 * CSVReader reader = new CSVReader(new FileReader(
		 * "/Users/zhang/Downloads/gc_19sprgcascs112_c1_fullgc_2019-04-21-19-55-37.txt")
		 * ); String[] nextLine; while ((nextLine = reader.readNext()) != null) { //
		 * nextLine[] is an array of values from the line System.out.println(nextLine[0]
		 * + "etc..."); }
		 
		String csvFilePath = "/Users/zhang/Downloads/gc_19sprgcascs112_c1_fullgc_2019-04-23-16-14-59.csv";
		LoadCsv(csvFilePath, 2);
	}
*/
}
