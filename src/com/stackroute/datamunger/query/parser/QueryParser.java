package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		
		
		

		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
    public String getFileName(String queryString) {
		
		String[] splitstring =  queryString.toLowerCase().split(" ");
		return splitstring[3];
    }

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
    public String getBaseQuery(String queryString) {
		if(queryString.toLowerCase().contains("where")) {
			String[] splitstring =  queryString.toLowerCase().split("where");
			return splitstring[0].trim();
		}
		else {
			String[] splitString=queryString.toLowerCase().split("group");
			return splitString[0].trim();
		}
		
	}


	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
     public List<String> getOrderByFields(String queryString) {
		
		if(queryString.contains(" by ")) {
			String[] query = queryString.split("by ");
			String[] out = query[1].split(" ");
			return Arrays.asList(out);
		}
		else return null;
	}       
    

	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
       public List<String> getGroupByFields(String queryString) {
   		if(queryString.contains(" group by ")) {
   			String[] query = queryString.split("by ");
   			String[] out = query[1].split(" ");
   			return Arrays.asList(out);
   		}
   		else return null;
   	}


	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
       public List<String>getFields(String queryString) {
   		
   		String[] splitstring =  queryString.toLowerCase().split(" ");
   		String[] splitbycomma = splitstring[1].toLowerCase().split(",");
   		
   		return Arrays.asList(splitbycomma);
   	    }

	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */
       public List<String> getConditions(String queryString) {
   		
   		if(queryString.toLowerCase().contains("where")) {
   			String[] splitstring =  queryString.toLowerCase().split("where");
   			
   			if(splitstring[1].toLowerCase().contains("order")) {
   				String[] split2 = splitstring[1].toLowerCase().split("order");
   				String[] split3 = split2[0].trim().split(" and | or ");
   				return Arrays.asList(split3);
   			}
   			
   			else if (splitstring[1].toLowerCase().contains("group")) {
   				String[] split2 = splitstring[1].toLowerCase().split("group");
   				String[] split3 = split2[0].trim().split(" and | or ");
   				return Arrays.asList(split3);
   			}
   			
   			else {
   				String[] split3 = splitstring[1].trim().split(" and | or ");
   				return Arrays.asList(split3);
   			}
   		}
   		else return null;
   	}

	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
       public List<String> getLogicalOperators(String queryString) {
   		String outand = "";
   		
   		if(queryString.toLowerCase().contains("or") || queryString.toLowerCase().contains("and")) {
   			String[] out = queryString.split(" ");
   			
   			for (int i=0 ; i<out.length ; i++) {
   				if(out[i].equals("and")) {
   					outand = outand.concat("and ");
   				}
   				else if(out[i].equals("or")) {
   					outand = outand.concat("or ");
   				}
   			}
   			String[] query = outand.trim().split(" ");
   			return Arrays.asList(query);
   		}
   		else {
   			return null;
   		}
   	}

	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */
       public List<AggregateFunction> getAggregateFunctions(String queryString) {
    	   List<AggregateFunction> aggregateList=new ArrayList<>();
    	   AggregateFunction aggregateFunction;
   		String check = "";
   		if(queryString.contains("min(")|| queryString.contains("max(")|| queryString.contains("avg(")|| queryString.contains(" sum(")|| queryString.contains("count(")) {
   			String[] query = queryString.split(" ");
   			String[] out = query[1].split(",");
   			for(int i=0 ; i<out.length; i++) {
   				if(out[i].contains(")")){
   					check=check.concat(out[i]+",");
   				}
   			}
   			String[] stringout = check.split(",");
   			for(int i=0;i<stringout.length;i++)
   			{
   				String Array1[]=stringout[i].split("\\(");
   				String field=Array1[0];
   				String Array2[]=Array1[i+1].split("\\)");
   				String function=Array2[0];
   				
   				aggregateFunction=new AggregateFunction(field, function);
   				aggregateList.add(aggregateFunction);
   			}
   		}
   		return aggregateList;
   	}

}