import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadDailySchedulersJob implements Job {

	private Logger log = Logger.getLogger(ReadDailySchedulersJob.class);

	public void execute(JobExecutionContext context)throws JobExecutionException {
		System.out.println("Daily Scheduler!");
		log.debug("TestJob run successfully...");

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		int lengthOfMeter = 0;
		int lengthOfINTRCHA = 0;
		int RegisterChannelLength = 0;

		String userNameSCS="southcoast";  
        String passWordSCS = "s8aSafra";  
        String ftpAddressSCS = "msftp.smartsynch.com";
        String ftpAddressFilePathSCS = "//southcoast@msftp.smartsynch.com/";
		
        Long fileSize=null;
        String schedulerStatus="Success";
        String schedulerMessage="All data Inserted properly";
        Boolean schedBool=true;
        
        //Properties props =null;
		//String directorysavexml="/Downloaded_FTP/";
        String directoryreadxml="c:/Downloaded_FTP/";
        
       /* String url = "jdbc:mysql://localhost:3306/";
		String db = "monitoring_platform";
		String driver = "com.mysql.jdbc.Driver";
		String dbUname="root";
		String dbPwd="EZ7YSVTsertFxFjL";*/
		
		String url = "jdbc:mysql://localhost:3306/";
		String db = "testdatabase";
		String driver = "com.mysql.jdbc.Driver";
		String dbUname="root";
		String dbPwd="root";
		//String fileName ="sc_123020130535.xml";
		String fileName ="sc_010320140535.xml";

      	
		System.out.println("readXML: " + directoryreadxml);
		Connection con = null;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url + db, dbUname, dbPwd);
			
		} catch (SQLException s) {

			System.out.println("SQL statement is not executed!");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		try {
			Statement st = con.createStatement();
			
			String Directory=directoryreadxml; 
			
			/*File dir = new File(Directory);
			FileFilter fileFilter = new WildcardFileFilter("*.xml");
			File[] files = dir.listFiles(fileFilter);

			//** The newest file comes first **//*
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			List<File> list =Arrays.asList(files);
			String newLst=list.get(0).toString();
			System.out.println(newLst);
			String fileName=new File(newLst).getName();*/
			System.out.println(fileName);
			System.out.println("FileName :" +fileName);
			
			if(fileName!=""){
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(new File(fileName));

			NodeList lengthOfMeters = doc.getElementsByTagName("Meter");
			lengthOfMeter = lengthOfMeters.getLength();

			NodeList lengthOfIntrChannl = doc.getElementsByTagName("IntervalChannel");
			lengthOfINTRCHA = lengthOfIntrChannl.getLength();

			NodeList RegisterChannel = doc.getElementsByTagName("RegisterChannel");
			RegisterChannelLength = RegisterChannel.getLength();

			

			// for meter
			for (int k = 0; k <= lengthOfMeter - 1; k++) {

				String tempchannel1 = "";
				String tempchannel2 = "";
				Double channelValue = 0.00;

				Node meterNode = lengthOfMeters.item(k);
				Element meterNodeElement = (Element) meterNode;

				System.out.println("--------------------------");
				System.out.println("Meter Serial Number : "	+ meterNodeElement.getAttribute("SerialNumber"));

				NodeList registerlist = meterNodeElement.getElementsByTagName("RegisterChannel");
				
				NodeList intervalChannel = meterNodeElement.getElementsByTagName("IntervalChannel");
				
				
				for (int j = 0; j < registerlist.getLength(); j++) {
					Node registerChannelNodes = registerlist.item(j);
					Element registerChannelElements = (Element) registerChannelNodes;
					if((registerChannelElements.getAttribute("UnitOfMeasure")).equals("TOTAL KMH")){

					tempchannel1 = registerChannelElements.getAttribute("ReadValue");
					try{
						channelValue=Double.parseDouble(tempchannel1);
						}catch(Exception numberissue){
							channelValue=0.0;
						}
					System.out.println("Channel Value :"+channelValue);
			

				System.out.println("out of first reg loop ");

				
					Node registerChannelNode = registerlist.item(j);
					Element registerChannelElement1 = (Element) registerChannelNode;

					Node intervalChannelNode = intervalChannel.item(j);
					Element intervalChannelElement = (Element) intervalChannelNode;
					
					System.out.println("ReadValue inside register loop:  "+ registerChannelElement1.getAttribute("ReadValue"));
					//String readTimeI = registerChannelElement1.getAttribute("ReadTime");
					String readTime = intervalChannelElement.getAttribute("StartTime");
					
					String splitReadTimeFull = readTime.substring(0, 10);
					int splitReadTimeDay = Integer.parseInt(readTime.substring(8, 10));
					String splitReadTimeMonth = readTime.substring(5, 7);
					String splitReadTimeYear = readTime.substring(0, 4);
					
					String dt = splitReadTimeYear+"-"+splitReadTimeMonth+"-"+splitReadTimeDay;
					System.out.println("dt "+dt);

					String splitReadTimeMonthName = new DateFormatSymbols().getShortMonths()[Integer.parseInt(splitReadTimeMonth) - 1];

					System.out.println("===============================================");
					
					DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
					DateTime dat = formatter.parseDateTime(dt);					
					String formattedDates = dat.minusDays(1).toString(formatter);
					
					System.out.println("formattedDates "+formattedDates);
					
					
					if (splitReadTimeDay == 1 && splitReadTimeMonthName.equals("Jan")) {
						System.out.println("******** channel value is (inside update statements) *********"	+ channelValue);
						System.out.println("DAY 1");
						
						// yearly
						String preValYear="";
						
						String chkFrPrevQYear="SELECT ReadValueKMH AS km FROM meterenergyproductionyearly WHERE serialNumber ='"+meterNodeElement.getAttribute("SerialNumber")+"' AND YearlyDate='"+formattedDates+"'";
						System.out.println("chkFrPrevQYear "+chkFrPrevQYear);
						ResultSet chkFrPrevYear = st.executeQuery(chkFrPrevQYear);
						
						if(chkFrPrevYear.next()){
							preValYear=chkFrPrevYear.getString("km");
							}
						
						System.out.println(" preValYear "+preValYear);
						

						
						ResultSet fetchYearData = st.executeQuery("Select * from meterenergyproductionyearly where SerialNumber = '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and Year = '"
								+ splitReadTimeYear + "'");
						
						double yearEnergyData = 0.00;
						
						if (fetchYearData.next()) {
							yearEnergyData = fetchYearData.getDouble("YearKWH");
							System.out.println("yearEnergy: "+ yearEnergyData);
						}
						
						//if(currentcount.equals("0")){
				
						if(!preValYear.equals("0") && !preValYear.equals("")){
							// yearly Insert
							
							Double val = 0.0;
							try{
							val = channelValue-Double.parseDouble(preValYear);
							}catch(Exception numberissue){
								val = 0.0;
							}
							System.out.println("Y VAL  "+val);
							Double val1 = val+yearEnergyData;
							System.out.println("val1 "+val1);
							
							st.executeUpdate("INSERT into meterenergyproductionyearly(SerialNumber,OwnerAccount,PurchaserAccount,Year,YearKWH,ReadValueKMH) "
									+ "VALUES('"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "',"
									+ " (select OwnerAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0) , "
									+ " (select PurchaserAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0),'"
									+ splitReadTimeYear
									+ "','"
									+ val1 
									+ "','"
									+ channelValue
									+ "'"
									+ ")");
							
							}
							else
							{
								System.out.println("inside yearly 1");
						st.executeUpdate("INSERT into meterenergyproductionyearly(SerialNumber,OwnerAccount,PurchaserAccount,Year,YearKWH,ReadValueKMH) "
								+ "VALUES('"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "',"
								+ " (select OwnerAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0) , "
								+ " (select PurchaserAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0),'"
								+ splitReadTimeYear
								+ "','"
								+ channelValue 
								+ "','"
								+ channelValue
								+ "'"
								+ ")");
							}
						
						// monthly
						
						String preValMonth="";
						
						String chkFrPrevQMonth="SELECT ReadValueKMH AS km FROM meterenergyproductionmonthly WHERE serialNumber ='"+meterNodeElement.getAttribute("SerialNumber")+"' AND MonthlyDate='"+formattedDates+"'";
						
						System.out.println("chkFrPrevQMonth for JAN "+chkFrPrevQMonth);
						
						ResultSet chkFrPrevMonth = st.executeQuery(chkFrPrevQMonth);
						
						if(chkFrPrevMonth.next()){
							preValMonth=chkFrPrevMonth.getString("km");
							}
						
						System.out.println(" preValMonthJAN "+preValMonth);
						
						//if(currentcount.equals("0")){
						
						if(!preValMonth.equals("0") && !preValMonth.equals("") ){
							// monthly Insert
							
							Double val = 0.0;
							
							try{
								
								val = channelValue-Double.parseDouble(preValMonth);
							 
							}catch(Exception numberissue){
									val=0.0;
							}
							System.out.println("M VAL  "+val);
							
						
							st.executeUpdate("INSERT into meterenergyproductionmonthly(SerialNumber,OwnerAccount,PurchaserAccount,Year,`"
									+ splitReadTimeMonthName
									+ "` ,ReadValueKMH,MonthlyDate) "
									+ "VALUES('"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "',"
									+ " (select OwnerAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0) , "
									+ " (select PurchaserAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0),'"
									+ splitReadTimeYear
									+ "','"
									+ val 
									+ "','"
									+ channelValue 
									+ "','"
									+ dt
									+ "'"
									+ ")");
							
						}
						
						else{
							
						st.executeUpdate("INSERT into meterenergyproductionmonthly(SerialNumber,OwnerAccount,PurchaserAccount,Year, `"
								+ splitReadTimeMonthName
								+ "` ,ReadValueKMH,MonthlyDate) "
								+ "VALUES('"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "',"
								+ " (select OwnerAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0) , "
								+ " (select PurchaserAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0),'"
								+ splitReadTimeYear
								+ "','"
								+ channelValue 
								+ "','"
								+ channelValue 
								+ "','"
								+ dt
								+ "'"
								+ ")");

						}
						// daily
						String currentcount="";						
						String preValM="";
						String chkFrPrevQ="SELECT ReadValueKMH AS km FROM meterenergyproductiondaily WHERE serialNumber ='"+meterNodeElement.getAttribute("SerialNumber")+"' AND DailyDate='"+formattedDates+"'";
						
						ResultSet chkFrPrev = st.executeQuery(chkFrPrevQ);
						
						if(chkFrPrev.next()){
							preValM=chkFrPrev.getString("km");
							}
						
						System.out.println(" preValM "+preValM);
						
						if(!preValM.equals("0")&& !preValM.equals("")){
						// Daily Insert
							Double val = 0.0;
							try{
								val = channelValue-Double.parseDouble(preValM);
							}catch(Exception numberissue){
								val=0.0;
							}
						st.executeUpdate("INSERT into meterenergyproductiondaily(SerialNumber,OwnerAccount,PurchaserAccount,Year,Month,day1,ReadValueKMH) "
								+ "VALUES('"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "',"
								+ " (select OwnerAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0) , "
								+ " (select PurchaserAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0),'"
								+ splitReadTimeYear
								+ "','"
								+ splitReadTimeMonthName
								+ "','"
								+ val
								+ "','"
								+ channelValue + "'" + ")");

					}
						else
						{
							st.executeUpdate("INSERT into meterenergyproductiondaily(SerialNumber,OwnerAccount,PurchaserAccount,Year,Month,day1,ReadValueKMH) "
									+ "VALUES('"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "',"
									+ " (select OwnerAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0) , "
									+ " (select PurchaserAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0),'"
									+ splitReadTimeYear
									+ "','"
									+ splitReadTimeMonthName
									+ "','"
									+channelValue
									+ "','"
									+ channelValue + "'" + ")");
						}
						
						
					}
					// MAIN ELSE
					else {
						if (splitReadTimeDay == 1 && !splitReadTimeMonthName.equals("Jan")) {
							System.out.println("DAY 1 - Diff Month");
							
							String currentcount="";
							
							String preValM="";
							String chkFrPrevQ="SELECT ReadValueKMH AS km FROM meterenergyproductiondaily WHERE serialNumber ='"+meterNodeElement.getAttribute("SerialNumber")+"' AND DailyDate='"+formattedDates+"'";
							
							ResultSet chkFrPrev = st.executeQuery(chkFrPrevQ);
							
							if(chkFrPrev.next()){
								preValM=chkFrPrev.getString("km");
								}
							
							System.out.println(" preValM "+preValM);
							
							if(!preValM.equals("0")&& !preValM.equals("")){
							// Daily Insert
								Double val = 0.0;
								try{
									val = channelValue-Double.parseDouble(preValM);
								}catch(Exception numberissue){
									val=0.0;
								}
							st.executeUpdate("INSERT into meterenergyproductiondaily(SerialNumber,OwnerAccount,PurchaserAccount,Year,Month,day1,ReadValueKMH) "
									+ "VALUES('"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "',"
									+ " (select OwnerAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0) , "
									+ " (select PurchaserAccount from meter where SerialNumber= '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and IsDeleted=0),'"
									+ splitReadTimeYear
									+ "','"
									+ splitReadTimeMonthName
									+ "','"
									+val
									+ "','"
									+ channelValue + "'" + ")");
							}
							else
							{
								st.executeUpdate("INSERT into meterenergyproductiondaily(SerialNumber,OwnerAccount,PurchaserAccount,Year,Month,day1,ReadValueKMH) "
										+ "VALUES('"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "',"
										+ " (select OwnerAccount from meter where SerialNumber= '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and IsDeleted=0) , "
										+ " (select PurchaserAccount from meter where SerialNumber= '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and IsDeleted=0),'"
										+ splitReadTimeYear
										+ "','"
										+ splitReadTimeMonthName
										+ "','"
										+channelValue
										+ "','"
										+ channelValue + "'" + ")");
							}
							// Monthly Insert
							
							String preValMonth="";
		
							String chkFrPrevQMonth="SELECT ReadValueKMH AS km FROM meterenergyproductionmonthly WHERE serialNumber ='"+meterNodeElement.getAttribute("SerialNumber")+"' AND MonthlyDate='"+formattedDates+"'";
							
							System.out.println("chkFrPrevQMonth "+chkFrPrevQMonth);
							
							ResultSet chkFrPrevMonth = st.executeQuery(chkFrPrevQMonth);
							
							if(chkFrPrevMonth.next()){
								preValMonth=chkFrPrevMonth.getString("km");
								}
							
							System.out.println(" preValMonth "+preValMonth);
							
							//if(currentcount.equals("0")){
							
							if(!preValMonth.equals("0") ){
								// monthly Insert
								
								Double val = 0.0;
								
								try{
								 val = channelValue-Double.parseDouble(preValMonth);
							}catch(Exception numberissue){
								val=0.0;
							}
								System.out.println("M VAL  "+val);
								
								st.executeUpdate("update meterenergyproductionmonthly set `"
										+ splitReadTimeMonthName
										+ "` =  '"
										+ val
										+ "',ReadValueKMH = '"
										+ channelValue
										+ "'"
										+ " where SerialNumber = '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "'  AND year= '"
										+ splitReadTimeYear
										+ "'");
					
	
								}
								else
								{
									st.executeUpdate("INSERT into meterenergyproductionmonthly(SerialNumber,OwnerAccount,PurchaserAccount,Year,`"+ splitReadTimeMonthName+"`,ReadValueKMH) "
											+ "VALUES('"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "',"
											+ " (select OwnerAccount from meter where SerialNumber= '"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and IsDeleted=0) , "
											+ " (select PurchaserAccount from meter where SerialNumber= '"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and IsDeleted=0),'"
											+ splitReadTimeYear
											+ "','"
											+ channelValue 
											+ "','"
											+ channelValue
											+ "'"
											+ ")");

									
								}
							
							//}
							// Yearly Insert
							
							String preValYear="";

							
							String chkFrPrevQYear="SELECT ReadValueKMH AS km FROM meterenergyproductionyearly WHERE serialNumber ='"+meterNodeElement.getAttribute("SerialNumber")+"' AND YearlyDate='"+formattedDates+"'";
							System.out.println("chkFrPrevQYear "+chkFrPrevQYear);
							ResultSet chkFrPrevYear = st.executeQuery(chkFrPrevQYear);
							
							if(chkFrPrevYear.next()){
								preValYear=chkFrPrevYear.getString("km");
								}
							
							System.out.println(" preValYear "+preValYear);
							

							
							ResultSet fetchYearData = st.executeQuery("Select * from meterenergyproductionyearly where SerialNumber = '"
									+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and Year = '"
									+ splitReadTimeYear + "'");
							
							double yearEnergyData = 0.00;
							
							if (fetchYearData.next()) {
								yearEnergyData = fetchYearData.getDouble("YearKWH");
								System.out.println("yearEnergy: "+ yearEnergyData);
							}
							
							//if(currentcount.equals("0")){
					
							if(!preValYear.equals("0") ){
								// yearly Insert
								
								Double val = 0.0;
								try{
								val = channelValue-Double.parseDouble(preValYear);
								}catch(Exception numberissue){
									val = 0.0;
								}
								System.out.println("Y VAL  "+val);
								Double val1 = val+yearEnergyData;
								System.out.println("val1 "+val1);
								
								st.executeUpdate("update meterenergyproductionyearly set YearKWH =  '"
										+ val1
										+ "',ReadValueKMH = '"
										+ channelValue
										+ "'"
										+ " where SerialNumber = '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and Year = '"
										+ splitReadTimeYear
										+ "'");
								
								}
								else
								{
									st.executeUpdate("INSERT into meterenergyproductionyearly(SerialNumber,OwnerAccount,PurchaserAccount,Year,YearKWH,ReadValueKMH) "
											+ "VALUES('"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "',"
											+ " (select OwnerAccount from meter where SerialNumber= '"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and IsDeleted=0) , "
											+ " (select PurchaserAccount from meter where SerialNumber= '"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and IsDeleted=0),'"
											+ splitReadTimeYear
											+ "','"
											+ channelValue 
											+ "','"
											+ channelValue
											+ "'"
											+ ")");
								}
							
						//}

						}
						else
						{							
							System.out.println("any day - any Month - Last ELSE");
							
							// Daily Insert 	
							String temQ="Select SerialNumber from meterenergyproductiondaily where "+ "SerialNumber = '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and Month = '"
								+ splitReadTimeMonthName + "'";
							
							System.out.println("temQ "+temQ);
							
							ResultSet dailyCheck= st.executeQuery("Select count(*) as currentcount from meterenergyproductiondaily where "
									+ "SerialNumber = '"+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and Month = '"	+ splitReadTimeMonthName + "'");
							
							String currentcount="";
							String preValue="";
							
							if(dailyCheck.next()){
								System.out.println("dailyCheck.getRow()   "+dailyCheck.getString("currentcount"));
								currentcount=dailyCheck.getString("currentcount");
							}
							
							System.out.println("dailyCheck.getRow() ANY DAY MONTH  "+dailyCheck.getString("currentcount"));
							if (currentcount.equals("0")) 
							{
								
								System.out.println("Insert for first time daily");
								String G = "INSERT into meterenergyproductiondaily(SerialNumber,OwnerAccount,PurchaserAccount,Year,Month,ReadValueKMH,Day"+splitReadTimeDay+",DailyDate)"
								+ "VALUES('"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "',"
								+ " (select OwnerAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0) , "
								+ " (select PurchaserAccount from meter where SerialNumber= '"
								+ meterNodeElement.getAttribute("SerialNumber")
								+ "' and IsDeleted=0),'"
								+ splitReadTimeYear
								+ "','"
								+ splitReadTimeMonthName
								+ "','"
								+ channelValue 
								+ "','"
								+ channelValue 
								+ "','" 
								+ dt 
								+ "')";
								
								System.out.println("G "+G);
								
								st.executeUpdate(G);

							}
							
							else
							{	
								System.out.println("update  daily any month any day");
								
								ResultSet preVal= st.executeQuery("Select ReadValueKMH as preVal from meterenergyproductiondaily where "
										+ "SerialNumber = '"+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and Month = '"	+ splitReadTimeMonthName + "'");
								
								if(preVal.next()){
									System.out.println("preVal "+preVal.getString("preVal"));
									preValue=preVal.getString("preVal");
								}
								Double val = 0.0;
								try{	
									val = channelValue-Double.parseDouble(preValue);
								}catch(Exception numberissue){
									val = 0.0;
								}
								st.executeUpdate("update meterenergyproductiondaily set DailyDate='"+dt+"',ReadValueKMH ='"+channelValue+"', day"+splitReadTimeDay+" = '"+ val+"'"
										  +" where SerialNumber = '"+meterNodeElement.getAttribute("SerialNumber")+"' AND month = '"
										  +splitReadTimeMonthName+"' AND year= '"+splitReadTimeYear+"'");			
							
							}
							// Daily Insert END
							
														
							// Monthly Insert
							String queryToCheckAnyDayAnyMonth = "Select SerialNumber from meterenergyproductionmonthly where "
									+ "SerialNumber = '"+ meterNodeElement.getAttribute("SerialNumber")
									+ "' and Year = '"+ splitReadTimeYear+ "'";
							System.out.println(queryToCheckAnyDayAnyMonth);
							
							ResultSet checkAnyDayAnyMonth = st.executeQuery("Select count(*) as currentcount from meterenergyproductionmonthly where "
											+ "SerialNumber = '"+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and Year = '"+ splitReadTimeYear + "' ");

							double monthEnergyData = 0.0;
							
							System.out.println("checkAnyDayAnyMonth "+checkAnyDayAnyMonth.getRow());
							
							if(checkAnyDayAnyMonth.next()){
							currentcount=checkAnyDayAnyMonth.getString("currentcount");
							}
							
							if (currentcount.equals("0")) {
								System.out.println("Month Name is ->"+splitReadTimeMonthName+"----");
								System.err.println("inside error");
								String q="INSERT into meterenergyproductionmonthly(SerialNumber,OwnerAccount,PurchaserAccount,Year,ReadValueKMH,`"+splitReadTimeMonthName+"`,MonthlyDate)"
										+ "VALUES('"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "',"
										+ " (select OwnerAccount from meter where SerialNumber= '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and IsDeleted=0) , "
										+ " (select PurchaserAccount from meter where SerialNumber= '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and IsDeleted=0),'"
										+ splitReadTimeYear
										+ "','"
										+ channelValue
										+ "','"
										+ channelValue 
										+ "','"
										+ dt
										+ "')";
								System.err.println(q);
								st.executeUpdate(q);
								System.err.println("inside error 1");
								
							}

							else {
								System.err.println("inside error 2");
								ResultSet fetchMonthData = st.executeQuery("Select * from meterenergyproductionmonthly where SerialNumber = '"
												+ meterNodeElement.getAttribute("SerialNumber")
												+ "' and Year = '"
												+ splitReadTimeYear + "'");

								if (fetchMonthData.next()) {
									monthEnergyData = fetchMonthData.getDouble(splitReadTimeMonthName);
									System.out.println("monthEnergyData: "+ monthEnergyData);
								}

								ResultSet preVal= st.executeQuery("Select ReadValueKMH as preVal from meterenergyproductionmonthly where "
										+ "SerialNumber = '"+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and Year = '"+ splitReadTimeYear + "'");
								
								if(preVal.next()){
									System.out.println("preVal "+preVal.getString("preVal"));
									preValue=preVal.getString("preVal");
								}
								
								double totalMonthEnergy = 0.0;
								try{
								 totalMonthEnergy = monthEnergyData+(channelValue-Double.parseDouble(preValue));
								}catch(Exception numberissue){
									totalMonthEnergy = 0.0;
								}
								
								System.out.println("Current total month energy is  "+ totalMonthEnergy);
								st.executeUpdate("update meterenergyproductionmonthly set MonthlyDate='"+dt+"',ReadValueKMH = '"+channelValue+"',`"
										+ splitReadTimeMonthName
										+ "` =  '"
										+ totalMonthEnergy
										+ "'"
										+ " where SerialNumber = '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "'  AND year= '"
										+ splitReadTimeYear
										+ "'");
							}
							System.err.println("inside yrlys");
							// Yearly Insert
							ResultSet checkAnyDayAnyYear = st.executeQuery("Select count(*) as currentcount from meterenergyproductionyearly where SerialNumber = '"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and Year = '"
											+ splitReadTimeYear + "'");

							double yearEnergyData = 0.00;
							
							if(checkAnyDayAnyYear.next()){
							currentcount=checkAnyDayAnyYear.getString("currentcount");
							}
							
							if (currentcount.equals("0")) {								
																
									st.executeUpdate("INSERT into meterenergyproductionyearly(SerialNumber,OwnerAccount,PurchaserAccount,Year,YearKWH,ReadValueKMH,YearlyDate) "
											+ "VALUES('"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "',"
											+ " (select OwnerAccount from meter where SerialNumber= '"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and IsDeleted=0) , "
											+ " (select PurchaserAccount from meter where SerialNumber= '"
											+ meterNodeElement.getAttribute("SerialNumber")
											+ "' and IsDeleted=0),'"
											+ splitReadTimeYear
											+ "','"
											+ channelValue
											+ "','"
											+ channelValue
											+ "','"
											+ dt
											+ "'"
											+ ")");

							}

							else 
							{
								
								ResultSet fetchYearData = st.executeQuery("Select * from meterenergyproductionyearly where SerialNumber = '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and Year = '"+ splitReadTimeYear + "'");

								if (fetchYearData.next()) {
									yearEnergyData = fetchYearData.getDouble("YearKWH");
									System.out.println("yearEnergy: "+ yearEnergyData);
									}															

								ResultSet preVal= st.executeQuery("Select ReadValueKMH as preVal from meterenergyproductionyearly where "
										+ "SerialNumber = '"+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and Year = '"+ splitReadTimeYear + "'");
								
								if(preVal.next()){
									System.out.println("preVal "+preVal.getString("preVal"));
									preValue=preVal.getString("preVal");
								}
								
								double totalYearEnergy = 0.0;
								
								try{	
								totalYearEnergy = yearEnergyData+ (channelValue-Double.parseDouble(preValue));
									}catch(Exception numberissue){
										totalYearEnergy = 0.0;
									}

								System.out.println("Current total year energy is  "+ totalYearEnergy);

								st.executeUpdate("update meterenergyproductionyearly set YearlyDate='"+dt+"' ,YearKWH ='"
										+ totalYearEnergy
										+ "' ,ReadValueKMH = '"+channelValue+"'"
										+ " where SerialNumber = '"
										+ meterNodeElement.getAttribute("SerialNumber")
										+ "' and Year = '"
										+ splitReadTimeYear
										+ "'");

							}

						}// end of else

					}// END OF MAIN ELSE

		//	end of register loop.
				}
				}
				System.out.println("*************** end of second loop ******************");
				System.out.println("Out of every loop");
				
				
			}
			String currentcount="";
			ResultSet queryToCheckIfExtrAppNameExists = st.executeQuery("Select count(*) as ExtrAppName from tbl_inbound_config where External_AppName = '"
				+this.getClass().getSimpleName()+ "' ");
			//System.out.println(queryToCheckIfExtrAppNameExists);
			//double monthEnergyData = 0.0;
			System.out.println("queryToCheckIfExtrAppNameExists "+queryToCheckIfExtrAppNameExists.getRow());
			if(queryToCheckIfExtrAppNameExists.next()){
			currentcount=queryToCheckIfExtrAppNameExists.getString("ExtrAppName");
			}
			if (currentcount.equals("0")) {
				
				st.executeUpdate("Insert into tbl_inbound_config (External_AppName,UserName,Password,External_AppName_URL,Activate_Date,Last_Run_Date,Last_Success_Date,Last_Run_Status) " +
						"values ('"+this.getClass().getSimpleName()+"','"+userNameSCS+"','"+passWordSCS+"','"+ftpAddressFilePathSCS+"',SYSDATE(),SYSDATE(),SYSDATE(),'"+ schedulerStatus + "')");
   						
			}
			else
			{
				if(schedBool==true){
				st.executeUpdate("Update tbl_inbound_config set Last_Run_Date=SYSDATE(),Last_Success_Date=SYSDATE(),Last_Run_Status='"+schedulerStatus+"' " +
						" where External_AppName='"+this.getClass().getSimpleName()+"'");
				}
				else{
					st.executeUpdate("Update tbl_inbound_config set Last_Run_Date=SYSDATE(),Last_Run_Status='"+schedulerStatus+"' " +
							" where External_AppName='"+this.getClass().getSimpleName()+"'");
				}
			}
			st.executeUpdate("Insert into tbl_inbound_log (Inbound_configid,Request_Data,Response_Data,Response_File_Name,Status,Run_DateANDTime,Response_File_Size,Response_File_Directory) " +
					"values ("+"(Select Inbound_configid from tbl_inbound_config where External_AppName='"+this.getClass().getSimpleName()+"')"+",'"+userNameSCS+"||"+passWordSCS+"||"+ftpAddressFilePathSCS+"','"+schedulerMessage+"','"+fileName+"','"+schedulerStatus+"',"+"SYSDATE()"+",'"+ fileSize + "','"+(Directory+fileName) + "')");
			

			}//End of IF loop
			
			else
			{
				schedulerStatus="Success";
            	schedulerMessage="Error! No File to read,File already read";
            	
            	String currentcount="";
    			ResultSet queryToCheckIfExtrAppNameExists = st.executeQuery("Select count(*) as ExtrAppName from tbl_inbound_config where External_AppName = '"
    				+this.getClass().getSimpleName()+ "' ");
    			//System.out.println(queryToCheckIfExtrAppNameExists);
    			//double monthEnergyData = 0.0;
    			System.out.println("checkAnyDayAnyMonth "+queryToCheckIfExtrAppNameExists.getRow());
    			if(queryToCheckIfExtrAppNameExists.next()){
    			currentcount=queryToCheckIfExtrAppNameExists.getString("ExtrAppName");
    			}
    			if (currentcount.equals("0")) {
    				
    				st.executeUpdate("Insert into tbl_inbound_config (External_AppName,UserName,Password,External_AppName_URL,Activate_Date,Last_Run_Date,Last_Success_Date,Last_Run_Status) " +
    						"values ('"+this.getClass().getSimpleName()+"','"+userNameSCS+"','"+passWordSCS+"','"+ftpAddressFilePathSCS+"',SYSDATE(),SYSDATE(),SYSDATE(),'"+ schedulerStatus + "')");
    				
    				st.executeUpdate("Insert into tbl_inbound_log (Inbound_configid,Request_Data,Response_Data,Response_File_Name,Status,Run_DateANDTime,Response_File_Size,Response_File_Directory) " +
        					"values ("+"(Select Inbound_configid from tbl_inbound_config where External_AppName='"+this.getClass().getSimpleName()+"')"+",'"+userNameSCS+"||"+passWordSCS+"||"+ftpAddressFilePathSCS+"','"+schedulerMessage+"','"+fileName+"','"+schedulerStatus+"',"+"SYSDATE()"+",'"+ fileSize + "','"+(Directory+fileName) + "')");
       						
    			}else{
            	st.executeUpdate("Update tbl_inbound_config set Last_Run_Date=SYSDATE(),Last_Run_Status='"+schedulerStatus+"',Last_Success_date=" +"SYSDATE()"+
						" where External_AppName='"+this.getClass().getSimpleName()+"'");
            	
            	st.executeUpdate("Insert into tbl_inbound_log (Inbound_configid,Request_Data,Response_Data,Response_File_Name,Status,Run_DateANDTime,Response_File_Size,Response_File_Directory) " +
    					"values ("+"(Select Inbound_configid from tbl_inbound_config where External_AppName='"+this.getClass().getSimpleName()+"')"+",'"+userNameSCS+"||"+passWordSCS+"||"+ftpAddressFilePathSCS+"','"+schedulerMessage+"','"+fileName+"','"+schedulerStatus+"',"+"SYSDATE()"+",'"+ fileSize + "','"+(Directory+fileName) + "')");
            	
            	System.out.println("Error! No File in Location to read for Current date, File already read!!!");
    			}
			}
			

		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

		catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} 

	}
		EXIT		

}