package com.glassbeam.scalar.extensibility.ibm

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source
import java.text.SimpleDateFormat
import java.util.Date
import util.control.Breaks._

class LatestBundle extends Parsable {

  def processToFile(p: Path): Path = {
	var latestFile=""
    def deleteOld(p:Path)={


val t=p.getParent
      //> t  : java.nio.file.Path = /home/ugan/ibm
  //var tempFile = new java.io.File(P1.toString).listFiles
//println(tempFile)

val tempFile=new java.io.File(t.toString).listFiles.filter(f => """.*collectedCodeLevels\.All\.\w+\-\w+\.htm$""".r.findFirstIn(f.getName).isDefined)

println(tempFile.last)
//var fil=new File(P1.toString)
latestFile=tempFile(0).toString
val temp2Compare=tempFile(0).lastModified()
for(temp<-tempFile)
{
if(temp.lastModified()>temp2Compare)
  { latestFile=temp.toString
  // println(latestFile+"Hello")
   }
      
   
}
 //var tk=tempFile.filter { x=> x.toString!=latestFile }

/*for(t<-tk)
{
t.delete()
}*/
}
deleteOld(p)

val pathToRename:Path=Paths.get(latestFile)
val pathRname =pathToRename.getFileName.toString
    println("File Path = " + latestFile)
    val simpleDateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy/mm/dd-HH:mm")
    var max_epoch: Long = 0
    var max_bundle = ""
    var curBV = ""
    var finalBV = ""
    val bundleVMRF = """.*Bundle\s+VMRF\:\s+(\S+)\s+.*""".r
    val bundleVRMF = """.*Date:\s+(\d+\/\d+\/\d+\-\d+\:\d+).*Bundle\s+VRMF\:\s+(\d+\.\d+\.\d+\.\d+).*""".r
   //val date_regex = """.*Date\:\s+(\d+\/\d+\/\d+\-\d+:\d+).*""".r
		//MTMS\:\s+\S+\s+Date\:\s+(\d+\/\d+\/\d+\-\d+:\d+).*""".r
    for (line <- Source.fromFile(latestFile).getLines()) {
      line match {
        case bundleVRMF(bundle_date, bundle_version) =>
          //println("Line Matched = " + line)
			  val dt: Date = simpleDateFormat.parse(bundle_date)
			  val epoch: Long = dt.getTime
			  if (epoch > max_epoch) {
			    max_epoch = epoch
			    max_bundle = bundle_version
			  }
        case bundleVMRF(curBundleVersion) =>
          curBV = curBundleVersion
        case _ =>
      }
    }

    println(s"system bundle version $max_bundle \n cur bundle version is $curBV")

    if(max_bundle.isEmpty && curBV.isEmpty){
      finalBV = ""
    }else if(max_bundle.isEmpty && curBV.nonEmpty){
      finalBV = curBV
    }else if(max_bundle.nonEmpty && curBV.isEmpty){
      finalBV = max_bundle
    }else if(max_bundle.nonEmpty && curBV.nonEmpty){
      finalBV = max_bundle
      val maxArray = max_bundle.split(".",3)
      val curArray = curBV.split(".",3)
      breakable {

        //for (x <- maxArray; y <- curArray) {
        for(i <- 0 to 2){
          if (maxArray(i) < curArray(i)) {
            finalBV = curBV
            break
          }
        }
      }
    }

    val file_out = p.getParent.toString + "/extensibility.HBBundVersion_" + finalBV + pathRname+".tmp"
    val fw = new FileWriter(file_out, true)
    val bw = new BufferedWriter(fw)
    val writer = new PrintWriter(bw)
	for(line<-Source.fromFile(latestFile).getLines)
	{
		writer.write(line + "\n")
	}
    
    writer.close()
    bw.close()
    fw.close()
    return FileSystems.getDefault.getPath(file_out)
  }

  def processFileToContext(p: Path): String = {
    return "success"
  }

  def processBundleToContext(p: Path): String = {
    return "Success"
  }
  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
  }

}
