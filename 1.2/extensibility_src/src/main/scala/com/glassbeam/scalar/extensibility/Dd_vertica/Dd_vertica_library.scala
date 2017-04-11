package com.glassbeam.scalar.extensibility.Dd_vertica

import java.io.{BufferedWriter, FileWriter, PrintWriter}
import java.nio.file.{FileSystems, Path}
import java.sql.{Connection, DriverManager}
import java.text.SimpleDateFormat
import java.util.Properties

import com.glassbeam.extensibility.traits.Parsable

import scala.collection.mutable.ListBuffer

/**
  * Created by ayush on 21/9/16.
  */
class Dd_vertica_library extends Parsable{


    //---------------DATABASE
  val data_schema = "didata_didata_podv1_library"
  val driver = "com.vertica.jdbc.Driver"
  val url = "jdbc:vertica://10.163.36.111:5433/dim"//"jdbc:vertica://10.172.137.151:5433/glassbeam"
  val username = "gbp"//"gbd"
  val password = "dbadmin123"//""
  val myProp = new Properties()
  myProp.put("user", "gbp")
  myProp.put("password", "dbadmin123")
  myProp.put("BackupServerNode", "10.163.36.112,10.163.36.113")

  val table_des = Map(

    """temp_library_proto""" ->"""(library varchar(128),lib_capacity varchar(128),lib_spaceleft varchar(128),lib_spaceoccu varchar(128),lib_spacespan varchar(128),time_epoch int,obs_id_name varchar(1024))""",

    """temp_library_proto_2""" ->"""(library varchar(128),lib_capacity varchar(128),lib_spaceleft varchar(128),lib_spaceoccu varchar(128),time_epoch int,obs_id_name varchar(1024))""",

    """temp_libus_proto""" ->"""(library varchar(128),libus_cloudtype varchar(128),libus_uploadds varchar(128),libus_downloadds varchar(128),libus_mediaagent varchar(128),libus_lowwtrmrk varchar(128),libus_dsthreshold varchar(128),libus_mntpthus varchar(128),libus_nowriters varchar(128),libus_status varchar(128),libus_assusrgrp varchar(128),libus_offreason varchar(128),libus_jobgb varchar(128),libus_mntpath varchar(128),libus_capacity varchar(128),libus_spaceleft varchar(128),libus_spoccupied varchar(128),libus_nonsimpandt varchar(128),libus_szappl varchar(128),libus_spacesave varchar(128),libus_allwrt varchar(128),libus_status_two varchar(128),libus_offreasont varchar(128),libus_flscan varchar(128),libus_frafl varchar(128),libus_frathresh varchar(128),libus_flfrag varchar(128),libus_frgper varchar(128),libus_succrun varchar(128),libus_desc varchar(128),time_epoch int,obs_id_name varchar(1024))""",

   """temp_libus_proto_2""" ->"""(library varchar(128),libus_cloudtype varchar(128),libus_uploadds varchar(128),libus_downloadds varchar(128),libus_mediaagent varchar(128),libus_lowwtrmrk varchar(128),libus_dsthreshold varchar(128),libus_mntpthus varchar(128),libus_nowriters varchar(128),libus_status varchar(128),libus_assusrgrp varchar(128),libus_offreason varchar(128),libus_jobgb varchar(128),libus_mntpath varchar(128),libus_capacity varchar(128),libus_spaceleft varchar(128),libus_spoccupied varchar(128),libus_szappl varchar(128),libus_spacesave varchar(128),libus_allwrt varchar(128),libus_status_two varchar(128),libus_offreasont varchar(128),libus_flscan varchar(128),libus_frafl varchar(128),libus_frathresh varchar(128),libus_flfrag varchar(128),libus_frgper varchar(128),libus_succrun varchar(128),libus_desc varchar(128),time_epoch int,obs_id_name varchar(1024))""",

    """temp_driver_proto""" ->"""(driver_lib varchar(128),driver_driver varchar(128),driver_drvusg varchar(128),driver_drvday varchar(128),driver_drvthr varchar(128),driver_drvthrday varchar(128),driver_nobkup varchar(128),driver_nobkday varchar(128),driver_norest varchar(128),driver_noresday varchar(128),driver_noerr varchar(128),driver_noerrday varchar(128),time_epoch int,obs_id_name varchar(1024))""",

    """temp_media_proto""" ->"""(media_agent varchar(128),media_mount_hr varchar(128),media_moubt_day varchar(128),media_streams_hr varchar(128),media_streams_day varchar(128),media_reser_hr varchar(128),media_reser_day varchar(128),media_jobs_hr varchar(128),media_jobs_day varchar(128),media_bkupsize_hr varchar(128),media_bkupsize_day varchar(128),time_epoch int,obs_id_name varchar(1024))""",

    """temp_commcell_proto""" -> """(location varchar(128),device_serial_no varchar(128),device_name varchar(128),time_epoch int,obs_id_name varchar(1024))"""
  )




