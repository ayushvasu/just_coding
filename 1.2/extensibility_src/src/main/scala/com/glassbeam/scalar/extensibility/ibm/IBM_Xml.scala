package com.glassbeam.scalar.extensibility.ibm

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.io.Source

class IBM_Xml extends Parsable {

  def processToFile(p: Path): Path = {
    println("Xml path is = " + p)
    val out_file_name = p.getFileName().toString();
    println("Absolute Xml path is = " + p.getParent)
    val file_out = p.getParent.toString + "/extensibility." + out_file_name + ".xml"
    val fw = new FileWriter(file_out, true)
    val bw = new BufferedWriter(fw)
    val writer = new PrintWriter(bw)

    for (line <- Source.fromFile(p.toString).getLines()) {
      //println(line.replaceAll(" \\& " , " \\&#38; "))
      writer.write(line.replaceAll(" \\& ", " \\&#38; "))
      writer.write("\n")
    }

    writer.close()
    bw.close()
    fw.close()

    return FileSystems.getDefault().getPath(file_out)
  }

  def processFileToContext(p: Path): String = {
    return "success"
  }

  def processBundleToContext(p: Path): String = {
    return "success"
  }
  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
  }

}
