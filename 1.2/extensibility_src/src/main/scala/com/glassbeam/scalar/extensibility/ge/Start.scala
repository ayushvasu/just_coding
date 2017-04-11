
import java.io.{BufferedWriter, File, FileWriter, PrintWriter}
import java.nio.file.{FileSystems, Path}
import java.sql.{Connection, DriverManager, ResultSet}
import java.text.SimpleDateFormat
import java.util

import com.glassbeam.extensibility.traits.Parsable
import java.util.Date

import scala.io.Source
import scala.util.matching.Regex

/**
  * Created by ayush on 3/1/17.
  */

object Start{
  val data_schema = "ge_test"
  val driver = "org.postgresql.Driver"
  val url = "jdbc:postgresql://127.0.0.1:5432/postgres"
  val username = "postgres"
  val password = "Ayush"

  case class KeyMetrics(t:Date,frequency:Int,duration:Long,distance:Long)

  //table
  val clm = """(time varchar(256),category varchar(256),
                    key varchar(256),frequency varchar(256),
                    duration varchar(256),distance varchar(256),
                    sysid varchar(128),ops_date_str varchar(128),customer_name varchar(256))"""
  val ins_clm = """time,category,key,frequency,duration,distance,sysid,ops_date_str,customer_name"""




  val pattern_bundle_1 = """.*\/\d{2}\-\w\w\w\-\d{4}\.\d{2}\.[\+\-]\d+\/([^\/]+).*""".r
  val pattern_bundle_2 = """.*\/\d{4}\-\w\w\w\-\d{2}\.\d{2}\.[\+\-]\d+\/([^\/]+).*""".r
  val pattern_bundle_3 = """.*\/permanent\/(.*)""".r
  val pattern_sysid = """SYS\s*\,\s*Serial\s*Number\s*\,\s*(\S+)""".r
  val pattern_ops = """LOG\,Date\/Time generated\,\"?\s*\S+\,\s*(\S+\s*\d+\,\s*\d+\s*\d+\:\d+\:\d+\s*\w\w)\"?""".r
  val pattern_data = """(\d{4}\s*\w+\s*\d{1,2}\s*\d{1,2}\:\d{1,2}\:\d{1,2}\.\d+)\,([^,]+)\,([^,]+)\,([^,]+)\,([^,]+)\,([^\n]+)""".r


  def main(args: Array[String]) {
    println("starting")
    var file_loc = new File("""/home/himanshu/mkd/12081215.log""");
    val P1 = FileSystems.getDefault().getPath(file_loc.getPath)
    val as = processToFile(P1)
    println(as)

  }
  
  def processToFile(p: Path): Path = {
    println("Logfile path is = " + p)
    println("Absolute path is = " + p.getParent)
    val out_file_name = p.getFileName.toString
    val file_out = p.getParent.toString + "/extensibility." + out_file_name
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
    val hospital = "ayush"

    //Finding SysCfg file in bundle
    var count_fo = 0
    
    //Reading SysCfg For Sysid and ops_date_str
    var sysid = "vasu"
    var ops_date_str = "2017-01-01 00:00:00"
    


      val file_out_c = p.getParent.toString + "/c." + out_file_name + ".csv"
      val file_out_km = p.getParent.toString + "/km." + out_file_name  + ".csv"
      val writer_c = new PrintWriter(new BufferedWriter(new FileWriter(file_out_c, false)))
      val writer_km = new PrintWriter(new BufferedWriter(new FileWriter(file_out_km, false)))
      change(writer_c,writer_km,p.toString)
      writer_c.close()
      writer_km.close()
      if(!sysid.isEmpty || sysid != null)
      {
        //load(sysid,ops_date_str,file_out_c,hospital)
        //load(sysid,ops_date_str,file_out_km,hospital)
      }
      val lines_c = Source.fromFile(file_out_c, "ISO-8859-1").getLines
      val lines_km = Source.fromFile(file_out_km, "ISO-8859-1").getLines
      writer.write("--KBSHADOW-C--\n")
      for (line <- lines_c) {
        writer.write(line+"\n")
      }
      writer.write("--KBSHADOW-KM--\n")
      for (line <- lines_km) {
        writer.write(line+"\n")
      }


    

    writer.close()
    FileSystems.getDefault.getPath(file_out)
  }
  
  
  
  
  
  

  def getRecursiveListOfFiles(dir: File): Array[File] = {
    val these = dir.listFiles
    these ++ these.filter(_.isDirectory).flatMap(getRecursiveListOfFiles)
  }

