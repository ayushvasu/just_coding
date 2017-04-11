package com.glassbeam.scalar.extensibility.meru
import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source

class Meru_rconf extends Parsable {

	def processToFile(p: Path): Path = {
		println("Logfile path is = "+ p)
println("Absolute path is = "+ p.getParent)
var out_file_name = p.getFileName().toString()
var file_out = p.getParent.toString+"/extensibility."+out_file_name+".log"
val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,true))))
	
		var flag:Int=0;
    for(line <-Source.fromFile(p.toString).getLines())
      {

          
           flag=0
          val temp=line.stripLineEnd
          val len=(line.stripLineEnd).length
           if(temp.matches("""^\S.*""") && len>=78){

                writer.write(temp)
                flag=1;
                }
           else if (temp.matches("""^\s{4}.*""")){

                writer.write(temp+"\n")
                }
           else if (temp.matches("""^\s{8}.*""")){

                writer.write(temp+"\n")
                }
           else if (flag==1){

                writer.write(temp+"\n")
                flag=0;
                }


          else{

                writer.write(temp+"\n")
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


class Meru_rcontroller extends Parsable {

	def processToFile(p: Path): Path = {
		println("Logfile path is = "+ p)
		var toCapture=""
		
println("Absolute path is = "+ p.getParent)
var out_file_name = p.getFileName().toString()
var file_out = p.getParent.toString+"/extensibility."+out_file_name+".log"
var done_out_read=p.getParent.toString
val wlanRplace ="""\/[^\/]+\/[^\/]+\/wlan\/ioscli""".r.replaceAllIn(done_out_read,"/done")

val done_contactRe="""\s*\<CustomerName\>\<\!\[CDATA\[(.+)\]\]\>\<\/CustomerName\>\s*""".r
val controlCon="""\s*(Contact\s+\:)(.*)$""".r
val writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,true))))
for (line<-Source.fromFile(wlanRplace).getLines)
{
line match 
{
case done_contactRe(g1)=>
toCapture=g1
case _=>
}

}
		var flag:Int=0;
    for(line <-Source.fromFile(p.toString).getLines())
      {
      var temp=""
      line match
      {
      case controlCon(g1,g2)=>
      if(g2.matches("""\s*"""))
      {
       temp=g1+g2+toCapture
      }
      else
      {
      temp=g1+g2
      }
writer.write(temp+"\n")
 case _ =>writer.write(line+"\n")
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
