package com.glassbeam.scalar.extensibility.Dd_vertica

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._

import scala.io.Source
import java.sql.DriverManager
import java.sql.Connection
import java.text.SimpleDateFormat
import java.util.{Date, Properties}
import java.text._

import scala.util.matching.Regex

/**
  * Created by ayush on 27/7/16.
  */
class Dd_vertica_csv extends Parsable {


  val data_schema = "didata_didata_podv1"
  val driver = "com.vertica.jdbc.Driver"
  val url = "jdbc:vertica://10.163.36.111:5433/dim"
  //"jdbc:vertica://10.172.137.151:5433/glassbeam"
  val username = "gbp"
  //"gbd"
  val password = "dbadmin123"
  //""
  val myProp = new Properties()
  myProp.put("user", "gbp")
  myProp.put("password", "dbadmin123")
  myProp.put("BackupServerNode", "10.163.36.112,10.163.36.113")

  //-------------------------------File_Pattern----------------------------------
  val pattern_org =               """.*organizations.*csv""".r
  val pattern_image =             """.*images.*csv""".r
  val pattern_servernic =         """.*servernics.*csv""".r
  val pattern_imgdsk =            """.*imagedisks.*csv""".r
  val pattern_vendor =            """.*vendors.*csv""".r
  val pattern_servers =           """.*servers.csv""".r
  val pattern_n1nwk =             """.*n1networks.*csv""".r
  val pattern_srvdisk =           """.*serverdisks.*csv""".r
  val pattern_srvsoft =           """.*serversoftwarelabels.*csv""".r
  val pattern_n2nwk =             """.*n2networks.*csv""".r
  val pattern_mcps =              """.*mcps.*csv""".r
  val pattern_netdomain =         """.*netdomains.*csv""".r
  val pattern_ipblock =           """.*ipblocks.*csv""".r
  val pattern_datastore =         """.*datastore.*csv""".r

