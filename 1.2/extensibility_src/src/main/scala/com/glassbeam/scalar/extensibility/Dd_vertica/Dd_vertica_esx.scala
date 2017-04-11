package com.glassbeam.scalar.extensibility.Dd_vertica

import java.io.{BufferedWriter, FileWriter, PrintWriter}
import java.nio.file.{FileSystems, Path}
import java.sql.{Connection, DriverManager}
import java.util.Properties

import com.glassbeam.extensibility.traits._

class Dd_vertica_esx extends Parsable {

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

  //------------------------------Collumns----------------------------------------------------------------------

  var esx_clust =""
  var esx_host =""
  var esx_vmname =""
  var esx_totmhz =""
  var esx_cpuusagemhz =""
  var esx_cpureservemhz =""
  var esx_numcpu =""
  var esx_insram =""
  var esx_coremhz =""
  var esx_constate =""
  var esx_vmallram =""
  var esx_vmscpucnt =""
  var esx_pow_on_vms =""
  var esx_pow_off_vms =""
  var esx_cputs =""
  var esx_cpu =""
  var esx_ramts =""
  var esx_ram =""
  var esx_loc =""
  var esx_time =""
  var esx_reservedmhz =""

  //------------------------------MAP for table name to temp tables structure ----------------------------------

  val temp_clmns =
    """(esx_clust varchar(128),esx_host varchar(128),esx_vmname varchar(128),esx_totmhz int,
      |esx_cpuusagemhz varchar(128),esx_cpureservemhz varchar(128),esx_numcpu int,esx_insram float,esx_coremhz int,
      |esx_constate varchar(128),esx_vmallram int,esx_vmscpucnt int,esx_pow_on_vms int,esx_pow_off_vms int,
      |esx_cputs varchar(128),esx_cpu float,esx_ramts varchar(128),esx_ram float,esx_reservedmhz varchar(128),
      |esx_loc varchar(128),esx_time varchar(128))""".stripMargin

  /*val table_des = Map(

    """temp_esx_proto1""" ->"""(esx_clust varchar(128), esx_host varchar(128),esx_totmhz int,esx_numcpu int,esx_insram float,esx_coremhz int,esx_constate varchar(128),esx_vmallram int,esx_vmscpucnt int,esx_pow_on_vms int,esx_pow_off_vms Int,esx_cputs varchar(128),esx_cpu float,esx_ramts varchar(128), esx_ram float)""",

    """temp3_esx_proto1""" ->"""(esx_clust varchar(128),esx_host varchar(128),esx_vmname varchar(128),esx_totmhz int,esx_cpuusagemhz varchar(128),esx_cpureservemhz varchar(128),esx_numcpu int,esx_insram float,esx_coremhz int,esx_constate varchar(128),esx_vmallram int,esx_vmscpucnt int,esx_pow_on_vms int,esx_pow_off_vms Int,esx_cputs varchar(128),esx_cpu float,esx_ramts varchar(128),esx_ram float)""",

    """temp4_esx_proto1""" ->"""(esx_clust varchar(128),esx_host varchar(128),esx_totmhz int,esx_numcpu int,esx_insram float,esx_coremhz int,esx_vmallram int,esx_vmscpucnt int,esx_pow_on_vms int,esx_pow_off_vms Int,esx_cputs varchar(128),esx_cpu float,esx_ramts varchar(128),esx_ram float)""",

    """temp5_esx_proto1""" ->"""(esx_clust varchar(128),esx_host varchar(128),esx_totmhz int,esx_reservedmhz varchar(128),esx_numcpu int,esx_insram float,esx_coremhz int,esx_constate varchar(128),esx_vmallram int,esx_vmscpucnt int,esx_pow_on_vms int,esx_pow_off_vms Int,esx_cputs varchar(128),esx_cpu float,esx_ramts varchar(128),esx_ram float)"""
  )*/

