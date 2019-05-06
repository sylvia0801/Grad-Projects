package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public class DBUtil {
	public static Connection getConnection() {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:easy_grader.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		return c;
	}
	
	public static int insertAndGetId(String sql,Object[] params) {
		Connection connection = getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int id = 0;
	    try {
	        pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        for (int i = 0; i < params.length; i++) {
	        	pstmt.setString(i + 1, String.valueOf(params[i]));
			}
	        pstmt.executeUpdate();
	        rs = pstmt.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        return -1;
	    }
	    return id;
	}

	// execute a sql statement
	public static boolean dosql(String sql) {
		boolean isSucceed = false;
		Connection conn = getConnection();
		try {
			System.out.println("sql:"+sql);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			isSucceed = ps.execute();
			System.out.println("dosql: " + sql);
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSucceed;
	}

	// retrieve data with out user input
	public static CachedRowSet select(String sql) throws SQLException {
		CachedRowSet crs = null;
		try {
			System.out.println("sql:"+sql);
			
			crs = new CachedRowSetImpl();
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			crs.populate(rs);
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return crs;
	}

	// retrieve data with user input
	public static CachedRowSet select(String sql, Object[] params) {
		CachedRowSet crs = null;
		try {
			System.out.println("sql:"+sql);
			System.out.print("Params: ");
			for(Object param: params) {
				System.out.print(" "+param);
			}
			System.out.println("");
			crs = new CachedRowSetImpl();
			Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setString(i + 1, String.valueOf(params[i]));
			}
			ResultSet rs = ps.executeQuery();
			crs.populate(rs);
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return crs;
	}
	public static boolean update(String sql,Object[] params) {
		boolean isSucceed = false;
		Connection conn = getConnection();
		try {
			System.out.println("sql:"+sql);
			System.out.print("Params: ");
			for(Object param: params) {
				System.out.print(param+" ");
			}
			System.out.println("");
			PreparedStatement ps = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setString(i + 1, String.valueOf(params[i]));
			}
			isSucceed = ps.execute();			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSucceed;
	}

}
