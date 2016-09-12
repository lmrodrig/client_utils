/* @ Copyright IBM Corporation 2014. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 * 
 * Created on Jun 23, 2014
 * 
 * Class in charge of managing SQL statements.
 *
 * Date       Flag Work Item   Name             Details
 * ---------- ---- ----------- ---------------- ----------------------------------
 * 2012-06-23  	   RCQ00305597 Cesar Mart@nez   -Initial Version
 * 2014-09-09 ~1   RCQ00266603 Gabriel Herrera	-Added new method executeQuery()	
 ******************************************************************************/

package com.ibm.rchland.mfgapps.client.utils.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentDataAccessObject {

	/**
	 * Build a delete sql instruction based on the passed in conditions and table name.
	 * @param conditions
	 * @param tableName
	 * @return the delete sql instruction
	 */
	public StringBuilder buildDeleteSQL(Map<String,String> conditions,String tableName)
	{
		Set<String> keys = conditions.keySet();
		int counter = 0;
		StringBuilder theSQL = new StringBuilder("DELETE FROM ");
		theSQL.append(tableName);
		
		if(conditions.size() != 0)
		{
			theSQL.append(" WHERE ");
		}
		for(String key: keys)
		{
			counter++;
			theSQL.append(key);
			theSQL.append(" = '");
			theSQL.append(conditions.get(key));
			theSQL.append("'");
			
			if(counter < keys.size())
			{
				theSQL.append(" AND ");
			}
		}
		
		return theSQL;
	}
	
	/**
	 * Build an insert sql instruction based on the passed key-value mapping and
	 * the table name.
	 * @param insertRow
	 * @param tableName
	 * @return the insert sql instruction.
	 */
	public StringBuilder buildInsertSQL(Map<String,String> insertRow, String tableName)
	{
		StringBuilder theSQL = new StringBuilder("INSERT INTO ");
		StringBuilder columnValues = new StringBuilder();
		Set<String> columnNames = insertRow.keySet();
		int counter = 0;
		
		theSQL.append(tableName);
		theSQL.append(" (");
		
		for(String column: columnNames)
		{
			counter++;
			theSQL.append(column);
			
			if(counter != columnNames.size())
			{
				theSQL.append(",");
			}
			
			columnValues.append(insertRow.get(column));
			
			if(counter != columnNames.size())
			{
				columnValues.append("','");
			}
			
		}

		theSQL.append(") VALUES('");
		columnValues.append("')");
		theSQL.append(columnValues.toString());
		
		return theSQL;
	}
	
	/**
	 * Calls and handles the delete/update sql statement
	 * @param sql
	 * @param conn
	 * @throws SQLException
	 */
	public void executeUpdate(StringBuilder sql, Connection conn) throws SQLException
	{
		Statement update = null;
		try {
			update = conn.createStatement();
			update.executeUpdate(sql.toString());
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
		finally{
			try
			{
				update.close();
			}
			catch(SQLException ex)
			{
				throw ex;
			}
		}
	}
	
	//~1A Start
	/**
	 * Calls and handles the select sql query sent as parameter.
	 * @param selectSQL - a string containing the sql query to be run.
	 * @param connection - the sql connection with which the statement will be run.
	 * @return An ArrayList<HashMap<String,String>> object with the results of the query.
	 * @throws SQLException if the select query was not successful. 
	 */
	public ArrayList<HashMap<String,String>> executeQuery(StringBuilder selectSQL, Connection connection) throws SQLException{
		
		int columnCount = 0;
		Statement query = null;
		ResultSet row = null;
		ResultSetMetaData rowMD = null;
		String columnName;
		ArrayList<HashMap<String,String>> rows = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> rowData;
		try{
			query = connection.createStatement();
			row = query.executeQuery(selectSQL.toString());
			rowMD = row.getMetaData();
			columnCount = rowMD.getColumnCount() + 1;
			while(row.next()){
				rowData = new HashMap<String, String>();
				for(int i=1; i < columnCount; i++){
					columnName = rowMD.getColumnName(i).trim();
					rowData.put(columnName, row.getString(columnName).trim());
				}
				rows.add(rowData);
			}
		}catch(NullPointerException e){
			System.out.println("No results found for query: \"" + selectSQL.toString() + "\"");
		}finally{
			query.close();
		}
		return rows;
	}
	//~1A End
}