  val column_des = Map(

    """temp_esx_proto1""" ->"""(esx_clust ,esx_host ,esx_totmhz ,esx_numcpu ,esx_insram ,esx_coremhz ,esx_constate ,esx_vmallram ,esx_vmscpucnt ,
esx_pow_on_vms ,esx_pow_off_vms ,esx_cputs ,esx_cpu ,esx_ramts , esx_ram )""",

    """temp3_esx_proto1""" ->"""(esx_clust ,esx_host ,esx_vmname ,esx_totmhz ,esx_cpuusagemhz ,esx_cpureservemhz ,esx_numcpu ,esx_insram ,
esx_coremhz ,esx_constate ,esx_vmallram ,esx_vmscpucnt ,esx_pow_on_vms ,esx_pow_off_vms ,esx_cputs ,esx_cpu ,esx_ramts ,esx_ram )""",

    """temp4_esx_proto1""" ->"""(esx_clust ,esx_host ,esx_totmhz ,esx_numcpu ,esx_insram ,esx_coremhz ,esx_vmallram ,esx_vmscpucnt ,
esx_pow_on_vms ,esx_pow_off_vms ,esx_cputs ,esx_cpu ,esx_ramts ,esx_ram )""",

    """temp5_esx_proto1""" ->"""(esx_clust ,esx_host ,esx_totmhz ,esx_reservedmhz ,esx_numcpu ,esx_insram ,esx_coremhz ,
esx_constate ,esx_vmallram ,esx_vmscpucnt ,esx_pow_on_vms ,esx_pow_off_vms ,esx_cputs ,esx_cpu ,esx_ramts ,esx_ram )"""
  )

  //+++++++++++++++FOR ESX DIFFERENT FORMAT++++++++++++++++++++++++++++++++++++

  val esx_form1_s ="""\"Clustername\"\,\"Hostname\"\,\"TotalMHz\"\,\"NumCPU\"\,\"InstalledRAM\"\,\"CoreMhz\"\,\"ConnectionState\"\,\"VMAllocatedRAM\"\,\"VMvCPUCount\"\,\"PoweredonVMs\"\,\"PoweredoffVMs\"\,\"CPU\%\-timestamp\"\,\"CPU\-\%\",\"RAM\%\-timestamp\"\,\"RAM\-\%\"[^\n]*""".r

  val big_line_esx1 = """\"?([^\"]+)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r

  val small_line_esx1 ="""^\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r

  //-----
  val esx_form2_s =
    """\"Clustername\"\,\"Hostname\"\,\"VMName\"\,\"TotalMHz\"\,\"CPUUsageMHz\"\,\"CPUReserveMHz\"\,\"NumCPU\"\,\"InstalledRAM\"\,\"CoreMhz\"\,\"ConnectionState\"\,\"VMAllocatedRAM\"\,\"VMvCPUCount\"\,\"PoweredonVMs\"\,\"PoweredoffVMs\"\,\"CPU\%\-timestamp\"\,\"CPU\-\%\",\"RAM\%\-timestamp\"\,\"RAM\-\%\"[^\n]*""".r

  val big_line_esx2 = """\"?([^\"]+)\"?\,\"?([^\"]*)\"?\,\"?([^\"]+)\"?\,\"?([^\"]+)\"?\,\"?([^\"]+)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r

  val small_line_esx2 = """\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r

  //-----
  val esx_form3_s =
    """\"Clustername\"\,\"Hostname\"\,\"TotalMHz\"\,\"NumCPU\"\,\"InstalledRAM\"\,\"CoreMhz\"\,\"VMAllocatedRAM\"\,\"VMvCPUCount\"\,\"PoweredonVMs\"\,\"PoweredoffVMs\"\,\"CPU\%\-timestamp\"\,\"CPU\-\%\",\"RAM\%\-timestamp\"\,\"RAM\-\%\"[^\n]*""".r

  val big_line_esx3 = """\"?([^\"]+)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r

  val small_line_esx3 = """\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r

  //-----
  val esx_form4_s =
    """\"Clustername\"\,\"Hostname\"\,\"TotalMHz\"\,\"ReservedMHz\"\,\"NumCPU\"\,\"InstalledRAM\"\,\"CoreMhz\"\,\"ConnectionState\"\,\"VMAllocatedRAM\"\,\"VMvCPUCount\"\,\"PoweredonVMs\"\,\"PoweredoffVMs\"\,\"CPU\%\-timestamp\"\,\"CPU\-\%\",\"RAM\%\-timestamp\"\,\"RAM\-\%\"[^\n]*""".r

  val big_line_esx4 = """\"?([^\"]+)\"?\,\"?([^\"]*)\"?\,\"?([^\"]+)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r

  val small_line_esx4 = """\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([\d]*)\"?\,\"?([\d]*)\"?\,\"?([^\"]*)\"?\,\"?([^\"]*)\"?\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"\,\"(\d+\/\d+\/\d+\s+\d+\:\d+\:\d+\s+\w+)\"\,\"([\d\.]+)\"""".r