  val pattern_location =  """.*Library\s*and\s*Drive\s*Report\s*\-\s*(\S+)\s*[^\n]*""".r
  val pattern_library =   """Library\,Capacity\s*\(GB\)\,Space\s*Left\s*\(GB\)\,Space\s*Occupied\s*\(GB\)\,Space\s*Occupied\s*By\s*Non\-Simpana\s*Data\s*\(GB\)\,\s*$""".r
  val pattern_library_2 =   """Library\,Capacity\s*\(GB\)\,Space\s*Left\s*\(GB\)\,Space\s*Occupied\s*\(GB\)\,\s*$""".r
  val pattern_libus_key = """Library\,Cloud\s+Server\s+Type[^\n]+""".r
  val pattern_driver =    """Library\,Drive\,Drive\s*Usage[^\n]+""".r
  val pattern_media =     """MediaAgent\,Number\s+of\s+Mounts[^\n]+""".r
  val pattern_serialid = """\s*CommCell\s+ID\s*\:\s*([^\n\r]+)""".r
  val pattern_devicename = """\s*CommCell\s*\:\s*([^\n\r]*)""".r
  val pattern_date = """.*Report\s*generated\s*on\s*(\d+\/\d+\/\d+\s*\d+:\d+:\d+).*""".r


  val pattern_lib_data = """([^,]+)\,([^,]*)\,([^,]*)\,([^,]*)\,([^,]*)\,""".r
  val pattern_lib_data_2 = """([^,]+)\,([^,]*)\,([^,]*)\,([^,]*)\,""".r
  val pattern_libus_data = """([^\,]+)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,?([^\,]*)\,?""".r
  val pattern_driver_data = """([^\,]+)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,([^\,]*)\,""".r
  val pattern_media_data = """([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,([^\,]+)\,[^\n]*""".r

