package com.content;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javaNK.util.data.MysqlModifier;
import javaNK.util.debugging.Logger;

public abstract class MysqlDataRow
{
	protected static abstract class DataField
	{
		Object classMember;
		
		public DataField() {
			this.classMember = classMember();
		}
		
		public String value() {
			Object value = classMember();
			String apostrophe = "'";
			String valueStr;
			
			if (classMember() instanceof String || classMember instanceof Timestamp)
				valueStr = apostrophe + value.toString() + apostrophe;
			else
				valueStr = value.toString();
			
			return valueStr;
		}
		
		public abstract String mysqlName();
		
		protected abstract Object classMember();
	}
	
	private List<DataField> keyFields, liquidFields;
	
	public MysqlDataRow() {
		this.keyFields = new ArrayList<DataField>();
		this.liquidFields = new ArrayList<DataField>();
		addFields();
	}
	
	public void save() {
		String query;
		DataField tempField;
		
		if (isInDatabase()) {
			query = "UPDATE " + tableName() + " SET ";
			
			for (int i = 0; i < liquidFields.size(); i++) {
				tempField = liquidFields.get(i);
				query = query.concat(tempField.mysqlName() + " = " + tempField.value() + " ");
				
				if (i < liquidFields.size() - 1) query = query.concat(", ");
				else query = query.concat(keyCondition());
			}
		}
		else {
			query = "INSERT INTO " + tableName() + "(";
			int fieldsAmount;
			
			/*
			 * Parenthesis section
			 */
			fieldsAmount = keyFields.size() + liquidFields.size();
			
			//key fields
			for (DataField field : keyFields) {
				fieldsAmount--;
				query = query.concat(field.mysqlName());
				
				if (fieldsAmount > 0) query = query.concat(", ");
				else query = query.concat(") VALUES (");
			}
			
			//liquid fields
			for (DataField field : liquidFields) {
				fieldsAmount--;
				query = query.concat(field.mysqlName());
				
				if (fieldsAmount > 0) query = query.concat(", ");
				else query = query.concat(") VALUES (");
			}
			
			/*
			 * Values section
			 */
			fieldsAmount = keyFields.size() + liquidFields.size();
			
			//key fields
			for (DataField field : keyFields) {
				fieldsAmount--;
				query = query.concat(field.value());
				
				if (fieldsAmount > 0) query = query.concat(", ");
				else query = query.concat(") ");
			}
			
			//liquid fields
			for (DataField field : liquidFields) {
				fieldsAmount--;
				query = query.concat(field.value());
				
				if (fieldsAmount > 0) query = query.concat(", ");
				else query = query.concat(") ");
			}
		}
		
		try { MysqlModifier.write(query); }
		catch (SQLException e) {
			Logger.error("The query:\n" + query + "\nwent wrong."
					   + "Copy and check it in any MySQL platform for errors.");
		}
	}
	
	public void delete() {
		String query = "DELETE FROM " + tableName() + " " + keyCondition();
		try { MysqlModifier.write(query); }
		catch (SQLException e) {}
	}
	
	protected boolean isInDatabase() {
		try {
			String query = "SELECT EXISTS ( "
					  	 + "	SELECT * "
					  	 + "	FROM " + tableName() + " "
					  	 + keyCondition() + ") AS key_exists";
			
			return MysqlModifier.readBOOLEAN(query, "key_exists");
		}
		catch (SQLException | NullPointerException e) { return false; }
	}
	
	protected String selectAllQuery() {
		return "SELECT * FROM " + tableName() + " " + keyCondition();
	}
	
	protected String keyCondition() {
		if (keyFields.isEmpty()) return "";
		else {
			String condition = "WHERE ";
			DataField tempField;
			
			for (int i = 0; i < keyFields.size(); i++) {
				tempField = keyFields.get(i);
				condition = condition.concat(tempField.mysqlName() + " = " + tempField.value());
				
				if (i < keyFields.size() - 1) condition = condition.concat(" AND ");
			}
			
			return condition;
		}
	}
	
	protected abstract String tableName();
	
	protected void addKeyField(DataField field) {
		keyFields.add(field);
	}
	
	protected void addLiquidField(DataField field) {
		liquidFields.add(field);
	}
	
	protected abstract void addFields();
}