  override def processToFile(p: Path): Path = {
    logger.info("startng esx")
  //println("Starting Loading "+p.getFileName)
    val file_out = p.getParent.toString + "/extensibility.compute.csv"
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, true)))
    val file_pattern = """.*\/\d+\-\w\w\w\-\d+\.\d+\.[\+\-]\d+\/[^\/]+\/(.*)""".r
    val checkPath = p.toString match {
      case file_pattern(l1) => l1
      case _ => p.toString

    }
    val pair = """Subject\:\s*(\S+)\s+.+?\:\s*(.+)""".r
    var location: String = ""
    for (line_done <- scala.io.Source.fromFile(p.getParent + "/done", "ISO-8859-1").getLines()) {
      line_done match {
        case pair(loc, obs) =>
          location = loc
        case _ =>
      } //end of match
    } //end of for

    var connection: Connection = null
    try {
      Class.forName("com.vertica.jdbc.Driver")
      connection = DriverManager.getConnection(url, myProp)
      connection.setAutoCommit(false)
      val line = scala.io.Source.fromFile(p.toString, "ISO-8859-1").getLines().toList
      if (check_File(connection, checkPath)) {
        //println("get true")
        val lines = scala.io.Source.fromFile(p.toString, "ISO-8859-1").getLines()
        if (line.exists(_.matches(esx_form1_s.toString()))) {
          //println("sample 1")
          change_from1(lines, writer)
          writer.close()
          load_temp("temp_esx_proto1", file_out, connection)
          esxcompute(connection, location, "temp_esx_proto1")

        } else if (line.exists(_.matches(esx_form2_s.toString()))) {
          //println("sample 2")
          change_from2(lines, writer)
          writer.close()
          load_temp("temp_esx_proto2", file_out, connection)
          esxcompute(connection, location, "temp_esx_proto2")

        } else if (line.exists(_.matches(esx_form3_s.toString()))) {
          //println("sample 3")
          change_from3(lines, writer)
          writer.close()
          load_temp("temp_esx_proto3", file_out, connection)
          esxcompute(connection, location, "temp_esx_proto3")

        } else if (line.exists(_.matches(esx_form4_s.toString()))) {
         // println("sample 4")
          change_from4(lines, writer)
          writer.close()
          load_temp("temp_esx_proto4", file_out, connection)
          esxcompute(connection, location, "temp_esx_proto4")

        } else {
          //println("sample nothing")
          logger.warning("File Format is not handle For compute-rpt (ESX) file"+p.toString)
        }
      }else{
        //println("check else")
      }
      connection.commit()
    } catch {
      case e: Exception =>
       // println("exception")
        logger.error(p.toString + " " + e.getClass.toString + " and " + e.getMessage)
        connection.rollback()
        connection.close()
    }
    //println("done")
    connection.close()

    FileSystems.getDefault.getPath(file_out)
  }


  //--------------------------------------------------------------------
  override def processFileToContext(p: Path): String = {
    "success"
  }

  override def processBundleToContext(p: Path): String = {
    "success"
  }

  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit = {}

  //-------------------------------------------------------------------

  //---------------------------------------Load in temp-table-----------------------------------------------------
  def load_temp(table: String, file: String, fconnection: Connection): Int = {
    //println("loading")
    val statement = fconnection.createStatement()
    val qu_temptbl = "CREATE LOCAL TEMPORARY TABLE " + table + "" + temp_clmns + "ON COMMIT preserve ROWS"
    statement.executeUpdate(qu_temptbl)
    val qu = "COPY " + table + column_des(table) + " FROM LOCAL \'" + file + "\' PARSER fcsvparser() NO COMMIT"
    //logger.info(qu_temptbl);
    //logger.info(qu);
    val copy = statement.executeUpdate(qu)
    statement.close()
    copy.toInt
  }

  //+++++++++++++++++++++++++++++++++++++++++++++++FIlename Check+++++++++++++++++++++++++++
  def check_File(fconnection: Connection, fname: String):Boolean = {
    //println("check_file")
    //println("file name "+fname)
    val statement = fconnection.createStatement()
    val resultSet = statement.executeQuery(s"""select * from $data_schema.checkFile where filename = '$fname'""")
    if (!resultSet.next()) {
      statement.executeUpdate(s"insert into $data_schema.checkFile(filename) values('$fname')")
      statement.close()
      //println("true")

      return true
    } else {
      //println("false")
      statement.close()
      return false
    }
  }

  //-------------------------------LOAD IN PERMANENT________________________________
  def esxcompute(fconnection: Connection, location: String, table_name: String): Unit = {
    val statement = fconnection.createStatement()
    val qu =
      s"""
	insert into $data_schema.esx_proto(esx_clust,esx_host,esx_vmname,esx_totmhz,esx_cpuusagemhz,esx_cpureservemhz,esx_numcpu,esx_insram,esx_coremhz,
esx_constate,esx_vmallram,esx_vmscpucnt,esx_pow_on_vms,esx_pow_off_vms,esx_cputs,esx_cpu,esx_ramts,esx_ram,esx_loc,
esx_time,esx_reservedmhz,esx_time_bkp)
select distinct esx_clust,esx_host,esx_vmname,esx_totmhz,esx_cpuusagemhz,esx_cpureservemhz,esx_numcpu,esx_insram,esx_coremhz,
esx_constate,esx_vmallram,esx_vmscpucnt,esx_pow_on_vms,esx_pow_off_vms,esx_cputs,esx_cpu,esx_ramts,esx_ram,'${location}',
esx_cputs :: timestamp,esx_reservedmhz,esx_cputs
from ${table_name}
"""
    //logger.info(qu);
    statement.executeUpdate(qu)
    statement.close()

  }

  //--------------------CHANGE FILE ACCORDING TO FORMAT-----------------------------------
  def change_from1(lines: Iterator[String], writer: PrintWriter) = {

    for (line <- lines) {
      line match {

        case esx_form1_s()=>
          writer.write(line+"\n");

        case big_line_esx1(esx_clustc,esx_hostc,esx_totmhzc,esx_numcpuc,esx_insramc,esx_coremhzc,esx_constatec,esx_vmallramc,
        esx_vmscpucntc,esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,esx_ramc)=>
          esx_clust=esx_clustc
          esx_host=esx_hostc
          esx_totmhz=esx_totmhzc
          esx_numcpu=esx_numcpuc
          esx_insram=esx_insramc
          esx_coremhz=esx_coremhzc
          esx_constate=esx_constatec
          esx_vmallram=esx_vmallramc
          esx_vmscpucnt=esx_vmscpucntc
          esx_pow_on_vms=esx_pow_on_vmsc
          esx_pow_off_vms=esx_pow_off_vmsc
          esx_cputs=esx_cputsc
          esx_cpu=esx_cpuc
          esx_ramts=esx_ramtsc
          esx_ram=esx_ramc
          writer.write(line+"\n")


        case small_line_esx1(esx_clustc,esx_hostc,esx_totmhzc,esx_numcpuc,esx_insramc,esx_coremhzc,esx_constatec,esx_vmallramc,
        esx_vmscpucntc,esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,esx_ramc)=>
          val  esx_clustc1 =
            if (esx_clustc != "" && esx_clustc != null && esx_clustc != "0") esx_clustc else esx_clust
          val  esx_hostc1 =
            if (esx_hostc != "" && esx_hostc != null && esx_hostc != "0") esx_hostc else esx_host
          val  esx_totmhzc1 =
            if (esx_totmhzc != "" && esx_totmhzc != null && esx_totmhzc != "0") esx_totmhzc else esx_totmhz
          val  esx_numcpuc1 =
            if (esx_numcpuc != "" && esx_numcpuc != null && esx_numcpuc != "0") esx_numcpuc else esx_numcpu
          val  esx_insramc1 =
            if (esx_insramc != "" && esx_insramc != null && esx_insramc != "0") esx_insramc else esx_insram
          val  esx_coremhzc1 =
            if (esx_coremhzc != "" && esx_coremhzc != null && esx_coremhzc != "0") esx_coremhzc else esx_coremhz
          val  esx_constatec1 =
            if (esx_constatec != "" && esx_constatec != null && esx_constatec != "0") esx_constatec else esx_constate
          val  esx_vmallramc1 =
            if (esx_vmallramc != "" && esx_vmallramc != null && esx_vmallramc != "0") esx_vmallramc else esx_vmallram
          val  esx_vmscpucntc1 =
            if (esx_vmscpucntc != "" && esx_vmscpucntc != null && esx_vmscpucntc != "0") esx_vmscpucntc else esx_vmscpucnt
          val  esx_pow_on_vmsc1 =
            if (esx_pow_on_vmsc != "" && esx_pow_on_vmsc != null && esx_pow_on_vmsc != "0" && esx_pow_on_vmsc != "1") esx_pow_on_vmsc else esx_pow_on_vms
          val  esx_pow_off_vmsc1 =
            if (esx_pow_off_vmsc != "" && esx_pow_off_vmsc != null && esx_pow_off_vmsc != "0" && esx_pow_off_vmsc != "1") esx_pow_off_vmsc else esx_pow_off_vms
          val  esx_cputsc1 =
            if (esx_cputsc != "" && esx_cputsc != null && esx_cputsc != "0") esx_cputsc else esx_cputs
          val  esx_cpuc1 =
            if (esx_cpuc != "" && esx_cpuc != null && esx_cpuc != "0") esx_cpuc else esx_cpu
          val  esx_ramtsc1 =
            if (esx_ramtsc != "" && esx_ramtsc != null && esx_ramtsc != "0") esx_ramtsc else esx_ramts
          val  esx_ramc1 =
            if (esx_ramc != "" && esx_ramc != null && esx_ramc != "0") esx_ramc else esx_ram
          val out_line = "\""+esx_clustc1 + "\",\"" + esx_hostc1 + "\",\"" + esx_totmhzc1 + "\",\"" + esx_numcpuc1 + "\",\"" + esx_insramc1 + "\",\"" + esx_coremhzc1 + "\",\"" +
            esx_constatec1 + "\",\"" + esx_vmallramc1 + "\",\"" + esx_vmscpucntc1 + "\",\"" + esx_pow_on_vmsc1 + "\",\"" + esx_pow_off_vmsc1 + "\",\"" + esx_cputsc1 + "\",\"" +
            esx_cpuc1 + "\",\"" + esx_ramtsc1 + "\",\"" + esx_ramc1+"\""
          writer.write(out_line.replaceAll("\"\"","")+"\n")

        case _ =>
      }
    }
  }

  def change_from2(lines: Iterator[String], writer: PrintWriter) = {

    for (line <- lines) {
      line match {
        case esx_form2_s()=>
          writer.write(line+"\n");

        case big_line_esx2(esx_clustc,esx_hostc,esx_vmnamec,esx_totmhzc,esx_cpuusagemhzc,esx_cpureservemhzc,esx_numcpuc,esx_insramc,
        esx_coremhzc,esx_constatec,esx_vmallramc,esx_vmscpucntc,esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,
        esx_ramc)=>
          esx_clust=esx_clustc
          esx_host=esx_hostc
          esx_vmname=esx_vmnamec
          esx_totmhz=esx_totmhzc
          esx_cpuusagemhz=esx_cpuusagemhzc
          esx_cpureservemhz=esx_cpureservemhzc
          esx_numcpu=esx_numcpuc
          esx_insram=esx_insramc
          esx_coremhz=esx_coremhzc
          esx_constate=esx_constatec
          esx_vmallram=esx_vmallramc
          esx_vmscpucnt=esx_vmscpucntc
          esx_pow_on_vms=esx_pow_on_vmsc
          esx_pow_off_vms=esx_pow_off_vmsc
          esx_cputs=esx_cputsc
          esx_cpu=esx_cpuc
          esx_ramts=esx_ramtsc
          esx_ram=esx_ramc
          writer.write(line+"\n")


        case small_line_esx2(esx_clustc,esx_hostc,esx_vmnamec,esx_totmhzc,esx_cpuusagemhzc,esx_cpureservemhzc,esx_numcpuc,esx_insramc,
        esx_coremhzc,esx_constatec,esx_vmallramc,esx_vmscpucntc,esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,
        esx_ramc)=>
          val  esx_clustc1 =
            if (esx_clustc != "" && esx_clustc != null && esx_clustc != "0") esx_clustc else esx_clust
          val  esx_hostc1 =
            if (esx_hostc != "" && esx_hostc != null && esx_hostc != "0") esx_hostc else esx_host
          val  esx_vmnamec1 =
            if (esx_vmnamec != "" && esx_vmnamec != null && esx_vmnamec != "0") esx_vmnamec else esx_vmname
          val  esx_totmhzc1 =
            if (esx_totmhzc != "" && esx_totmhzc != null && esx_totmhzc != "0") esx_totmhzc else esx_totmhz
          val  esx_cpuusagemhzc1 =
            if (esx_cpuusagemhzc != "" && esx_cpuusagemhzc != null && esx_cpuusagemhzc != "0") esx_cpuusagemhzc else esx_cpuusagemhz
          val  esx_cpureservemhzc1 =
            if (esx_cpureservemhzc != "" && esx_cpureservemhzc != null && esx_cpureservemhzc != "0") esx_cpureservemhzc else esx_cpureservemhz
          val  esx_numcpuc1 =
            if (esx_numcpuc != "" && esx_numcpuc != null && esx_numcpuc != "0") esx_numcpuc else esx_numcpu
          val  esx_insramc1 =
            if (esx_insramc != "" && esx_insramc != null && esx_insramc != "0") esx_insramc else esx_insram
          val  esx_coremhzc1 =
            if (esx_coremhzc != "" && esx_coremhzc != null && esx_coremhzc != "0") esx_coremhzc else esx_coremhz
          val  esx_constatec1 =
            if (esx_constatec != "" && esx_constatec != null && esx_constatec != "0") esx_constatec else esx_constate
          val  esx_vmallramc1 =
            if (esx_vmallramc != "" && esx_vmallramc != null && esx_vmallramc != "0") esx_vmallramc else esx_vmallram
          val  esx_vmscpucntc1 =
            if (esx_vmscpucntc != "" && esx_vmscpucntc != null && esx_vmscpucntc != "0") esx_vmscpucntc else esx_vmscpucnt
          val  esx_pow_on_vmsc1 =
            if (esx_pow_on_vmsc != "" && esx_pow_on_vmsc != null && esx_pow_on_vmsc != "0" && esx_pow_on_vmsc != "1") esx_pow_on_vmsc else esx_pow_on_vms
          val  esx_pow_off_vmsc1 =
            if (esx_pow_off_vmsc != "" && esx_pow_off_vmsc != null && esx_pow_off_vmsc != "0" && esx_pow_off_vmsc != "1") esx_pow_off_vmsc else esx_pow_off_vms
          val  esx_cputsc1 =
            if (esx_cputsc != "" && esx_cputsc != null && esx_cputsc != "0") esx_cputsc else esx_cputs
          val  esx_cpuc1 =
            if (esx_cpuc != "" && esx_cpuc != null && esx_cpuc != "0") esx_cpuc else esx_cpu
          val  esx_ramtsc1 =
            if (esx_ramtsc != "" && esx_ramtsc != null && esx_ramtsc != "0") esx_ramtsc else esx_ramts
          val  esx_ramc1 =
            if (esx_ramc != "" && esx_ramc != null && esx_ramc != "0") esx_ramc else esx_ram

          val out_line = "\""+esx_clustc1 + "\",\"" + esx_hostc1 + "\",\"" + esx_vmnamec1 + "\",\"" + esx_totmhzc1 + "\",\"" + esx_cpuusagemhzc1 +
            "\",\"" + esx_cpureservemhzc1 + "\",\"" + esx_numcpuc1 + "\",\"" + esx_insramc1 + "\",\"" + esx_coremhzc1 + "\",\"" +
            esx_constatec1 + "\",\"" + esx_vmallramc1 + "\",\"" + esx_vmscpucntc1 + "\",\"" + esx_pow_on_vmsc1 + "\",\"" +
            esx_pow_off_vmsc1 + "\",\"" + esx_cputsc1 + "\",\"" + esx_cpuc1 + "\",\"" + esx_ramtsc1 + "\",\"" + esx_ramc1 + "\""
          writer.write(out_line.replaceAll("\"\"","")+"\n")

        case _ =>
      }
    }
  }

  def change_from3(lines: Iterator[String], writer: PrintWriter) = {

    for (line <- lines) {
      line match {
        case esx_form3_s()=>
          writer.write(line+"\n");

        case big_line_esx3(esx_clustc,esx_hostc,esx_totmhzc,esx_numcpuc,esx_insramc,esx_coremhzc,esx_vmallramc,esx_vmscpucntc,
        esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,esx_ramc)=>
          esx_clust=esx_clustc
          esx_host=esx_hostc
          esx_totmhz=esx_totmhzc
          esx_numcpu=esx_numcpuc
          esx_insram=esx_insramc
          esx_coremhz=esx_coremhzc
          esx_vmallram=esx_vmallramc
          esx_vmscpucnt=esx_vmscpucntc
          esx_pow_on_vms=esx_pow_on_vmsc
          esx_pow_off_vms=esx_pow_off_vmsc
          esx_cputs=esx_cputsc
          esx_cpu=esx_cpuc
          esx_ramts=esx_ramtsc
          esx_ram=esx_ramc
          writer.write(line+"\n")

        case small_line_esx3(esx_clustc,esx_hostc,esx_totmhzc,esx_numcpuc,esx_insramc,esx_coremhzc,esx_vmallramc,esx_vmscpucntc,
        esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,esx_ramc)=>
          val  esx_clustc1 =
            if (esx_clustc != "" && esx_clustc != null && esx_clustc != "0") esx_clustc else esx_clust
          val  esx_hostc1 =
            if (esx_hostc != "" && esx_hostc != null && esx_hostc != "0") esx_hostc else esx_host
          val  esx_totmhzc1 =
            if (esx_totmhzc != "" && esx_totmhzc != null && esx_totmhzc != "0") esx_totmhzc else esx_totmhz
          val  esx_numcpuc1 =
            if (esx_numcpuc != "" && esx_numcpuc != null && esx_numcpuc != "0") esx_numcpuc else esx_numcpu
          val  esx_insramc1 =
            if (esx_insramc != "" && esx_insramc != null && esx_insramc != "0") esx_insramc else esx_insram
          val  esx_coremhzc1 =
            if (esx_coremhzc != "" && esx_coremhzc != null && esx_coremhzc != "0") esx_coremhzc else esx_coremhz
          val  esx_vmallramc1 =
            if (esx_vmallramc != "" && esx_vmallramc != null && esx_vmallramc != "0") esx_vmallramc else esx_vmallram
          val  esx_vmscpucntc1 =
            if (esx_vmscpucntc != "" && esx_vmscpucntc != null && esx_vmscpucntc != "0") esx_vmscpucntc else esx_vmscpucnt
          val  esx_pow_on_vmsc1 =
            if (esx_pow_on_vmsc != "" && esx_pow_on_vmsc != null && esx_pow_on_vmsc != "0" && esx_pow_on_vmsc != "1") esx_pow_on_vmsc else esx_pow_on_vms
          val  esx_pow_off_vmsc1 =
            if (esx_pow_off_vmsc != "" && esx_pow_off_vmsc != null && esx_pow_off_vmsc != "0" && esx_pow_off_vmsc != "1") esx_pow_off_vmsc else esx_pow_off_vms
          val  esx_cputsc1 =
            if (esx_cputsc != "" && esx_cputsc != null && esx_cputsc != "0") esx_cputsc else esx_cputs
          val  esx_cpuc1 =
            if (esx_cpuc != "" && esx_cpuc != null && esx_cpuc != "0") esx_cpuc else esx_cpu
          val  esx_ramtsc1 =
            if (esx_ramtsc != "" && esx_ramtsc != null && esx_ramtsc != "0") esx_ramtsc else esx_ramts
          val  esx_ramc1 =
            if (esx_ramc != "" && esx_ramc != null && esx_ramc != "0") esx_ramc else esx_ram

          val out_line = "\""+esx_clustc1 + "\",\"" + esx_hostc1 + "\",\"" + esx_totmhzc1 + "\",\"" + esx_numcpuc1 + "\",\"" +
            esx_insramc1 + "\",\"" + esx_coremhzc1 + "\",\"" + esx_vmallramc1 + "\",\"" + esx_vmscpucntc1 + "\",\"" +
            esx_pow_on_vmsc1 + "\",\"" + esx_pow_off_vmsc1 + "\",\"" + esx_cputsc1 + "\",\"" + esx_cpuc1 + "\",\"" +
            esx_ramtsc1 + "\",\"" + esx_ramc1 +"\""
          writer.write(out_line.replaceAll("\"\"","")+"\n")
        case _ =>
      }
    }
  }

  def change_from4(lines: Iterator[String], writer: PrintWriter) = {

    for (line <- lines) {
      line match {
        case esx_form4_s()=>
          writer.write(line+"\n");

        case big_line_esx4(esx_clustc,esx_hostc,esx_totmhzc,esx_reservedmhzc,esx_numcpuc,esx_insramc,esx_coremhzc,esx_constatec,esx_vmallramc,
        esx_vmscpucntc,esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,esx_ramc)=>
          esx_clust=esx_clustc
          esx_host=esx_hostc
          esx_totmhz=esx_totmhzc
          esx_reservedmhz=esx_reservedmhzc
          esx_numcpu=esx_numcpuc
          esx_insram=esx_insramc
          esx_coremhz=esx_coremhzc
          esx_constate=esx_constatec
          esx_vmallram=esx_vmallramc
          esx_vmscpucnt=esx_vmscpucntc
          esx_pow_on_vms=esx_pow_on_vmsc
          esx_pow_off_vms=esx_pow_off_vmsc
          esx_cputs=esx_cputsc
          esx_cpu=esx_cpuc
          esx_ramts=esx_ramtsc
          esx_ram=esx_ramc
          writer.write(line+"\n")

        case small_line_esx4(esx_clustc,esx_hostc,esx_totmhzc,esx_reservedmhzc,esx_numcpuc,esx_insramc,esx_coremhzc,esx_constatec,esx_vmallramc,
        esx_vmscpucntc,esx_pow_on_vmsc,esx_pow_off_vmsc,esx_cputsc,esx_cpuc,esx_ramtsc,esx_ramc)=>
          val  esx_clustc1 =
            if (esx_clustc != "" && esx_clustc != null && esx_clustc != "0") esx_clustc else esx_clust
          val  esx_hostc1 =
            if (esx_hostc != "" && esx_hostc != null && esx_hostc != "0") esx_hostc else esx_host
          val  esx_totmhzc1 =
            if (esx_totmhzc != "" && esx_totmhzc != null && esx_totmhzc != "0") esx_totmhzc else esx_totmhz
          val  esx_reservedmhzc1 =
            if (esx_reservedmhzc != "" && esx_reservedmhzc != null && esx_reservedmhzc != "0") esx_reservedmhzc else esx_reservedmhz
          val  esx_numcpuc1 =
            if (esx_numcpuc != "" && esx_numcpuc != null && esx_numcpuc != "0") esx_numcpuc else esx_numcpu
          val  esx_insramc1 =
            if (esx_insramc != "" && esx_insramc != null && esx_insramc != "0") esx_insramc else esx_insram
          val  esx_coremhzc1 =
            if (esx_coremhzc != "" && esx_coremhzc != null && esx_coremhzc != "0") esx_coremhzc else esx_coremhz
          val  esx_constatec1 =
            if (esx_constatec != "" && esx_constatec != null && esx_constatec != "0") esx_constatec else esx_constate
          val  esx_vmallramc1 =
            if (esx_vmallramc != "" && esx_vmallramc != null && esx_vmallramc != "0") esx_vmallramc else esx_vmallram
          val  esx_vmscpucntc1 =
            if (esx_vmscpucntc != "" && esx_vmscpucntc != null && esx_vmscpucntc != "0") esx_vmscpucntc else esx_vmscpucnt
          val  esx_pow_on_vmsc1 =
            if (esx_pow_on_vmsc != "" && esx_pow_on_vmsc != null && esx_pow_on_vmsc != "0" && esx_pow_on_vmsc != "1") esx_pow_on_vmsc else esx_pow_on_vms
          val  esx_pow_off_vmsc1 =
            if (esx_pow_off_vmsc != "" && esx_pow_off_vmsc != null && esx_pow_off_vmsc != "0" && esx_pow_off_vmsc != "1") esx_pow_off_vmsc else esx_pow_off_vms
          val  esx_cputsc1 =
            if (esx_cputsc != "" && esx_cputsc != null && esx_cputsc != "0") esx_cputsc else esx_cputs
          val  esx_cpuc1 =
            if (esx_cpuc != "" && esx_cpuc != null && esx_cpuc != "0") esx_cpuc else esx_cpu
          val  esx_ramtsc1 =
            if (esx_ramtsc != "" && esx_ramtsc != null && esx_ramtsc != "0") esx_ramtsc else esx_ramts
          val  esx_ramc1 =
            if (esx_ramc != "" && esx_ramc != null && esx_ramc != "0") esx_ramc else esx_ram

          val out_line = "\""+ esx_clustc1 + "\",\"" + esx_hostc1 + "\",\"" + esx_totmhzc1 + "\",\"" + esx_reservedmhzc1 +
            "\",\"" + esx_numcpuc1 + "\",\"" + esx_insramc1 + "\",\"" + esx_coremhzc1 + "\",\"" + esx_constatec1 + "\",\"" +
            esx_vmallramc1 + "\",\"" + esx_vmscpucntc1 + "\",\"" + esx_pow_on_vmsc1 + "\",\"" + esx_pow_off_vmsc1 + "\",\"" +
            esx_cputsc1 + "\",\"" + esx_cpuc1 + "\",\"" + esx_ramtsc1 + "\",\"" + esx_ramc1 +"\""
          writer.write(out_line.replaceAll("\"\"","")+"\n")


        case _ =>
      }
    }
  }


}