  override def processToFile(p: Path): Path = {

    val file_out = p.getParent.toString + "/extensibility.log"
    val file_name = p.getFileName
    val file_pattern = """.*\/\d+\-\w\w\w\-\d+\.\d+\.[\+\-]\d+\/[^\/]+\/(.*)""".r
    val checkPath = p.toString match{
	case file_pattern(l1)=>l1
	case _ => p.toString	

	}
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, true)))
    writer.close()
    val lines_csv = scala.io.Source.fromFile(p.toString, "ISO-8859-1").getLines()
    var location = ""
    var device_serial_no = ""
    var device_name = ""
    var report_date = ""


    //--------------list for distinct name space---------------------------
    var lst_library = new ListBuffer[String]()
    var lst_libus_key = new ListBuffer[String]()
    var lst_driver = new ListBuffer[String]()
    var lst_media = new ListBuffer[String]()

    var flag = 0
    var library_flag = 0
    var libus_flag = 0

    for (line <- lines_csv) {
      line match{
        case pattern_location(loc) => location = loc

        case pattern_serialid(sid) => device_serial_no = sid

        case pattern_devicename(dvname) => device_name = dvname

        case pattern_date(dtstr) => report_date = dtstr

        case pattern_library() =>
          lst_library += line
          flag = 1
	  library_flag = 1

        case pattern_library_2() =>
          lst_library += line
          flag = 10
	  library_flag = 2

        case pattern_libus_key() =>
          lst_libus_key += line
          flag = 2
	  if(line.contains("Space Occupied By Non-Simpana Data"))
		{libus_flag = 1}

        case pattern_driver() =>
          lst_driver += line
          flag = 3

        case pattern_media() =>
          lst_media += line
          flag = 4

        case _=>
          if(flag == 1)
            if(line.matches(pattern_lib_data.toString())) lst_library += line
          if(flag == 10)
            if(line.matches(pattern_lib_data_2.toString())) lst_library += line
          if(flag == 2)
            if(line.matches(pattern_libus_data.toString())) lst_libus_key += line
          if(flag == 3)
            if(line.matches(pattern_driver_data.toString())) lst_driver += line
          if(flag == 4)
            if(line.matches(pattern_media_data.toString())) lst_media += line
      }
    }
    val df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
    val epoch_dt = df.parse(report_date).getTime.toString
    val epoch_dt_comcell = epoch_dt.substring(0,epoch_dt.length-3)
    //---------------------------commcell file
    val file_commcell = p.getParent.toString + "/extensibility.commcell.csv"
    val writer_commcell = new PrintWriter(new BufferedWriter(new FileWriter(file_commcell, true)))
    writer_commcell.write("Location,Device Serial Num,Device Name,obs_dt,file_name\n")
    writer_commcell.write(s"$location,$device_serial_no,$device_name,$epoch_dt_comcell,$file_name\n")
    writer_commcell.close()

    //--------------------library file
    val file_library = p.getParent.toString + "/extensibility.library.csv"
    val writer_library = new PrintWriter(new BufferedWriter(new FileWriter(file_library, true)))
    //writer_library.write("Library,Capacity (GB),Space Left (GB),Space Occupied (GB),Space Ocup Non-Simpana Data (GB),obs_dt,file_name\n")
    if(lst_library.nonEmpty)
      lst_library.foreach{
        x=>
          write(x.stripSuffix(",") + "," + epoch_dt.substring(0,epoch_dt.length-3) + "," + file_name,writer_library)
      }
    writer_library.close()

    //--------------libus file
    val file_libus = p.getParent.toString + "/extensibility.libus.csv"
    val writer_libus = new PrintWriter(new BufferedWriter(new FileWriter(file_libus, true)))
    //writer_libus.write("obs_dt,file_name\n")
    //writer_libus.write("obs_dt,file_name\n")
    if(lst_libus_key.nonEmpty)
      lst_libus_key.foreach{
        x=>
          write(x.stripSuffix(",") + "," + epoch_dt.substring(0,epoch_dt.length-3) + "," + file_name,writer_libus)
      }
    writer_libus.close()


    //---------------------driver file
    val file_driver = p.getParent.toString + "/extensibility.driver.csv"
    val writer_driver = new PrintWriter(new BufferedWriter(new FileWriter(file_driver, true)))
    //writer_driver.write("Library,Drive,Drive Usage per Hr,Drive Usage per 24Hr,Drive Throughput per Hr,Drive Throughput per 24Hr,Number of Backup per Hr,")
    //writer_driver.write("Number of Backup per 24Hr,Number of Restores per Hr,Number of Restores per 24Hr,Number of Errors per Hr,Number of Errors per 24Hr,")
    //writer_driver.write("obs_dt,file_name\n")
    if(lst_driver.nonEmpty)
      lst_driver.foreach{
        x=>
          write(x.stripSuffix(",") + "," + epoch_dt.substring(0,epoch_dt.length-3) + "," + file_name,writer_driver)
      }
    writer_driver.close()


    //------------------------media file
    val file_media = p.getParent.toString + "/extensibility.media.csv"
    val writer_media = new PrintWriter(new BufferedWriter(new FileWriter(file_media, true)))
    //writer_media.write("MediaAgent,Number of Mounts (per Hr),Number of Mounts (per 24Hr),Max Number of Streams (per Hr),Max Number of Streams (per 24Hr),Number of Reservations (per Hr),Number of Reservations (per 24Hr),")
    //writer_media.write("Average Number of Jobs (per Hr),Average Number of Jobs (per 24Hr),Backup Size (per Hr),Backup Size (per 24Hr),")
    //writer_media.write("obs_dt,file_name\n")
    if(lst_media.nonEmpty)
      lst_media.foreach{
        x=>
          write(x.stripSuffix(",") + "," + epoch_dt.substring(0,epoch_dt.length-3) + "," + file_name,writer_media)
      }
    writer_media.close()

   //---------------load to vertica
    var connection: Connection = null
    try {
      Class.forName("com.vertica.jdbc.Driver")
      connection = DriverManager.getConnection(url, myProp)
      connection.setAutoCommit(false)
	if(check_File(connection,checkPath)){
      fcommcell(connection, load_temp("temp_commcell_proto", file_commcell, connection), System.currentTimeMillis())
      
      if(library_flag == 1)
      	flibrary(connection, load_temp("temp_library_proto", file_library, connection), System.currentTimeMillis())

      if(library_flag == 2)
	flibrary_2(connection, load_temp("temp_library_proto_2", file_library, connection), System.currentTimeMillis())

      fdriver(connection, load_temp("temp_driver_proto", file_driver, connection), System.currentTimeMillis())

     if(libus_flag == 1)
      {
	flibus(connection, load_temp("temp_libus_proto", file_libus, connection), System.currentTimeMillis())
      }else{
	flibus_2(connection, load_temp("temp_libus_proto_2", file_libus, connection), System.currentTimeMillis())
	} 	

      fmedia(connection, load_temp("temp_media_proto", file_media, connection), System.currentTimeMillis())
	}
      connection.commit()
      connection.close()


    }catch {
      case e:Exception => logger.error(p.toString+" "+e.getMessage)
      connection.close()
    }

    FileSystems.getDefault.getPath(file_out)
  }

  def write(ln: String,writer: PrintWriter) = writer.write(ln + "\n")

  override def processFileToContext(p: Path): String = {"success"}

  override def processBundleToContext(p: Path): String = {"success"}
