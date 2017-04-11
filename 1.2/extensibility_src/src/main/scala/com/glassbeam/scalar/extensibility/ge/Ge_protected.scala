package com.glassbeam.scalar.extensibility.ge

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._

import scala.io.Source
import scala.collection.mutable.ListBuffer
import scala.util.matching.{Regex, _}
import com.glassbeam.extensibility.traits.Parsable

/**
  * Created by ayush on 16/8/16.
  */
class Ge_protected extends Parsable{
  val pattern_login = """^.*Login\s*as.*""".r
  val pattern_time = """^(\d{4}\/\d{2}\/\d{2}\s*\d{1,2}:\d{1,2}:\d{1,2}\.\d+).*Login.*""".r
  override def processToFile(p: Path): Path = {
    println("Logfile path is = " + p)
    println("Absolute path is = " + p.getParent)
    val out_file_name = p.getFileName.toString
    val file_out = p.getParent.toString + "/extensibility.op." + out_file_name

    try {
      val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
      val lines = Source.fromFile(p.toString).getLines()
      for(line<-lines) {
        line match {
          case pattern_login() =>
            val pattern_time(sess_ts) = line
            writer.write(sess_ts + " --Log Of--\n")
            writer.write(line + "\n")
          case _ =>
            writer.write(line + "\n")
        }
      }
      writer.close()
    } catch {
      case e: Exception =>
        val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
        val lines = Source.fromFile(p.toString, "ISO-8859-1").getLines()
        for (line <- lines) {
          line match {
            case pattern_login() =>
              val pattern_time(sess_ts) = line
              writer.write(sess_ts + " --Log Of--\n")
              writer.write(line + "\n")
            case _ =>
              writer.write(line + "\n")
          }
        }
        writer.close()
      }//catch


    FileSystems.getDefault.getPath(file_out)

  }

  override def processFileToContext(p: Path): String = {
    "success"
  }

  override def processBundleToContext(p: Path): String = {
    "success"
  }
  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
  }

}
