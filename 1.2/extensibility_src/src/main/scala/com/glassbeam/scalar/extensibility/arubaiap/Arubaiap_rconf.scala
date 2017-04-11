package com.glassbeam.scalar.extensibility.arubaiap
import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source
import java.util.Calendar
import java.text.SimpleDateFormat

class Arubaiap_rconf extends Parsable {

	def processToFile(p: Path): Path = {
		logger.info("Logfile path is = "+ p)
		logger.info("Absolute path is = "+ p.getParent)
		var out_file_name = p.getFileName().toString()
		var file_out = p.getParent.toString+"/extensibility.date.txt"
		val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,true))))
	
	// log for extensibility file 
	//val pattern = """(.*\/lcp\/gbnewplatform\-package.*?\/).*""".r
	// val pattern = """(.*\/lcp\/current\/).*""".r
	//val pattern(pathlogs) = p.toString()
	//var file_out2 = "/home/anurags/dev/lcp/current/logs/ext_arubaiap.log"
	//var oldName = new File(file_out2);
	//var newName = new File(file_out2+"_"+Calendar.getInstance().getTime());
	/*if(oldName.length() > 104857600)
		{
				 if(oldName.renameTo(newName)) {
        							 System.out.println("renamed");
      							}
		}
	val writer2 = new PrintWriter(new BufferedWriter((new FileWriter(file_out2,true))))
	*/
	//writer2.write("\n"+Calendar.getInstance().getTime()+"------------File Received  "+out_file_name+"\n")
	//writer2.write("Absolute  path is = " + p.getParent+"\n") 

// end logs
 
// Reading done file
                                val pair= """(.*)\=(.*)""".r
                                var ticket_no :String = ""
                                var cust_name :String = ""
                                var email :String = ""
                for(linedone <- scala.io.Source.fromFile(p.getParent+"/done").getLines()){
                                                //println(linedone)
                                                val pair(name,value) = linedone
                                                if(name == "ticket_number"){ ticket_no = value}
                                                if(name == "cust_name"){ cust_name = value}
                                                if(name == "to_email"){ email = value}
                                }
                                //println(ticket_no+":"+cust_name+":"+email)
// end done file reading


	        var lines = Source.fromFile(p.toString).getLines()
		
  
		try {
              		for(line <- lines) { }
			lines = Source.fromFile(p.toString).getLines()
                	} catch {
                  		case e: Exception =>
                    		lines = Source.fromFile(p.toString, "ISO-8859-1").getLines()
                	}	
		var fl=0
                for(line <- lines){
				val a = """Current\s+Time\s*\:\s*((\d+)\-\d\d\-\d\d\s*\d+\:\d+\:\d+)""".r
			if(line.matches("""Current\s+Time\s*\:\s*\d+\-\d\d\-\d\d\s*\d+\:\d+\:\d+""")) {
				var tmp = line
				val a(timedate,year) = tmp
				fl=1
				//println(timedate)
				//println(year)
				if(year ==  "1970"){
					val cdate= Calendar.getInstance.getTime
					//println(cdate)
					val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					var newdate= format.format(cdate)
					//println(newdate)
					logger.info("File Modified:Changed "+" | "+p.getParent+"/"+out_file_name+" | "+ticket_no+" | "+cust_name+" | "+email)
					//writer2.write("["+newdate+"] File Modified:Changed "+p.getParent+"/"+out_file_name+" "+ticket_no+" "+cust_name+" "+email+"\n")
					writer.write("Current Time     :"+newdate+"\n")
					writer.write("Time Flag        :Changed"+"\n")
				}
				else{
					writer.write(line+"\n")
					writer.write("Time Flag        :Original"+"\n")    	
				}
			}
			else if(line.matches("""Current\s+Time\s*\:""")){
					fl=1
					val cdate= Calendar.getInstance.getTime
                                        val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        var newdate= format.format(cdate)
                                       // println(newdate)
					logger.info("File Modified:Inserted "+" | "+p.getParent+"/"+out_file_name+" | "+ticket_no+" | "+cust_name+" | "+email)
					//writer2.write("["+newdate+"] File Modified:Inserted "+p.getParent+"/"+out_file_name+" "+ticket_no+" "+cust_name+" "+email+"\n")
					writer.write("Current Time     :"+newdate+"\n")
                                        writer.write("Time Flag        :Inserted"+"\n")
	
			}
			else
			{
				//writer.write(line+"\n")
			}
			
	   	}
		if(fl==0){
					val cdate= Calendar.getInstance.getTime
					//println(cdate)
					val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					var newdate= format.format(cdate)
					logger.info("File Modified:Inserted "+" | "+p.getParent+"/"+out_file_name+" | "+ticket_no+" | "+cust_name+" | "+email)
					//writer2.write("["+newdate+"] File Modified:Changed "+p.getParent+"/"+out_file_name+" "+ticket_no+" "+cust_name+" "+email+"\n")
					writer.write("Current Time     :"+newdate+"\n")
					writer.write("Time Flag        :Inserted"+"\n")
			
		}
       		//writer2.close() 
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
