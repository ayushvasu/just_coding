package com.glassbeam.scalar.extensibility.vytron
import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source
import java.io._
import scala.util.control._
import scala.collection.mutable._
class Vytron_rconf extends Parsable {


  def processToFile(p: Path): Path = {
  println("Logfile path is = "+ p)
  println("Absolute path is = "+ p.getParent)
  var out_file_name=p.getFileName().toString()



  var infile=Source.fromFile(p.toString())
  var file_out = p.getParent.toString+"/extensibility."+out_file_name+".log"
  //var fileouttemp = p.getParent.toString+"tempfile"+".log"
  //val workf="""\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+[^\(]*\(WORKFLOW\)\s*(CATHETER_TENSION|CATHETER_RELAX|START_SCAN_ONLY|START_SCAN|STOP_SCAN)\s*\,.*""".r
  val pattrn="""(\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+).*""".r
  var temp1=""
  var temp2=""
  var temp3=""
  var flag:Int=0;
  var flag2:Int=0;
  var first:Int=0
  //val writer = new PrintWriter(new BufferedWriter((new FileWriter(fileouttemp,false))))
  val writer2=new PrintWriter(new BufferedWriter((new FileWriter(file_out,false))))
  var lst:ListBuffer[String]=ListBuffer()
  var lines=infile.getLines()
  var flg=0
  var flg2=0
  var ll=""

  if(out_file_name.contains("xlog")){
    for (line <- lines) {
      ll=line
      if ((line.contains("START_SCAN, User started scan with roll angle offset") || line.contains("START_SCAN_ONLY, User started scan only")) && flg2 == 0) {
        flg2 = 1
        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        writer2.write(temp + "===START_SCAN===" + "\n")
        writer2.write(line + "\n")
      }else if((line.contains("START_SCAN, User started scan with roll angle offset") || line.contains("START_SCAN_ONLY, User started scan only")) && flg2 == 1){
        flg2 = 1
        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        writer2.write(temp + " "+"END_OF_STARTSCAN: [0, 0, 0] (END_OF_STARTSCAN) end_of_start_scan,Session Incomplete"+"\n")
        writer2.write(temp + "===END_OF_STARTSCAN===" + "\n")
        writer2.write(temp + "===START_SCAN===" + "\n")
        writer2.write(line + "\n")
      }else if(line.contains("STOP_SCAN, User stopped scan")){
        flg2 = 0
        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        writer2.write(line + "\n")
        writer2.write(temp + "===END_OF_STARTSCAN===" + "\n")

      }else if (line.contains("START_SESSION") && flg == 0) {
        flg = 1
        flg2= 0
        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        writer2.write(line + "\n")
        writer2.write(temp + "===PREPARATION===" + "\n")
        writer2.write(temp + " "+"PREPARATIONTEMP: [0, 0, 0] (PREPARATION_TEMP) start_session,User Session Started"+"\n")
      } else if (line.contains("START_SESSION") && flg == 1) {
        flg = 1

        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        if(flg2 != 0){
          writer2.write(temp + " "+"END_OF_STARTSCAN: [0, 0, 0] (END_OF_STARTSCAN) end_of_start_scan,Session Incomplete"+"\n")
        }
        writer2.write(temp + " "+"END_OF_CHILDSESSION: [0, 0, 0] (END_OF_CHILDSESSION) end_of_child_session,Session Incomplete"+"\n")
        writer2.write("===ENDOFCHILDNAMESPACE==="+"\n")
        writer2.write(temp + " "+"END_OF_SESSION: [0, 0, 0] (END_OF_SESSION) end_of_session,Session Incomplete"+"\n")
        writer2.write("===ENDOFNAMESPACE==="+ "\n")
        writer2.write(line + "\n")
        writer2.write(temp + "===PREPARATION===" + "\n")
        writer2.write(temp + " "+"PREPARATIONTEMP: [0, 0, 0] (PREPARATION_TEMP) start_session,User Session Started"+"\n")
        flg2=0
      } else if (line.contains("END_SESSION")) {
        flg = 0
        var temp="""\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        if(flg2 != 0){
          writer2.write(temp + " "+"END_OF_STARTSCAN: [0, 0, 0] (END_OF_STARTSCAN) end_of_start_scan,Session Incomplete"+"\n")
        }
        flg2=0
        writer2.write(line + "\n")
        writer2.write(temp + " "+"END_OF_CHILDSESSION: [0, 0, 0] (END_OF_CHILDSESSION) end_of_child_session,Session complete"+"\n")
        writer2.write("===ENDOFCHILDNAMESPACE==="+"\n")
        writer2.write(temp + " "+"END_OF_SESSION: [0, 0, 0] (END_OF_SESSION) end_of_session,Session Complete"+"\n")
        writer2.write("===ENDOFNAMESPACE==="+ "\n")

      } else if (line.contains("THERAPY_REQUESTED, Entering Therapy Mode")) {
        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        if(flg2 != 0){
          writer2.write(temp + " "+"END_OF_STARTSCAN: [0, 0, 0] (END_OF_STARTSCAN) end_of_start_scan,Session Incomplete"+"\n")
        }
        writer2.write(temp + " "+"END_OF_CHILDSESSION: [0, 0, 0] (END_OF_CHILDSESSION) end_of_child_session,Session Done"+"\n")
        writer2.write(temp + "===THERAPY===" + "\n")
        writer2.write(line + "\n")
        flg2 = 0

      } else if (line.contains("MAPPING_REQUESTED, Entering Map Mode")) {
        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        if(flg2 != 0){
          writer2.write(temp + " "+"END_OF_STARTSCAN: [0, 0, 0] (END_OF_STARTSCAN) end_of_start_scan,Session Incomplete"+"\n")
        }
        writer2.write(temp + " "+"END_OF_CHILDSESSION: [0, 0, 0] (END_OF_CHILDSESSION) end_of_child_session,Session Done"+"\n")
        writer2.write(temp + "===MAPPING===" + "\n")
        writer2.write(line + "\n")
        flg2=0

      } else if (line.contains("PRETHERAPY_REQUESTED, Entering PreTherapy Mode")) {
        var temp = """\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(line).mkString
        if(flg2 != 0){
          writer2.write(temp + " "+"END_OF_STARTSCAN: [0, 0, 0] (END_OF_STARTSCAN) end_of_start_scan,Session Incomplete"+"\n")
        }
        writer2.write(temp + " "+"END_OF_CHILDSESSION: [0, 0, 0] (END_OF_CHILDSESSION) end_of_child_session,Session Done"+"\n")
        writer2.write(temp + "===PRETHERAPY===" + "\n")
        writer2.write(line + "\n")
        flg2=0
      }

      else {
        writer2.write(line + "\n")
      }
    }

    if(flg==1){

      var temp="""\w+\s*\d+\s*\d+\:\d+\:\d+\.\d+""".r.findFirstIn(ll).mkString
      if(flg2==1){
        writer2.write(temp + " "+"END_OF_STARTSCAN: [0, 0, 0] (END_OF_STARTSCAN) end_of_start_scan,Session complete"+"\n")
        writer2.write(temp + "===END_OF_STARTSCAN===" + "\n")
      }
      writer2.write(temp + " "+"END_OF_CHILDSESSION: [0, 0, 0] (END_OF_CHILDSESSION) end_of_child_session,Session Incomplete"+"\n")
      writer2.write("===ENDOFCHILDNAMESPACE==="+"\n")
      writer2.write(temp + " "+"END_OF_SESSION: [0, 0, 0] (END_OF_SESSION) end_of_session,Session Incomplete"+"\n")
      writer2.write("===ENDOFNAMESPACE==="+ "\n")
    }
  }
  else{
    var pnss=false
    var pnse=false
    var lastline=""
    println("inside extensibility")
    for (line <- lines) {
      lastline=line
      if(line.contains("Entering transition OperatingHSM::T_StartNewSession")){
       writer2.write(line+ "\n")
        pnss=true
      }
      else if(line.contains("Entering transition OperatingHSM::T_EndSession")){
        val temp="""^\[\d+\/\d+\/\d+\s\d+\:\d+\:\d+\.\d+]""".r.findFirstIn(line).mkString
        writer2.write(line + "\n")
        writer2.write(temp + "===Exiting transition OperatingHSM===" + "\n")
        pnss=false
      }
      else {
        writer2.write(line + "\n")
      }
    }

    if(pnss==true){
      val temp="""^\[\d+\/\d+\/\d+\s\d+\:\d+\:\d+\.\d+]""".r.findFirstIn(lastline).mkString
      writer2.write(temp + "===Exiting transition OperatingHSM===" + "\n")
    }
  }

  writer2.close


  return FileSystems.getDefault().getPath(file_out)
  }
  def processFileToContext(p: Path): String ={
		return "success"
  }
  def processBundleToContext(p: Path): String ={
		return "success"
  }                                                 //> main: (args: Array[String])Unit
def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
	}

}
