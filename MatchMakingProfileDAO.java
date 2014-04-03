package org.mindlinks.HibernateDAO;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
//import javax.persistence.Query;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
//import javax.management.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.transform.Transformers;
import org.mindlinks.Bean.MatchingProfiles;
import org.mindlinks.Bean.Profile;
import org.mindlinks.Bean.User;


import com.mysql.jdbc.log.Log;



public class MatchMakingProfileDAO
{
	private static final Logger  log =Logger.getLogger(MatchMakingProfileDAO.class);

	public void saveMatchMakingProfile8(MatchingProfiles mprofile)
	{

		log.info("ENTER:: THE SaveMatching Profile DAO"+mprofile.getGender());
		SessionFactory sf =HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		try
		{
			log.info("ENTER:: THE CREATE PROFILE  DAO TRY BLOCK"+mprofile.getCaste());
			log.info("ENTER:: THE SaveMatching Profile DAO"+mprofile.getGender());
			session = sf.openSession();
			trans = session.beginTransaction();
			mprofile.setCreateddate(new Date(utilDate.getTime()));
			//mprofile.setCreatedby(new Date(utilDate.getTime()));
			mprofile.setModifiedDate(new Date(utilDate.getTime()));
			mprofile.setGender(mprofile.getGender());
			mprofile.setReligion(mprofile.getReligion());
			mprofile.setCaste(mprofile.getCaste());
			mprofile.setFromage(mprofile.getFromage());
			mprofile.setToage(mprofile.getToage());
			System.out.println("The date is"+utilDate);

			//fts.beginTransaction();
			log.info("ENTER::Before save"+mprofile);
			session.save(mprofile);
			trans.commit();
			log.info("ENTER:: After Save"+mprofile);

		}catch (Exception e) 
		{
			// TODO: handle exception
			System.out.println("The message is "+e.getMessage());

		}


	}
	
