package com.glassbeam.scalar.extensibility.romi

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source

class Romi_reverse extends Parsable {

	def processToFile(p: Path): Path = {
		println("Logfile path is = "+ p)
		println("Absolute path is = "+ p.getParent)
		var out_file_name = p.getFileName().toString()
			var file_out = p.getParent.toString+"/extensibility."+out_file_name+".log"
                	val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,true))))
		var head_content = ""
		var table_content = ""
		var table_nck = ""
		var line1 =""
		var line2=""
		var flag = 0
		var add_section = 1
		var dash_flag=0
		val nck_reg = """NCK\s+CHN_STATE_CHANGED.*""".r.pattern
		val bundle_regex = """.*Sys\s+Entry\-Id\s+Date\s+Time\s*""".r.pattern
		val space_line = """\s+.*""".r.pattern

		for(line <- Source.fromFile(p.toString).getLines()){
			var m = bundle_regex.matcher(line).matches
			if(m){flag=1;
				dash_flag=1;}
			if(flag == 0){
				writer.write(line)
				writer.write("\n")
			}else
			{
				if(dash_flag==1 || dash_flag==2){
				writer.write(line)
                                writer.write("\n")
					dash_flag= dash_flag + 1;
				}
				else{
					var n = space_line.matcher(line).matches
					var a = nck_reg.matcher(line).matches
					if(a){
					add_section = 0
					}
					if(add_section == 1){
					if(n){
					line2 = line
					table_content = line1 + line2 +"\n"+table_content
					//	writer.write(line)
					}else{
					line1 = line 
					//	writer.write("\n")
					//	writer.write(line)
					}
					}else{
					if(n){
					line2 = line
					table_nck = table_nck+"\n"+line1 + line2 +"\n"+table_content
					add_section = 1
					table_content = ""
					//	writer.write(line)
					}else{
					line1 = line 
					//	writer.write("\n")
					//	writer.write(line)
					}
					}
				}
			}
			//println(line.replaceAll(" \\& " , " \\&#38; "))
		}
                	writer.write(table_nck)
			if(add_section == 1){	
			writer.write("\n\n");
			writer.write("\nADDITIONAL DATA\n");
                	writer.write(table_content)
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
