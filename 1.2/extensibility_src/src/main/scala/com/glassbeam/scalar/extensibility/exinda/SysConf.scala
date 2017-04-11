package com.glassbeam.scalar.extensibility.exinda

import java.io.{BufferedWriter, FileWriter, PrintWriter}
import java.nio.file.{FileSystems, Path}

import com.glassbeam.extensibility.traits.Parsable

/**
  * Created by ayush on 1/11/16.
  */
class SysConf extends Parsable {

  val sec_pattern = """System information""".r

  val hname_pattern = """^Hostname:.*""".r

  val version_pattern = """^Version:.*""".r

  val hsid_pattern = """^Host ID:.*""".r

  val platfrm_pattern = """^Platform:.*""".r

  val ram_pattern = """^RAM:.*""".r

  val serial_pattern = """^Serial:.*""".r

  val ctime_pattern = """^Current\s*time:.*""".r

  val sysutm_pattern = """^System\s*uptime:.*""".r

  val prorelse_pattern = """^Product\s*release:.*""".r

  val promodel_pattern = """^Product\s*model:.*""".r

  val licensedmodel_pattern = """^Licensed\s*model:.*""".r

  val buildsrc_pattern = """^Build\s*source:.*""".r

  val date_pattern = """^Date:.*""".r

  val uptime_pattern = """^Uptime:.*""".r

  val cmd_line_pattern = """^\s*\S+\s+\d+\s+\d+\s+\d+\s+\d+\s+\S+\s+\d+\s+\S+\s+\d+\s+\d+\s+\S+\s+\S+\s+\S+\s+\S+\s*\d*\s+\S+\s+.+""".r

  override def processToFile(p: Path): Path = {
    val file_out = p.getParent.toString + "/extensibility.part1.txt"
    val file_name = p.getFileName
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
    val lines = scala.io.Source.fromFile(p.toString).getLines()
    writer.write("System Information:\n")
    var hsid_flag = false
    var platfrm_flag = false
    var ram_flag = false
    var serial_flag = false
    var ctime_flag = false
    var sysutm_flag = false
    var prorelse_flag = false
    var promodel_flag = false
    var licensedmodel_flag = false
    var buildsrc_flag = false
    var uptime_flag = false
    for (line <- lines) {
      line match {
        case hname_pattern() =>
          writer.write(line + "\n")

        case version_pattern() =>
          writer.write(line + "\n")
 
       case hsid_pattern() =>
          if (!hsid_flag)
            writer.write(line + "\n")
          hsid_flag = true
  
      case platfrm_pattern() =>
	if (!platfrm_flag)
          writer.write(line + "\n")
	platfrm_flag = true

        case ram_pattern() =>
        if (!ram_flag)
          writer.write(line + "\n")
	ram_flag = true

        case serial_pattern() =>
        if (!serial_flag)
          writer.write(line + "\n")
	serial_flag = true

        case sysutm_pattern() =>
        if (!sysutm_flag)
          writer.write(line + "\n")
	sysutm_flag = true
 
       case prorelse_pattern() =>
        if (!prorelse_flag)
          writer.write(line + "\n")
	prorelse_flag = true

        case promodel_pattern() =>
        if (!promodel_flag)
          writer.write(line + "\n")
	promodel_flag = true

        case licensedmodel_pattern() =>
        if (!licensedmodel_flag)
          writer.write(line + "\n")
	licensedmodel_flag = true

        case buildsrc_pattern() =>
        if (!buildsrc_flag)
          writer.write(line + "\n")
	buildsrc_flag = true

        case uptime_pattern() =>
        if (!uptime_flag)
          writer.write(line + "\n")
	uptime_flag = true
        case _ =>
      }

    }
    /*writer.write("\n\n\n\n\n")
    //writer.write("""Output of 'ps -AwwL -o user,pid,lwp,ppid,nlwp,pcpu,pri,nice,vsize,rss,tty,stat,wchan:12,start,bsdtime,command':""")
    val command_pattern = """Output of \'ps -AwwL -o user,pid,lwp,ppid,nlwp,pcpu,pri,nice,vsize,rss,tty,stat,wchan\:12,start,bsdtime,command\'\s*\:""".r
    var cmd_flag = false
    val lines_ps = scala.io.Source.fromFile(p.toString).getLines()
    for (line <- lines_ps) {
      if (line.matches(command_pattern.toString)) {
		cmd_flag = true
	 writer.write("""Output of 'ps -AwwL -o user,pid,lwp,ppid,nlwp,pcpu,pri,nice,vsize,rss,tty,stat,wchan:12,start,bsdtime,command':""")
	 writer.write("\n")
    }
      if (cmd_flag)
        line match {
          case cmd_line_pattern() =>
            writer.write(line+"\n")
          case _=> 
        }
      if(cmd_flag && line.matches("""^[=]+$""")) {
	cmd_flag = false
	}

    }*/
    writer.close()
    FileSystems.getDefault.getPath(file_out)
  }

  override def processFileToContext(p: Path): String = "success"

  override def processBundleToContext(p: Path): String = "success"

  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
        }

}



