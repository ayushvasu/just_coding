package com.glassbeam.scalar.extensibility.vce
import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source

class Vce_rconf extends Parsable {

	def processToFile(p: Path): Path = {
		return p
	}
  	def processFileToContext(p: Path): String ={
		return "success"
	}
  	def processBundleToContext(p: Path): String ={
		return "success"
	}
	def findFilesOfPattern(infiles: List[Path], outfile: Path){
	    //var out_file_name = "extensibility.DimmBL.log"
	    //var outfile = ""
	    logger.info("Outfile is :"+outfile.toString)
	    for (p <- infiles) {
	      //val newPath=p.toString.replaceAll("""(\/[^\/]*){4}$""","")
	      //outfile = newPath + "/" +out_file_name
	      val blade = """CIMC\d+\_TechSupport""".r.findFirstMatchIn(p.toString).mkString;
	      val fname = new File(p.toString).getName
	      val writer = new PrintWriter(new BufferedWriter((new FileWriter(outfile.toString, true))))
	      var lines = Iterator[String]()
	      try {
		lines = Source.fromFile(p.toString).getLines()
	      } catch {
		case e: Exception =>
		  lines = Source.fromFile(p.toString, "ISO-8859-1").getLines()
	      }
	      writer.write("Start of " + blade + "File Name "+fname +"\n" )
	      for (line <- lines) {
		writer.write(line + "\n")

	      }
		  writer.close()

	    }
	    //return FileSystems.getDefault().getPath(outfile)

  }
}
