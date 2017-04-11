package com.glassbeam.scalar.extensibility.vytron

import com.glassbeam.extensibility.traits._
import java.nio.file._
import scala.io.Source
import java.io._

class Vytron_xconf extends Parsable {


  def processToFile(p: Path): Path = {
    println("Logfile path is = " + p)
    println("Absolute path is = " + p.getParent)
    var out_file_name = p.getFileName.toString
    var file_out = p.getParent.toString + "/extensibility.sysid.log"
    val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out, true))))
    var infile = Source.fromFile(p.toString)
    var lines = infile.getLines().toList
    var sysid_pattern = """^[^)]*\)\s*Hostname\s+is\s*(\S+)""".r
    if (lines.exists(_.matches(sysid_pattern.toString)))
      for (line <- lines) {
        if (line.matches(sysid_pattern.toString())) {
          writer.write("\n" + line)
        }
      }
    writer.close
    return FileSystems.getDefault().getPath(file_out)
  }

  def processFileToContext(p: Path): String = {
    return "success"
  }

  def processBundleToContext(p: Path): String = {
    return "success"
  }

  //> main: (args: Array[String])Unit
  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit = {
  }

}
