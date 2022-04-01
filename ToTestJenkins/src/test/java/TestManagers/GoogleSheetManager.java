package TestManagers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleSheetManager extends OAuth2Utils{
	
	private final String APPLICATION_NAME = "Google Sheets Read & Write";
	private String spreadsheetId;
	private String sheetId;
	private String range;
	private int keywordcolumnnumber;
	private Map<String,Map<String,String>> map = new HashMap<String, Map<String, String>>();
	private List<String> columns = new ArrayList<String>();
	private List<String> allkeywords = new ArrayList<String>();
	
	
	public GoogleSheetManager() {
		// TODO Auto-generated constructor stub
		//super();
		this.spreadsheetId = getConfig("spreadsheetId");
		this.sheetId = getConfig("sheetId");
		this.range = getConfig("testdatarange");
		this.keywordcolumnnumber = Integer.valueOf(getConfig("keywordcolumnnumber"));
	}

	@SuppressWarnings("unchecked")
	public String setMap()
	{
		String res="";
		// Build a new authorized API client service.
		NetHttpTransport HTTP_TRANSPORT;
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME)
					.build();
			ValueRange response = service.spreadsheets().values()
					.get(spreadsheetId, "'"+sheetId+"'!"+range)
					.execute();


			List<List<Object>> values = response.getValues();

			if (values == null || values.isEmpty()) {
				System.out.println("No data found.");
			} else {
				//System.out.println("List size "+values.size());
				int count = 0;
				for (@SuppressWarnings("rawtypes") List row : values) {
					// Print columns A and E, which correspond to indices 0 and 4.
					//System.out.printf("%s, %s\n", row.get(0), row.get(4));
					if(count == 0)
					{
						columns = row;
					}
					else {
						Map<String,String> valuemap = new HashMap<String, String>();
						for(int j=0;j<row.size();j++)
						{
							valuemap.put(columns.get(j).toString(), row.get(j).toString());
						}
						allkeywords.add(row.get(keywordcolumnnumber).toString());
						map.put(row.get(keywordcolumnnumber).toString(), valuemap);
					}
					count++;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public String getData(String testcasename, String columnname)
	{
		
		String res = "";
		Map<String,String> lst = new HashMap<String,String>();
		try {
			lst = map.get(testcasename);
		}
		catch(Exception e)
		{
			System.out.println("The keyword "+testcasename+" is invalid. Please check.");
		}
		try {
			res = lst.get(columnname).toString();
		}
		catch(Exception e)
		{
			System.out.println("The column name "+columnname+" is invalid. Please check.");
		}

		return res;
	}

	public String getData(String columnname)
	{
		return getData(keyWord, columnname);
	}
	
	public boolean write(String testcasename, String columnname, String value)//
	{
		boolean res =false;
		int rowindex=0;
		int colindex=0;
		// Build a new authorized API client service.
		NetHttpTransport HTTP_TRANSPORT;
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME)
					.build();
			List<Request> requests = new ArrayList<Request>();

			List<CellData> values = new ArrayList<CellData>();

			try {
				rowindex = allkeywords.indexOf(testcasename) + 1;
			}
			catch(NullPointerException e)
			{
				System.out.println("The keyword "+testcasename+" is invalid. Please check.");
				return false;
			}
			try {
				colindex = columns.indexOf(columnname);
			}
			catch(NullPointerException e)
			{
				System.out.println("The column name "+columnname+" is invalid. Please check.");
				return false;
			}

			values.add(new CellData()
					.setUserEnteredValue(new ExtendedValue()
							.setStringValue(value)));
			requests.add(new Request()
					.setUpdateCells(new UpdateCellsRequest()
							.setStart(new GridCoordinate()
									.setSheetId(0)
									.setRowIndex(rowindex)
									.setColumnIndex(colindex))
							.setRows(Arrays.asList(
									new RowData().setValues(values)))
							.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

			BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
					.setRequests(requests);

			service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
			.execute();
			res = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res =false;
		}
		catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res =false;
		}
		return res;
	}
}