def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
  }

  //---------------------------------------Load in temp-table-----------------------------------------------------
  def load_temp(table: String, file: String, fconnection: Connection): Int = {
    val statement = fconnection.createStatement()
    val qu_temptbl = "CREATE LOCAL TEMPORARY TABLE " + table + "" + table_des(table) + "ON COMMIT preserve ROWS"
    statement.executeUpdate(qu_temptbl)
    val qu = "COPY " + table + " FROM LOCAL \'" + file + "\' PARSER fcsvparser() NO COMMIT"
    val copy = statement.executeUpdate(qu)
    statement.close()
    //println("number of line written is "+copy+" "+table)
    copy.toInt
  }

  //+++++++++++++++++++++++++++++++++++++++++++++++FIlename Check+++++++++++++++++++++++++++
  def check_File(fconnection: Connection,fname: String)={
	val statement = fconnection.createStatement()
	val resultSet = statement.executeQuery(s"""select * from $data_schema.checkFile where filename = '$fname'""")
	if (!resultSet.next()) {
	statement.executeUpdate(s"insert into $data_schema.checkFile(filename) values('$fname')")
	statement.close()
	true
	}else{
	statement.close()
	false
	}
  }

  //+++++++++++++++++++++++++++++++++++++++++++++++ library +++++++++++++++++++++++++++++++++++++++++++++++++

  def flibrary(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""
	insert into $data_schema.library_proto(lib_capacity,lib_spaceleft,lib_spaceoccu,lib_spacespan,library,time_epoch,obs_id_name)
	select distinct lib_capacity,lib_spaceleft,lib_spaceoccu,lib_spacespan,library,time_epoch,obs_id_name from temp_library_proto
	"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun

  //+++++++++++++++++++++++++++++++++++++++++++++++ library-2 +++++++++++++++++++++++++++++++++++++++++++++++++

  def flibrary_2(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""
	insert into $data_schema.library_proto(lib_capacity,lib_spaceleft,lib_spaceoccu,library,time_epoch,obs_id_name)
	select distinct lib_capacity,lib_spaceleft,lib_spaceoccu,library,time_epoch,obs_id_name from temp_library_proto_2
	"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun

  //+++++++++++++++++++++++++++++++++++++++++++++++ media +++++++++++++++++++++++++++++++++++++++++++++++++

  def fmedia(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.media_proto(media_bkupsize_day,media_bkupsize_hr,media_jobs_day,media_jobs_hr,media_moubt_day,media_mount_hr,media_reser_day,media_reser_hr,media_streams_day,media_streams_hr,media_agent,time_epoch,obs_id_name)
	select distinct media_bkupsize_day,media_bkupsize_hr,media_jobs_day,media_jobs_hr,media_moubt_day,media_mount_hr,media_reser_day,media_reser_hr,media_streams_day,media_streams_hr,media_agent,time_epoch,obs_id_name
 from temp_media_proto
	"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun


  //+++++++++++++++++++++++++++++++++++++++++++++++ driver +++++++++++++++++++++++++++++++++++++++++++++++++

  def fdriver(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""
	insert into $data_schema.driver_proto(driver_lib,driver_driver,driver_drvusg,driver_drvday,driver_drvthr,driver_drvthrday,driver_nobkup,driver_nobkday,driver_norest,driver_noresday,driver_noerr,driver_noerrday,time_epoch,obs_id_name)
	select distinct driver_lib,driver_driver,driver_drvusg,driver_drvday,driver_drvthr,driver_drvthrday,driver_nobkup,driver_nobkday,driver_norest,driver_noresday,driver_noerr,driver_noerrday,time_epoch,obs_id_name	
from temp_driver_proto
"""
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun




  //+++++++++++++++++++++++++++++++++++++++++++++++ libus +++++++++++++++++++++++++++++++++++++++++++++++++

  def flibus(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()
					 
    val qu = s"""
	insert into $data_schema.libus_proto(library,libus_cloudtype,libus_uploadds,libus_downloadds,libus_mediaagent,libus_lowwtrmrk,libus_dsthreshold,libus_mntpthus,libus_nowriters,libus_status,libus_assusrgrp,libus_offreason,libus_jobgb,libus_mntpath,libus_capacity,libus_spaceleft,libus_spoccupied,libus_nonsimpandt,libus_szappl,libus_spacesave,libus_allwrt,libus_status_two,libus_offreasont,libus_flscan,libus_frafl,libus_frathresh,libus_flfrag,libus_frgper,libus_succrun,libus_desc,time_epoch,obs_id_name)
	select distinct library,libus_cloudtype,libus_uploadds,libus_downloadds,libus_mediaagent,libus_lowwtrmrk,libus_dsthreshold,libus_mntpthus,libus_nowriters,libus_status,libus_assusrgrp,libus_offreason,libus_jobgb,libus_mntpath,libus_capacity,libus_spaceleft,libus_spoccupied,libus_nonsimpandt,libus_szappl,libus_spacesave,libus_allwrt,libus_status_two,libus_offreasont,libus_flscan,libus_frafl,libus_frathresh,libus_flfrag,libus_frgper,libus_succrun,libus_desc,time_epoch,obs_id_name
	from temp_libus_proto	
	"""
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun


  //----------

  //+++++++++++++++++++++++++++++++++++++++++++++++ libus_2 +++++++++++++++++++++++++++++++++++++++++++++++++

  def flibus_2(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()
					 
    val qu = s"""
	insert into $data_schema.libus_proto(library,libus_cloudtype,libus_uploadds,libus_downloadds,libus_mediaagent,libus_lowwtrmrk,libus_dsthreshold,libus_mntpthus,libus_nowriters,libus_status,libus_assusrgrp,libus_offreason,libus_jobgb,libus_mntpath,libus_capacity,libus_spaceleft,libus_spoccupied,libus_szappl,libus_spacesave,libus_allwrt,libus_status_two,libus_offreasont,libus_flscan,libus_frafl,libus_frathresh,libus_flfrag,libus_frgper,libus_succrun,libus_desc,time_epoch,obs_id_name)
	select distinct library,libus_cloudtype,libus_uploadds,libus_downloadds,libus_mediaagent,libus_lowwtrmrk,libus_dsthreshold,libus_mntpthus,libus_nowriters,libus_status,libus_assusrgrp,libus_offreason,libus_jobgb,libus_mntpath,libus_capacity,libus_spaceleft,libus_spoccupied,libus_szappl,libus_spacesave,libus_allwrt,libus_status_two,libus_offreasont,libus_flscan,libus_frafl,libus_frathresh,libus_flfrag,libus_frgper,libus_succrun,libus_desc,time_epoch,obs_id_name
	from temp_libus_proto_2	
	"""
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun


  //----------

  //+++++++++++++++++++++++++++++++++++++++++++++++ commcell +++++++++++++++++++++++++++++++++++++++++++++++++

  def fcommcell(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""
	insert into $data_schema.commcell_proto(location,device_serial_no,device_name,time_epoch,obs_id_name)
	select distinct location,device_serial_no,device_name,time_epoch,obs_id_name 
	from temp_commcell_proto	
	"""
	
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun
}