	public void saveMatchMakingProfile(MatchingProfiles mprofile)
	{

		log.info("ENTER:: THE SaveMatching Profile DAO"+mprofile.getGender());
		String ToEmailId = null;
		String htmlText1 = "";
		String male = "male";
		String female = "female";
		StringBuilder HtmlContent = new StringBuilder();
		String gender = "";
		SessionFactory sf =HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		
		MatchingProfiles profiles = new MatchingProfiles();
		
	
		
		log.info("ENTER IN TO THE SESSION IN THE PROFILE GET ID ");
		
		Criteria crt = null;
		System.out.println("ProfileId inside profiledao is  " + mprofile.getProfileid());
		
		try
		{
			
			session = sf.openSession();
			trans = session.beginTransaction();
			crt = session.createCriteria(MatchingProfiles.class);
			profiles = (MatchingProfiles) crt.add(Restrictions.eq("profileid", mprofile.getProfileid())).uniqueResult();
			if(profiles==null)
			{
				System.out.println("+++++++++++++++save  profile ++++++++++++++++++++++++++");
				
				
				log.info("ENTER:: THE CREATE PROFILE  DAO TRY BLOCK"+mprofile.getCaste());
				log.info("ENTER:: THE SaveMatching Profile DAO"+mprofile.getGender());
				//session = sf.openSession();
				//trans = session.beginTransaction();
				mprofile.setCreateddate(new Date(utilDate.getTime()));
				//mprofile.setCreatedby(new Date(utilDate.getTime()));
				mprofile.setModifiedDate(new Date(utilDate.getTime()));
				mprofile.setGender(mprofile.getGender());
				mprofile.setReligion(mprofile.getReligion());
				mprofile.setCaste(mprofile.getCaste());
				mprofile.setFromage(mprofile.getFromage());
				mprofile.setToage(mprofile.getToage());
				System.out.println("The date is"+utilDate);
				
				//fts.beginTransaction();
				log.info("ENTER::Before save"+mprofile);
				session.save(mprofile);
				trans.commit();
				log.info("ENTER:: After Save"+mprofile);
				
				System.out.println("+++++++++++++++save  profile ++++++++++++++++++++++++++");
				
				System.out.println(">>>>>>>>>>>>>>>>Matchmakig Profile send email start here<<<<<<<<<<<<<<<<<<<<<");

				Map<String, ArrayList<Profile>> matchedprofiles = new HashMap<String, ArrayList<Profile>>();
				
				int i = 0;
				System.out.println("<<<<<<<<<<<<<<<<<<1st for>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				i++;
				// System.out.println("caste is    "+r.getCaste());
				String[] castearray = mprofile.getCaste().split(",");
				String castes = "";
				// System.out.println("count is    "+i);

				for (int j = 0; j < castearray.length; j++) // spilting the
															// caste
				{
					if (j < castearray.length - 1) {
						castes = castes + castearray[j].trim() + "','";
					} else {
						castes = castes + castearray[j].trim();
					}

				}
				castes = "'" + castes + "'";
				System.out.println("caste is  "+i+ "   " +castes);
				
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				// System.out.println("Education is "+r.getEducation());
				String[] eduArray = mprofile.getEducation().split(",");
				String education = "";
				for (int k = 0; k < eduArray.length; k++) // Spilting education
				{
					if (k < eduArray.length - 1) {
						education = education + eduArray[k].trim() + "','";
					} else {
						education = education + eduArray[k].trim();
					}
				}

				education = "'" + education + "'";
				// System.out.println("Education is"+education);

				// System.out.println("Work location is:"+r.getWorklocation());
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				String[] workarray = mprofile.getWorklocation().split(",");
				String work = "";
				for (int n = 0; n < eduArray.length; n++) {
					if (n < workarray.length - 1) // Spiliting worklocation
					{
						work = work + workarray[n].trim() + "','";
					} else {
						work = work + workarray[n].trim();
					}
				}
				work = "'" + work + "'";
				System.out.println("worklocation is----------------->"+work);
				String fromage = mprofile.getFromage();
				System.out.println("From age is-------------->"+fromage);
				String toage = mprofile.getToage();
				System.out.println("To age is:---------->" + toage);
				
				String gender1 = "'" + mprofile.getGender() + "'";
				// System.out.println(strFemale);
				System.out.println("---------------------------Before query excecute-----------------"+gender1);
				System.out.println("---------------------------Before query excecute-----------------");
				
				ArrayList<Profile> profiles1 = (ArrayList<Profile>) session
						.createQuery(
								"from Profile p where  p.caste in(" + castes
										+ ") and p.education in(" + education
										+ ")and p.workplace in(" + work
										+ ") and p.age BETWEEN " + fromage
										+ " and " + toage + " and p.gender ="
										+ gender1).list();

				System.out.println("---------------------------Before query excecute-----------------");

				System.out.println("The Email id is by" + mprofile.getUsername());
				ToEmailId = mprofile.getUsername();
				System.out.println("<<<<<<<<<<<<<<<<<<<<ToEmailId>>>>>>>>>>>>>>>>>>>>>>>>>"+ ToEmailId);
				matchedprofiles.put(mprofile.getUsername(), profiles1);
				// System.out.println("++++++Matching profile email matchmaking email id+++++++++"+matchedprofiles);

				ArrayList<Profile> profiless = matchedprofiles.get(mprofile.getUsername());
				System.out.println(matchedprofiles);
				
				
				int a=0;
				
				int count=0;
				for (Profile p : profiless) {
					
					//System.out.println("------------------------>>>>>"+profiless.get(i));
					
					//for(int l=0;l<=9;l++)
					//{
						
					
					
					//	System.out.println("**********************The L value is*********************"+l);
						
						
					
					System.out.println("-----------iterate profile------------");
					System.out.println("+++++++++++++++The profile size is++++++++:"+profiless.size());
					
					//System.out.println("-----------<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>>------------"+profiles.size());
					System.out.println("In profile id is " + p.getProfileid());
					System.out.println("The emailed matching is" + p.getEmail());
					System.out.println("The castge is:" + p.getCaste());
					System.out.println("The country living is:"+ p.getCountrylivingin());
					System.out.println("The state is:" + p.getState());
					System.out.println("The district is:" + p.getCity());
					System.out.println("The email id is:" + p.getEmail());

					System.out.println("+++++++++ in inside for each The profiles+++++++++++"+ p);
					System.out.println("-----------<<<<<<<<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>------------");
					System.out.println("r.getProfileid()" + mprofile.getProfileid());
					System.out.println("r.getProfileid()" + p.getProfileid());

					System.out.println("The gender is:" + mprofile.getGender());
					System.out.println("The gender is:" + mprofile.getGender());
					Properties prop = new Properties();
					prop.load(MatchMakingProfileSendEmail.class.getClassLoader().getResourceAsStream("resources.properties"));
					String mysangathi = prop.getProperty("mysangathi");
				  //  String mysangathi="http://localhost:8094/mysangathi";
					
					if (mprofile.getGender().equals(female)) {					//Female profiles
						gender = mprofile.getGender();
						System.out.println("The gender is:--------->"+ mprofile.getGender());
						htmlText1 = "<table width=830px height=20px bgcolor=#E6E6FA border=1px RULES=NONE FRAME=BOX />";
						htmlText1 = htmlText1
								+ "<tr align=left><td width=70px> <img src=\"cid:image1\" height=70px width=70px  >"
								
								+ "</td>"
								+ "<td><b><ul>"
								+ "\t"+ p.getFirstname()+","
								+ "\t" + p.getAge() + "," + p.getReligion()
								+ "," + p.getCaste() + "," + p.getCity() + ","
								+ p.getState() + "," + p.getCountry() + "</ul>"

								+ "<ul><a href="+mysangathi+"/UI/Subscribe_ViewPartnersProfile.jsp?profileid="+ mprofile.getProfileid()+ "&username="+ p.getProfileid()
				         	+ ">view profile</a></ul>"

								+ "</td> </tr></table>";
						// }
						System.out.println("++++++++++++hhhh+++++++++++>>>>>>>>>>>>>>>"+ htmlText1);
						HtmlContent.append(htmlText1);
					} else {
						gender = mprofile.getGender();   //Male profiles
						System.out.println("The gender is----------->>>>"+ gender);
								
						htmlText1 = "<table width=830px height=20px bgcolor=#E6E6FA border=1px RULES=NONE FRAME=BOX />";
						
						htmlText1 = htmlText1
								+ "<tr align=left><td width=70px> <img src=\"cid:image1\" height=70px width=70px  >"
								// +"<td width=10px colspan=3>"
								+ "</td>"
								+ "<td><b><ul>"
								+ "\t" + p.getAge() + "," + p.getReligion()
								+ "," + p.getCaste() + "," + p.getCity() + ","
								+ p.getState() + "," + p.getCountry() + "</ul>"

								+ "<ul><a href="+mysangathi+"/UI/Subscribe_ViewPartnersProfile.jsp?profileid="+ mprofile.getProfileid()+ "&username="+ p.getProfileid()
				   	+ ">view profile.</a></ul>"

								+ "</td> </tr></table>";
						// }
						System.out.println("++++++++++++hhhh+++++++++++>>>>>>>>>>>>>>>"+ htmlText1);
						HtmlContent.append(htmlText1);
					}
					
					count++;
					if(count==9)
					{
						System.out.println(">>>>>>>>>>>>>>>>>>>>inside if count is 10<<<<<<<<<<<<<<<<<<<"+count);
						break;
					}
				}//end of for
				System.out.println("=========================send email code starts here====================================");
				System.out.println("-----------------HtmlContent------------------------->"+ HtmlContent);
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.googlemail.com");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");

				javax.mail.Session mailSession;
				mailSession = javax.mail.Session.getDefaultInstance(props,
						new Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(
										"admin@mysangathi.com",
										"mysangathi123#");
							}
						});

				try {
					
					String FromAddress="";
					String ToAddress="";
					String Content2="";
					String Subject="";

					System.out.println("Inside mail sending try block");
					Message message = new MimeMessage(mailSession);
					message.setFrom(new InternetAddress("admin@mysangathi.com"));
					// System.out.println("To email id is:"+toemail);
					// System.out.println("<----------The to email addres is:------------->"+toemailarray);
					System.out.println("To email id is:" + mprofile.getUsername());

					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(mprofile.getUsername())); //mprofile.getUsername()
					message.setSubject("Your perfect Matching profiles");

					Multipart multipart = new javax.mail.internet.MimeMultipart("related");
					
					BodyPart messageBodyPart = new MimeBodyPart();
					
					String Content = "<table width=830px  div bgcolor=#FFFFE0 style=#2C6700   border=1 border: thick solid #FF0000 >"   //outer table
							+ "<table width=830px  border=1 height=60px CELLSPACING=0 CELLPADDING=0 BORDERCOLOR=#2C6700 >"    //Inner table inserted 2 images
							// +"<tbody>"
							+ "<tr><td height=60px width=200px><img src=\"cid:image2\" height=80px width=270px /></td>"
							+ " <img src=\"cid:image\" height=80px width=350px /> "
							+ "<td height=60px width=200px>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><u>Contact Us:</u></b><br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;<b> Office</b>  (080) 26723353 <br>&nbsp;&nbsp;&nbsp;&nbsp;<b> Mobile </b>- (91) 9535214563<br>"
							+ "<b>Email:</b>"
							+ "contact@mysangathi.com"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;www.mysangathi.com</td>"
							+ "</table>"
							+ "<b><br/> <font color=#2C6700 size="
							+ 3
							+ ">Dear "
							+ mprofile.getName()
							+ ",</b><br/>"
							+ "<p ><font  size="
							+ 2
							+ ">MySangathi team suggest the following newly posted / updated profile(s) suit your selected criteria.</font><br>  "
							+ "<p><font color=#7D053F size="
							+ 2
							+ "><b>Latest Matches For You</b></font><p>"
							+ HtmlContent.toString()  //Appending male or female profile

							+ "<p><a href="
							+ "www.mysangathi.com"
							+ "><p><b>View More Matches</b></p></a> "
							+ "</br><p><b><span> Best Regards, </span></p></b>"
							+ " <p><b><span>  B.H.Manjunath IAS Rtd </p></b></span>  <p><b><span> Founder:MySangathi.com Matrimonial Services.</b></span> </p>"
							+ "</br><b><span>   <p>Web Site:  www.mysangathi.com</p><p>Mail Id:  admin@mysangathi.com</p></b></span></table>  ";
					
					messageBodyPart.setContent(Content, "text/html");
					
					//added the thread class for save the email history table on 12-02-13 of javed
					//start
					FromAddress="admin@mysangathi.com";
					ToAddress=mprofile.getUsername();
					Content2="<table width=830px  div bgcolor=#FFFFE0 style=#2C6700   border=1 border: thick solid #FF0000 >"   //outer table
						+ "<table width=830px  border=1 height=60px CELLSPACING=0 CELLPADDING=0 BORDERCOLOR=#2C6700 >"    //Inner table inserted 2 images
						// +"<tbody>"
						+ "<tr><td height=60px width=200px><img src=\"cid:image2\" height=80px width=270px /></td>"
						+ " <img src=\"cid:image\" height=80px width=350px /> "
						+ "<td height=60px width=200px>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><u>Contact Us:</u></b><br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;<b> Office</b>  (080) 26723353 <br>&nbsp;&nbsp;&nbsp;&nbsp;<b> Mobile </b>- (91) 9535214563<br>"
						+ "<b>Email:</b>"
						+ "contact@mysangathi.com"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;www.mysangathi.com</td>"
						+ "</table>"
						+ "<b><br/> <font color=#2C6700 size="
						+ 3
						+ ">Dear "
						+ mprofile.getName()
						+ ",</b><br/>"
						+ "<p ><font  size="
						+ 2
						+ ">MySangathi team suggest the following newly posted / updated profile(s) suit your selected criteria.</font><br>  "
						+ "<p><font color=#7D053F size="
						+ 2
						+ "><b>Latest Matches For You</b></font><p>"
						+ HtmlContent.toString()  //Appending male or female profile

						+ "<p><a href="
						+ "www.mysangathi.com"
						+ "><p><b>View More Matches</b></p></a> "
						+ "</br><p><b><span> Best Regards, </span></p></b>"
						+ " <p><b><span>  B.H.Manjunath IAS Rtd </p></b></span>  <p><b><span> Founder:MySangathi.com Matrimonial Services.</b></span> </p>"
						+ "</br><b><span>   <p>Web Site:  www.mysangathi.com</p><p>Mail Id:  admin@mysangathi.com</p></b></span></table>  ";
					
					Subject="Your perfect Matching profiles";
					//added the thread class for save the email history table on 12-02-13 javed
					Thread emailhandler3 = new Thread(new EmailHandlerThread(FromAddress, ToAddress, Content2, Subject,0 ));
					emailhandler3.start();
					//End Here
					
					// add it
					multipart.addBodyPart(messageBodyPart);
					// second part (the image)
					if (gender.equals(female)) {
						messageBodyPart = new MimeBodyPart();
						//DataSource fds = new FileDataSource("D:\\images\\profileFemale.jpg");
						DataSource fds = new FileDataSource("/usr/share/schedulerimages/image/profileFemale.jpg");
						messageBodyPart.setDataHandler(new DataHandler(fds));
						messageBodyPart.setHeader("Content-ID", "<image1>");
						multipart.addBodyPart(messageBodyPart);
					}
					if (gender.equals(male)) {
						messageBodyPart = new MimeBodyPart(); 
						//DataSource fds = new FileDataSource("D:\\images\\profileMale.jpg");
						DataSource fds = new FileDataSource("/usr/share/schedulerimages/image/profileMale.jpg");
						messageBodyPart.setDataHandler(new DataHandler(fds));
						messageBodyPart.setHeader("Content-ID", "<image1>");
						multipart.addBodyPart(messageBodyPart);

					}
					// add it
					
					messageBodyPart = new MimeBodyPart();
					//DataSource fds1 = new FileDataSource("D:\\images\\Capture.jpg");
					DataSource fds1 = new FileDataSource("/usr/share/schedulerimages/image/Capture.jpg");
					messageBodyPart.setDataHandler(new DataHandler(fds1));
					messageBodyPart.setHeader("Content-ID", "<image>");
					multipart.addBodyPart(messageBodyPart);

					messageBodyPart = new MimeBodyPart();
					//DataSource fds2 = new FileDataSource("D:\\images\\hlogo.jpg");
					DataSource fds2 = new FileDataSource("/usr/share/schedulerimages/image/hlogo.jpg");
					messageBodyPart.setDataHandler(new DataHandler(fds2));
					messageBodyPart.setHeader("Content-ID", "<image2>");
					multipart.addBodyPart(messageBodyPart);

					// put everything together

					message.setContent(multipart);

					System.out.println("Before mailsending" + profiles1.size());
					if (profiles1.size() >= 1) {

						Transport.send(message);
						System.out.println("Mail Sent");

					}
					HtmlContent.delete(0, HtmlContent.length());

				}// End 2nd try block
				catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("EXception is:" + e);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("EXception is:" + e);
				}

			//}//End the matching profiles list
				
				
				
				
				
			}else
			{
				
			
			//System.out.println("+++++++++++++++no profile is fiend++++++++++++++++++++++++++");
			
			
			//System.out.println("-----------profiles.getReligion()------------->>>>>"+profiles.getReligion());
			//System.out.println("----------profiles.getCaste()-------------->>>>>"+profiles.getCaste());
			//System.out.println("------------profiles.getEducation()------------>>>>>"+profiles.getEducation());
			//System.out.println("------------profiles.getWorklocation()------------>>>>>"+profiles.getWorklocation());
			
			
			//System.out.println("-----------mprofile.getReligion()------------->>>>>"+mprofile.getReligion());
			//System.out.println("----------mprofile.getCaste()-------------->>>>>"+mprofile.getCaste());
			//System.out.println("------------mprofile.getEducation()------------>>>>>"+mprofile.getEducation());
			//System.out.println("------------mprofile.getWorklocation()------------>>>>>"+mprofile.getWorklocation());
			
			profiles.setCaste(mprofile.getCaste());
			profiles.setReligion(mprofile.getReligion());
			profiles.setFromage(mprofile.getFromage());
			profiles.setToage(mprofile.getToage());
			profiles.setEducation(mprofile.getEducation());
			profiles.setWorklocation(mprofile.getWorklocation());
			profiles.setModifiedDate(new java.util.Date());
			
			session.update(profiles);
			trans.commit();
			log.info("EXIT:: THE UPDATE PROFILE  DAO");
			System.out.println("+++++++++++++++update match making  profile is is created++++++++++++++++++++++++++");
			
			
			System.out.println(">>>>>>>>>>>>>>>>Update Matchmakig Profile send email start here<<<<<<<<<<<<<<<<<<<<<");

			Map<String, ArrayList<Profile>> matchedprofiles = new HashMap<String, ArrayList<Profile>>();
			
			int i = 0;
			System.out.println("<<<<<<<<<<<<<<<<<<update 1st for>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			i++;
			// System.out.println("caste is    "+r.getCaste());
			String[] castearray = mprofile.getCaste().split(",");
			String castes = "";
			// System.out.println("count is    "+i);

			for (int j = 0; j < castearray.length; j++) // spilting the
														// caste
			{
				if (j < castearray.length - 1) {
					castes = castes + castearray[j].trim() + "','";
				} else {
					castes = castes + castearray[j].trim();
				}

			}
			castes = "'" + castes + "'";
			System.out.println("caste is  "+i+ "   " +castes);
			
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			// System.out.println("Education is "+r.getEducation());
			String[] eduArray = mprofile.getEducation().split(",");
			String education = "";
			for (int k = 0; k < eduArray.length; k++) // Spilting education
			{
				if (k < eduArray.length - 1) {
					education = education + eduArray[k].trim() + "','";
				} else {
					education = education + eduArray[k].trim();
				}
			}

			education = "'" + education + "'";
			// System.out.println("Education is"+education);

			// System.out.println("Work location is:"+r.getWorklocation());
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<update>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			String[] workarray = mprofile.getWorklocation().split(",");
			String work = "";
			for (int n = 0; n < eduArray.length; n++) {
				if (n < workarray.length - 1) // Spiliting worklocation
				{
					work = work + workarray[n].trim() + "','";
				} else {
					work = work + workarray[n].trim();
				}
			}
			work = "'" + work + "'";
			System.out.println("worklocation is----------------->"+work);
			String fromage = mprofile.getFromage();
			System.out.println("From age is-------------->"+fromage);
			String toage = mprofile.getToage();
			System.out.println("To age is:---------->" + toage);
			
			String gender1 = "'" + mprofile.getGender() + "'";
			// System.out.println(strFemale);
			System.out.println("--------------------update-------Before query excecute-----------------"+gender1);
			System.out.println("--------------------update-------Before query excecute-----------------");
			
			ArrayList<Profile> profiles1 = (ArrayList<Profile>) session
					.createQuery(
							"from Profile p where  p.caste in(" + castes
									+ ") and p.education in(" + education
									+ ")and p.workplace in(" + work
									+ ") and p.age BETWEEN " + fromage
									+ " and " + toage + " and p.gender ="
									+ gender1).list();

			System.out.println("---------------------------Before query excecute-----------------");

			System.out.println("The Email id is by" + mprofile.getUsername());
			ToEmailId = mprofile.getUsername();
			System.out.println("<<<<<<<<<<<<<<<<<<<<ToEmailId>>>>>>>>>>>>>>>>>>>>>>>>>"+ ToEmailId);
			matchedprofiles.put(mprofile.getUsername(), profiles1);
			// System.out.println("++++++Matching profile email matchmaking email id+++++++++"+matchedprofiles);

			ArrayList<Profile> profiless = matchedprofiles.get(mprofile.getUsername());
			System.out.println(matchedprofiles);
			
			
			int a=0;
			
			int count=0;
			for (Profile p : profiless) {
				
				//System.out.println("------------------------>>>>>"+profiless.get(i));
				
				//for(int l=0;l<=9;l++)
				//{
					
				
				
				//	System.out.println("**********************The L value is*********************"+l);
					
					
				
				System.out.println("-----------iterate profile------------");
				System.out.println("+++++++++++++++The profile size is++++++++:"+profiless.size());
				
				//System.out.println("-----------<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>>------------"+profiles.size());
				System.out.println("In profile id is " + p.getProfileid());
				System.out.println("The emailed matching is" + p.getEmail());
				System.out.println("The castge is:" + p.getCaste());
				System.out.println("The country living is:"+ p.getCountrylivingin());
				System.out.println("The state is:" + p.getState());
				System.out.println("The district is:" + p.getCity());
				System.out.println("The email id is:" + p.getEmail());

				System.out.println("+++++++++ in inside for each The profiles+++++++++++"+ p);
				System.out.println("-----------<<<<<<<<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>------------");
				System.out.println("r.getProfileid()" + mprofile.getProfileid());
				System.out.println("r.getProfileid()" + p.getProfileid());

				System.out.println("The gender is:" + mprofile.getGender());
				System.out.println("The gender is:" + mprofile.getGender());
				Properties prop = new Properties();
				prop.load(MatchMakingProfileSendEmail.class.getClassLoader().getResourceAsStream("resources.properties"));
				String mysangathi = prop.getProperty("mysangathi");
			  //  String mysangathi="http://localhost:8094/mysangathi";
				
				if (mprofile.getGender().equals(female)) {					//Female profiles
					gender = mprofile.getGender();
					System.out.println("The gender is:--------->"+ mprofile.getGender());
					htmlText1 = "<table width=830px height=20px bgcolor=#E6E6FA border=1px RULES=NONE FRAME=BOX />";
					htmlText1 = htmlText1
							+ "<tr align=left><td width=70px> <img src=\"cid:image1\" height=70px width=70px  >"
							
							+ "</td>"
							+ "<td><b><ul>"
							+ "\t"+ p.getFirstname()+","
							+ "\t" + p.getAge() + "," + p.getReligion()
							+ "," + p.getCaste() + "," + p.getCity() + ","
							+ p.getState() + "," + p.getCountry() + "</ul>"

							+ "<ul><a href="+mysangathi+"/UI/Subscribe_ViewPartnersProfile.jsp?profileid="+ mprofile.getProfileid()+ "&username="+ p.getProfileid()
			         	+ ">view profile</a></ul>"

							+ "</td> </tr></table>";
					// }
					System.out.println("++++++++++++hhhh+++++++++++>>>>>>>>>>>>>>>"+ htmlText1);
					HtmlContent.append(htmlText1);
				} else {
					gender = mprofile.getGender();   //Male profiles
					System.out.println("The gender is----------->>>>"+ gender);
							
					htmlText1 = "<table width=830px height=20px bgcolor=#E6E6FA border=1px RULES=NONE FRAME=BOX />";
					
					htmlText1 = htmlText1
							+ "<tr align=left><td width=70px> <img src=\"cid:image1\" height=70px width=70px  >"
							// +"<td width=10px colspan=3>"
							+ "</td>"
							+ "<td><b><ul>"
							+ "\t" + p.getAge() + "," + p.getReligion()
							+ "," + p.getCaste() + "," + p.getCity() + ","
							+ p.getState() + "," + p.getCountry() + "</ul>"

							+ "<ul><a href="+mysangathi+"/UI/Subscribe_ViewPartnersProfile.jsp?profileid="+ mprofile.getProfileid()+ "&username="+ p.getProfileid()
			   	+ ">view profile.</a></ul>"

							+ "</td> </tr></table>";
					// }
					System.out.println("++++++++++++hhhh+++++++++++>>>>>>>>>>>>>>>"+ htmlText1);
					HtmlContent.append(htmlText1);
				}
				
				count++;
				if(count==9)
				{
					System.out.println(">>>>>>>>>>>>>>>>>>>>inside if count is 10<<<<<<<<<<<<<<<<<<<"+count);
					break;
				}
			}//end of for
			System.out.println("=========================send email code starts here====================================");
			System.out.println("-----------------HtmlContent------------------------->"+ HtmlContent);
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.googlemail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			javax.mail.Session mailSession;
			mailSession = javax.mail.Session.getDefaultInstance(props,
					new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									"admin@mysangathi.com",
									"mysangathi123#");
						}
					});

			try {
				
				String FromAddress="";
				String ToAddress="";
				String Content2="";
				String Subject="";

				System.out.println("Inside mail sending try block");
				Message message = new MimeMessage(mailSession);
				message.setFrom(new InternetAddress("admin@mysangathi.com"));
				// System.out.println("To email id is:"+toemail);
				// System.out.println("<----------The to email addres is:------------->"+toemailarray);
				System.out.println("To email id is:" + mprofile.getUsername());

				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(mprofile.getUsername())); //mprofile.getUsername()
				message.setSubject("Your perfect Matching profiles");

				Multipart multipart = new javax.mail.internet.MimeMultipart("related");
				
				BodyPart messageBodyPart = new MimeBodyPart();
				
				String Content = "<table width=830px  div bgcolor=#FFFFE0 style=#2C6700   border=1 border: thick solid #FF0000 >"   //outer table
						+ "<table width=830px  border=1 height=60px CELLSPACING=0 CELLPADDING=0 BORDERCOLOR=#2C6700 >"    //Inner table inserted 2 images
						// +"<tbody>"
						+ "<tr><td height=60px width=200px><img src=\"cid:image2\" height=80px width=270px /></td>"
						+ " <img src=\"cid:image\" height=80px width=350px /> "
						+ "<td height=60px width=200px>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><u>Contact Us:</u></b><br>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;<b> Office</b>  (080) 26723353 <br>&nbsp;&nbsp;&nbsp;&nbsp;<b> Mobile </b>- (91) 9535214563<br>"
						+ "<b>Email:</b>"
						+ "contact@mysangathi.com"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;www.mysangathi.com</td>"
						+ "</table>"
						+ "<b><br/> <font color=#2C6700 size="
						+ 3
						+ ">Dear "
						+ mprofile.getName()
						+ ",</b><br/>"
						+ "<p ><font  size="
						+ 2
						+ ">MySangathi team suggest the following newly posted / updated profile(s) suit your selected criteria.</font><br>  "
						+ "<p><font color=#7D053F size="
						+ 2
						+ "><b>Latest Matches For You</b></font><p>"
						+ HtmlContent.toString()  //Appending male or female profile

						+ "<p><a href="
						+ "www.mysangathi.com"
						+ "><p><b>View More Matches</b></p></a> "
						+ "</br><p><b><span> Best Regards, </span></p></b>"
						+ " <p><b><span>  B.H.Manjunath IAS Rtd </p></b></span>  <p><b><span> Founder:MySangathi.com Matrimonial Services.</b></span> </p>"
						+ "</br><b><span>   <p>Web Site:  www.mysangathi.com</p><p>Mail Id:  admin@mysangathi.com</p></b></span></table>  ";
				
				messageBodyPart.setContent(Content, "text/html");
				
				//added the thread class for save the email history table on 12-02-13 of javed
				//start
				FromAddress="admin@mysangathi.com";
				ToAddress=mprofile.getUsername();
				Content2="<table width=830px  div bgcolor=#FFFFE0 style=#2C6700   border=1 border: thick solid #FF0000 >"   //outer table
					+ "<table width=830px  border=1 height=60px CELLSPACING=0 CELLPADDING=0 BORDERCOLOR=#2C6700 >"    //Inner table inserted 2 images
					// +"<tbody>"
					+ "<tr><td height=60px width=200px><img src=\"cid:image2\" height=80px width=270px /></td>"
					+ " <img src=\"cid:image\" height=80px width=350px /> "
					+ "<td height=60px width=200px>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><u>Contact Us:</u></b><br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;<b> Office</b>  (080) 26723353 <br>&nbsp;&nbsp;&nbsp;&nbsp;<b> Mobile </b>- (91) 9535214563<br>"
					+ "<b>Email:</b>"
					+ "contact@mysangathi.com"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;www.mysangathi.com</td>"
					+ "</table>"
					+ "<b><br/> <font color=#2C6700 size="
					+ 3
					+ ">Dear "
					+ mprofile.getName()
					+ ",</b><br/>"
					+ "<p ><font  size="
					+ 2
					+ ">MySangathi team suggest the following newly posted / updated profile(s) suit your selected criteria.</font><br>  "
					+ "<p><font color=#7D053F size="
					+ 2
					+ "><b>Latest Matches For You</b></font><p>"
					+ HtmlContent.toString()  //Appending male or female profile

					+ "<p><a href="
					+ "www.mysangathi.com"
					+ "><p><b>View More Matches</b></p></a> "
					+ "</br><p><b><span> Best Regards, </span></p></b>"
					+ " <p><b><span>  B.H.Manjunath IAS Rtd </p></b></span>  <p><b><span> Founder:MySangathi.com Matrimonial Services.</b></span> </p>"
					+ "</br><b><span>   <p>Web Site:  www.mysangathi.com</p><p>Mail Id:  admin@mysangathi.com</p></b></span></table>  ";
				
				Subject="Your perfect Matching profiles";
				//added the thread class for save the email history table on 12-02-13 javed
				Thread emailhandler3 = new Thread(new EmailHandlerThread(FromAddress, ToAddress, Content2, Subject,0 ));
				emailhandler3.start();
				//End Here
				
				// add it
				multipart.addBodyPart(messageBodyPart);
				// second part (the image)
				if (gender.equals(female)) {
					messageBodyPart = new MimeBodyPart();
					//DataSource fds = new FileDataSource("D:\\images\\profileFemale.jpg");
					DataSource fds = new FileDataSource("/usr/share/schedulerimages/image/profileFemale.jpg");
					messageBodyPart.setDataHandler(new DataHandler(fds));
					messageBodyPart.setHeader("Content-ID", "<image1>");
					multipart.addBodyPart(messageBodyPart);
				}
				if (gender.equals(male)) {
					messageBodyPart = new MimeBodyPart(); 
					//DataSource fds = new FileDataSource("D:\\images\\profileMale.jpg");
					DataSource fds = new FileDataSource("/usr/share/schedulerimages/image/profileMale.jpg");
					messageBodyPart.setDataHandler(new DataHandler(fds));
					messageBodyPart.setHeader("Content-ID", "<image1>");
					multipart.addBodyPart(messageBodyPart);

				}
				// add it
				
				messageBodyPart = new MimeBodyPart();
				//DataSource fds1 = new FileDataSource("D:\\images\\Capture.jpg");
				DataSource fds1 = new FileDataSource("/usr/share/schedulerimages/image/Capture.jpg");
				messageBodyPart.setDataHandler(new DataHandler(fds1));
				messageBodyPart.setHeader("Content-ID", "<image>");
				multipart.addBodyPart(messageBodyPart);

				messageBodyPart = new MimeBodyPart();
				//DataSource fds2 = new FileDataSource("D:\\images\\hlogo.jpg");
				DataSource fds2 = new FileDataSource("/usr/share/schedulerimages/image/hlogo.jpg");
				messageBodyPart.setDataHandler(new DataHandler(fds2));
				messageBodyPart.setHeader("Content-ID", "<image2>");
				multipart.addBodyPart(messageBodyPart);

				// put everything together

				message.setContent(multipart);

				System.out.println("Before mailsending" + profiles1.size());
				if (profiles1.size() >= 1) {

					Transport.send(message);
					System.out.println("Mail Sent");

				}
				HtmlContent.delete(0, HtmlContent.length());

			}// End 2nd try block
			catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("EXception is:" + e);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("EXception is:" + e);
			}
			
			}
			
		}catch (Exception e) 
		{
			// TODO: handle exception
			System.out.println("The message is "+e.getMessage());
			
		}
		
		
	}

	// Match the MatchingProfiles from profile
	boolean matchMakingProfiles33()
	{
		log.info("Inside matchmaking profile");
		SessionFactory sf=null;
		Session session=null;
		Transaction trans=null;
		String htmlText1="";
		int m=0;

		String ToEmailId=null;
		StringBuilder  HtmlContent=new StringBuilder();
		
		String firstName="";


		try{
			System.out.println("Try block");
			sf=HibernateUtil.getSessionFactory();
			session=sf.openSession();
			trans=session.beginTransaction();
			System.out.println(" Matching the profiles");
			//Query query1=(Query) session.createQuery("From MatchingProfiles");
			//System.out.println(query1);
			// List castelist = (List) session.createQuery("FROM MatchingProfiles").list();
			/* cri=session.createCriteria(MatchingProfiles.class);
			 user = (User) cri.add(Restrictions.eq("profileid", )).uniqueResult();*/

			/*Query query = (Query) session.createQuery("select c.caste,c.education from MatchingProfiles c");
			List selectedrows = (List) ((Criteria) query).list();
			 System.out.println(selectedrows);*/

			ArrayList<MatchingProfiles> results = (ArrayList<MatchingProfiles>) session.createQuery("from MatchingProfiles").list();


			Map<String,ArrayList<Profile>> matchedprofiles=new HashMap<String,ArrayList<Profile>>();
			int i=0;
			for(MatchingProfiles r:results){
				
				
				System.out.println("<<<<<<<<<<<<<<<<<<1st for>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				i++;
			//	System.out.println("caste is    "+r.getCaste());
				String[] castearray=r.getCaste().split(",");
				String castes="";
				//System.out.println("count is    "+i);

				for (int j = 0; j < castearray.length; j++)    //spilting the caste
				{
					if(j<castearray.length-1){
						castes=castes+castearray[j]+"','";
					}else{
						castes=castes+castearray[j];
					}

				}
				castes="'"+castes+"'";
				//System.out.println("caste is  "+i+ "   " +castes);

				//System.out.println("Education is "+r.getEducation());
				String[] eduArray=r.getEducation().split(",");
				String education="";
				for (int k = 0; k < eduArray.length; k++)		//Spilting education
				{
					if(k<eduArray.length-1)
					{
						education=education+eduArray[k]+"','";
					}else
					{
						education=education+eduArray[k];
					}
				}


				education="'"+education+"'";
			//	System.out.println("Education is"+education);

			//	System.out.println("Work location is:"+r.getWorklocation());

				String[] workarray=r.getWorklocation().split(",");
				String work="";
				for (int n = 0; n < eduArray.length; n++)
				{
					if(n<workarray.length-1)						//Spiliting worklocation
					{
						work=work+workarray[n]+"','";
					}else
					{
						work=work+workarray[n];
					}
				}
				work="'"+work+"'";
				//System.out.println("worklocation is----------------->"+work);
				String fromage=r.getFromage();
				//System.out.println("From age is-------------->"+fromage);
				String toage=r.getToage();
				System.out.println("To age is:---------->"+toage);
				//System.out.println("The gender from matchmaking profile---->>>>"+r.getGender());
				String strFemale="'"+r.getGender()+"'";
			//	System.out.println(strFemale);
				log.info("---------------------------Before query excecute-----------------");
				ArrayList<Profile> profiles = (ArrayList<Profile>) session.createQuery("from Profile p where  p.caste in("+castes+") and p.education in("+education+")and p.workplace in("+work+") and p.age BETWEEN "+fromage+" and "+toage+" and p.gender ="+strFemale).list();

				log.info("---------------------------Before query excecute-----------------");

				log.info("The Email id is by"+r.getUsername());
				ToEmailId=r.getUsername();
				log.info("<<<<<<<<<<<<<<<<<<<<ToEmailId>>>>>>>>>>>>>>>>>>>>>>>>>"+ToEmailId);
				matchedprofiles.put(r.getUsername(), profiles);
				//System.out.println("++++++Matching profile email matchmaking email id+++++++++"+matchedprofiles);
				Properties prop = new Properties();

				prop.load(MatchMakingProfileSendEmail.class.getClassLoader().getResourceAsStream("resources.properties"));
				String mysangathi = prop.getProperty("mysangathi");

               //  String mysangathi="http://localhost:8094/mysangathi";

				ArrayList<Profile> profiless=matchedprofiles.get(r.getUsername());
				System.out.println(matchedprofiles);

				log.info("-----------iterate profile------------");
				for (Profile p:profiless)
				{
					log.info("-----------<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>>------------");
					log.info("In profile id is "+p.getProfileid());
					log.info("The emailed matching is"+p.getEmail());
					log.info("The castge is:"+p.getCaste());
					log.info("The country living is:"+p.getCountrylivingin());
					log.info("The state is:"+p.getState());
					log.info("The district is:"+p.getCity());
					log.info("The email id is:"+p.getEmail());

					log.info("+++++++++ in inside for each The profiles+++++++++++"+p);
					log.info("-----------<<<<<<<<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>------------");

					if(r.getProfileid()==(p.getProfileid()))
					{
						log.info("The first name is:"+p.getFirstname());
						firstName=p.getFirstname();
						log.info("The first name is:"+firstName);
					}

					log.info("The gender is:"+r.getGender());
					log.info("The gender is:"+p.getGender());
					
					 htmlText1=
						 "<table width=100% bgcolor=#E6E6FA border=1px height=5%/>"
						 +"<tbody"
						 +"<tr>";
						 
							 htmlText1=htmlText1+"<p><br><b> Profile ID: "+p.getProfileid()+"</font><p></b><br/>"
							 +"<td colspan=0/></tr>"						 
							 
							+"<ul><p><b><p>Age :"+p.getAge()+"</p>"+"</br><p>Religion :"+p.getReligion()+"</p>"
							+"<p>caste:"+p.getCaste()+"</p>"
							+"<p>city:"+p.getCity()+"</p>"
							+"<p>sate:"+p.getState()+"</p>"
							+"<p>country :"+p.getCountry()+"</ul></p>"
							+"<ul> <a href="+mysangathi+"/UI/Subscribe_ViewPartnersProfile.jsp?profileid="+ r.getProfileid()+ "&username="+ p.getProfileid()
					+ ">Click here to view profile.</a></u>"
							+" </table>";
						// }
						log.info("++++++++++++hhhh+++++++++++>>>>>>>>>>>>>>>"+htmlText1);
						
						//htmlText1;
						HtmlContent.append(htmlText1);
					
				}
				
				
			
				
				
			}
			
			///System.out.println("-----------------HtmlContent------------------------->"+htmlText1);
			   System.out.println("-----------------HtmlContent------------------------->"+HtmlContent);
			    Properties  props=new Properties();
				props.put("mail.smtp.host", "smtp.googlemail.com");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");
				
				javax.mail.Session mailSession;
				mailSession=javax.mail.Session.getDefaultInstance(props, new  Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication("admin@mysangathi.com", "mysangathi123#");
					}
				});
				
				try
				{
					 System.out.println("Inside mail sending try block");
					Message  message=new MimeMessage(mailSession);
					message.setFrom(new InternetAddress("admin@mysangathi.com"));
					System.out.println("To email id is:"+ToEmailId);
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("javedbasha.s.m01@gmail.com"));
					message.setSubject("Your perfect Matching profiles");
					
			        Multipart multipart = new javax.mail.internet.MimeMultipart("related");  
			  
			        
			        BodyPart messageBodyPart = new MimeBodyPart(); 
			       
			       String  Content="<b> <font color=#2C6700 size="+3+">Dear "+firstName+ ",</b><br/>" +
						"<p ><font  size="+2+">We feel the following newly posted / updated profile(s) suit your Desired Partner Preference at MySangathi.com.</font></p><br>  " +
						 " \n<p><font color=#7D053F size="+3+"><b>Latest Matches For You</b></font><p>"+"<table width=100% div style=#2C6700 bgcolor=#C4C3D0 border=1px height=100%/>"+HtmlContent.toString()
						 
						 +"<a href="+"www.mysangathi.com"+"><p><b>View more matches</b></p></a><br/>"
							//+"<a>  href="+"http://www.mysangathi.com/"+" style="+"text-decoration:underline"+" target="+"_blank"+"more details"+"</a>"
							+ "</br><p><b><span> Best Regards, </span></p></b>"						
							+ " <p><b><span>  B.H.Manjunath IAS Rtd </p></b></span>  <p><b><span> Founder:MySangathi.com Matrimonial Services.</b></span> </p>"
							+ "</br><b><span>   <p>Web Site:  www.mysangathi.com</p><p>Mail Id:  admin@mysangathi.com</p></b></span></table>  " ;
			       
			      
				
			        messageBodyPart.setContent(Content, "text/html"); 
			        multipart.addBodyPart(messageBodyPart); 
			        message.setContent(multipart); 
			  
			        Transport.send(message);
			        System.out.println("done");
			        
			        
			        
			        

			    
				}
				catch (AddressException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e);
				}
				catch (MessagingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e);
				}
				
				
			

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		return true;

	}
	
	
	
	boolean matchMakingProfiles() {
		log.info("Inside matchmaking profile");
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		String htmlText1 = "";
		String ToEmailId = null;
		StringBuilder HtmlContent = new StringBuilder();
		String firstName = "";
		String male = "male";
		String female = "female";
		String gender = "";

		try {
			System.out.println("Try block");
			sf = HibernateUtil.getSessionFactory();
			session = sf.openSession();
			trans = session.beginTransaction();
			System.out.println(" Matching the profiles");

			ArrayList<MatchingProfiles> results = (ArrayList<MatchingProfiles>) session.createQuery("from MatchingProfiles").list();  // getting records from matching profiles

			Map<String, ArrayList<Profile>> matchedprofiles = new HashMap<String, ArrayList<Profile>>();
			int i = 0;
			for (MatchingProfiles r : results) { //Iterate the matching profiles list

				System.out.println("<<<<<<<<<<<<<<<<<<1st for>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				i++;
				// System.out.println("caste is    "+r.getCaste());
				String[] castearray = r.getCaste().split(",");
				String castes = "";
				// System.out.println("count is    "+i);

				for (int j = 0; j < castearray.length; j++) // spilting the
															// caste
				{
					if (j < castearray.length - 1) {
						castes = castes + castearray[j] + "','";
					} else {
						castes = castes + castearray[j];
					}

				}
				castes = "'" + castes + "'";
				// System.out.println("caste is  "+i+ "   " +castes);

				// System.out.println("Education is "+r.getEducation());
				String[] eduArray = r.getEducation().split(",");
				String education = "";
				for (int k = 0; k < eduArray.length; k++) // Spilting education
				{
					if (k < eduArray.length - 1) {
						education = education + eduArray[k] + "','";
					} else {
						education = education + eduArray[k];
					}
				}

				education = "'" + education + "'";
				// System.out.println("Education is"+education);

				// System.out.println("Work location is:"+r.getWorklocation());

				String[] workarray = r.getWorklocation().split(",");
				String work = "";
				for (int n = 0; n < eduArray.length; n++) {
					if (n < workarray.length - 1) // Spiliting worklocation
					{
						work = work + workarray[n] + "','";
					} else {
						work = work + workarray[n];
					}
				}
				work = "'" + work + "'";
				// System.out.println("worklocation is----------------->"+work);
				String fromage = r.getFromage();
				// System.out.println("From age is-------------->"+fromage);
				String toage = r.getToage();
				System.out.println("To age is:---------->" + toage);
				// System.out.println("The gender from matchmaking profile---->>>>"+r.getGender());
				String gender1 = "'" + r.getGender() + "'";
				// System.out.println(strFemale);
				System.out.println("---------------------------Before query excecute-----------------");
				ArrayList<Profile> profiles = (ArrayList<Profile>) session
						.createQuery(
								"from Profile p where  p.caste in(" + castes
										+ ") and p.education in(" + education
										+ ")and p.workplace in(" + work
										+ ") and p.age BETWEEN " + fromage
										+ " and " + toage + " and p.gender ="
										+ gender1).list();

				System.out.println("---------------------------Before query excecute-----------------");

				System.out.println("The Email id is by" + r.getUsername());
				ToEmailId = r.getUsername();
				System.out.println("<<<<<<<<<<<<<<<<<<<<ToEmailId>>>>>>>>>>>>>>>>>>>>>>>>>"+ ToEmailId);
				matchedprofiles.put(r.getUsername(), profiles);
				// System.out.println("++++++Matching profile email matchmaking email id+++++++++"+matchedprofiles);

				ArrayList<Profile> profiless = matchedprofiles.get(r.getUsername());
				System.out.println(matchedprofiles);
				
				
				int a=0;
				
				
				List<Profile>   list=new ArrayList<Profile>();

				
					
				
				System.out.println("=====================================================>>>"+profiless.listIterator());
				int count=0;
				for (Profile p : profiless) {
					
					//System.out.println("------------------------>>>>>"+profiless.get(i));
					
					//for(int l=0;l<=9;l++)
					//{
						
					
					
					//	System.out.println("**********************The L value is*********************"+l);
						
						
					
					System.out.println("-----------iterate profile------------");
					System.out.println("+++++++++++++++The profile size is++++++++:"+profiless.size());
					
					System.out.println("-----------<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>>------------"+profiles.size());
					System.out.println("In profile id is " + p.getProfileid());
					System.out.println("The emailed matching is" + p.getEmail());
					System.out.println("The castge is:" + p.getCaste());
					System.out.println("The country living is:"+ p.getCountrylivingin());
					System.out.println("The state is:" + p.getState());
					System.out.println("The district is:" + p.getCity());
					System.out.println("The email id is:" + p.getEmail());

					System.out.println("+++++++++ in inside for each The profiles+++++++++++"+ p);
					System.out.println("-----------<<<<<<<<<<<<<<<<<the matching profiles are>>>>>>>>>>>>>>>>>------------");
					System.out.println("r.getProfileid()" + r.getProfileid());
					System.out.println("r.getProfileid()" + p.getProfileid());

					System.out.println("The gender is:" + r.getGender());
					System.out.println("The gender is:" + p.getGender());
				    String mysangathi="http://localhost:8094/mysangathi";
					
					if (r.getGender().equals(female)) {					//Female profiles
						gender = r.getGender();
						System.out.println("The gender is:--------->"+ r.getGender());
						htmlText1 = "<table width=830px height=20px bgcolor=#E6E6FA border=1px RULES=NONE FRAME=BOX />";
						htmlText1 = htmlText1
								+ "<tr align=left><td width=70px> <img src=\"cid:image1\" height=70px width=70px  >"
								
								+ "</td>"
								+ "<td><b><ul>"
								+ "\t" + p.getAge() + "," + p.getReligion()
								+ "," + p.getCaste() + "," + p.getCity() + ","
								+ p.getState() + "," + p.getCountry() + "</ul>"

								+ "<ul><a href="+mysangathi+"/UI/Subscribe_ViewPartnersProfile.jsp?profileid="+ r.getProfileid()+ "&username="+ p.getProfileid()
				         	+ "<p>view profile.<p></ul></a>"

								+ "</td> </tr></table>";
						// }
						System.out.println("++++++++++++hhhh+++++++++++>>>>>>>>>>>>>>>"+ htmlText1);
						HtmlContent.append(htmlText1);
					} else {
						gender = r.getGender();   //Male profiles
						System.out.println("The gender is----------->>>>"+ gender);
								
						htmlText1 = "<table width=830px height=20px bgcolor=#E6E6FA border=1px RULES=NONE FRAME=BOX />";
						
						htmlText1 = htmlText1
								+ "<tr align=left><td width=70px> <img src=\"cid:image1\" height=70px width=70px  >"
								// +"<td width=10px colspan=3>"
								+ "</td>"
								+ "<td><b><ul>"
								+ "\t" + p.getAge() + "," + p.getReligion()
								+ "," + p.getCaste() + "," + p.getCity() + ","
								+ p.getState() + "," + p.getCountry() + "</ul>"

								+ "<ul><a href="+mysangathi+"/UI/Subscribe_ViewPartnersProfile.jsp?profileid="+ r.getProfileid()+ "&username="+ p.getProfileid()
				   	+ ">&nbsp;&nbsp;&nbsp;<p>view profile.</p></ul></a>"

								+ "</td> </tr></table>";
						// }
						System.out.println("++++++++++++hhhh+++++++++++>>>>>>>>>>>>>>>"+ htmlText1);
						HtmlContent.append(htmlText1);
					}
					
					count++;
					if(count==9)
					{
						System.out.println(">>>>>>>>>>>>>>>>>>>>inside if count is 10<<<<<<<<<<<<<<<<<<<"+count);
						break;
					}
				}//end of for
					
				Properties props = System.getProperties();
				props.load(MatchMakingProfileDAO.class.getClassLoader().getResourceAsStream("resources.properties"));
				String Email_ID = props.getProperty("Email_Id");
				String Itron_Email = props.getProperty("Itron_Email");
				log.info( "<<<<<<<<<<<<<<-The mail is sending-scott@southcoastsolar.com>>>>>>>>>>>>>>>");
				String FromAddress3 = "admin@mysangathi.com";
				String ToAddress3 = "javedbasha.s.m01@gmail.com";
				log.info( "<<<<<<<<<<<<<<-Before mail is sent->>>>>>>>>>>>>>>");
			 	String Content = "<table width=830px  div bgcolor=#FFFFE0 style=#2C6700   border=1 border: thick solid #FF0000 >"   //outer table
					+ "<table width=830px  border=1 height=60px CELLSPACING=0 CELLPADDING=0 BORDERCOLOR=#2C6700 >"    //Inner table inserted 2 images
					// +"<tbody>"
					+ "<tr><td height=60px width=200px><img src=\"cid:image2\" height=80px width=270px /></td>"
					+ " <img src=\"cid:image\" height=80px width=350px /> "
					+ "<td height=60px width=200px>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><u>Contact Us:</u></b><br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;<b> Office</b>  (080) 26723353 <br>&nbsp;&nbsp;&nbsp;&nbsp;<b> Mobile </b>- (91) 9535214563<br>"
					+ "<b>Email:</b>"
					+ "contact@mysangathi.com"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;www.mysangathi.com</td>"
					+ "</table>"
					+ "<b><br/> <font color=#2C6700 size="
					+ 3
					+ ">Dear "
					+ r.getName()
					+ ",</b><br/>"
					+ "<p ><font  size="
					+ 2
					+ ">MySangathi team suggest the following newly posted / updated profile(s) suit your selected criteria.</font><br>  "
					+ "<p><font color=#7D053F size="
					+ 2
					+ "><b>Latest Matches For You</b></font><p>"
					+ HtmlContent.toString()  //Appending male or female profile

					+ "<p><a href="
					+ "www.mysangathi.com"
					+ "><p><b>View More Matches</b></p></a> "
					+ "</br><p><b><span> Best Regards, </span></p></b>"
					+ " <p><b><span>  B.H.Manjunath IAS Rtd </p></b></span>  <p><b><span> Founder:MySangathi.com Matrimonial Services.</b></span> </p>"
					+ "</br><b><span>   <p>Web Site:  www.mysangathi.com</p><p>Mail Id:  admin@mysangathi.com</p></b></span></table>  ";
			
			
				// String Subject3="Activate the Meter - Montoring Platform";
				String Subject3 = "ICSACT-Meter  is ready for provisioning";

				log.info("<<<<<<<<<<<<<<-Before the thread class->>>>>>>>>>>>>>>");
				Thread emailhandler3 = new Thread(new EmailHandlerThread(FromAddress3, ToAddress3, Content, Subject3,0 ));

				emailhandler3.start();
				log.info("<<<<<<<<<<<<<<-After mail is sent->>>>>>>>>>>>>>>");
				
				
				
				/*System.out.println("-----------------HtmlContent------------------------->"+ HtmlContent);
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.googlemail.com");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");

				javax.mail.Session mailSession;
				mailSession = javax.mail.Session.getDefaultInstance(props,
						new Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(
										"admin@mysangathi.com",
										"mysangathi123#");
							}
						});

				try {

					System.out.println("Inside mail sending try block");
					Message message = new MimeMessage(mailSession);
					message.setFrom(new InternetAddress("admin@mysangathi.com"));
					// System.out.println("To email id is:"+toemail);
					// System.out.println("<----------The to email addres is:------------->"+toemailarray);
					System.out.println("To email id is:" + r.getUsername());

					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(r.getUsername())); //r.getUsername()
					message.setSubject("Your perfect Matching profiles");

					Multipart multipart = new javax.mail.internet.MimeMultipart("related");
					
					BodyPart messageBodyPart = new MimeBodyPart();
					
					String Content = "<table width=830px  div bgcolor=#FFFFE0 style=#2C6700   border=1 border: thick solid #FF0000 >"   //outer table
							+ "<table width=830px  border=1 height=60px CELLSPACING=0 CELLPADDING=0 BORDERCOLOR=#2C6700 >"    //Inner table inserted 2 images
							// +"<tbody>"
							+ "<tr><td height=60px width=200px><img src=\"cid:image2\" height=80px width=270px /></td>"
							+ " <img src=\"cid:image\" height=80px width=350px /> "
							+ "<td height=60px width=200px>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><u>Contact Us:</u></b><br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;<b> Office</b>  (080) 26723353 <br>&nbsp;&nbsp;&nbsp;&nbsp;<b> Mobile </b>- (91) 9535214563<br>"
							+ "<b>Email:</b>"
							+ "contact@mysangathi.com"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;www.mysangathi.com</td>"
							+ "</table>"
							+ "<b><br/> <font color=#2C6700 size="
							+ 3
							+ ">Dear "
							+ r.getName()
							+ ",</b><br/>"
							+ "<p ><font  size="
							+ 2
							+ ">MySangathi team suggest the following newly posted / updated profile(s) suit your selected criteria.</font><br>  "
							+ "<p><font color=#7D053F size="
							+ 2
							+ "><b>Latest Matches For You</b></font><p>"
							+ HtmlContent.toString()  //Appending male or female profile

							+ "<p><a href="
							+ "www.mysangathi.com"
							+ "><p><b>View More Matches</b></p></a> "
							+ "</br><p><b><span> Best Regards, </span></p></b>"
							+ " <p><b><span>  B.H.Manjunath IAS Rtd </p></b></span>  <p><b><span> Founder:MySangathi.com Matrimonial Services.</b></span> </p>"
							+ "</br><b><span>   <p>Web Site:  www.mysangathi.com</p><p>Mail Id:  admin@mysangathi.com</p></b></span></table>  ";
					
					messageBodyPart.setContent(Content, "text/html");
					
					// add it
					multipart.addBodyPart(messageBodyPart);
					// second part (the image)
					if (gender.equals(female)) {
						messageBodyPart = new MimeBodyPart();
						DataSource fds = new FileDataSource(
								"D:\\images\\profileFemale.jpg");
						messageBodyPart.setDataHandler(new DataHandler(fds));
						messageBodyPart.setHeader("Content-ID", "<image1>");
						multipart.addBodyPart(messageBodyPart);
					}
					if (gender.equals(male)) {
						messageBodyPart = new MimeBodyPart(); 
						DataSource fds = new FileDataSource(
								"D:\\images\\profileMale.jpg");
						messageBodyPart.setDataHandler(new DataHandler(fds));
						messageBodyPart.setHeader("Content-ID", "<image1>");
						multipart.addBodyPart(messageBodyPart);

					}
					// add it
					
					messageBodyPart = new MimeBodyPart();
					DataSource fds1 = new FileDataSource(
							"D:\\images\\Capture.jpg");
					messageBodyPart.setDataHandler(new DataHandler(fds1));
					messageBodyPart.setHeader("Content-ID", "<image>");
					multipart.addBodyPart(messageBodyPart);

					messageBodyPart = new MimeBodyPart();
					DataSource fds2 = new FileDataSource(
							"D:\\images\\hlogo.jpg");
					messageBodyPart.setDataHandler(new DataHandler(fds2));
					messageBodyPart.setHeader("Content-ID", "<image2>");
					multipart.addBodyPart(messageBodyPart);

					// put everything together

					message.setContent(multipart);

					System.out.println("Before mailsending" + profiles.size());
					if (profiles.size() >= 1) {

						Transport.send(message);
						System.out.println("Mail Sent");

					}
					HtmlContent.delete(0, HtmlContent.length());

				}// End 2nd try block
				catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("EXception is:" + e);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("EXception is:" + e);
				}*/

			}//End the matching profiles list
			

		} //End the first try block
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return true;

	} // End the MatchMaking Method

	public static void main(String[] args)
	{
		MatchMakingProfileDAO mdao=new MatchMakingProfileDAO();
		mdao.matchMakingProfiles();


	}

ABCD

}
