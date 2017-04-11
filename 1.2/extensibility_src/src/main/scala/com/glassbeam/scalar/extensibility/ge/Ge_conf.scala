package com.glassbeam.scalar.extensibility.ge

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._

import scala.io.Source
import scala.collection.mutable.ListBuffer
import scala.util.matching._


class Ge_conf extends Parsable {

  val pattern_version = """SYS\s*\,\s*Overall\s*SW\s*version\s*\,\s*R6.*""".r
  val pattern_sPatient = """^\d{4}\/\d{2}\/\d{2}\s*\d{1,2}:\d{1,2}:\d{1,2}\.\d+.*Starting\s*Patient.*""".r
  val pattern_nExam = """^\d{4}\/\d{2}\/\d{2}\s*\d{1,2}:\d{1,2}:\d{1,2}\.\d+.*new\s*exam[^A-Za-z0-1].*""".r
  val pattern_ePatient = """^\d{4}\/\d{2}\/\d{2}\s*\d{1,2}:\d{1,2}:\d{1,2}\.\d+.*Ending\s*Patient.*""".r

  val pattern_1 = """^\d{4}\/\d{2}\/\d{2}\s*\d{1,2}:\d{1,2}:\d{1,2}(?:\.\d+)?\s*\;?\s*\S+\s*\;?.*RunState.*Modes.*Probe.*Appl.*""".r
  val pattern_2 = """^\d{4}\/\d{2}\/\d{2}\s*\d{1,2}:\d{1,2}:\d{1,2}.*?\sBuffer\:\:UpdateCompletedImageWrite.*""".r

  val pattern_actscan =
    """.*Activating\s*package\:\s*Scanner.*$""".r
  //Activating package: Scanner
  val pattern_actvgu =
    """.*Activating\s*package\:\s*VGU.*$""".r
  //Activating package: VGU
  val pattern_acttd =
    """.*Activating\s*package\:\s*3D.*$""".r
  //Activating package: 3D
  val pattern_actcarm =
    """.*Activate\s*comment\s*and\s*arrow\s*mode.*$""".r
  //Activate comment and arrow mode
  val pattern_actmesm =
    """.*Activate\s*Measurements\s*mode.*$""".r
  //Activate Measurements mode
  val pattern_actcam =
    """.*Activate\s*Compare\s*Assistant\s*mode.*$""".r
  //Activate Compare Assistant mode
  val pattern_scnast =
    """.*Launching\s*Exam\s*Protocol\.\.\.\s*Protocol\s*Selected\:.*""".r
  //++Launching Exam Protocol... Protocol Selected
  val pattern_image =
    """.*CImageHandler::HandleStoreImage.*""".r
  //create image
  val pattern_time =
    """^(\d{4}\/\d{2}\/\d{2}\s*\d{1,2}:\d{1,2}:\d{1,2}\.\d+)\s*.*""".r
  val pattern_runmode =
    """.*RunMode\=Live.*""".r
  //runmode image handler
  val pattern_lstill =
    """.*m_bLiveStoreSingleFrame\s*[tTrue].*""".r
  //for lstill for image handler


