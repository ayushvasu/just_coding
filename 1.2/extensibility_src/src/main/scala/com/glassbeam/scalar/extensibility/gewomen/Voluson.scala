package com.glassbeam.scalar.extensibility.gewomen

import java.io.{BufferedWriter, FileWriter, PrintWriter}
import java.nio.file.{FileSystems, Path}

import com.glassbeam.extensibility.traits.Parsable

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by ayush on 9/12/16.
  */
class Voluson extends Parsable {
  val pattern_sPatient = """.*OnProcessEvent.*OCB_BTN_START_EXAM_ACTIONS.*""".r
  val pattern_nExam = """.*OnProcessEvent.*hk_endexam.*""".r
  val pattern_probe = """.+Events\s*Probe.+connector.+""".r
  val pattern_mode = """.+Mode(?:\[\d*\])?\=\s*\S+\s*$""".r
  val pattern_linebrake = """^(\d+\-\d+\.\d+).+""".r

  val pattern_stat_temp = """.*TemperatureRegulation.cpp.*""".r
  val patterm_stat_memory = """.*Graph\s*available\s*memory.*""".r


  override def processToFile(p: Path): Path = {
    println("Logfile path is = " + p)
    println("Absolute path is = " + p.getParent)
    val out_file_name = p.getFileName.toString
    val file_out = p.getParent.toString + "/extensibility." + out_file_name
    var lines = Source.fromFile(p.toString).getLines()
    try {
      val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
      val lines2 = Source.fromFile(p.toString, "ISO-8859-1").getLines()
      if (lines2.exists(_.matches(pattern_nExam.toString)) || lines2.exists(_.matches(pattern_sPatient.toString)))
        gFileEdit(writer, lines)
      else
        writer.write("No Session")
      writer.close()
    } catch {
      case e: Exception =>
        val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
        lines = Source.fromFile(p.toString, "ISO-8859-1").getLines()
        val lines2 = Source.fromFile(p.toString, "ISO-8859-1").getLines()
        if (lines2.exists(_.matches(pattern_nExam.toString)) || lines2.exists(_.matches(pattern_sPatient.toString)))
          gFileEdit(writer, lines)
        else
          writer.write("No Session")
        writer.close()
    }

    FileSystems.getDefault.getPath(file_out)
  }

  //---------------------------Functions------------------------------------------------------------
  override def processFileToContext(p: Path): String = "success"

  override def processBundleToContext(p: Path): String = "success"
  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
	}

  def gFileEdit(writer: PrintWriter, lines: Iterator[String]) = {
    var temprature_list = new ListBuffer[String]()
    var memory_lst = new ListBuffer[String]()
    def write(ln: String) = writer.write(ln + "\n")
    var flag_start = false
    var session_first_probe = true
    var session_first_mode = true
    var probe_still = false
    var mode_still = false
    var exam_id = ""
    var evt_time = ""
    for (line <- lines) {
      if ((flag_start || line.matches(pattern_sPatient.toString)) && line.matches(pattern_linebrake.toString)) {
        val pattern_linebrake(time_p) = line
        evt_time = time_p
        line match {
          case pattern_sPatient() =>
            exam_id = time_p
            flag_start = true
            writer.write("-------------------New Exam Started with Next Line-----------------\n")
            writer.write(line + "\n")
            session_first_probe = true
            session_first_mode = true

          case pattern_nExam() =>
            if (mode_still) writer.write(time_p + "--------------End Of Mode\n")
            mode_still = false
            if (probe_still) writer.write(time_p + "--------------End Of Probe\n")
            probe_still = false
            writer.write(line)
            writer.write("___________________Exam Ends________________________\n")
            flag_start = false
            writer.write(s"----------------------Exam Stats $exam_id --------------------------\n")
            writer.write("----Temprature----\n")
            if(temprature_list.nonEmpty)temprature_list.foreach(write(_))
            temprature_list.clear()
            writer.write("----Memory----\n")
            if(memory_lst.nonEmpty)memory_lst.foreach(write(_))
            memory_lst.clear()
            writer.write("----------------------End Stats---------------------------\n")




          case pattern_probe() =>
            if (mode_still) writer.write(time_p + "--------------End Of Mode\n")
            mode_still = false
            probe_still = true
            if (session_first_probe) {
              writer.write(line + "\n")
              session_first_probe = false
            } else {
              writer.write(time_p + "--------------End Of Probe\n")
              writer.write(line + "\n")
            }
          case pattern_stat_temp() =>
            writer.write(line+"\n")
            temprature_list += line

          case patterm_stat_memory() =>
            writer.write(line+"\n")
            memory_lst += line


          case pattern_mode() =>
            if (probe_still) {
              mode_still = true
              if (session_first_mode) {
                writer.write(line + "\n")
                session_first_mode = false
              } else {
                writer.write(time_p + "--------------End Of Mode\n")
                writer.write(line + "\n")
              }
            }

          case _ => writer.write(line + "\n")
        }
      } //if
    } //for
    if(temprature_list.nonEmpty || memory_lst.nonEmpty){
      if (mode_still) writer.write(evt_time + "--------------End Of Mode\n")

      if (probe_still) writer.write(evt_time + "--------------End Of Probe\n")
      writer.write(s"----------------------Exam Stats $exam_id --------------------------\n")
      writer.write("----Temprature----\n")
      if(temprature_list.nonEmpty)temprature_list.foreach(write(_))
      temprature_list.clear()
      writer.write("----Memory----\n")
      if(memory_lst.nonEmpty)memory_lst.foreach(write(_))
      memory_lst.clear()
      writer.write("----------------------End Stats---------------------------\n")
    }
  }


}