  //-----------------------------MAP TABLE TO TEMP_TABLE-------------------------------------------
  val table_des = Map(

    """temp_org_proto""" -> """(org_geo varchar(128),org_rpyear int,org_rpmonth int,org_rpday int,org_rphour int,org_rpyday int,org_epoch int NOT NULL,org_gdocversion varchar(128),org_org_id varchar(128) NOT NULL,org_cust_name varchar(128),org_vendorname varchar(128),org_vendor_id varchar(128),org_acc_status varchar(128),org_acc_vrfd varchar(128))""",

    """temp_srvimg_proto""" -> """(srv_img_geo_cfg varchar(128),srv_img_rpyr int,srv_img_rpmnth int,srv_img_rpday int,srv_img_rphour int,srv_img_rpyday int,srv_img_epoch int,srv_img_gdocver varchar(128),srv_img_idd varchar(128),srv_img_org_id varchar(128),srv_img_vmid varchar(128),srv_img_crdate varchar(128),srv_img_dvtype varchar(128),srv_img_loc varchar(128),srv_img_os varchar(128))""",

    """temp_servernic_proto""" -> """(servernic_id varchar(128),servernic_nic_id varchar(128),servernic_rpyr int,servernic_rpmnth int,servernic_rpday int,servernic_rphour int,servernic_rpyday int,servernic_rpepoch int,servernic_rpgdoc varchar(128))""",

    """temp_imgdsk_proto""" -> """(imgdsk_imgid varchar(128),imgdsk_disktype varchar(128),imgdsk_diskid varchar(128),srv_img_strg numeric(37,15),imgdsk_unit varchar(128),imgdsk_spd varchar(128),imgdsk_state varchar(128),imgdsk_rpyr int, imgdsk_rpmnth int,imgdsk_rpday int,imgdsk_rphour int,imgdsk_rpyday int,imgdsk_epoch int,imgdsk_gdocver int)""",

    """temp_vendors_proto""" -> """(vendors_geo varchar(128),vendors_rpyr int,vendors_rpmnth int,vendors_rpday int,vendors_rphour int,vendors_rpyday int,vendors_rpepoch int,vendors_gdoc varchar(128),vendors_id varchar(128),vendors_desc varchar(128),vendors_hmloc varchar(128))""",

    """temp_srvinfo_proto""" -> """(srv_info_geo varchar(128),srvinfo_rpyr int,srvinfo_rpmnth int,srvinfo_rpday int, srvinfo_rphour int,srvinfo_rpyday int,srvinfo_epoch int,srvinfo_gdocver varchar(128),srv_info_srvr_id varchar(128),srvinfo_name varchar(128),srv_info_org_id varchar(128),srvinfo_vm_id varchar(128),srvinfo_crt_dt varchar(128),srvinfo_dvtype varchar(128),srv_info_loc varchar(128),srvinfo_gstate varchar(128),srv_info_cpu varchar(128),srvinfo_cpucores varchar(128),srvinfo_cpuspdrsvr varchar(128),srvinfo_cpuspdrsvrunt varchar(128),srvinfo_cpuspdlmt varchar(128),srvinfo_cpuspdlmtunt varchar(128),srv_info_ram varchar(128),srvinfo_ramunt varchar(128),srv_info_srvr_os varchar(128),srvinfo_assetid varchar(128),srvinfo_servplan varchar(128),srvinfo_bstate varchar(128),srvinfo_started varchar(128))""",

    """temp_ntwk_proto""" -> """(ntwk_geo varchar(128),ntwk_rpyr int,ntwk_rpmnth int,ntwk_rpday int,ntwk_rphour int,ntwk_rpyday int,ntwk_epoch int,ntwk_gdocver varchar(128),ntwk_con_id varchar(128),ntwk_hostname varchar(128),ntwk_con_state varchar(128),ntwk_maintenance varchar(128),ntwk_hyid varchar(128),ntwk_ntid varchar(128),ntwk_name varchar(128),ntwk_netype varchar(128),ntwk_org_id varchar(128),ntwk_location varchar(128),ntwk_ntstate varchar(128),ntwk_crtdate varchar(128))""",

    """temp_ntwk_proto_1""" -> """(ntwk_geo varchar(128),ntwk_rpyr int,ntwk_rpmnth int,ntwk_rpday int,ntwk_rphour int,ntwk_rpyday int,ntwk_epoch int,ntwk_gdocver varchar(128),ntwk_con_id varchar(128),ntwk_hostname varchar(128),ntwk_con_state varchar(128),ntwk_maintenance varchar(128),ntwk_hyid varchar(128),ntwk_ntid varchar(128),ntwk_name varchar(128),ntwk_netype varchar(128),ntwk_org_id varchar(128),ntwk_location varchar(128),ntwk_ntstate varchar(128),ntwk_crtdate varchar(128),ntwk_hyp_clustid varchar(128))""",

    """temp_serverdisk_proto""" -> """(serverdisk_id varchar(128),serverdisk_type varchar(128),serverdisk_disk_id varchar(128),serverdisk_scsi_id varchar(128),serverdisk_capacity_sz int,serverdisk_capacity_unt varchar(128),serverdisk_spd varchar(128),serverdisk_ste varchar(128),serverdisk_rpyr int,serverdisk_rpmnth int,serverdisk_rpday int,serverdisk_rphour int,serverdisk_rpyday int,serverdisk_epoch int,serverdisk_rpgdoc varchar(128))""",

    """temp_servsoft_proto""" -> """(servsoft_id varchar(128), servsoft_disname varchar(128),servsoft_rpyr int,servsoft_rpmnth int, servsoft_rpday int,servsoft_rphour int,servsoft_rpyday int,servsoft_rpepoch int,servsoft_gdoc varchar(128))""",

    """temp_ntwo_proto""" -> """(ntwo_geo varchar(128),ntwo_rpyr int,ntwo_rpmnth int, ntwo_rpday int,ntwo_rphour int,ntwo_rpyday int,ntwo_epoch int,ntwo_gdoc varchar(128),ntwo_fvlanid varchar(128),ntwo_lfgrpid varchar(128),ntwo_vlannum varchar(128),ntwo_vlanname varchar(128),ntwo_vlandesc varchar(128),ntwo_vlaninmain varchar(128),ntwo_cldntrkid varchar(128),ntwo_org_id varchar(128),ntwo_dtcid varchar(128), ntwo_alname varchar(128),ntwo_alstate varchar(128),ntwo_alcrtime varchar(128),ntwo_alntdomid varchar(128))""",

    """temp_ntwo_proto1""" -> """(ntwo_geo varchar(128),ntwo_rpyr int,ntwo_rpmnth int, ntwo_rpday int,ntwo_rphour int,ntwo_rpyday int,ntwo_epoch int,ntwo_gdoc varchar(128),ntwo_fvlanid varchar(128),ntwo_lfgrpid varchar(128),ntwo_vlannum varchar(128),ntwo_vlanname varchar(128),ntwo_vlandesc varchar(128),ntwo_vlaninmain varchar(128),ntwo_cldntrkid varchar(128),ntwo_org_id varchar(128),ntwo_dtcid varchar(128), ntwo_alname varchar(128),ntwo_alstate varchar(128),ntwo_alcrtime varchar(128),ntwo_alntdomid varchar(128),ntwo_hypid varchar(128))""",

    """temp_mcps_proto""" -> """(mcps_geo varchar(128),mcps_rpyr int,mcps_rpmnth int,mcps_rpday int,mcps_rphour int,mcps_rpyday int,mcps_rpepoch int NOT NULL,mcps_gdoc varchar(128),mcps_id varchar(128) NOT NULL,mcps_dname varchar(128),mcps_dtype varchar(128),mcps_sitname varchar(128))""",

    """temp_netdomain_proto""" -> """(netdomain_geo varchar(128),netdomain_rpyr int,netdomain_rpmnth int,netdomain_rpday int,netdomain_rphour int,netdomain_rpyday int,netdomain_epoch int, netdomain_gdoc varchar(128),netdomain_rodomnam varchar(128),netdomain_inmain varchar(128),netdomain_hypid varchar(128),netdomain_id varchar(128),netdomain_name varchar(128),netdomain_type varchar(128),netdomain_org_id varchar(128),netdomain_state varchar(128),netdomain_ctime varchar(128))""",

    """temp_ipblock_proto""" -> """(ipblock_geo varchar(128), ipblock_rpyr int,ipblock_rpmnth int,ipblock_rpday int,ipblock_rphour int,ipblock_rpyday int,ipblock_epoch int,ipblock_gdoc varchar(128),ipblock_blktyp varchar(128),ipblock_snetsz varchar(128),ipblock_state varchar(128),ipblock_inmain varchar(128),ipblock_none varchar(128),ipblock_ntwo varchar(128))""",

    """temp_ds_proto""" ->"""(ds_clust varchar(128),ds_name varchar(128),ds_cap_gb float,ds_free_space float,ds_storagetier varchar(128),ds_storagetype varchar(128))""",

    """temp3_ds_proto""" ->"""(ds_clust varchar(128),ds_name varchar(128),ds_cap_gb float,ds_free_space float,ds_storagetier varchar(128),ds_storagetype varchar(128),ds_readiops varchar(128),ds_writeiops varchar(128))""",

    """temp4_ds_proto""" ->"""(ds_clust varchar(128),ds_name varchar(128),ds_cap_gb float,ds_free_space float,ds_storagetier varchar(128),ds_storagetype varchar(128),ds_provisionedgb varchar(128),ds_overprovisionedgb varchar(128))"""
  )

