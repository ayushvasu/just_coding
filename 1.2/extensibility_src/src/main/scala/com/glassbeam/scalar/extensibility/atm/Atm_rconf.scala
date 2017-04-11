package com.glassbeam.scalar.extensibility.atm
import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source
import scala.collection.mutable._
class Atm_rconf extends Parsable {

	def processToFile(p: Path): Path = {
		println("Logfile path is = "+ p)
println("Absolute path is = "+ p.getParent)
var out_file_name = p.getFileName().toString()
var file_out = p.getParent.toString+"/extensibility."+out_file_name+".log"
val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,false))))
	
	var A = Map[String,String]()	
	// Please specify the path of the file from which we have to take the atm_details.
	for(line <- Source.fromFile("/home/shailesh/atm_details.csv").getLines()){
		
		val code = """([^\,]+)\,(.*)""".r
		line match{
		case code(key,value) => (key , value)
		A.update (key,value)
		}	
		//println(line)
	}
	//print (A)	

	A.keys.foreach{ i =>
		println(i,A(i))
	}	


	val code1= """^(\w+)\t(\w+).*""".r
    var temp=""
    for(line <-Source.fromFile(p.toString).getLines())
      {

           line match{
		case code1(key,value) => (key,value)
	        temp=A.getOrElse(value,"")
		//println(line+"\t"+temp)
		writer.write(line+"\t"+temp+"\n")
		case none =>
	   }
	}

		writer.close()
		return FileSystems.getDefault().getPath(file_out)
	}
  	def processFileToContext(p: Path): String ={
		return "success"
	}
  	def processBundleToContext(p: Path): String ={
		return "success"
	}
	def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
	}

}