  def processToFile(p: Path): Path = {

    println("Logfile path is = " + p)
    println("Absolute path is = " + p.getParent)
    val out_file_name = p.getFileName.toString
    val file_out = p.getParent.toString + "/extensibility." + out_file_name

    var version = 0
    val bundle_path = p.getParent.toString

    var count_fo = 0
    val file_b = new File(bundle_path)
    var folder_search = file_b.toString
    var file_ary = getRecursiveListOfFiles(file_b).filter(_.isFile).filter(_.getName == "SysCfg.csv")
    while (file_ary.length < 1 && !folder_search.matches(""".*\/\d{2}\-\w\w\w\-\d{4}\.\d{2}\.[\+\-]\d+\/[^\/]+\/?$""") &&
      count_fo < 7) {
      count_fo = count_fo + 1
      val pattern_path = """(.*\/).+""".r
      val pattern_path(bundle_path_1) = folder_search
      folder_search = bundle_path_1
      val file_b1 = new File(bundle_path_1)
      file_ary = getRecursiveListOfFiles(file_b1).filter(_.isFile).filter(_.getName == "SysCfg.csv")
    }


    if(file_ary.length>0) {
      var sys_cfg = Source.fromFile(file_ary(0).toString).getLines

      try {
        val ver_it = sys_cfg.filter(_.matches(pattern_version.toString))
        version = if (ver_it.isEmpty) 0 else 6
      } catch {
        case e: Exception =>
          sys_cfg = Source.fromFile(file_ary(0), "ISO-8859-1").getLines
          val ver_it = sys_cfg.filter(_.matches(pattern_version.toString))
          version = if (ver_it.isEmpty) 0 else 6
      } //catch

      var lines = Source.fromFile(p.toString).getLines()
      try {
        val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
        val lines_2 = Source.fromFile(p.toString).getLines()
        if (lines_2.exists(_.matches(pattern_nExam.toString)) || lines_2.exists(_.matches(pattern_sPatient.toString)))
          if (version != 6)
            gFileEdit(writer, lines)
          else gFileEdit_six(writer, lines)
        writer.close()
      } catch {
        case e: Exception =>
          val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
          lines = Source.fromFile(p.toString, "ISO-8859-1").getLines()
          val lines_2 = Source.fromFile(p.toString, "ISO-8859-1").getLines()
          if (lines_2.exists(_.matches(pattern_nExam.toString)) || lines_2.exists(_.matches(pattern_sPatient.toString)))
            if (version != 6)
              gFileEdit(writer, lines)
            else gFileEdit_six(writer, lines)
          writer.close()
      }
    }else{
      val writer = new PrintWriter(new BufferedWriter(new FileWriter(file_out, false)))
      writer.write("SysCfg.csv Not Found")
      writer.close()
    }

    FileSystems.getDefault.getPath(file_out)
  } //end of process to file

  //-----------------------------------------------------

  def processFileToContext(p: Path): String = {
    "success"
  }

  def processBundleToContext(p: Path): String = {
    "success"
  }

  def getRecursiveListOfFiles(dir: File): Array[File] = {
    val these = dir.listFiles
    these ++ these.filter(_.isDirectory).flatMap(getRecursiveListOfFiles)
  }

  //-----------------------------------------------------

  def gFileEdit(writer: PrintWriter, lines: Iterator[String]) = {
    var lst = new ListBuffer[String]()
    var rmpa_lst = new ListBuffer[Int]()
    def write(ln: String) = writer.write(ln + "\n")
    var img_idx = 0
    var flag_img = false
    var flag_img_ramp = false
    var flag_rmpa = false
    var flag_start = false
    var flag_scanpack = false
    for (line <- lines) {
      /*if(flag_rmpa){
        lst+=line+"--"
        lst+="==End Session Child one=="
        flag_rmpa =false
      }*/
      if(flag_start || line.matches(pattern_nExam.toString))
        line match {
          case pattern_nExam() =>
            lst += "==End Parent Session=="
            if(lst.nonEmpty)lst.foreach(write(_))
            lst.clear()
            lst += "==NEW_PARENT_SESSION==" + line
            lst += line
            flag_start =true
            flag_scanpack = false
            flag_img_ramp = false
            flag_rmpa = false
            rmpa_lst.clear()

          case pattern_2() =>
            lst += line
            lst += "==End Session Child Two=="


          case pattern_actscan() =>
            lst += line
            lst += "==End Session Child Scanner=="
            flag_scanpack = true


          case pattern_actvgu() =>
            lst += line
            lst += "==End Session Child VGU=="


          case pattern_acttd() =>
            lst += line
            lst += "==End Session Child 3D=="


          case pattern_actcarm() =>
            lst += line
            lst += "==End Session Child comment and arrow mode=="


          case pattern_actmesm() =>
            lst += line
            lst += "==End Session Child Measurements mode=="


          case pattern_actcam() =>
            lst += line
            lst += "==End Session Child Compare Assistant mode=="


          case pattern_scnast() =>
            lst += line
            lst += "==End Session Child scan assist=="


          case pattern_image() =>
            lst += line
            flag_img = true
            flag_img_ramp = true
            img_idx = lst.indexOf(line)
            if(rmpa_lst.nonEmpty)
            {
              rmpa_lst.foreach(x=>lst(x) += "Image = true")
              rmpa_lst.clear()
            }

          case pattern_lstill() =>
            lst += line
            if(lst.indexOf(line)-img_idx < 31 && lst(img_idx).matches(pattern_runmode.toString))
            {
              lst(img_idx) +=  "  RunMode=LiveStill"
              img_idx = 0
            }


          case pattern_1()=>
            lst += line
	    lst += "==End Session One=="
            if(flag_scanpack && flag_img_ramp) {
              flag_rmpa = true
              lst(lst.indexOf(line)) += "Image = true"
            }else if(flag_scanpack)
            {
              flag_rmpa = true
              rmpa_lst += lst.indexOf(line)
            }


          case _ =>
            if(flag_img){
              lst+="==End of image=="
              lst += line
              lst(lst.indexOf(line)-2) += line
              flag_img =false
            }else lst += line


        }//line match

    }//for
    lst += "==End Parent Session=="
    if(lst.nonEmpty)lst.foreach(write(_))
  }//function

