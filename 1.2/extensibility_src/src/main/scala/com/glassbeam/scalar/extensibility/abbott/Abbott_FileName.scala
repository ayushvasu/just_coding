package com.glassbeam.scalar.extensibility.abbott

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source

class Abbott_FileName  extends Parsable {

	def processToFile(p: Path): Path = {

		var out_file_name = p.getFileName().toString()
		var file_out = p.getParent.toString+"/extensibility."+out_file_name
		val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,true))))
	
	        var lines = Source.fromFile(p.toString).getLines()

                try {
                        for(line <- lines) { }
                        lines = Source.fromFile(p.toString).getLines()
                        } catch {
                                case e: Exception =>
                                lines = Source.fromFile(p.toString, "ISO-8859-1").getLines()
                        }	
		writer.write(out_file_name+"\n")  
		
                for(line <- lines)
			writer.write(line+"\n")
        
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