  /*---------------Load KBSHADOW-----------------------------
  def load(sysid:String,ops_date_str:String,p:String,hospital_a:String)={
    val pattern_name = """([^-\.]+).*""".r
    val pattern_name(hospital) = hospital_a
    val table_name = if(p.matches(""".*\/[cC][^\/]*\.csv$""")) "kbshadowc" else "kbshadowkm"
    val per_table =
      s"""CREATE TABLE IF NOT EXISTS ${data_schema}.${table_name}${clm}"""
    val tmp_table =
      s"""CREATE LOCAL TEMP TABLE temp_ge${clm} ON COMMIT preserve ROWS;"""

    val lines = Source.fromFile(p, "ISO-8859-1").getLines

    val upd_q = s"""update temp_ge set sysid = '${sysid}',ops_date_str = '${ops_date_str}',customer_name = '${hospital}'"""
    val chek_key =
      s"""${table_name}.sysid=temp_ge.sysid and
         ${table_name}.ops_date_str=temp_ge.ops_date_str and
         ${table_name}.customer_name=temp_ge.customer_name and
	 ${table_name}.time=temp_ge.time
       """
    val mergeQuery =
      s"""Insert into ${data_schema}.${table_name}($ins_clm)
         select $ins_clm from temp_ge where not exists
         (select * from ${data_schema}.${table_name} where $chek_key)"""

    var connection: Connection = null
    try{
      Class.forName("org.postgresql.Driver")
      connection = DriverManager.getConnection(url, username, password)
      connection.setAutoCommit(false)
      val statement = connection.createStatement()
      statement.executeUpdate(per_table)
      statement.executeUpdate(tmp_table)
      val ins_qu = """insert into temp_ge(time,category,key,frequency,duration,distance) values"""
      var s_qu_up = new scala.collection.mutable.ListBuffer[String]()
      for(line<-lines){
        line match{
          case pattern_data(time,category,key,frequency,duration,distance)=>
            s_qu_up += s"""('$time','$category','$key','$frequency','$duration','$distance'),"""
          case _ =>
        }
      }
      //println(ins_qu+s_qu_up.mkString.stripSuffix(","))
      statement.executeUpdate(ins_qu+s_qu_up.mkString.stripSuffix(","))
      statement.executeUpdate(upd_q)
      statement.executeUpdate(mergeQuery)
      statement.close()
      connection.commit()
      connection.close()
    }catch{
      case e:Exception=>
        println(e)
        connection.rollback()
    }
  }
*/
  //---------------Load KBSHADOW-----------------------------
  def load_vertica(sysid:String,ops_date_str:String,p:String,hospital_a:String)={
    val pattern_name = """([^-\.]+).*""".r
    val pattern_name(hospital) = hospital_a
    val table_name = if(p.matches(""".*\/[cC][^\/]*\.csv$""")) "kbshadowc" else "kbshadowkm"
    val per_table =
      s"""CREATE TABLE IF NOT EXISTS ${data_schema}.${table_name}${clm}"""
    val tmp_table =
      s"""CREATE LOCAL TEMP TABLE temp_ge${clm} ON COMMIT preserve ROWS;"""

    val qu = s"COPY temp_ge(time,category,key,frequency,duration,distance) FROM LOCAL \'" + p + "\' PARSER fcsvparser() NO COMMIT"

    val upd_q = s"""update temp_ge set sysid = '${sysid}',ops_date_str = '${ops_date_str}',customer_name = '${hospital}'"""
    val chek_key =
      s"""t.sysid=s.sysid and
          t.ops_date_str=s.ops_date_str and
          t.customer_name=s.customer_name and
	        t.time=s.time
       """
    val mergeQuery =
      s"""MERGE INTO ${data_schema}.${table_name} t
          USING temp_ge s
          ON $chek_key
          WHEN MATCHED THEN
          UPDATE SET sysid = s.sysid
          WHEN NOT MATCHED THEN
          ($ins_clm)
          VALUES
          ($ins_clm_source)"""

    var connection: Connection = null
    try{
      Class.forName("com.vertica.jdbc.Driver")
      connection = DriverManager.getConnection(url, username, password)
      connection.setAutoCommit(false)
      val statement = connection.createStatement()
      statement.executeUpdate(per_table)
      statement.executeUpdate(tmp_table)
      statement.executeUpdate(qu)
      statement.executeUpdate(upd_q)
      statement.executeUpdate(mergeQuery)
      statement.close()
      connection.commit()
      connection.close()
    }catch{
      case e:Exception=>
        println(e)
        connection.rollback()
    }
  }
  //---------------Change KBSHADOW-----------------------------------

  def change(writer_c: PrintWriter,writer_km: PrintWriter,path:String)={
    var keyCache=""
    var tCache =new Date()
    var frequency=0
    var duration:Long=0
    var map = new util.HashMap[String, KeyMetrics]()
    writer_c.write("Timestamp,Category,Key_Code,Frequency,Duration,Distance\n")
    val lines = Source.fromFile(path, "ISO-8859-1").getLines
    lines.next()
    try {
      for (line <- lines) {
        try {
          val line_part = line.split(" ")
	  if(line_part.length>5){
          val tstamp = line_part(0) + " " + line_part(1) + " " + line_part(2) + " " + line_part(3)
          val key = line_part(4) + " " + line_part(5)
          val t = new SimpleDateFormat("yyyy MMM dd hh:mm:ss.SSS").parse(tstamp)
          frequency += 1
          if (!keyCache.equals(key)) {
            if (map.get(key) == null) {
              map.put(key, KeyMetrics(t, frequency, duration, 0))
            }
            if (frequency > 1)
              duration = t.getTime - tCache.getTime
            else
              duration = 0
            if (!keyCache.equals("")) {
              writer_c.write(new SimpleDateFormat("yyyy MMM dd hh:mm:ss.SSS").format(tCache) + "," + keyCache.replaceFirst(" ", ",") + "," + frequency + "," + duration + ",0\n")
              val km = map.get(key)
              map.put(key, KeyMetrics(km.t, km.frequency + frequency, km.duration + duration, 0))
              duration = 0
              frequency = 0
            }
            keyCache = key
            tCache.setTime(t.getTime)
          }
	 }
        }catch {
          case e:Exception=>
            println("Exception ->"+e.getMessage)
        }
      }//end of for
      val iter = map.entrySet().iterator()
      writer_km.write("Timestamp,Category,Key_Code,Frequency,Duration,Distance\n")
      while(iter.hasNext){
        val e = iter.next
        val key = e.getKey
        val km = e.getValue
        writer_km.write(new SimpleDateFormat("yyyy MMM dd hh:mm:ss.SSS").format(km.t) + "," + key.replaceFirst(" ",",")  + "," + km.frequency + "," + km.duration + "," + km.distance + "\n")

      }

    }catch{
      case e:Exception =>
        println("Exception ->"+e.getMessage)
    }
  }

}