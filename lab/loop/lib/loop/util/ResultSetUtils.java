package loop.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResultSetUtils {
	public static List<Map<String, Object>> convertResultSet(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, Object>> rtn = new LinkedList<Map<String, Object>>();
		while (rs.next()){
			Map<String, Object> mapOfColValues = new LinkedHashMap<String, Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				String key = rsmd.getColumnName(i);
				Object value = rs.getObject(i);
				mapOfColValues.put(key, value);
			}
			rtn.add(mapOfColValues);
		}
		
		return rtn;
	}
}
