package com.glassbeam.scalar.extensibility.aruba
import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source

class Aruba_rconf extends Parsable {

	def processToFile(p: Path): Path = {
		logger.info("Logfile path is = "+ p)
		logger.info("Absolute path is = "+ p.getParent)
var out_file_name = p.getFileName().toString()
var file_out = p.getParent.toString+"/extensibility."+out_file_name+".log"
val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,true))))
	
		var flag:Int=0;
		var flag1:Int=0;
		var flag2:Int=0;
	        var lines = Source.fromFile(p.toString).getLines()
		
  
		try {
              		for(line <- lines) { }
			lines = Source.fromFile(p.toString).getLines()
                	} catch {
                  		case e: Exception =>
                    		lines = Source.fromFile(p.toString, "ISO-8859-1").getLines()
                	}	
		
                for(line <- lines){
			if(line.matches(""".*\s+is\s+.*""") && flag1==0) {
				flag1=flag1+2;
			 	//println("hostname")
			 	writer.write("Host Name: ")
			}
	  		if(line.matches(""".*\s*.rash\s+.*""") && flag2==0){
                        	flag2=flag+2;
			 	//println("crash")
                       	 	writer.write("CrashInfo: ")
                		}
			//val temp=line.stripLineEnd
           		if(line.matches("""^show.*""") && flag>1 ){
				//writer.write("===END OF SECTION==="+"\n")
                     		}
                	if(line.matches("""^show.*""") && flag==0 ){
				flag=flag+2
                		}
			writer.write(line+"\n")
			
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