  //+++++++++++++++FOR N2NETWORK DIFFERENT FORMAT++++++++++++++++++++++++++++++++++++
  val n2_form1_s =
    """geo.*vlan\.f5_vlan_id,vlan\.leaf_group_id,vlan\.vlan_number.*\.network_domain_id\s*$""".r
  val n2_form2_s = """geo.*vlan\.f5_vlan_id,vlan\.leaf_group_id,vlan\.vlan_number.*\.hypervisor_id\s*$""".r

  //+++++++++++++++FOR N1NETWORK DIFFERENT FORMAT++++++++++++++++++++++++++++++++++++
  val n1_form1_s =
    """geo.*context\.context_id,context\.host_name,context\.context_state,context\.in_maintenance.*allocation\.create_date\s*$""".r
  val n1_form2_s = 
    """geo.*context\.context_id,context\.host_name,context\.context_state,context\.in_maintenance.*context\.hypervisor_cluster_id\s*$""".r

  //++++++++++++++++++++++++++++++++DS DIFF FORMAT ++++++++++++++++++++
  val ds_form1_s =
    """\"Clustername\"\,\"DSName\"\,\"CapacityGB\"\,\"FreespaceGB\"\,\"StorageTier\"\,\"StorageType\"\s*"""
  val ds_form2_s =
    """\"Clustername\"\,\"DSName\"\,\"CapacityGB\"\,\"FreespaceGB\"\,\"StorageTier\"\,\"StorageType\",\"ReadIOPS\",\"WriteIOPS\"\s*"""
  val ds_form3_s =
    """\"Clustername\"\,\"DSName\"\,\"CapacityGB\"\,\"FreespaceGB\"\,\"StorageTier\"\,\"StorageType\",\"ProvisionedGB\",\"overprovisionedGB\"\s*"""


  //+++++++++++++++++++++++++++++++++For Obs_Ts+++++++++++++++++++++++++++++++++++++++++++++++++++++
  val pattern_epoch =
    """\w+\,\d+\,\d+\,\d+\,\d+\,\d+\,(\d+)\,[^\,]*\,[^\,]+\,(?:\"[^\"]*\")*[^\,]*\,[^\,]+\,[^\,]*\,[^\,]+\,[^\,]+\,[^\,]*\,[^\,]*\,[^\,]*\,[^\,]*\,[^\,]*\,\w*\,\d*\,\w*\,\d*\,\w*\,[^\,]*\,[^\,]*\,[^\,]*\,[^\,]*\,[^\n]+""".r