  def gFileEdit_six(writer: PrintWriter, lines: Iterator[String]) = {
    var lst = new ListBuffer[String]()
    var rmpa_lst = new ListBuffer[Int]()
    def write(ln: String) = writer.write(ln + "\n")
    var flag_img_ramp = false
    var img_idx = 0
    var flag_img = false
    var rmpa_idx = 0
    var flag_rmpa = false
    var flag_start = false
    var flag_scanpack = false
    for (line <- lines) {
      /*if(flag_rmpa){
        lst += line+"--"
        lst+="==End Session Child one=="
        flag_rmpa = false
      }*/
      if(flag_start || line.matches(pattern_sPatient.toString))
        line match {
          case pattern_sPatient() =>
            lst += "==End Parent Session=="
            if(lst.nonEmpty)lst.foreach(write(_))
            lst.clear()
            lst += "==NEW_PARENT_SESSION==" + line
            lst += line
            flag_start = true
            flag_rmpa = false
            rmpa_lst.clear()

          case pattern_ePatient()=>
            lst +=line
            flag_scanpack = false
            flag_start = false
            rmpa_idx = 0
            flag_img_ramp = false

          case pattern_2() =>

            lst += line
            lst += "==End Session Child Two=="

          case pattern_actscan() =>
            lst += line
            lst += "==End Session Child Scanner=="
            flag_scanpack = true

          case pattern_actvgu() =>
            lst += line
            lst += "==End Session Child VGU=="

          case pattern_acttd() =>
            lst += line
            lst += "==End Session Child 3D=="

          case pattern_actcarm() =>
            lst += line
            lst += "==End Session Child comment and arrow mode=="

          case pattern_actmesm() =>
            lst += line
            lst += "==End Session Child Measurements mode=="

          case pattern_actcam() =>
            lst += line
            lst += "==End Session Child Compare Assistant mode=="

          case pattern_scnast() =>
            lst += line
            lst += "==End Session Child scan assist=="

          case pattern_image() =>
            lst += line
            flag_img = true
            flag_img_ramp = true
            img_idx = lst.indexOf(line)
            if(rmpa_lst.nonEmpty)
            {
              rmpa_lst.foreach(x=>lst(x) += "Image = true")
              rmpa_lst.clear()
            }
          case pattern_lstill() =>
            lst += line
            if(lst.indexOf(line)-img_idx < 31 && lst(img_idx).matches(pattern_runmode.toString))
            {
              lst(img_idx) +=  "RunMode=LiveStill"
              img_idx = 0
            }

          case pattern_1()=>
            lst += line
	    lst += "==End Session One=="
            if(flag_scanpack && flag_img_ramp) {
              flag_rmpa = true
              lst(lst.indexOf(line)) += "Image = true"
            }else if(flag_scanpack)
            {
              flag_rmpa = false
              rmpa_lst += lst.indexOf(line)
            }

          case _ =>
            if(flag_img){
              lst+="==End of image=="
              lst += line
              lst(lst.indexOf(line)-2) += line
              flag_img =false
            }else lst += line

        }//line match

    }//for loop
    lst += "==End Parent Session=="
    if(lst.nonEmpty)lst.foreach(write(_))
  }//function
	def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
	}


}