  override def processToFile(p: Path): Path = {

    val file_out = p.getParent.toString + "/extensibility.Bundle_files.log"
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, true)))
    val file_pattern = """.*\/\d+\-\w\w\w\-\d+\.\d+\.[\+\-]\d+\/[^\/]+\/(.*)""".r
    val checkPath = p.toString match{
	case file_pattern(l1)=>l1
	case _ => p.toString	

	}
    if(p.getFileName.toString.matches(pattern_servers.toString))
    {	  
	  def check(line:String):Long= {
            val pattern_epoch(epoch) = line
            (epoch+"000").toLong
          }
          val line = scala.io.Source.fromFile(p.toString, "ISO-8859-1").getLines()
          val max_epoch = line.filter(_.matches(pattern_epoch.toString)).map(x=>check(x)).toArray.max
          writer.write("current Time-" +new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(max_epoch.toLong))
    }
    var connection: Connection = null
    try {
      Class.forName("com.vertica.jdbc.Driver")
      connection = DriverManager.getConnection(url, myProp)
      connection.setAutoCommit(false)
      if(check_File(connection,checkPath)){
      val table_result = p.getFileName.toString match {

        case pattern_org() =>
          forg(connection, load_temp("temp_org_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_image() =>
          fsrvimg(connection, load_temp("temp_srvimg_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_servernic() =>
          fservernic(connection, load_temp("temp_servernic_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_vendor() =>
          fvendors(connection, load_temp("temp_vendors_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_imgdsk() =>
          fimgdsk(connection, load_temp("temp_imgdsk_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_servers() =>
          fsrvinfo(connection, load_temp("temp_srvinfo_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_n1nwk() =>
	  val line = scala.io.Source.fromFile(p.toString, "ISO-8859-1").getLines().toList
	  if (line.exists(_.matches(n1_form1_s.toString))) {
          fntwk(connection, load_temp("temp_ntwk_proto", p.toString, connection), System.currentTimeMillis())
	  } else if (line.exists(_.matches(n1_form2_s.toString))) {
          fntwk_2(connection, load_temp("temp_ntwk_proto_1", p.toString, connection), System.currentTimeMillis())
          }

        case pattern_srvdisk() =>
          fserverdisk(connection, load_temp("temp_serverdisk_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_srvsoft() =>
          fservsoft(connection, load_temp("temp_servsoft_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_n2nwk() =>
          val line = scala.io.Source.fromFile(p.toString, "ISO-8859-1").getLines().toList
          if (line.exists(_.matches(n2_form1_s.toString))) {
            fntwo(connection, load_temp("temp_ntwo_proto", p.toString, connection), System.currentTimeMillis())
          } else if (line.exists(_.matches(n2_form2_s.toString))) {
            fntwo_2(connection, load_temp("temp_ntwo_proto1", p.toString, connection), System.currentTimeMillis())
          } else {
            logger.warning("File Format is not handle For n2 Network")
            "NO"
          }

        case pattern_mcps() =>
          fmcps(connection, load_temp("temp_mcps_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_netdomain() =>
          fnetdomain(connection, load_temp("temp_netdomain_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_ipblock() =>
          fipblock(connection, load_temp("temp_ipblock_proto", p.toString, connection), System.currentTimeMillis())

        case pattern_datastore() =>
          val pair = """Subject\:\s*(\S+)\s+.+?\:\s*(.+)""".r
          var location: String = ""
          var obs_dt: String = ""
          for (line_done <- scala.io.Source.fromFile(p.getParent + "/done", "ISO-8859-1").getLines()) {
            line_done match {
              case pair(loc, obs) =>
                location = loc
                val  simpleDateFormat:SimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
                val  date:Date = simpleDateFormat.parse(obs)
                val ans = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
                obs_dt = ans
              case _ =>
            } //end of match
          } //end of for
          val line = scala.io.Source.fromFile(p.toString, "ISO-8859-1").getLines().toList
          //dsdatastore(fconnection: Connection, location: String, obs_dt: String, table_name: String, copy: Int, start_ts: Long):
          if(line.exists(_.matches(ds_form1_s))){
            dsdatastore(connection,location,obs_dt,"temp_ds_proto", load_temp("temp_ds_proto", p.toString, connection), System.currentTimeMillis())
          }else if(line.exists(_.matches(ds_form2_s))){
            dsdatastore(connection,location,obs_dt,"temp3_ds_proto", load_temp("temp3_ds_proto", p.toString, connection), System.currentTimeMillis())
          }else if(line.exists(_.matches(ds_form3_s))){
            dsdatastore(connection,location,obs_dt,"temp4_ds_proto", load_temp("temp4_ds_proto", p.toString, connection), System.currentTimeMillis())
          }else{
            logger.warning("File Format is not handle For datastore file")
            "NO"
          }

        case _ => logger.warning("File Pattern is not exist"+p.toString); "NO"

      }//end of match
	
      
	}
	connection.commit() 
    } catch {
      case e: Exception => logger.error(p.toString+" "+e.getClass.toString + " and " + e.getMessage)
        connection.rollback()
        connection.close()
    }

    //--------------------------------------------------------------------------------------------------------
    connection.close()
    writer.close()
    FileSystems.getDefault.getPath(file_out)
  }

  //end of processToFile
  //----------------------------------------PARSABLE UNUSED FUNCTION-----------------------------------------------
  override def processFileToContext(p: Path): String = {
    "success"
  }

  override def processBundleToContext(p: Path): String = {
    "success"
  }

def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
  }
  //--------------------------------------------------------------------------------------------------------------

  //---------------------------------------Load in temp-table-----------------------------------------------------
  def load_temp(table: String, file: String, fconnection: Connection): Int = {
    val statement = fconnection.createStatement()
    val qu_temptbl = "CREATE LOCAL TEMPORARY TABLE " + table + "" + table_des(table) + "ON COMMIT preserve ROWS"
    statement.executeUpdate(qu_temptbl)
    val qu = "COPY " + table + " FROM LOCAL \'" + file + "\' PARSER fcsvparser() NO COMMIT"
    val copy = statement.executeUpdate(qu)
    statement.close()
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

  //+++++++++++++++++++++++++++++++++++++++++++++++ org +++++++++++++++++++++++++++++++++++++++++++++++++

  def forg(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

     val qu = s"""
insert into $data_schema.org_proto
(org_geo,org_rpyear,org_rpmonth,org_rpday,org_rphour,org_rpyday,org_epoch,org_gdocversion,org_org_id,org_cust_name,org_vendorname,org_vendor_id,org_acc_status,org_acc_vrfd)
select distinct  org_geo,org_rpyear,org_rpmonth,org_rpday,org_rphour,org_rpyday,org_epoch,org_gdocversion,org_org_id,org_cust_name,org_vendorname,org_vendor_id,org_acc_status,org_acc_vrfd
from temp_org_proto
"""

    statement.executeUpdate(qu)
   
    statement.close()
    "yes"

  } //---------------fun

  //++++++++++++++++++++++++++++++++++++imageDisk+++++++++++++++++++++++++++++++++++++++++++++++++++++

  def fimgdsk(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    //----------taking unique rows from file data--------------
    
    val qu = s"""
insert into $data_schema.imgdsk_proto(imgdsk_imgid,imgdsk_disktype,imgdsk_diskid,srv_img_strg,imgdsk_unit,imgdsk_spd,imgdsk_state,imgdsk_rpyr,imgdsk_rpmnth,imgdsk_rpday,imgdsk_rphour,imgdsk_rpyday,imgdsk_epoch,imgdsk_gdocver)
select distinct  imgdsk_imgid,imgdsk_disktype,imgdsk_diskid,srv_img_strg,imgdsk_unit,imgdsk_spd,imgdsk_state,imgdsk_rpyr,imgdsk_rpmnth,imgdsk_rpday,imgdsk_rphour,imgdsk_rpyday,imgdsk_epoch,imgdsk_gdocver
 from temp_imgdsk_proto
"""

    statement.executeUpdate(qu)
   
    statement.close()
    "yes"

  } //---------fun

  //+++++++++++++++++++++++++++++++++++++++++++++++++images ++++++++++++++++++++++++++++++++++++++++

  def fsrvimg(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    
    //----------taking unique rows from file data--------------
   
    val qu =
     s"""
insert into $data_schema.srvimg_proto(srv_img_geo_cfg,srv_img_rpyr,srv_img_rpmnth,srv_img_rpday,srv_img_rphour,srv_img_rpyday,srv_img_epoch,srv_img_gdocver,srv_img_idd,srv_img_org_id,srv_img_vmid,srv_img_crdate,srv_img_dvtype,srv_img_loc,srv_img_os)
select distinct   srv_img_geo_cfg,srv_img_rpyr,srv_img_rpmnth,srv_img_rpday,srv_img_rphour,srv_img_rpyday,srv_img_epoch,srv_img_gdocver,srv_img_idd,srv_img_org_id,srv_img_vmid,srv_img_crdate,srv_img_dvtype,srv_img_loc,srv_img_os
from temp_srvimg_proto
"""

    statement.executeUpdate(qu)
   
    statement.close()
    "yes"

  } //------------fun

  //++++++++++++++++++++++++++++++++++++++++++++++++mcps+++++++++++++++++++++++++++++++++++++++++++++++++++

  def fmcps(fconnection: Connection, copy: Int, start_ts: Long): String = {
    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.mcps_proto(mcps_geo,mcps_rpyr,mcps_rpmnth,mcps_rpday,mcps_rphour,mcps_rpyday,mcps_rpepoch,mcps_gdoc,mcps_id,mcps_dname,mcps_dtype,mcps_sitname)
select distinct  mcps_geo,mcps_rpyr,mcps_rpmnth,mcps_rpday,mcps_rphour,mcps_rpyday,mcps_rpepoch,mcps_gdoc,mcps_id,mcps_dname,mcps_dtype,mcps_sitname
from temp_mcps_proto
"""


    statement.executeUpdate(qu)
   
    statement.close()
    "yes"

  } //-------------fun

  //+++++++++++++++++++++++++++++++++++++++++++n1network+++++++++++++++++++++++++++++++++++++++++++++++++

  def fntwk(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

   

    val qu = s"""insert into $data_schema.ntwk_proto(ntwk_geo,ntwk_rpyr,ntwk_rpmnth,ntwk_rpday,ntwk_rphour,ntwk_rpyday,ntwk_epoch,ntwk_gdocver,ntwk_con_id,ntwk_hostname,ntwk_con_state,ntwk_maintenance,ntwk_hyid,ntwk_ntid,ntwk_name,ntwk_netype,ntwk_org_id,ntwk_location,ntwk_ntstate,ntwk_crtdate)
 select distinct  ntwk_geo,ntwk_rpyr,ntwk_rpmnth,ntwk_rpday,ntwk_rphour,ntwk_rpyday,ntwk_epoch,ntwk_gdocver,ntwk_con_id,ntwk_hostname,ntwk_con_state,ntwk_maintenance,ntwk_hyid,ntwk_ntid,ntwk_name,ntwk_netype,ntwk_org_id,ntwk_location,ntwk_ntstate,ntwk_crtdate
from temp_ntwk_proto
"""

    
    statement.executeUpdate(qu)
   
    statement.close()

    "yes"

  } //----------fun

  //+++++++++++++++++++++++++++++++++++++++++++n1network+++++++++++++++++++++++++++++++++++++++++++++++++

  def fntwk_2(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.ntwk_proto(ntwk_geo,ntwk_rpyr,ntwk_rpmnth,ntwk_rpday,ntwk_rphour,ntwk_rpyday,ntwk_epoch,ntwk_gdocver,ntwk_con_id,ntwk_hostname,ntwk_con_state,ntwk_maintenance,ntwk_hyid,ntwk_ntid,ntwk_name,ntwk_netype,ntwk_org_id,ntwk_location,ntwk_ntstate,ntwk_crtdate)
 select distinct  ntwk_geo,ntwk_rpyr,ntwk_rpmnth,ntwk_rpday,ntwk_rphour,ntwk_rpyday,ntwk_epoch,ntwk_gdocver,ntwk_con_id,ntwk_hostname,ntwk_con_state,ntwk_maintenance,ntwk_hyid,ntwk_ntid,ntwk_name,ntwk_netype,ntwk_org_id,ntwk_location,ntwk_ntstate,ntwk_crtdate
from temp_ntwk_proto_1
"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //----------fun


  //+++++++++++++++++++++++++++++++++++++++++++++++++++n2networks_form1+++++++++++++++++++++++++++++++++++++++++++++

  def fntwo(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()
    
    val qu = s"""
insert into $data_schema.ntwo_proto(ntwo_geo,ntwo_rpyr,ntwo_rpmnth,ntwo_rpday,ntwo_rphour,ntwo_rpyday,ntwo_epoch,ntwo_gdoc,ntwo_fvlanid,ntwo_lfgrpid,ntwo_vlannum,ntwo_vlanname,ntwo_vlandesc,ntwo_vlaninmain,ntwo_cldntrkid,ntwo_org_id,ntwo_dtcid,ntwo_alname,ntwo_alstate,ntwo_alcrtime,ntwo_alntdomid)
select distinct  ntwo_geo,ntwo_rpyr,ntwo_rpmnth,ntwo_rpday,ntwo_rphour,ntwo_rpyday,ntwo_epoch,ntwo_gdoc,ntwo_fvlanid,ntwo_lfgrpid,ntwo_vlannum,ntwo_vlanname,ntwo_vlandesc,ntwo_vlaninmain,ntwo_cldntrkid,ntwo_org_id,ntwo_dtcid,ntwo_alname,ntwo_alstate,ntwo_alcrtime,ntwo_alntdomid
from temp_ntwo_proto
"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //-------------------fun

  //+++++++++++++++++++++++++++++++++++++++++++++++++++n2networks_form2+++++++++++++++++++++++++++++++++++++++++++++

  def fntwo_2(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""
insert into $data_schema.ntwo_proto(ntwo_geo,ntwo_rpyr,ntwo_rpmnth,ntwo_rpday,ntwo_rphour,ntwo_rpyday,ntwo_epoch,ntwo_gdoc,ntwo_fvlanid,ntwo_lfgrpid,ntwo_vlannum,ntwo_vlanname,ntwo_vlandesc,ntwo_vlaninmain,ntwo_cldntrkid,ntwo_org_id,ntwo_dtcid,ntwo_alname,ntwo_alstate,ntwo_alcrtime,ntwo_alntdomid,ntwo_hypid)
select distinct  ntwo_geo,ntwo_rpyr,ntwo_rpmnth,ntwo_rpday,ntwo_rphour,ntwo_rpyday,ntwo_epoch,ntwo_gdoc,ntwo_fvlanid,ntwo_lfgrpid,ntwo_vlannum,ntwo_vlanname,ntwo_vlandesc,ntwo_vlaninmain,ntwo_cldntrkid,ntwo_org_id,ntwo_dtcid,ntwo_alname,ntwo_alstate,ntwo_alcrtime,ntwo_alntdomid,ntwo_hypid
from temp_ntwo_proto1
"""
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //-------------------fun

  //++++++++++++++++++++++++++++++++++++++++++++++++++++++netdomain+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  def fnetdomain(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.netdomain_proto(netdomain_geo,netdomain_rpyr,netdomain_rpmnth,netdomain_rpday,netdomain_rphour,netdomain_rpyday,netdomain_epoch,netdomain_gdoc,netdomain_rodomnam,netdomain_inmain,netdomain_hypid,netdomain_id,netdomain_name,netdomain_type,netdomain_org_id,netdomain_state,netdomain_ctime)
select distinct  netdomain_geo,netdomain_rpyr,netdomain_rpmnth,netdomain_rpday,netdomain_rphour,netdomain_rpyday,netdomain_epoch,netdomain_gdoc,netdomain_rodomnam,netdomain_inmain,netdomain_hypid,netdomain_id,netdomain_name,netdomain_type,netdomain_org_id,netdomain_state,netdomain_ctime
from temp_netdomain_proto
"""
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"
  } //--------------fun

  //++++++++++++++++++++++++++++++++++++serverdisk++++++++++++++++++++++++++++++++++++++++++++++++++

  def fserverdisk(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.serverdisk_proto(serverdisk_id,serverdisk_type,serverdisk_disk_id,serverdisk_scsi_id,serverdisk_capacity_sz,serverdisk_capacity_unt,serverdisk_spd,serverdisk_ste,serverdisk_rpyr,serverdisk_rpmnth,serverdisk_rpday,serverdisk_rphour,serverdisk_rpyday,serverdisk_epoch,serverdisk_rpgdoc)
select distinct serverdisk_id,serverdisk_type,serverdisk_disk_id,serverdisk_scsi_id,serverdisk_capacity_sz,serverdisk_capacity_unt,serverdisk_spd,serverdisk_ste,serverdisk_rpyr,serverdisk_rpmnth,serverdisk_rpday,serverdisk_rphour,serverdisk_rpyday,serverdisk_epoch,serverdisk_rpgdoc
from temp_serverdisk_proto
"""
    

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //------------------fun

  //++++++++++++++++++++++++++++++++++++++++++++++++++++severnic++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  def fservernic(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.servernic_proto(servernic_id,servernic_nic_id,servernic_rpyr,servernic_rpmnth,servernic_rpday,servernic_rphour,servernic_rpyday,servernic_rpepoch,servernic_rpgdoc)
select distinct servernic_id,servernic_nic_id,servernic_rpyr,servernic_rpmnth,servernic_rpday,servernic_rphour,servernic_rpyday,servernic_rpepoch,servernic_rpgdoc
from temp_servernic_proto
"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //-------------------fun

  //+++++++++++++++++++++++++++++++++++++++++++++++++++servers+++++++++++++++++++++++++++++++++++++++++++++++++++++

  def fsrvinfo(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.srvinfo_proto(srv_info_geo,srvinfo_rpyr,srvinfo_rpmnth,srvinfo_rpday,srvinfo_rphour,srvinfo_rpyday,srvinfo_epoch,srvinfo_gdocver,srv_info_srvr_id,srvinfo_name,srv_info_org_id,srvinfo_vm_id,srvinfo_crt_dt,srvinfo_dvtype,srv_info_loc,srvinfo_gstate,srv_info_cpu,srvinfo_cpucores,srvinfo_cpuspdrsvr,srvinfo_cpuspdrsvrunt,srvinfo_cpuspdlmt,srvinfo_cpuspdlmtunt,srv_info_ram,srvinfo_ramunt,srv_info_srvr_os,srvinfo_assetid,srvinfo_servplan,srvinfo_bstate,srvinfo_started)
select distinct srv_info_geo,srvinfo_rpyr,srvinfo_rpmnth,srvinfo_rpday,srvinfo_rphour,srvinfo_rpyday,srvinfo_epoch,srvinfo_gdocver,srv_info_srvr_id,srvinfo_name,srv_info_org_id,srvinfo_vm_id,srvinfo_crt_dt,srvinfo_dvtype,srv_info_loc,srvinfo_gstate,srv_info_cpu,srvinfo_cpucores,srvinfo_cpuspdrsvr,srvinfo_cpuspdrsvrunt,srvinfo_cpuspdlmt,srvinfo_cpuspdlmtunt,srv_info_ram,srvinfo_ramunt,srv_info_srvr_os,srvinfo_assetid,srvinfo_servplan,srvinfo_bstate,srvinfo_started
from temp_srvinfo_proto
"""
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //------------------fun

  //++++++++++++++++++++++++++++++++++++++++++serversoftware+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  def fservsoft(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.servsoft_proto(servsoft_id,servsoft_disname,servsoft_rpyr,servsoft_rpmnth,servsoft_rpday,servsoft_rphour,servsoft_rpyday,servsoft_rpepoch,servsoft_gdoc)
select distinct servsoft_id,servsoft_disname,servsoft_rpyr,servsoft_rpmnth,servsoft_rpday,servsoft_rphour,servsoft_rpyday,servsoft_rpepoch,servsoft_gdoc
from temp_servsoft_proto
""" 
    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //---------------fun

  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++vendors+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  def fvendors(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.vendors_proto(vendors_geo,vendors_rpyr,vendors_rpmnth,vendors_rpday,vendors_rphour,vendors_rpyday,vendors_rpepoch,vendors_gdoc,vendors_id,vendors_desc,vendors_hmloc)
select distinct vendors_geo,vendors_rpyr,vendors_rpmnth,vendors_rpday,vendors_rphour,vendors_rpyday,vendors_rpepoch,vendors_gdoc,vendors_id,vendors_desc,vendors_hmloc
from temp_vendors_proto
"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //-------------------fun

  //+++++++++++++++++++++++++++++++++++++++++++++ipblock+++++++++++++++++++++++++++++++++++++++++++++++++

  def fipblock(fconnection: Connection, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    val qu = s"""insert into $data_schema.ipblock_proto(ipblock_geo,ipblock_rpyr,ipblock_rpmnth,ipblock_rpday,ipblock_rphour,ipblock_rpyday,ipblock_epoch,ipblock_gdoc,ipblock_blktyp,ipblock_snetsz,ipblock_state,ipblock_inmain,ipblock_none,ipblock_ntwo)
select distinct ipblock_geo,ipblock_rpyr,ipblock_rpmnth,ipblock_rpday,ipblock_rphour,ipblock_rpyday,ipblock_epoch,ipblock_gdoc,ipblock_blktyp,ipblock_snetsz,ipblock_state,ipblock_inmain,ipblock_none,ipblock_ntwo
from temp_ipblock_proto where ipblock_none is not null or ipblock_ntwo is not null
"""

    statement.executeUpdate(qu)
   
    statement.close()
    
    "yes"

  } //--------------------fun
  //++++++++++++++++++++++++ESX AND DS BUNDLE++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  def dsdatastore(fconnection: Connection, location: String, obs_dt: String, table_name: String, copy: Int, start_ts: Long): String = {

    val statement = fconnection.createStatement()

    
    val qul = "Update temp2_ds_proto set ds_loc = '" + location + "',ds_time = '" + obs_dt + "'"
    val qud_1 ="""create LOCAL temp table temp2_ds_proto(ds_clust VARCHAR(128),ds_name VARCHAR(128),ds_cap_gb FLOAT,ds_free_space FLOAT,ds_storagetier VARCHAR(128),ds_storagetype VARCHAR(128),ds_readiops VARCHAR(128),ds_writeiops VARCHAR(128),ds_loc VARCHAR(128),ds_time VARCHAR(128),ds_provisionedgb VARCHAR(128),ds_overprovisionedgb VARCHAR(128)) ON COMMIT preserve ROWS"""
    var qud = ""

    if (table_name == "temp_ds_proto") {
      qud ="""insert into temp2_ds_proto(ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype) select distinct ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype from temp_ds_proto where ds_clust is not null"""
    }

    if (table_name == "temp3_ds_proto") {
      qud ="""insert into temp2_ds_proto(ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype,ds_readiops,ds_writeiops) select distinct ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype,ds_readiops,ds_writeiops from temp3_ds_proto where ds_clust is not null"""
    }

    if (table_name == "temp4_ds_proto") {
      qud ="""insert into temp2_ds_proto(ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype,ds_provisionedgb,ds_overprovisionedgb) select distinct ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype,ds_provisionedgb,ds_overprovisionedgb from temp4_ds_proto where ds_clust is not null"""
    }

    //"Clustername","DSName","CapacityGB","FreespaceGB","StorageTier","StorageType"
    //"Clustername","DSName","CapacityGB","FreespaceGB","StorageTier","StorageType","ProvisionedGB","overprovisionedGB"
    //"Clustername","DSName","CapacityGB","FreespaceGB","StorageTier","StorageType","ReadIOPS","WriteIOPS"

    val qu =
      s"""INSERT INTO $data_schema.ds_proto(ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype,ds_readiops,ds_writeiops,ds_provisionedgb,ds_overprovisionedgb,ds_loc,
ds_time)
select ds_clust,ds_name,ds_cap_gb,ds_free_space,ds_storagetier,ds_storagetype,ds_readiops,ds_writeiops,ds_provisionedgb,ds_overprovisionedgb,ds_loc,
ds_time :: timestamp
from temp2_ds_proto
""" 
    statement.executeUpdate(qud_1)

    statement.executeUpdate(qud)

    statement.executeUpdate(qul)
    
    statement.executeUpdate(qu)

    statement.close()

    "yes"

  } //------------------------------fun


}
